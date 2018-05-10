package cn.jey1016.cameraanim;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jey1016.cameraanim.bean.BusEvent;
import cn.jey1016.cameraanim.util.CommonFunction;
import cn.jey1016.cameraanim.util.StatusBarUtil;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.iv_share_img)
    ImageView ivShareImg;
    @BindView(R.id.mainContainer)
    RelativeLayout mainContainer;

    private String imageUrl = "http://new-weather.oss-cn-shanghai.aliyuncs.com/7534de0597bc53f0f91e23f34f37afc6.jpg";
    private Bitmap bitmap;// 拍照返回bitmap

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doBeforeSetcontentView();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        StatusBarUtil.setStatusWhite(this, mainContainer);
        Glide.with(MainActivity.this)
                .load(imageUrl)
                .error(R.color.color_FFD166)
                .dontAnimate()
                .centerCrop()
                .into(ivShareImg);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShareEvent(final BusEvent busEvent) {
        if (busEvent.getCode() == 1) {
            bitmap = (Bitmap) busEvent.getData();
            if (isFinishing()) return;
            ivShareImg.setImageBitmap(bitmap);
        }
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

    @OnClick(R.id.iv_share_img)
    public void onViewClicked() {
        navtoCamera();
    }

    private void navtoCamera() {
        Intent intent = new Intent(this, CameraActivity.class);
        // 传递bitmap
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 将图片进行质量压缩
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
            byte[] bitmapByte = baos.toByteArray();
            intent.putExtra("bitmap", bitmapByte);
        }else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 将图片进行质量压缩
            Bitmap bmp = ((GlideBitmapDrawable)ivShareImg.getDrawable()).getBitmap();
            bmp.compress(Bitmap.CompressFormat.JPEG, 10, baos);
            byte[] bitmapByte = baos.toByteArray();
            intent.putExtra("bitmap", bitmapByte);
        }
        // 创建一个 rect 对象来存储共享元素位置信息
        Rect rect = new Rect();
        ivShareImg.getGlobalVisibleRect(rect);
        // 将位置信息附加到 intent 上
        intent.setSourceBounds(rect);
        startActivity(intent);
        // 关闭activity跳转默认动画
        overridePendingTransition(0, 0);
    }
}
