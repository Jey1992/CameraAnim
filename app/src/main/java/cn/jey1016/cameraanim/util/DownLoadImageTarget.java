package cn.jey1016.cameraanim.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;

public class DownLoadImageTarget implements Target<Bitmap> {
    private static final String TAG = "DownloadImageTarget";

    @Override
    public void onStart() {}

    @Override
    public void onStop() {}

    @Override
    public void onDestroy() {}

    @Override
    public void onLoadStarted(Drawable placeholder) {}

    @Override
    public void onLoadFailed(Exception e, Drawable errorDrawable) {}

    @Override
    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
        Log.d(TAG, "请求成功");
    }

    @Override
    public void onLoadCleared(Drawable placeholder) {}

    @Override
    public void getSize(SizeReadyCallback cb) {
        cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
    }

    @Override
    public void setRequest(Request request) {}

    @Override
    public Request getRequest() {
        return null;
    }
}
