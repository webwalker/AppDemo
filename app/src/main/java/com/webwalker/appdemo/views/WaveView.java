package com.webwalker.appdemo.views;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;

import com.webwalker.appdemo.R;

/**
 * Created by xujian on 2017/1/18.
 */
public class WaveView extends View {

    public final static Property<WaveView, Integer> FILL_COLOR =
            new Property<WaveView, Integer>(Integer.class, "fillColor") {
                @Override
                public Integer get(WaveView view) {
                    return view.getFillColor();
                }

                @Override
                public void set(WaveView view, Integer value) {
                    view.setFillColor(value);
                }
            };

    public final static Property<WaveView, Integer> RADIUS =
            new Property<WaveView, Integer>(Integer.class, "radius") {
                @Override
                public Integer get(WaveView view) {
                    return view.getRadius();
                }

                @Override
                public void set(WaveView view, Integer value) {
                    view.setRadius(value);
                }
            };

    private final Paint mCirclePaint = new Paint();

    private int mCenterX;
    private int mCenterY;
    private int mRadius;
    private int mStrokeWidth;

    public WaveView(Context context) {
        this(context, null /* attrs */);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0 /* defStyleAttr */);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.WaveView, defStyleAttr, 0 /* defStyleRes */);

        mCenterX = a.getDimensionPixelSize(R.styleable.WaveView_centerX, 0);
        mCenterY = a.getDimensionPixelSize(R.styleable.WaveView_centerY, 0);
        mRadius = a.getDimensionPixelSize(R.styleable.WaveView_radius, 50);
        mStrokeWidth = a.getDimensionPixelSize(R.styleable.WaveView_wStrokeWidth, 3);

        int fillColor = a.getColor(R.styleable.WaveView_fillColor, Color.BLUE);
        int fillEndColor = a.getColor(R.styleable.WaveView_fillEndColor, -1);
        if (fillEndColor == -1) {
            mCirclePaint.setColor(fillColor);
        } else {
            LinearGradient sweepGradient = new LinearGradient(0f,
                    a.getDimensionPixelSize(R.styleable.WaveView_gradientStart, 0),
                    0, a.getDimensionPixelSize(R.styleable.WaveView_gradientEnd, 0),
                    fillColor, fillEndColor, Shader.TileMode.CLAMP);
            mCirclePaint.setShader(sweepGradient);
        }
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mStrokeWidth);
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(mCenterX, mCenterY, mRadius, mCirclePaint);
    }

    public final int getFillColor() {
        return mCirclePaint.getColor();
    }

    public WaveView setFillColor(int color) {
        if (mCirclePaint.getColor() != color) {
            mCirclePaint.setColor(color);

            invalidate(mCenterX, mCenterY, mRadius);
        }
        return this;
    }

    public final float getCenterX() {
        return mCenterX;
    }

    public WaveView setCenterX(int centerX) {
        final float oldCenterX = mCenterX;
        if (oldCenterX != centerX) {
            mCenterX = centerX;

            invalidate(oldCenterX, mCenterY, mRadius);
            invalidate(centerX, mCenterY, mRadius);
        }

        return this;
    }

    public final float getCenterY() {
        return mCenterY;
    }

    public WaveView setCenterY(int centerY) {
        final float oldCenterY = mCenterY;
        if (oldCenterY != centerY) {
            mCenterY = centerY;

            invalidate(mCenterX, oldCenterY, mRadius);
            invalidate(mCenterX, centerY, mRadius);
        }

        return this;
    }

    public final int getRadius() {
        return mRadius;
    }

    public WaveView setRadius(int radius) {
        final float oldRadius = mRadius;
        if (oldRadius != radius) {
            mRadius = radius;

            invalidate(mCenterX, mCenterY, oldRadius);
            if (radius > oldRadius) {
                invalidate(mCenterX, mCenterY, radius);
            }
        }

        return this;
    }

    private void invalidate(float centerX, float centerY, float radius) {
        invalidate((int) (centerX - radius - 0.5f), (int) (centerY - radius - 0.5f),
                (int) (centerX + radius + 0.5f), (int) (centerY + radius + 0.5f));
    }
}