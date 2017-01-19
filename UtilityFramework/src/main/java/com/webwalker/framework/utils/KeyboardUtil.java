package com.webwalker.framework.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import java.util.ArrayList;

/**
 * Created by xujian on 2016/12/20.
 */
public class KeyboardUtil {
    private Activity context;
    private ArrayList<OnKeyboardStateChangedListener> stateListeners;      //软键盘状态监听列表
    private ViewTreeObserver.OnGlobalLayoutListener layoutChangeListener;
    private View decorView;

    public KeyboardUtil(Activity context) {
        this.context = context;
        this.decorView = context.getWindow().getDecorView();
    }

    public interface OnKeyboardStateChangedListener {
        void OnStateChanged(boolean visible, int keyboardHeight, int displayHeight);
    }

    //注册软键盘状态变化监听
    public void addChangedListener(OnKeyboardStateChangedListener listener) {
        if (listener != null) {
            stateListeners.add(listener);
        }
    }

    //取消软键盘状态变化监听
    public void removeChangedListener(OnKeyboardStateChangedListener listener) {
        if (listener != null) {
            stateListeners.remove(listener);
        }
    }

    public void listen() {
        stateListeners = new ArrayList<>();
        layoutChangeListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            int previousKeyboardHeight = -1;

            @Override
            public void onGlobalLayout() {
                //判断窗口可见区域大小
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHeight = rect.bottom;// - rect.top;
                int screenHeight = decorView.getHeight(); //屏幕高度
                int heightDifference = screenHeight - displayHeight;
                //可见性
                if (previousKeyboardHeight != heightDifference) {
                    //如果屏幕高度和Window可见区域高度差值大于整个屏幕高度的1/3，则表示软键盘显示中，否则软键盘为隐藏状态。
                    //boolean visible = heightDifference > screenHeight / 3;
                    boolean hide = (double) displayHeight / screenHeight > 0.8;
                    notifyListener(!hide, heightDifference, displayHeight);
                }
                previousKeyboardHeight = screenHeight;
            }
        };
        //注册布局变化监听
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(layoutChangeListener);
    }

    private void notifyListener(boolean visible, int keyboardHeight, int displayHeight) {
        for (int i = 0; i < stateListeners.size(); i++) {
            OnKeyboardStateChangedListener listener = stateListeners.get(i);
            listener.OnStateChanged(visible, keyboardHeight, displayHeight);
        }
    }

    public void unListen(Activity context) {
        //移除布局变化监听
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            decorView.getViewTreeObserver().removeOnGlobalLayoutListener(layoutChangeListener);
        } else {
            decorView.getViewTreeObserver().removeGlobalOnLayoutListener(layoutChangeListener);
        }
    }
}
