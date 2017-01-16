package com.webwalker.appdemo.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.view.View;
import android.widget.ImageView;

import com.webwalker.appdemo.R;
import com.webwalker.appdemo.common.Params;
import com.webwalker.appdemo.views.DrawCanvasView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DrawActivity extends BaseActivity {
    @Bind(R.id.v_canvas)
    DrawCanvasView canvas;
    @Bind(R.id.iv_icon)
    ImageView imageView;

    public DrawActivity() {
    }

    public DrawActivity(Params params) {
        super(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        ButterKnife.bind(this);

        drawPathAnim();
        alpha();
    }

    @Override
    public String getLabel() {
        return "DrawPaint";
    }

    private void drawPathAnim() {
        Animator animator = ObjectAnimator.ofFloat(canvas, DrawCanvasView.Percentage, 0.0f, 1.0f);
        animator.setDuration(2000);
        animator.start();
    }

    private void alpha() {
        //alpha渐变从1到0再到1，后面可以设置无限的参数
        ValueAnimator fadeAnimator = ObjectAnimator.ofFloat(imageView, View.ALPHA, 1.0f, 0.0f, 1.0f);
        fadeAnimator.setInterpolator(PathInterpolatorCompat.create(0.33f, 0f, 0.66f, 1f));
        fadeAnimator.setDuration(3000);
        final AnimatorSet alertAnimator = new AnimatorSet();
        alertAnimator.playTogether(fadeAnimator);
        alertAnimator.start();
    }
}
