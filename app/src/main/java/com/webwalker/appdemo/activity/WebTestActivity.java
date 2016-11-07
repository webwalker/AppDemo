package com.webwalker.appdemo.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.webwalker.appdemo.R;
import com.webwalker.appdemo.web.MyWebChromeClient;
import com.webwalker.appdemo.web.MyWebViewClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebTestActivity extends BaseActivity {
    @Bind(R.id.etUrl)
    EditText etUrl;
    @Bind(R.id.btnOpen)
    Button btnOpen;
    @Bind(R.id.linearLayout)
    RelativeLayout linearLayout;
    @Bind(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_test);
        ButterKnife.bind(this);

        init();
    }

    public void init() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());
    }

    @OnClick({R.id.btnOpen})
    public void click(View view) {
        String url = etUrl.getText().toString();

        if (view.getId() == R.id.btnOpen) {
            webView.loadUrl(url);
        }
    }

    @Override
    public String getLabel() {
        return "WebTest";
    }
}
