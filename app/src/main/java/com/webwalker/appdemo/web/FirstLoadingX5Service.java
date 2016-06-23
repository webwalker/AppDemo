package com.webwalker.appdemo.web;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by xujian on 2016/6/1.
 */
public class FirstLoadingX5Service extends IntentService {
    private final static String TAG = "X5CoreService";

    public FirstLoadingX5Service() {
        super("");
    }

    public FirstLoadingX5Service(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        QbSdk.preInit(this, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                Log.d(TAG, "onCoreInitFinished");
            }

            @Override
            public void onViewInitFinished() {
                Log.d(TAG, "onViewInitFinished");
                //此处需要new一下X5才能完成初始化
                new WebView(getApplicationContext());
            }
        });
    }
}
