package com.webwalker.appdemo.web;

import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.webwalker.framework.utils.MessageUtil;

/**
 * @author xu.jian
 *
 */
public class MyWebChromeClient extends WebChromeClient {
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        MessageUtil.showShortToast(view.getContext(), message);
        result.confirm();
        return true;
    }
}
