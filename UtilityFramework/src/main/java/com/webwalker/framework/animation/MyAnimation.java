package com.webwalker.framework.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import com.webwalker.framework.App;

/**
 * 使用Camera实现3D动画效果，这里的Camera不是相机，而是场景动画
 * Created by xujian on 2017/1/18.
 */
public class MyAnimation extends Animation {
    int mCenterX, mCenterY;
    Camera camera = new Camera();

    public MyAnimation() {
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        Log.i(App.TAG, interpolatedTime + "");
        Matrix matrix = t.getMatrix();
        camera.save();
        camera.translate(0f, 0f, (1300 - 1300 * interpolatedTime));
        camera.rotateY(360 * interpolatedTime);
        camera.getMatrix(matrix);
        matrix.preTranslate(-mCenterX, -mCenterY);
        matrix.postTranslate(mCenterX, mCenterY);
        camera.restore();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        //初始化中间坐标
        mCenterX = width / 2;
        mCenterY = height / 2;

        setDuration(2000);
        setFillAfter(true);
        setInterpolator(new LinearInterpolator());
    }
}
