package com.webwalker.appdemo.guide;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class TestSurfaceView extends SurfaceView implements Callback, Runnable {

	private SurfaceHolder surfaceHolder;
	private int width, height;
	private boolean bExit;
	private Paint paint;
	private Rect rect;

	public TestSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		surfaceHolder = this.getHolder();
		surfaceHolder.addCallback(this);
		rect = new Rect(100, 0, 120, 20);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		this.setKeepScreenOn(true);
		bExit = false;
	}

	private void draw() {
		Canvas canvas = surfaceHolder.lockCanvas();
		canvas.drawColor(Color.BLACK);
		rect.top += 100;
		if (rect.top > height)
			rect.top = 0;
		rect.bottom = rect.top + 20;
		canvas.drawRect(rect, paint);
		surfaceHolder.unlockCanvasAndPost(canvas);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		width = this.getWidth();
		height = this.getHeight();
		new Thread(this).start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

	@Override
	public void run() {
		while (!bExit) {
			draw();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}