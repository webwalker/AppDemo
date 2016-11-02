package com.webwalker.appdemo.base;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import com.webwalker.appdemo.views.TempView;

/**
 * 一个View定制一个Behavior及依赖的View
 */
public class CoordinatorBehavior extends CoordinatorLayout.Behavior<Button> {
    private int width;

    public CoordinatorBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics display = context.getResources().getDisplayMetrics();
        width = display.widthPixels;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, Button child, View dependency) {
        //如果dependency是TempView的实例，说明它就是我们所需要的Dependency，被依赖的View
        return dependency instanceof TempView; //返回false表示child不依赖dependency，ture表示依赖
    }

    //每次dependency位置发生变化，都会执行onDependentViewChanged方法
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, Button btn, View dependency) {
        //根据dependency的位置，设置Button的位置
        int top = dependency.getTop();
        int left = dependency.getLeft();

        int x = width - left - btn.getWidth();
        int y = top;

        setPosition(btn, x, y);
        return true;
    }

    private void setPosition(View v, int x, int y) {
        CoordinatorLayout.MarginLayoutParams layoutParams = (CoordinatorLayout.MarginLayoutParams) v.getLayoutParams();
        layoutParams.leftMargin = x;
        layoutParams.topMargin = y;
        v.setLayoutParams(layoutParams);
    }

//    @Override
//    public float getScrimOpacity(CoordinatorLayout parent, Button child) {
//        return 0.8f;
//    }
//
//    @Override
//    public int getScrimColor(CoordinatorLayout parent, Button child) {
//        return Color.GRAY;
//    }
}
