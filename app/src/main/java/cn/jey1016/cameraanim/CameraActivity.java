package cn.jey1016.cameraanim;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wonderkiln.camerakit.CameraKit;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jey1016.cameraanim.bean.BusEvent;
import cn.jey1016.cameraanim.util.CommonFunction;
import cn.jey1016.cameraanim.util.OptAnimationLoader;
import cn.jey1016.cameraanim.util.PhotoUtils;
import cn.jey1016.cameraanim.util.StatusBarUtil;
import cn.jey1016.cameraanim.view.CameraControls;

public class CameraActivity extends Activity {
    // 调用相机固定参数
    public static final int CODE_GALLERY_REQUEST = 0xa0;
    public static final int CODE_CAMERA_REQUEST = 0xa1;
    public static final int CODE_RESULT_REQUEST = 0xa2;
    public static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    public static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;

    @BindView(R.id.camera)
    CameraView cameraView;
    @BindView(R.id.title_bar_container_ll)
    LinearLayout topbar;
    @BindView(R.id.flash_button)
    ImageView flashButton;
    @BindView(R.id.camera_controls)
    CameraControls cameraControls;

    @BindView(R.id.blackCover)
    View blackCover;// 退场背景
    @BindView(R.id.fl_cover_container)
    FrameLayout fl_cover_container;// 水印容器
    @BindView(R.id.fl_controls_container)
    FrameLayout fl_controls_container;// 底部控制器
    @BindView(R.id.fl_camera)
    FrameLayout fl_camera;// 相机容器

    @BindView(R.id.coverImage)
    ImageView coverImage;
    @BindView(R.id.iv_share_target)
    ImageView ivShareTarget;
    @BindView(R.id.tv_share_target)
    TextView tvShareTarget;
    @BindView(R.id.tv_share_target_day)
    TextView tvShareTargetDay;
    @BindView(R.id.tv_share_target_insist_day)
    TextView tvShareTargetInsistDay;

    private Rect mRect;// 分享页图片的位置
    private float scale = 0;// 图片对屏幕宽比
    private static final AccelerateDecelerateInterpolator DEFAULT_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    public static int ANIMDURATION = 500;// 动画时间
    private boolean isAnim = true;// 判断动画结束后初始化相机
    private Bitmap previewBitmap;// 预览图bitmap
    Bitmap bitmap;
    private String imageUrl = "http://new-weather.oss-cn-shanghai.aliyuncs.com/7534de0597bc53f0f91e23f34f37afc6.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doBeforeSetcontentView();
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 设置layout前配置
     */
    private void doBeforeSetcontentView() {
        // 透明状态栏
        StatusBarUtil.statusTransparent(this);
        // 设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void initView() {
        StatusBarUtil.setStatusDark(this, topbar);
        byte [] bis = getIntent().getByteArrayExtra("bitmap");
        if (bis!=null) {
            bitmap= BitmapFactory.decodeByteArray(bis, 0, bis.length);
        }
        cameraView.setZoom(0.1f);
        cameraView.setCropOutput(true);
        cameraView.setFlash(CameraKit.Constants.FLASH_AUTO);
        inital();
    }

