package com.webwalker.appdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.webwalker.appdemo.R;

public class FrescoActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresco);
    }

    @Override
    public String getLabel() {
        return "Fresco Image";
    }
}
