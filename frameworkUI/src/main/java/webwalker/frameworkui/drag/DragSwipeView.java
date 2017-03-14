package webwalker.frameworkui.drag;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by xujian on 2016/12/1.
 */
public class DragSwipeView extends RelativeLayout {
    private ViewDragHelper mDragger;
    private int mRange;
    View mLeftView;  //正常显示的View
    View mRightView;//编辑模式下显示的View
    private int mRightWidth; //右边View的宽度

    public DragSwipeView(Context context) {
        this(context, null);
    }

    public DragSwipeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public DragSwipeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragger.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragger.processTouchEvent(event);
        return true;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mLeftView = getChildAt(0);
        mRightView = getChildAt(1);
        int left = getMeasuredWidth();//左边此时完全显示 宽度是整个View宽度
        //左边View放于左边View的右边 超出屏幕 故不显示
        mRightView.layout(left, 0, left + mRightView.getMeasuredWidth(), mRightView.getMeasuredHeight());
        mRange = left - mRightView.getMeasuredWidth();
        mRightWidth = mRightView.getWidth();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    public void computeScroll() {  //ViewCompat.postInvalidateOnAnimation
        //不断回调此函数
        if (mDragger.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() { //初始化ViewDragHelper
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                if (child == mLeftView) {  //左侧View的left值最大为0，最小为-mRightWidth
                    left = Math.min(0, Math.max(-mRightWidth, left));
                }
                if (child == mRightView) {//同上
                    left = Math.min(mLeftView.getWidth() + mRightWidth, Math.max(mLeftView.getWidth() - mRightWidth, left));
                }
                return left;
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (xvel > 0) {//显示左边view
                    //某个View自动滚动到指定的位置，初速度为0，可在任何地方调用，动画移动会回调continueSettling(boolean)方法，直到结束
                    mDragger.smoothSlideViewTo(mLeftView, 0, 0);
                } else if ((xvel == 0) && (mLeftView.getLeft() > (-mRightWidth / 3))) {//显示左边view
                    mDragger.smoothSlideViewTo(mLeftView, 0, 0);
                } else if ((xvel == 0) && (mLeftView.getLeft() <= (-mRightWidth / 3))) {
                    mDragger.smoothSlideViewTo(mLeftView, -mRightWidth, 0);
                } else if (xvel < 0) {
                    mDragger.smoothSlideViewTo(mLeftView, -mRightWidth, 0);
                }
                ViewCompat.postInvalidateOnAnimation(DragSwipeView.this);
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                if (changedView == mLeftView) {
                    mRightView.offsetLeftAndRight(dx);
                } else if (changedView == mRightView) {
                    mLeftView.offsetLeftAndRight(dx);
                }
                ViewCompat.postInvalidateOnAnimation(DragSwipeView.this);//一定要刷新，不然会没效果
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return mRange;
            }
        });
    }
}