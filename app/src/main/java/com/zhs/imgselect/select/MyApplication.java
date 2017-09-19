package com.zhs.imgselect.select;

import android.app.Application;
import android.content.Context;
import com.zhs.app.imgselect.R;

public class MyApplication extends Application {
    private static MyApplication app;
    private Context mContext;

    public static MyApplication getInstance() {
        return app;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        mContext = getApplicationContext();
    }
}
