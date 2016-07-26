package com.webwalker.appdemo.web;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.EditText;

import com.webwalker.appdemo.activity.BaseActivity;
import com.webwalker.appdemo.R;
import com.webwalker.framework.utils.MessageUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

public class WebViewActivity extends BaseActivity {
    @InjectView(R.id.webView)
    WebView webView;
    @InjectView(R.id.etUrl)
    EditText etUrl;
    Response response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.btnOpen)
    public void clickOpen() {
        webView.loadUrl(etUrl.getText().toString());
    }

    @OnClick(R.id.btnRequest)
    public void clickRequest() {
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                //.tlsVersions(TlsVersion.TLS_1_2)
                .build();
        final OkHttpClient client = new OkHttpClient.Builder()
                .followSslRedirects(true)
                .protocols(Arrays.asList(Protocol.HTTP_2, Protocol.HTTP_1_1, Protocol.SPDY_3))
                //.connectionSpecs(Collections.singletonList(spec))
                .retryOnConnectionFailure(true)
                .connectTimeout(25, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(30, 120, TimeUnit.SECONDS))
                .build();

        final Request request = new Request.Builder()
                .url(etUrl.getText().toString())
                .build();
        // Execute the request and retrieve the response.
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                System.out.println(response.protocol());
                System.out.println(response.body().string());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MessageUtil.showShortToast(WebViewActivity.this, response.protocol() + "");
                    }
                });
            }
        });
    }
}
