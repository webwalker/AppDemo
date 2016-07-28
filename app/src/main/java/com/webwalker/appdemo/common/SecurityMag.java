package com.webwalker.appdemo.common;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;

/**
 * Created by xujian on 2016/7/27.
 */
public class SecurityMag {
    private SecretKey key;
    private AlgorithmParameterSpec paramSpec;
    private Cipher cipher;

    public SecurityMag(SecretKey mkey, AlgorithmParameterSpec mparamSpec, Cipher mcipher) {
        this.key = mkey;
        this.paramSpec = mparamSpec;
        this.cipher = mcipher;
    }

    public String encode(String msg) {
        String strHex = "";
        try {
            // 用密钥和一组算法参数初始化此 cipher
            cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            // 对要加密的内容进行编码处理,
            strHex = byte2hex(cipher.doFinal(msg.getBytes()));
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return strHex;
    }

    public String decode(String value) {
        String strContent = "";
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
            // 对要解密的内容进行编码处理
            strContent = new String(cipher.doFinal(hex2byte(value)));
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        return strContent;
    }

    /**
     * 将二进制转化为16进制字符串
     *
     * @param b 二进制字节数组
     * @return String
     */
    public String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    /**
     * 十六进制字符串转化为2进制
     *
     * @param hex
     * @return
     */
    public byte[] hex2byte(String hex) {
        byte[] ret = new byte[8];
        byte[] tmp = new byte[0];
        try {
            tmp = hex.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 8; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    /**
     * 将两个ASCII字符合成一个字节； 如："EF"--> 0xEF
     *
     * @param src0 byte
     * @param src1 byte
     * @return byte
     */
    public byte uniteBytes(byte src0, byte src1) {
        byte _b0 = Byte.decode("0x" + new String(new byte[]{src0}))
                .byteValue();
        _b0 = (byte) (_b0 << 4);
        byte _b1 = Byte.decode("0x" + new String(new byte[]{src1}))
                .byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }
}