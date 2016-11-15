package webwalker.frameworkui.textview;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by bjmaoqisheng on 2016/11/14.
 */
public class ExpandTextView extends TextView {
    private static final String TAG = "ExpandTextView";
    public static int STATE_NUll = 0;
    public static int STATE_OPENED = 1;
    public static int STATE_CLOSE = 2;
    public static int DEFAULT_CLOSE_LINE_NUM = 2;
    public static String DEFAULT_MORE_CONTENT = " 点击加载更多...";

    public int currentState = STATE_NUll;
    /**
     * 折叠时候的行数 min 1
     */
    protected int closeLineNum = DEFAULT_CLOSE_LINE_NUM;
    /**
     * 首次折叠状态监听
     */
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;
    /**
     * 保存原本的text
     */
    private String originString;
    private String moreContext = DEFAULT_MORE_CONTENT;

    public ExpandTextView(Context context) {
        super(context);

    }


    public ExpandTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExpandTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public int getCloseLineNum() {
        return closeLineNum;
    }

    public void setCloseLineNum(int closeLineNum) throws ExpandNumException {
        if (closeLineNum <= 0) {
            throw initExpandNumException();
        }
        this.closeLineNum = closeLineNum;
    }

    private void init() {
        add2LineListener();
    }

    /**
     *
     */
    public void setTextContentAdd2LineListener(String text) {
        add2LineListener();
        this.setText(text);
    }

    /**
     * STATE_CLOSE 状态监听器
     */
    private void add2LineListener() {
        removeGlobalListener();
        onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                currentState = STATE_CLOSE;
                removeGlobalListener();
                try {
                    updateCloseView();
                } catch (ExpandNumException e) {
                    e.printStackTrace();
                }
            }
        };

        ViewTreeObserver viewTreeObserver = this.getViewTreeObserver();
        viewTreeObserver
                .addOnGlobalLayoutListener(onGlobalLayoutListener
                );
    }

    /**
     * 折叠时候更新
     *
     * @throws ExpandNumException closeLineNum must >0
     */
    public void updateCloseView() throws ExpandNumException {
        if (currentState != STATE_CLOSE) return;
        Log.d(TAG, "onGlobalLayout() called");
        int lineCount = ExpandTextView.this.getLineCount();
        if (lineCount > closeLineNum) {
            originString = ExpandTextView.this.getText().toString();


            String more = getMoreContext();

            Field privateStringField = null;
            TextPaint paint = null;
            try {
                privateStringField = TextView.class.getDeclaredField("mTextPaint");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            if (privateStringField != null) {
                privateStringField.setAccessible(true);
                try {
                    Object o = privateStringField.get(this);
                    if (o != null && o instanceof Paint) {
                        paint = (TextPaint) o;
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (paint == null) return;

            float desiredWidth = Layout.getDesiredWidth(more, paint);
            int lastLineEnd = 0;
            if (closeLineNum == 0) {
                //// TODO: 2016/11/14
                ExpandNumException expandNumException = initExpandNumException();
                throw expandNumException;
            }
            if (closeLineNum >= 1) {
                lastLineEnd = ExpandTextView.this.getLayout().getLineEnd(closeLineNum - 2);
            }
            int lineEnd = ExpandTextView.this.getLayout().getLineEnd(closeLineNum - 1);
            int start = lastLineEnd;
            int end = lineEnd;
            int morePosition = lastLineEnd;
            for (int j = end; j >= start; j--) {
                String tempString = originString.substring(j, end);
                float tempWidth = Layout.getDesiredWidth(tempString, paint);
                if (tempWidth > desiredWidth) {
                    morePosition = j;
                    break;
                }
            }

            String substring = originString.substring(0, morePosition);

            substring += more;
            setText(substring);
        }
    }

    @NonNull
    private String getMoreContext() {
        return moreContext;
    }

    public void setMoreContext(String moreContext) {
        if (moreContext == null) this.moreContext = DEFAULT_MORE_CONTENT;
        this.moreContext = moreContext;
    }

    private ExpandNumException initExpandNumException() {
        return buildExpandNumException("closeLineNum must >=1");
    }

    @NonNull
    private ExpandNumException buildExpandNumException(String detailMessage) {
        return new ExpandNumException(detailMessage);
    }

    /**
     * 展示时候更新
     */
    public void updateOpenedView() {
        if (originString != null) {
            setText(originString);
        }
    }


    public synchronized void removeGlobalListener() {
        ViewTreeObserver viewTreeObserver = this.getViewTreeObserver();
        if (onGlobalLayoutListener != null) {
            viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener);
            onGlobalLayoutListener = null;
        }
    }

    /**
     *
     */
    public void setOpen() {
        if (currentState == STATE_OPENED) return;
        currentState = STATE_OPENED;
        updateOpenedView();
    }

    /**
     * 设置折叠
     */
    public void setClose() {
        if (currentState == STATE_CLOSE) return;
        currentState = STATE_CLOSE;
        try {
            this.updateCloseView();
        } catch (ExpandTextView.ExpandNumException e) {
            e.printStackTrace();
        }
    }

    /**
     * 是否是展开状态
     *
     * @return
     */
    public boolean isOpened() {
        if (currentState == STATE_OPENED) return true;
        return false;
    }

    /**
     * 是否是折叠状态
     *
     * @return
     */
    public boolean isClose() {
        if (currentState == STATE_CLOSE) return true;
        return false;
    }

    /**
     * 移除监听，设置状态为 STATE_NUll，和一般的TextView 一样了。
     */
    public void resetState() {
        removeGlobalListener();
        currentState = STATE_NUll;
        requestLayout();

    }

    public class ExpandNumException extends Exception {
        public ExpandNumException(String detailMessage) {
            super(detailMessage);
        }
    }

}
