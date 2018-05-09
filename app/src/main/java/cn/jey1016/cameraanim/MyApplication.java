package cn.jey1016.cameraanim;

import android.app.Application;
import android.app.Notification;

import cn.jey1016.cameraanim.util.CrashHandler;

/**
 * Created by jey on 2018/4/4.
 */

public class MyApplication extends Application {
    private static MyApplication app;

    public MyApplication() {
        app = this;
    }

    public static synchronized MyApplication getInstance() {
        if (app == null) {
            app = new MyApplication();
        }
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

    }

}
