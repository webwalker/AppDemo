package com.ymatou.lib;

/**
 * Created by xujian on 2016/7/26.
 */
public class SecurityUtil {
    //加密本地方法
    public static native String encodeFromC(String text, int length);

    //解密本地方法
    public static native String decodeFromC(String text, int length);

    //AES Key
    public static native byte[] getKeyValue();

    public static native byte[] getIv();
}
