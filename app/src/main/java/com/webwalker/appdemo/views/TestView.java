package com.webwalker.appdemo.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.webwalker.appdemo.R;

/**
 * Created by xujian on 2016/12/8.
 */

public class TestView extends LinearLayout {
    public TestView(Context context) {
        super(context);
        init(context);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        inflate(context, R.layout.recycle_item, this);
    }
}
