package com.webwalker.framework.animation;

import android.animation.TimeInterpolator;

/**
 * Created by xujian on 2017/1/18.
 */
public class CustomInterpolator implements TimeInterpolator {
    @Override
    public float getInterpolation(float input) {
        input *= 0.8f;
        return input * input;
    }
}
