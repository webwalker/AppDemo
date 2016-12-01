package com.webwalker.appdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * @author xu.jian
 */
public abstract class BaseActivity extends AppCompatActivity {
    public Bundle params;
    public int layoutId;
    protected String label;

    public BaseActivity() {
    }

    public BaseActivity(Bundle params) {
        this.params = params;
    }

    public BaseActivity(int layoutId, String label) {
        this.layoutId = layoutId;
        this.label = label;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layoutId = getIntent().getIntExtra("id", 0);
    }

    public abstract String getLabel();

    protected Bundle getBundle(String key, boolean value) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(key, value);
        return bundle;
    }
}
