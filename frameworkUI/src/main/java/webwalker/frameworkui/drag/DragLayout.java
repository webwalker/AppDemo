package webwalker.frameworkui.drag;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import webwalker.frameworkui.R;

/**
 * Created by Flavien Laurent (flavienlaurent.com) on 23/08/13.
 */
public class DragLayout extends LinearLayout {
    private final ViewDragHelper mDragHelper;

    private View mDragView1;
    private View mDragView2;

    private boolean mDragEdge;
    private boolean mDragHorizontal;
    private boolean mDragCapture;
    private boolean mDragVertical;
    private boolean anyDrag;
    private boolean moveTogether;

    private Point initPointPosition = new Point();

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDragHelper = ViewDragHelper.create(this, 1f, new DragHelperCallback());
    }

    @Override
    protected void onFinishInflate() {
        mDragView1 = findViewById(R.id.drag1);
        mDragView2 = findViewById(R.id.drag2);
    }

    public void setDragHorizontal(boolean dragHorizontal) {
        mDragHorizontal = dragHorizontal;
        mDragView2.setVisibility(View.GONE);
    }

    public void setDragVertical(boolean dragVertical) {
        mDragVertical = dragVertical;
        mDragView2.setVisibility(View.GONE);
    }

    public void setDragEdge(boolean dragEdge) {
        //控制是否可拖动:左边，上边，右边，下边拖动
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT); //左边界可以拖动
        //mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL); //手指直接在边界的其他地方拖动此时也能把这个iv拖走
        //mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT | ViewDragHelper.EDGE_RIGHT);//左右拖动
        mDragEdge = dragEdge;
        mDragView2.setVisibility(View.GONE);
    }

    public void setAnyDrag(boolean anyDrag) {
        this.anyDrag = anyDrag;
    }

    public void setMoveTogether(boolean moveTogether) {
        this.moveTogether = moveTogether;
    }

    public void setDragCapture(boolean dragCapture) {
        mDragCapture = dragCapture;
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //需要当前触摸的子View进行拖拽移动就返回true获得捕获事件
            if (mDragCapture) {
                return child == mDragView1;
            }
            //return child == blueView || child == redView;
            return true;
        }

        //上面的view被捕获时调用
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (moveTogether) {
                super.onViewPositionChanged(changedView, left, top, dx, dy);
                if (changedView == mDragView1) {
                    //拖动蓝色方块时，红色也跟随移动
                    mDragView2.layout(mDragView2.getLeft() + dx, mDragView2.getTop() + dy,
                            mDragView2.getRight() + dx, mDragView2.getBottom() + dy);
                } else if (changedView == mDragView2) {
                    //拖动红色方块时，蓝色也跟随移动
                    mDragView1.layout(mDragView1.getLeft() + dx, mDragView1.getTop() + dy,
                            mDragView1.getRight() + dx, mDragView1.getBottom() + dy);
                }
            } else {
                invalidate();
            }
        }

        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
        }

        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);
        }

        @Override
        public boolean onEdgeLock(int edgeFlags) {
            return super.onEdgeLock(edgeFlags);
        }

        @Override
        public int getOrderedChildIndex(int index) {
            return super.getOrderedChildIndex(index);
        }

        //获取View水平方向的拖拽范围
        //拖动时又不影响点击事件
        //horizontalDragRange、verticalDragRange大于0时
        //对应的move事件才会捕获。否则就是丢弃直接丢给子view处理
        @Override
        public int getViewHorizontalDragRange(View child) {
            //让CaptureView可以点击
            //返回拖拽子View在相应方向上可以被拖动的最远距离，默认为0
            return getMeasuredWidth() - child.getMeasuredWidth();
        }

        //获取View垂直方向的拖拽范围
        @Override
        public int getViewVerticalDragRange(View child) {
            //让CaptureView可以点击
            //返回拖拽子View在相应方向上可以被拖动的最远距离，默认为0
            return getMeasuredHeight() - child.getMeasuredHeight();
        }

        //边界拖动时回调
        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
