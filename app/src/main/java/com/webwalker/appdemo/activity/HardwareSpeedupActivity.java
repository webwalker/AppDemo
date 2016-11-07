package com.webwalker.appdemo.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.webwalker.appdemo.R;
import com.webwalker.appdemo.common.Urls;
import com.webwalker.appdemo.web.MyWebChromeClient;
import com.webwalker.appdemo.web.MyWebViewClient;
import com.webwalker.framework.widget.web.WebViewUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HardwareSpeedupActivity extends BaseActivity {
    @Bind(R.id.webView1)
    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hardware_speedup);
        ButterKnife.bind(this);

        WebSettings settings = WebViewUtils.initWebView(wv);
        wv.setWebViewClient(new MyWebViewClient());
        wv.setWebChromeClient(new MyWebChromeClient());
        wv.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        wv.loadUrl(Urls.SINA);
    }

    @Override
    public String getLabel() {
        return "硬件加速";
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wv.canGoBack()) {
            wv.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @JavascriptInterface
    public void test() {

    }
}
