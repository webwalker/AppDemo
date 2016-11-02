package com.webwalker.appdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.webwalker.appdemo.R;
import com.webwalker.appdemo.common.SecurityMag;
import com.ymatou.lib.AESUtil;
import com.ymatou.lib.SecurityUtil;
import com.ymatou.lib.TestEntry;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JniActivity extends AppCompatActivity {
    @Bind(R.id.tvResult)
    TextView tvResult;
    @Bind(R.id.etString)
    EditText etString;

    private String inputString;
    private final String TAG = "App";
    private StringBuilder sb = new StringBuilder();

    private byte[] keyValue;
    private byte[] iv;
    private SecurityMag secMag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni);
        ButterKnife.bind(this);

        System.loadLibrary("Ymt");
    }

    @OnClick(R.id.btnJni)
    public void clickJni() {
        //init
        sb = new StringBuilder();
        inputString = etString.getText().toString();
        //function
        readString();
        simpleEncrypt();
        testAES();
        aesPartEncrypt();

        //output
        tvResult.setText(sb.toString());
        Log.i(TAG, sb.toString());
    }

    private void readString() {
        appendLine("读取串：" + TestEntry.getStrFromC());
    }

    private void simpleEncrypt() {
        String encrypt = SecurityUtil.encodeFromC(inputString, inputString.length());
        String decrypt = SecurityUtil.decodeFromC(encrypt, encrypt.length());
        appendLine("简单加密：" + encrypt);
        appendLine("简单解密：" + decrypt);
    }

    //解密数据不对
    private void testAES() {
        byte[] Key = {1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8};
        byte[] Msg = inputString.getBytes();//{1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8};
        // byte[] Cipher = { -26, 106, -62, -14, 20, -30, -81, 19, 121, 34, -63,
        // 41, 28, 87, -16, 12};
        byte[] Cipher = new byte[inputString.length()];
        byte[] DecryptResult = new byte[inputString.length()];

        appendLine("------------AES 原文----------------");
        appendLine(inputString);
        for (int i = 0; i < Msg.length; i++) {
            append(Msg[i] + ",");
        }
        AESUtil.Encrypt(Msg, Key, Cipher, Msg.length);
        appendLine("------------AES 密文----------------");
        for (int i = 0; i < Cipher.length; i++) {
            append(Cipher[i] + ",");
        }
        appendLine();
        AESUtil.Decrypt(Cipher, Key, DecryptResult, Cipher.length);
        appendLine("------------AES 解密后----------------");
        for (int i = 0; i < DecryptResult.length; i++) {
            append(DecryptResult[i] + ",");
        }
        try {
            appendLine();
            String string = new String(DecryptResult, "UTF-8");
            appendLine(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        appendLine();
    }

    private void aesPartEncrypt() {
        keyValue = SecurityUtil.getKeyValue();
        iv = SecurityUtil.getIv();
        if (null != keyValue && null != iv) {
            KeyGenerator kgen;
            try {
                kgen = KeyGenerator.getInstance("AES");
                kgen.init(128, new SecureRandom(keyValue));
                SecretKey key = kgen.generateKey();
                IvParameterSpec paramSpec = new IvParameterSpec(iv);
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                secMag = new SecurityMag(key, paramSpec, cipher);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            }
        }

        String encryt = secMag.encode(inputString);
        String decrypt = secMag.decode(encryt);
        appendLine("aes part encryp:" + encryt);
        append("aes part decrypt: " + decrypt);
    }

    private StringBuilder append(String message) {
        return sb.append(message);
    }

    private StringBuilder appendLine() {
        return append("\r\n");
    }

    private StringBuilder appendLine(String message) {
        return append(message).append("\r\n");
    }
}