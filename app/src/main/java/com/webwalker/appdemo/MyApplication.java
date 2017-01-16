package com.webwalker.appdemo;

import com.webwalker.framework.image.YImageLoader;

/**
 * Created by xujian on 2017/1/13.
 */
public class MyApplication extends App {
    @Override
    public void onCreate() {
        super.onCreate();

        YImageLoader.init(getApplicationContext());
    }
}
