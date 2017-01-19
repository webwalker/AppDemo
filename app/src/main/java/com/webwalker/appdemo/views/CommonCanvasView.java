package com.webwalker.appdemo.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

import com.webwalker.appdemo.R;
import com.webwalker.framework.interfaces.ICallback;

/**
 * Created by xujian on 2017/1/16.
 */
public class CommonCanvasView extends View {
    private Canvas canvas;
    private Paint paint;
    private Path path;
    private float percentage;

    private ICallback callback;

    public CommonCanvasView(Context context) {
        super(context);
    }

    public CommonCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CommonCanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        if (callback != null)
            callback.action(canvas);
    }

    public void setCallback(ICallback callback) {
        this.setVisibility(VISIBLE);
        this.callback = callback;
    }
}
