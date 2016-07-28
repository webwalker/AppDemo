package com.ymatou.lib;

/**
 * Created by xujian on 2016/7/27.
 */
public class AESUtil {
    public synchronized static native int Encrypt(byte[] msg, byte[] key, byte[] cipher, int length);

    public synchronized static native int Decrypt(byte[] cipher, byte[] key, byte[] result, int length);
}
