package cn.jey1016.cameraanim.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wonderkiln.camerakit.CameraKitEventCallback;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import cn.jey1016.cameraanim.R;
import cn.jey1016.cameraanim.util.PhotoUtils;

public class CameraControls extends LinearLayout {

    private int cameraViewId = -1;
    private CameraView cameraView;

    private int coverViewId = -1;
    private View coverView;

    @BindView(R.id.picture_preview)
    ImageView picturePreview;
    @BindView(R.id.captureButton)
    ImageView captureButton;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.captureSuc)
    ImageView captureSuc;
    @BindView(R.id.recapture)
    TextView recapture;

    private OnCaptureImgCallBack onCaptureImgCallBack;
    private OnBackCallBack onBackCallBack;
    private OnPhotoSelCallBack onPhotoSelCallBack;

    public CameraControls(Context context) {
        this(context, null);
    }

    public CameraControls(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraControls(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(getContext()).inflate(R.layout.camera_controls, this);
        ButterKnife.bind(this);
        // 设置相册第一张照片
        String firstP = PhotoUtils.getSystemPhotoLast(getContext());
        Glide.with(getContext())
                .load(firstP)
                .crossFade()
                .centerCrop()
                .thumbnail(0.2f)
                .into(picturePreview);

        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.CameraControls,
                    0, 0);

            try {
                cameraViewId = a.getResourceId(R.styleable.CameraControls_camera, -1);
                coverViewId = a.getResourceId(R.styleable.CameraControls_cover, -1);
            } finally {
                a.recycle();
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (cameraViewId != -1) {
            View view = getRootView().findViewById(cameraViewId);
            if (view instanceof CameraView) {
                cameraView = (CameraView) view;
                cameraView.bindCameraKitListener(this);
            }
        }

        if (coverViewId != -1) {
            View view = getRootView().findViewById(coverViewId);
            if (view != null) {
                coverView = view;
            }
        }
    }

    @OnTouch(R.id.captureButton)
    boolean onTouchCapture(View view, MotionEvent motionEvent) {
        handleViewTouchFeedback(view, motionEvent);
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                break;
            }

            case MotionEvent.ACTION_UP: {
                mHandler.sendEmptyMessage(0);
                cameraView.captureImage(new CameraKitEventCallback<CameraKitImage>() {
                    @Override
                    public void callback(CameraKitImage event) {
                        if (onCaptureImgCallBack != null){
                            onCaptureImgCallBack.onCapture(event);
                        }
                    }
                });
                break;
            }
        }
        return true;
    }

    protected Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 0:// 点击拍照
                    initUI(true,false);
                    break;
                case 1:// 拍完照片
                    initUI(false,true);
                    break;
                case 2:// 正常模式
                    initUI(false,false);
                    break;
            }
            return false;
        }
    });

    /**
     * 控制拍照时底部UI展示
     * @param show 是否展示进度条
     * @param isSuc 是否拍照成功
     */
    public void initUI(boolean show,boolean isSuc){
        progressBar.setVisibility(show?VISIBLE:GONE);
        captureButton.setVisibility(show||isSuc?GONE:VISIBLE);
        captureSuc.setVisibility(isSuc?VISIBLE:GONE);
        recapture.setVisibility(isSuc?VISIBLE:GONE);
        picturePreview.setVisibility(isSuc?GONE:VISIBLE);
    }

    @OnClick({R.id.back,R.id.picture_preview,R.id.captureSuc,R.id.recapture})
    public void onTouchBack(View view){
        switch (view.getId()){
            case R.id.back:
                if (onBackCallBack != null) onBackCallBack.onBack(true);
                break;
            case R.id.picture_preview:
                if (onPhotoSelCallBack != null) onPhotoSelCallBack.onClick();
                break;
            case R.id.captureSuc:
                if (onBackCallBack != null) onBackCallBack.onBack(false);
                break;
            case R.id.recapture:// 重新拍照
                coverView.setVisibility(GONE);
                if (!cameraView.isStarted()){
                    cameraView.start();
                }
                mHandler.sendEmptyMessage(2);
                break;
        }
    }

    boolean handleViewTouchFeedback(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                touchDownAnimation(view);
                return true;
            }

            case MotionEvent.ACTION_UP: {
                touchUpAnimation(view);
                return true;
            }

            default: {
                return true;
            }
        }
    }

    void touchDownAnimation(View view) {
        view.animate()
                .scaleX(0.88f)
                .scaleY(0.88f)
                .setDuration(300)
                .setInterpolator(new OvershootInterpolator())
                .start();
    }

    void touchUpAnimation(View view) {
        view.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(300)
                .setInterpolator(new OvershootInterpolator())
                .start();
    }

    // 拍照监听
    public void setOnCaptureImgListener(OnCaptureImgCallBack onCaptureImgCallBack){
        this.onCaptureImgCallBack = onCaptureImgCallBack;
    }

    // 返回监听
    public void setOnBackListener(OnBackCallBack onBackListener){
        this.onBackCallBack = onBackListener;
    }

    // 点击相册
    public void setOnPhotoSelListener(OnPhotoSelCallBack onPhotoSelListener){
        this.onPhotoSelCallBack = onPhotoSelListener;
    }

    public void destroy(){
        onCaptureImgCallBack = null;
        mHandler.removeCallbacksAndMessages(null);
    }

    public interface OnCaptureImgCallBack{
        void onCapture(CameraKitImage cameraKitImage);
    }

    public interface OnBackCallBack{
        void onBack(boolean isClose);
    }

    public interface OnPhotoSelCallBack{
        void onClick();
    }

}
