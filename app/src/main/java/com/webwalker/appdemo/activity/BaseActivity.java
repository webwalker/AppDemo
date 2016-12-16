package com.webwalker.appdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.webwalker.appdemo.common.Params;

/**
 * @author xu.jian
 */
public abstract class BaseActivity extends AppCompatActivity {
    public Params params;

    public BaseActivity() {
    }

    public BaseActivity(Params params) {
        this.params = params;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        params = (Params) getIntent().getSerializableExtra("params");
    }

    protected int getLayoutId() {
        return params.getLayout();
    }

    protected String getLabels() {
        return params.getLabel();
    }

    public abstract String getLabel();
}
