package com.webwalker.appdemo.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;

import com.webwalker.appdemo.R;
import com.webwalker.appdemo.common.Params;
import com.webwalker.framework.widget.anim.AnimatedPathView;

public class SVGActivity extends BaseActivity {
    public SVGActivity() {
    }

    public SVGActivity(Params params) {
        super(params);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svg);

        final AnimatedPathView view = (AnimatedPathView) findViewById(R.id.animated_path);

        ViewTreeObserver observer = view.getViewTreeObserver();
        if (observer != null) {
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                    float[][] points = new float[][]{
                            {0, 0},
                            {view.getWidth(), 0},
                            {view.getWidth(), view.getHeight()},
                            {0, view.getHeight()},
                            {0, 0},
                            {view.getWidth(), view.getHeight()},
                            {view.getWidth(), 0},
                            {0, view.getHeight()}
                    };
                    view.setPath(points);
                }
            });
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ObjectAnimator anim = ObjectAnimator.ofFloat(view, "percentage", 0.0f, 1.0f);
                anim.setDuration(2000);
                anim.setInterpolator(new LinearInterpolator());
                anim.start();
            }
        });
    }

    @Override
    public String getLabel() {
        return "SVG绘图";
    }
}
