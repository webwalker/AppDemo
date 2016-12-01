package com.webwalker.appdemo.activity;

import android.os.Bundle;

import com.webwalker.appdemo.R;

public class SlideDragActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_drag);
    }

    @Override
    public String getLabel() {
        return "侧滑删除";
    }
}
