package com.webwalker.appdemo.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;

import com.webwalker.appdemo.R;

/**
 * Created by xujian on 2017/1/16.
 */
public class DrawCanvasView extends View {
    private Canvas canvas;
    private Paint paint;
    private Path path;
    private float percentage;
    private int radius; //半径

    public DrawCanvasView(Context context) {
        super(context);
    }

    public DrawCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DrawCanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        //获得属性值
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.paintAnimAttr);
        //percentage = ta.getFloat(R.styleable.paintAnimAttr_percentage, 0.0f);
        ta.recycle();
    }

    //自定义属性，方便属性动画播放时调用
    public final static Property<DrawCanvasView, Float> Percentage =
            new Property<DrawCanvasView, Float>(Float.class, "percentage") {
                @Override
                public Float get(DrawCanvasView view) {
                    return view.getPercentage();
                }

                @Override
                public void set(DrawCanvasView view, Float value) {
                    view.setPercentage(value);
                }
            };

    public DrawCanvasView setPercentage(float value) {
        final float oldPercentage = percentage;
        if (oldPercentage != value) {
            percentage = value;
            invalidate();
        }

        return this;
    }

    public final float getPercentage() {
        return percentage;
    }

    //定义属性的第二种方式
    public int getPointRadius() {
        return 50;
    }

    public void setPointRadius(int radius) {
        this.radius = radius;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
//        paint();
//        animPaint();
//        canvas.drawPath(path, paint); //最后的线会自动连接起始点
        secondProperty();
    }

    //使用path绘制曲线, 单一drawLine没法实现
    private void paint() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE); //一定要设置为画线条
        path = new Path();
        path.moveTo(100, 100);   //定位path的起点
        path.lineTo(100, 200);
        path.lineTo(200, 150); //三角形
        path.lineTo(200, 50);  //平行四边形
        path.close();
    }

    private void animPaint() {
        PathMeasure measure = new PathMeasure(path, false);
        float pathLength = measure.getLength();
        PathEffect effect = new DashPathEffect(new float[]{pathLength, pathLength}, pathLength - pathLength * percentage);
        paint.setPathEffect(effect);
    }

    private void secondProperty() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(300, 300, radius, paint);
    }
}
