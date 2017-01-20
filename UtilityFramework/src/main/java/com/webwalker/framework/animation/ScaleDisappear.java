package com.webwalker.framework.animation;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by xujian on 2017/1/20.
 */
public class ScaleDisappear {
    public static void animToTagOnWindows(Activity activity, View tagView, View toView, float scale) {
        int[] toXY = new int[2];
        toView.getLocationOnScreen(toXY);

        int centerX = (int) (toXY[0] + toView.getMeasuredWidth() / 2f);
        int centerY = (int) (toXY[1] + toView.getMeasuredHeight() / 2f);
        animToTagOnWindows(activity, tagView, centerX, centerY, scale);
    }

    public static void animToTagOnWindows(Activity activity, View tagView, int toCenterX, int toCenterY, float scale) {
        int[] winXY = new int[2];
        tagView.getLocationOnScreen(winXY);

        float toX = tagView.getMeasuredWidth() * scale;
        float toY = tagView.getMeasuredHeight() * scale;
        float pivotX = (toCenterX - winXY[0]) * 1f / tagView.getMeasuredWidth();
        float pivotY = (toCenterY - winXY[1]) * 1f / tagView.getMeasuredHeight();

        ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f, toX, 1f, toY, Animation.RELATIVE_TO_SELF, pivotX, Animation.RELATIVE_TO_SELF, pivotY);

        final ImageView tempMoveView = new ImageView(activity);
        tempMoveView.setScaleType(ImageView.ScaleType.FIT_XY);
        Bitmap tempBm = getViewBitmap(tagView);
        tempMoveView.setImageBitmap(tempBm);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(tagView.getMeasuredWidth(), tagView.getMeasuredHeight());
        params.setMargins(winXY[0], winXY[1], winXY[0] + tagView.getMeasuredWidth(), winXY[1] + tagView.getMeasuredHeight());
        tempMoveView.setLayoutParams(params);

        final FrameLayout frameLayout = (FrameLayout) activity.getWindow().getDecorView().getRootView();
        frameLayout.addView(tempMoveView);

        scaleAnimation.setDuration(2000);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //移除临时显示动画的view
                frameLayout.removeView(tempMoveView);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        tempMoveView.startAnimation(scaleAnimation);
    }

    /**
     * 获得View的Bitmap
     *
     * @param v
     * @return
     */
    private static Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            Log.e("Folder", "failed getViewBitmap(" + v + ")", new RuntimeException());
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }
}
