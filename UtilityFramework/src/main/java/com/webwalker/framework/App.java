package com.webwalker.framework;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

public class App extends android.support.multidex.MultiDexApplication {
    public static final String TAG = "AppDemo";
    private static App app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static Application get() {
        return app;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
