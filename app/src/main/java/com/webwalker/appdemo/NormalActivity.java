package com.webwalker.appdemo;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.webwalker.appdemo.common.Urls;
import com.webwalker.appdemo.web.MyWebChromeClient;
import com.webwalker.appdemo.web.MyWebViewClient;
import com.webwalker.framework.utils.FileUtil;
import com.webwalker.framework.widget.web.WebViewUtils;

public class NormalActivity extends BaseActivity {
    WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);

        wv = aq.id(R.id.webView1).getWebView();
        Button button1 = (Button) findViewById(R.id.button1);
        WebSettings settings = WebViewUtils.initWebView(wv);
        wv.setWebViewClient(new MyWebViewClient());
        wv.setWebChromeClient(new MyWebChromeClient());

        String data = FileUtil.read(this, Urls.LOCAL_TEST);

        String url = "http://www.utanbaby.com/active/guimilianmengdahui?from=timeline&isappinstalled=0";

        // wv.loadDataWithBaseURL("file:///", data, "text/html", "utf-8", null);
        wv.loadUrl(url);

        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String js = "javascript:(function(){var ev = document.createEvent('MouseEvents');ev.initMouseEvent('click', true, true, window, 1, 0, 0, 0, 0, false, false, false, false, 0, null);"
                        + "document.getElementById('votePiaoBtn_110').dispatchEvent(ev);})()";
                // String js =
                // "javascript:alert(HTMLSpanElement.prototype.click);";
                wv.loadUrl(js);
            }
        });
    }

}
