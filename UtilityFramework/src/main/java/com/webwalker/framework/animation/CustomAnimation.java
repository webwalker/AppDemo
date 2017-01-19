package com.webwalker.framework.animation;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by xujian on 2017/1/18.
 */
public class CustomAnimation extends Animation {
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        t.getMatrix().setTranslate((float) (Math.sin(10 * interpolatedTime) * 50), 0);
    }
}
