package com.webwalker.appdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.webwalker.appdemo.R;
import com.webwalker.appdemo.common.Urls;
import com.webwalker.appdemo.web.MyWebChromeClient;
import com.webwalker.appdemo.web.MyWebViewClient;
import com.webwalker.framework.utils.FileUtil;
import com.webwalker.framework.widget.web.WebViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebTestActivity extends BaseActivity {
    @BindView(R.id.etUrl)
    EditText etUrl;
    @BindView(R.id.btnOpen)
    Button btnOpen;
    @BindView(R.id.linearLayout)
    RelativeLayout linearLayout;
    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_test);
        ButterKnife.bind(this);

        init();
    }

    public void init() {
        WebSettings settings = WebViewUtils.initWebView(webView);
        settings.setJavaScriptEnabled(true);

        //webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
    }

    @OnClick({R.id.btnOpen})
    public void click(View view) {
        String url = etUrl.getText().toString();

        if (view.getId() == R.id.btnOpen) {
            webView.loadUrl(url);

            String data = FileUtil.read(this, Urls.LOCAL_TEST);
            // wv.loadDataWithBaseURL("file:///", data, "text/html", "utf-8", null);
//            String js = "javascript:(function(){var ev = document.createEvent('MouseEvents');ev.initMouseEvent('click', true, true, window, 1, 0, 0, 0, 0, false, false, false, false, 0, null);"
//                    + "document.getElementById('votePiaoBtn_110').dispatchEvent(ev);})()";
            // String js =
            // "javascript:alert(HTMLSpanElement.prototype.click);";
            //webView.loadUrl(js);
        }
    }

    @Override
    public String getLabel() {
        return "WebTest";
    }
}