    /**
     * 初始化场景
     */
    private void inital(){
        blackCover.setVisibility(View.GONE);
        // bind水印数据
        if (bitmap!=null) coverImage.setImageBitmap(bitmap);
        else Glide.with(CameraActivity.this)
                .load(imageUrl)
                .error(R.color.color_FFD166)
                .dontAnimate()
                .centerCrop()
                .into(coverImage);
        // 设置 水印和覆盖图 的位置，使其和上一个界面中图片的位置重合
        mRect = getIntent().getSourceBounds();
        scale = 1f * CommonFunction.getScreenWidth(this) / mRect.width();
        FrameLayout.LayoutParams params;
        params = new FrameLayout.LayoutParams(mRect.width(), mRect.height());
        params.setMargins(mRect.left, mRect.top, mRect.right, mRect.bottom);
        fl_cover_container.setLayoutParams(params);

        // 设置camera的大小边界
        FrameLayout.LayoutParams paramsc = new FrameLayout.LayoutParams(CommonFunction.getScreenWidth(this),
                (int) (mRect.height()*scale));
        int marginTop = CommonFunction.dp2px(this,48)+ CommonFunction.getStatusBarHeight(this);
        paramsc.setMargins(0,marginTop,0,0);
        fl_camera.setLayoutParams(paramsc);
        // 先设置camera透明,动画结束后设置可见
        fl_camera.setVisibility(View.GONE);

        // 设置底部control的高度和初始化
        int marginTopControl = CommonFunction.getStatusBarHeight(this)
                +CommonFunction.dp2px(this,48)
                +(int) (mRect.height()*scale);
        int controlHeight = CommonFunction.getScreenSize(this)[1]-marginTopControl;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(CommonFunction.getScreenWidth(this),
                controlHeight);
        layoutParams.setMargins(0,marginTopControl,0,0);
        fl_controls_container.setLayoutParams(layoutParams);
        cameraControls.setOnCaptureImgListener(new CameraControls.OnCaptureImgCallBack() {
            @Override
            public void onCapture(final CameraKitImage cameraKitImage) {
                if (isFinishing()) return;
                CameraActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showCoverImage(cameraKitImage.getBitmap());
                    }
                });
            }
        });
        cameraControls.setOnBackListener(new CameraControls.OnBackCallBack() {
            @Override
            public void onBack(boolean isClose) {
                cameraControls.destroy();
                if (isClose || previewBitmap == null) {
                    finish();
                    overridePendingTransition(0,R.anim.camera_slip_exit);
                } else {
                    //TODO 拍照或者选照片裁剪之后返回到分享页
                    CameraActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            runExitAnim();
                        }
                    });
                }
            }
        });
        cameraControls.setOnPhotoSelListener(new CameraControls.OnPhotoSelCallBack() {
            @Override
            public void onClick() {
                autoObtainStoragePermission();
            }
        });

        runEnterAnim();
    }

    /**
     * 入场动画
     */
    private void runEnterAnim(){
        // 如果不做伸缩,原本Y偏移位置计算方法:(status+title)-(status+marginTop) = title-marginTop = 48-15 = 33
        float transY = CommonFunction.dp2px(this,33);
        fl_cover_container.animate()
                .setInterpolator(DEFAULT_INTERPOLATOR)
                .setDuration(ANIMDURATION)
                .scaleX(scale)
                .scaleY(scale)
                .translationY(transY + (mRect.height() * scale - mRect.height()) / 2)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        isAnim = false;
                        coverImage.startAnimation(
                                OptAnimationLoader.loadAnimation(CameraActivity.this, R.anim.camera_img_exit_anim));
                        fl_camera.setVisibility(View.VISIBLE);
                        if (!cameraView.isStarted()){
                            // 启动相机
                            cameraView.start();
                        }
                    }
                })
                .start();
        fl_controls_container.startAnimation(OptAnimationLoader.loadAnimation(this, R.anim.camera_slip_enter));
        topbar.startAnimation(OptAnimationLoader.loadAnimation(this, R.anim.camera_top_slip_in));
        // 动画结束后隐藏coverImage
        coverImage.postDelayed(new Runnable() {
            @Override
            public void run() {
                coverImage.setVisibility(View.GONE);
            }
        },ANIMDURATION+300);
    }

    /**
     * 退场动画
     */
    private void runExitAnim(){
        EventBus.getDefault().post(new BusEvent(1,"拍照回调bitmap",previewBitmap));
        previewBitmap = null;
        cameraView.stop();
        fl_camera.setVisibility(View.GONE);
        blackCover.setVisibility(View.VISIBLE);
        fl_cover_container.animate()
                .setInterpolator(DEFAULT_INTERPOLATOR)
                .setDuration(ANIMDURATION)
                .scaleX(1)
                .scaleY(1)
                .translationY(0)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        overridePendingTransition(0,0);
                    }
                })
                .start();
        fl_controls_container.startAnimation(OptAnimationLoader.loadAnimation(this, R.anim.camera_slip_exit));
        topbar.startAnimation(OptAnimationLoader.loadAnimation(this, R.anim.camera_top_slip_out));
    }

    /**
     * 拍照结束之后,显示拍照后的预览图
     * @param bitmap
     */
    private void showCoverImage(final Bitmap bitmap) {
        cameraControls.initUI(false,true);
        previewBitmap = bitmap;
        coverImage.setVisibility(View.VISIBLE);
        coverImage.setImageBitmap(bitmap);
        cameraView.stop();
    }

    @OnClick({R.id.facing_button, R.id.flash_button, R.id.captureButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.facing_button:
                if (cameraView.isFacingFront()) {
                    cameraView.setFacing(CameraKit.Constants.FACING_BACK);
                    changeViewImageResource((ImageView) view, R.mipmap.ic_facing_change,-360);
                } else {
                    cameraView.setFacing(CameraKit.Constants.FACING_FRONT);
                    changeViewImageResource((ImageView) view, R.mipmap.ic_facing_change,-360);
                }
                break;
            case R.id.flash_button:
                if (cameraView.getFlash() == CameraKit.Constants.FLASH_AUTO) {
                    cameraView.setFlash(CameraKit.Constants.FLASH_ON);
                    changeViewImageResource((ImageView) view, R.mipmap.ic_flash_on,360);
                } else if (cameraView.getFlash() == CameraKit.Constants.FLASH_ON){
                    cameraView.setFlash(CameraKit.Constants.FLASH_OFF);
                    changeViewImageResource((ImageView) view, R.mipmap.ic_flash_off,360);
                }else {
                    cameraView.setFlash(CameraKit.Constants.FLASH_AUTO);
                    changeViewImageResource((ImageView) view, R.mipmap.ic_flash_auto,360);
                }
                break;
        }
    }

    /**
     * imageview点击动画
     * @param imageView
     * @param resId
     * @param rotation 旋转角度
     */
    private void changeViewImageResource(final ImageView imageView, @DrawableRes final int resId, int rotation) {
        imageView.setRotation(0);
        imageView.animate()
                .rotationBy(rotation)
                .setDuration(400)
                .setInterpolator(new OvershootInterpolator())
                .start();

        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setImageResource(resId);
            }
        }, 120);
    }

    //-----------------------------选取相册照片------------------------

    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri cropImageUri;

    private int OUTPUT_X = 0;// 裁剪宽度
    private int OUTPUT_Y = 0;// 裁剪高度

    /**
     * 自动获取sdk权限
     */
    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //访问相册完成回调
                case CODE_GALLERY_REQUEST:
                    if (PhotoUtils.hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            newUri = FileProvider.getUriForFile(this, "cn.nineton.starget.fileprovider", new File(newUri.getPath()));
                        }
                        OUTPUT_X = CommonFunction.getScreenWidth(this);
                        OUTPUT_Y = (int) (mRect.height()*scale);
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, OUTPUT_X, OUTPUT_Y, CODE_RESULT_REQUEST);
                    } else {
                        Toast.makeText(this,"没有SD卡",Toast.LENGTH_LONG).show();
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    if (bitmap != null) {
                        showCoverImage(bitmap);
                    }
                    break;
                default:
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isAnim){
            cameraView.start();
        }
    }

    @Override
    public void onPause() {
        cameraView.stop();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0,R.anim.camera_slip_exit);
    }
}
