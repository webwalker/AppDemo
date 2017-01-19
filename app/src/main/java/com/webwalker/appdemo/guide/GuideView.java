package com.webwalker.appdemo.guide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.webwalker.appdemo.guide.GuideItem.Shape;
import com.webwalker.appdemo.guide.GuideItem.TipDirection;
import com.webwalker.appdemo.guide.GuideItem.TipGravity;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导试图组合控件 <br>
 * 引导、动画 <br>
 * <br>
 * <p>
 * 也可按百分比来处理：设计图的宽高为width, height,然后view1的位置为x1,y1,在真实手机上，手机屏幕宽为m_w,手机屏幕高为m_h，
 * 以m_w/width *x1得到x, m_h/height*y1得到y
 *
 * @author xujian
 */
public class GuideView extends View {
    private Canvas canvas;
    private Context context;
    private List<GuideItem> guideItems;
    private boolean showOneByOne;
    private int currentIndex = 0;
    private Point point;

    public GuideView(Context context) {
        this(context, null);
    }

    public GuideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        guideItems = new ArrayList<GuideItem>();
        this.context = context;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        this.canvas = canvas;
        if (!showOneByOne) { // 一次性刷新显示
            int viewCount = guideItems.size();
            for (int i = 0; i < viewCount; i++) {
                drawCanvas(guideItems.get(i));
            }
        } else { // 逐渐显示出来
            if (currentIndex >= guideItems.size())
                return;
            drawCanvas(guideItems.get(currentIndex));
        }
    }

    /**
     * 向画布上画引导内容
     *
     * @param gi
     */
    private void drawCanvas(GuideItem gi) {
        canvas.save();
        Bitmap tempBitmap = Bitmap.createBitmap(point.x, point.y, Config.ARGB_4444);
        Canvas temptCanvas = new Canvas(tempBitmap);
        temptCanvas.drawColor(Color.parseColor("#60000000"));

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        paint.setAntiAlias(true);
        paint.setStyle(Style.FILL);
        // paint.setDither(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        switch (gi.getTargetShape()) {
            case Rectangle:
                temptCanvas.drawRect(gi.getTargetRect(), paint);
                break;
            case Circle:
                int cx = gi.getTargetWidth() / 2 + gi.getTargetRect().left
                        + gi.getTargetOffsetLeft();
                int cy = gi.getTargetHeight() / 2 + gi.getTargetRect().top
                        + gi.getTargetOffsetTop();
                int radius = (int) Math.sqrt(Math.pow(gi.getTargetWidth(), 2)
                        + Math.pow(gi.getTargetHeight(), 2)) / 2;

                // for (int i = 1; i <= radius; i++) {
                // temptCanvas.drawCircle(cx, cy, i, paint);
                // invalidate();
                // }
                temptCanvas.drawCircle(cx, cy, radius, paint);
                break;
            default:
                break;
        }

        canvas.drawBitmap(tempBitmap, 0, 0, null);
        canvas.restore();

        setPositionAnim(gi);
    }

    /**
     * 定位并播放提示动画
     *
     * @param gi
     */
    private void setPositionAnim(GuideItem gi) {
        View tipView = gi.getTipView();

        int left = 0;
        int top = 0;
        int right = left + tipView.getWidth();
        int bottom = 0;

        if (gi.getTipDirection() == TipDirection.Top) {
            top = gi.getTargetRect().top - gi.getTargetHeight() / 2
                    + gi.getTipOffsetTop();
            bottom = gi.getTargetRect().bottom - gi.getTargetHeight();
        } else {
            top = gi.getTargetRect().top + gi.getTargetHeight()
                    + gi.getTipOffsetTop();
            bottom = gi.getTargetRect().bottom + gi.getTargetHeight() / 2;
        }

        // 显示位置处理
        Rect rect = null;
        switch (gi.getTipGravity()) {
            case Center:
                rect = getCenterRect(gi.getTipView());
                left = rect.left;
                right = rect.right;
                break;
            case Right:
                rect = getRightRect(gi.getTipView());
                left = rect.left;
                right = rect.right;
                break;
            case Left:
                // rect = getLeftRect(gi.getTipView());
                // left = rect.left;
                // right = rect.right;
                break;
        }
        tipView.layout(left, top, right, bottom);

        // 出场动画
        tipView.setVisibility(View.VISIBLE);
        Animation outAnim = AnimationUtils.loadAnimation(context,
                gi.getTipInAnim());
        tipView.startAnimation(outAnim);
    }

    // 引导提示居中对齐时的位置
    private Rect getCenterRect(View v) {
        if (point == null)
            return null;
        int left = point.x / 2 - v.getWidth() / 2;
        int top = point.y / 2 - v.getHeight() / 2;
        int right = point.x / 2 + v.getWidth() / 2;
        int bottom = point.y / 2 + v.getHeight() / 2;
        return new Rect(left, top, right, bottom);
    }

    // 引导提示居右对齐时的位置
    private Rect getRightRect(View v) {
        if (point == null)
            return null;
        int left = point.x - v.getWidth();
        int top = point.y - v.getHeight();
        int right = point.x + v.getWidth();
        return new Rect(left, top, right, 0);
    }

    private Rect getLeftRect(View v) {
        if (point == null)
            return null;
        int left = 0;
        int right = point.x - v.getWidth();
        return new Rect(left, 0, right, 0);
    }

    /**
     * 初始化
     *
     * @param context
     * @param showOneByOne 是否逐一显示动画
     */
    public void init(Context context, boolean showOneByOne) {
        Activity act = (Activity) context;
        if (act != null) {
            DisplayMetrics dm = new DisplayMetrics();
            Display dp = act.getWindowManager().getDefaultDisplay();
            dp.getMetrics(dm);

            point = new Point(dm.widthPixels, dm.heightPixels);
        }
        this.showOneByOne = showOneByOne;
    }

    /**
     * 注册guide指示对象
     *
     * @param targetView
     */

    public void regist(Shape shape, View targetView, View tipView,
                       TipDirection tipDirection, TipGravity tipGravity, int tipInAnim,
                       int tipOutAnim) {
        regist(shape, targetView, tipView, tipDirection, 0, tipGravity, 0,
                tipInAnim, tipOutAnim);
    }

    public void regist(Shape shape, View targetView, View tipView,
                       TipDirection tipDirection, int tipOffsetTop, TipGravity tipGravity,
                       int tipOffsetLeft, int tipInAnim, int tipOutAnim) {
        regist(shape, targetView, 0, 0, tipView, tipDirection, tipOffsetTop,
                tipGravity, tipOffsetLeft, tipInAnim, tipOutAnim);
    }

    public void regist(Shape shape, View targetView, View tipView,
                       TipDirection tipDirection, int tipOffsetTop, int tipInAnim,
                       int tipOutAnim) {
        regist(shape, targetView, 0, 0, tipView, tipDirection, tipOffsetTop,
                TipGravity.Left, 0, tipInAnim, tipOutAnim);
    }

    /**
     * 注册引导对象
     *
     * @param shape            形状
     * @param targetView       待引导的目标视图
     * @param targetOffsetLeft 相对于目标视图左边的偏移位置，用于微调整
     * @param targetOffsetTop  相对于目标视图顶部的偏移位置，用于微调整
     * @param tipView          引导提示
     * @param tipDirection     引导提示在目标视图的上面还是下面
     * @param tipOffsetTop     相对于顶部的偏移位置
     * @param tipGravity       引导提示的水平对齐方式
     * @param tipOffsetLeft    引导提示相对于水平居中的偏移位置，用于微调整
     * @param tipInAnim        引导提示进场动画
     * @param tipOutAnim       引导提示离场动画
     */
    public void regist(final Shape shape, final View targetView,
                       final int targetOffsetLeft, final int targetOffsetTop,
                       final View tipView, final TipDirection tipDirection,
                       final int tipOffsetTop, final TipGravity tipGravity,
                       final int tipOffsetLeft, final int tipInAnim, final int tipOutAnim) {
        targetView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        targetView.getViewTreeObserver()
                                .removeOnPreDrawListener(this);
                        Rect rect = new Rect();
                        rect.left = targetView.getLeft();
                        rect.right = targetView.getRight();
                        rect.top = targetView.getTop();
                        rect.bottom = targetView.getBottom();

                        GuideItem gi = new GuideItem();
                        gi.setTargetShape(shape);
                        gi.setTargetView(targetView);
                        gi.setTargetRect(rect);
                        gi.setTargetWidth(targetView.getWidth());
                        gi.setTargetHeight(targetView.getHeight());
                        gi.setTargetOffsetTop(targetOffsetTop);
                        gi.setTargetOffsetLeft(targetOffsetLeft);
                        gi.setTipView(tipView);
                        gi.setTipDirection(tipDirection);
                        gi.setTipOffsetTop(tipOffsetTop);
                        gi.setTipOffsetLeft(tipOffsetLeft);
                        gi.setTipGravity(tipGravity);
                        gi.setTipInAnim(tipInAnim);
                        gi.setTipOutAnim(tipOutAnim);

                        if (guideItems == null) {
                            guideItems = new ArrayList<GuideItem>();
                        }
                        guideItems.add(gi);
                        return true;
                    }
                });
    }

    /**
     * 点击时显示下一个提示引导
     */
    public void showNext() {
        if (currentIndex < guideItems.size()) {
            final GuideItem gi = guideItems.get(currentIndex);
            if (gi.getTipOutAnim() != 0) {
                Animation anim = AnimationUtils.loadAnimation(context,
                        gi.getTipOutAnim());
                gi.getTipView().startAnimation(anim);
                gi.getTargetView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gi.getTipView().setVisibility(View.INVISIBLE);
                    }
                }, 500);
            }

            currentIndex++;
            invalidate();
        } else {
            this.setVisibility(View.GONE);
        }
    }
}
