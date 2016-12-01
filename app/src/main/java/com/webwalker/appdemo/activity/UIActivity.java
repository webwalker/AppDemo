package com.webwalker.appdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.webwalker.appdemo.R;

public class UIActivity extends BaseActivity {
    public UIActivity(){}

    public UIActivity(int layoutId, String label) {
        super(layoutId, label);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup parentView = (ViewGroup) View.inflate(this, R.layout.activity_ui, null);
        setContentView(parentView);
        if (layoutId > 0) {
            View view = View.inflate(this, layoutId, parentView);
        }
    }

    @Override
    public String getLabel() {
        return label;
    }
}
