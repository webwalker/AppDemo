package com.webwalker.appdemo.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.webwalker.appdemo.App;
import com.webwalker.appdemo.R;
import com.webwalker.appdemo.common.Consts;
import com.webwalker.appdemo.common.DexTools;
import com.webwalker.appdemo.model.PatchTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * inject的方式将补丁class打成dex文件，放到SDcard卡加载注入
 * 示例文件：PatchTest
 */
public class PatchActivity extends BaseActivity {
    @Bind(R.id.tv_num)
    TextView tvNum;
    @Bind(R.id.btn_fix)
    Button btnFix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patch);
        ButterKnife.bind(this);

        tvNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PatchTest test = new PatchTest();
                tvNum.setText(test.plus() + "");
            }
        });
        btnFix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fixBug();
            }
        });
    }

    @Override
    public String getLabel() {
        return "热补丁";
    }

    private void fixBug() {
        copyDex();
        try {
            DexTools.loadFixedDex(App.get());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void copyDex() {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            File dir = getDir(Consts.DEX_DIR, Context.MODE_PRIVATE);
            File fileOpt = new File(dir, "patch.dex");
            if (fileOpt.exists()) return;
            File file = new File(Environment.getExternalStorageDirectory().getPath(), "patch.dex");
            boolean exists = file.exists();
            Uri uri = Uri.fromFile(file);
            inputStream = getContentResolver().openInputStream(uri);
            outputStream = new FileOutputStream(fileOpt);
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
