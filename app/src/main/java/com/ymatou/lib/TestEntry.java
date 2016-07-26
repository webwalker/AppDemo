package com.ymatou.lib;

/**
 * Created by xujian on 2016/7/22.
 */
public class TestEntry {
    static {
        System.loadLibrary("Ymt");
    }

    public static native String getStrFromC();
}
