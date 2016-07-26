package com.webwalker.appdemo.common;

/**
 * Created by xujian on 2016/7/26.
 */
public class JniUtil {
    static {
        System.loadLibrary("Ymt");
    }

    public native String getStrFromC();
}
