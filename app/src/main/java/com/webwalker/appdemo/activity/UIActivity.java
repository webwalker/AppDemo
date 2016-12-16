package com.webwalker.appdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.webwalker.appdemo.R;
import com.webwalker.appdemo.common.Params;

public class UIActivity extends BaseActivity {
    public UIActivity() {
    }

    public UIActivity(Params params) {
        super(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup parentView = (ViewGroup) View.inflate(this, R.layout.activity_ui, null);
        setContentView(parentView);
        if (getLayoutId() > 0) {
            View view = View.inflate(this, getLayoutId(), parentView);
        }
    }

    @Override
    public String getLabel() {
        return super.getLabels();
    }
}
