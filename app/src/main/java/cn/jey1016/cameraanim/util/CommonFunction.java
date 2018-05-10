package cn.jey1016.cameraanim.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import static android.text.TextUtils.isEmpty;

/**
 * Created by jey on 2018/4/3.
 */

public class CommonFunction {
    private static final String packageName = "cn.nineton.starget";
    /**
     * make dp to px according to phone pixel
     */
    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(1 * value);
    }

    /**
     * 获取设备AndroidID
     *
     * @param context 上下文
     * @return AndroidID
     */
    @SuppressLint("HardwareIds")
    public static String getAndroidID(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * 获取屏幕宽高
     * @param context
     * @return 宽度+高度数组
     */
    public static int[] getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return new int[]{outMetrics.widthPixels, outMetrics.heightPixels};
    }

    /**
     * 跳转到应用市场
     */
    public static void jumpToMarket(Activity activity) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            activity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取文件里面的json数据
     * @param context
     * @param fileName
     * @return
     */
    public static String getJson(Context context, String fileName) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 防止双击
     * @param view
     */
    public static void voidDoubleClick(final View view){
        view.setClickable(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setClickable(true);
            }
        }, 2000);
    }

    /**
     * 获取控件宽度
     */
    public static int getViewWidth(final View v) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
        int width = v.getMeasuredWidth();
        return width;
    }

    /**
     * 获取控件高度 get view heigth
     */
    public static int getViewHeight(final View v) {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
        int height = v.getMeasuredHeight();
        return height;
    }

    /**
     * 获取指定View的截图
     * @param view
     */
    public static Bitmap getImageOfView(View view) {
        int width1 = view.getMeasuredWidth();
        int height1 = view.getMeasuredHeight();
        Bitmap bmp1 = Bitmap.createBitmap(width1, height1,
                Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bmp1));
        Canvas canvas = new Canvas(bmp1);
        Paint paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        canvas.drawBitmap(bmp1, 0, 0, null);
        return bmp1;
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 通过资源id转化成Bitmap
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitmapById(Context context, int resId)
    {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;

        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 按长方形裁切图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap imageCropWithRect(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        int with = bitmap.getWidth(); // 得到图片的宽，高

        int nw, nh, retX;
        nw = with / 2;
        nh = with;
        retX = with / 2;

        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, nw, nh, null,
                false);
        if (bitmap != null && !bitmap.equals(bmp) && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bmp;
    }

    /**
     * 按长方形裁切图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap imageCropWithRect1(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        int with = bitmap.getWidth(); // 得到图片的宽，高

        int nw, nh, retX;
        nw = with / 2;
        nh = with;
        retX = with / 2;

        Bitmap bmp = Bitmap.createBitmap(bitmap, retX, 0, nw, nh, null,
                false);
        if (bitmap != null && !bitmap.equals(bmp) && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bmp;
    }

    public static Bitmap[] getCropBitmaps(Bitmap bitmap){
        if (bitmap == null) {
            return null;
        }

        int with = bitmap.getWidth(); // 得到图片的宽，高
        int height = bitmap.getHeight();

        int nw, nh, retX;
        nw = with / 2;
        nh = height;
        retX = with / 2;

        Bitmap[] bitmaps = new Bitmap[2];
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, nw, nh, null,
                false);
        Bitmap bmp1 = Bitmap.createBitmap(bitmap, retX, 0, nw, nh, null,
                false);

        bitmaps[0] = bmp;
        bitmaps[1] = bmp1;
        if (bitmap != null && !bitmap.equals(bmp) && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return bitmaps;
    }
}
