package com.webwalker.appdemo.activity;

import android.os.Bundle;

import com.webwalker.appdemo.R;
import com.webwalker.appdemo.common.Params;

public class PaintActivity extends BaseActivity {
    public PaintActivity() {
    }

    public PaintActivity(Params params) {
        super(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);
    }

    @Override
    public String getLabel() {
        return super.getLabels();
    }
}
