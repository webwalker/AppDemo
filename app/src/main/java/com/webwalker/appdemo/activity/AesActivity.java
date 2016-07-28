package com.webwalker.appdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.webwalker.appdemo.R;
import com.webwalker.framework.utils.EncryptAesCbcWithIntegrity;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import static com.webwalker.framework.utils.EncryptAesCbcWithIntegrity.decryptString;
import static com.webwalker.framework.utils.EncryptAesCbcWithIntegrity.encrypt;
import static com.webwalker.framework.utils.EncryptAesCbcWithIntegrity.generateKey;
import static com.webwalker.framework.utils.EncryptAesCbcWithIntegrity.generateKeyFromPassword;
import static com.webwalker.framework.utils.EncryptAesCbcWithIntegrity.generateSalt;
import static com.webwalker.framework.utils.EncryptAesCbcWithIntegrity.keyString;
import static com.webwalker.framework.utils.EncryptAesCbcWithIntegrity.keys;
import static com.webwalker.framework.utils.EncryptAesCbcWithIntegrity.saltString;

public class AesActivity extends AppCompatActivity {
    public static final String TAG = "AES";

    private static boolean PASSWORD_BASED_KEY = true;
    private static String EXAMPLE_PASSWORD = "LeighHunt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aes);

        try {
            EncryptAesCbcWithIntegrity.SecretKeys key;
            if (PASSWORD_BASED_KEY) {//example for password based keys
                String salt = saltString(generateSalt());
                //If you generated the key from a password, you can store the salt and not the key.
                Log.i(TAG, "Salt: " + salt);
                key = generateKeyFromPassword(EXAMPLE_PASSWORD, salt);
            } else {
                key = generateKey();
                //Note: If you are generating a random key, you'll probably be storing it somewhere
            }

            // The encryption / storage & display:

            String keyStr = keyString(key);
            key = null; //Pretend to throw that away so we can demonstrate converting it from str

            String textToEncrypt = "We, the Fairies, blithe and antic,\n" +
                    "Of dimensions not gigantic,\n" +
                    "Though the moonshine mostly keep us,\n" +
                    "Oft in orchards frisk and peep us. ";
            Log.i(TAG, "Before encryption: " + textToEncrypt);

            // Read from storage & decrypt
            key = keys(keyStr); // alternately, regenerate the key from password/salt.
            EncryptAesCbcWithIntegrity.CipherTextIvMac civ = encrypt(textToEncrypt, key);
            Log.i(TAG, "Encrypted: " + civ.toString());

            String decryptedText = decryptString(civ, key);
            Log.i(TAG, "Decrypted: " + decryptedText);
            //Note: "String.equals" is not a constant-time check, which can sometimes be problematic.
            Log.i(TAG, "Do they equal: " + textToEncrypt.equals(decryptedText));
        } catch (GeneralSecurityException e) {
            Log.e(TAG, "GeneralSecurityException", e);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "UnsupportedEncodingException", e);
        }
    }
}