//            if (edgeFlags == ViewDragHelper.EDGE_LEFT) {
//                mDragHelper.captureChildView(mDragView1, pointerId);
//            }
            if (mDragEdge) {
                mDragHelper.captureChildView(mDragView1, pointerId);
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (releasedChild == mDragView1) {
                //以松手前的滑动速度为初值，让捕获到的子View自动滚动到指定位置，只能在Callback的onViewReleased()中使用
                mDragHelper.settleCapturedViewAt(initPointPosition.x, initPointPosition.y);
                //flingCapturedView 以松手前的滑动速度为初值，让捕获到的子View在指定范围内fling惯性运动
                //abort 中断动画
                invalidate();
            }
            /*int centerLeft = getMeasuredWidth() / 2 - releasedChild.getMeasuredWidth() / 2;
            if (releasedChild.getLeft() < centerLeft) {
                //在左半边，应该向左缓慢移动,不用scroller，ViewDragHelper已封装好
                mDragHelper.smoothSlideViewTo(releasedChild, 0, releasedChild.getTop());
                //仍需要刷新！
                ViewCompat.postInvalidateOnAnimation(DragLayout.this);
//                scroller.startScroll();
//                invalidate();

            } else {
                //在右半边，向右缓慢移动
                mDragHelper.smoothSlideViewTo(releasedChild, getMeasuredWidth() - releasedChild.getMeasuredWidth(), releasedChild.getTop());
                ViewCompat.postInvalidateOnAnimation(DragLayout.this);
            }*/
        }

        /**
         * 控制child在垂直方向的移动(边界）
         *
         * @param child
         * @param top   ViewDragHelper会将当前child的top值改变成返回的值
         * @param dy    相较于上一次child在水平方向上移动的
         * @return
         */
        //拖拽的子View在所属方向上移动的位置，child为拖拽的子View，left为子view应该到达的x坐标，dx为挪动差值
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (anyDrag || mDragVertical) {
                final int topBound = getPaddingTop();
                final int bottomBound = getHeight() - mDragView1.getHeight();
                final int newTop = Math.min(Math.max(top, topBound), bottomBound);
                return newTop;
            }
            return super.clampViewPositionVertical(child, top, dy);
        }

        /**
         * 控制child在水平方向的移动（边界）
         *
         * @param child
         * @param left  ViewDragHelper会将当前child的left值改变成返回的值
         * @param dx    相较于上一次child在水平方向上移动的
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            /*if (left < 0) {
                //限制左边界
                left = 0;
            } else if (left > (getMeasuredWidth() - child.getMeasuredWidth())) {
                //限制右边界
                left = getMeasuredWidth() - child.getMeasuredWidth();
            }*/
            if (anyDrag || mDragHorizontal || mDragCapture || mDragEdge) {
                //让滑动的范围在屏幕内：如果left的值 在leftbound和rightBound之间 那么就返回left
                //如果left的值 比 leftbound还要小 那么就说明 超过了左边界 那我们只能返回给他左边界的值
                //如果left的值 比rightbound还要大 那么就说明 超过了右边界，那我们只能返回给他右边界的值
                final int leftBound = getPaddingLeft();
                final int rightBound = getWidth() - mDragView1.getWidth();
                final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
                return newLeft;
            }
            return super.clampViewPositionHorizontal(child, left, dx);
        }
    }

    //invalidate() —> draw() —>computeScroll()
    @Override
    public void computeScroll() {
        //判断是否完成了整个滑动
        //动画移动会回调continueSettling(boolean)来动态刷新界面
        //在调用settleCapturedViewAt()、flingCapturedView()和smoothSlideViewTo()时，
        // 该方法返回true，一般重写父view的computeScroll方法，进行该方法判断
        if (mDragHelper.continueSettling(true)) {
            //ViewCompat.postInvalidateOnAnimation(DragLayout.this);
            invalidate();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        initPointPosition.x = mDragView1.getLeft();
        initPointPosition.y = mDragView1.getTop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mDragHelper.processTouchEvent(ev);
        //消费掉此事件，自己来处理
        return true;
    }
}
