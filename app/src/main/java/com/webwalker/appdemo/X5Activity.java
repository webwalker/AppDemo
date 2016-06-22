package com.webwalker.appdemo;

import android.app.Activity;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class X5Activity extends Activity {
    WebView wv;
    //http://sq0.ymatou.com/forBuyerApp/discover
    //https://http2.akamai.com/
    String url = "http://sq0.ymatou.com/forBuyerApp/discover";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_x5);

        wv = (WebView) findViewById(R.id.webView);
        wv.setWebViewClient(new MyWebViewClient());
        wv.requestFocus();

        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);
        //settings.setBlockNetworkLoads(true);
        /*settings.setBlockNetworkImage(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setJavaScriptEnabled(true);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setSupportZoom(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDatabasePath("/");
        settings.setAppCachePath("/");
        settings.setAppCacheEnabled(true);
        settings.setAppCacheMaxSize(50 * 1024 * 1024);*/

        //wv.loadUrl("http://evt.ymatou.com/activity_4688_Capp?forBuyerApp_needJumpFlag=1&UserId=395528&AccessToken=7A14222AD0A4A56E5DCF417AEACB125F60304602F4C43BE38F908A44CB4182976613D3A87C875652E5B5920D55D40486EFF7ECF048BF89AB&AccessToken=7A14222AD0A4A56E5DCF417AEACB125F60304602F4C43BE38F908A44CB4182976613D3A87C875652E5B5920D55D40486EFF7ECF048BF89AB&DeviceToken=865982029005926&UserId=395528");
//        wv.loadUrl("http://www.baidu.com");
//        wv.loadUrl("http://sq0.ymatou.com:80/forBuyerApp/discover?AccessToken=B5220EAD274C427A11F4EF4F836C741533EDEE862E7C10AABF6615326F1E2F14FC5071A401257D9125BDD10C23A4D6E17C8BB37633030FBF&DeviceToken=866962020250787&UserId=4085");
//        Uri uri = Uri.parse("http://sq0.ymatou.com:80/forBuyerApp/discover?AccessToken=B5220EAD274C427A11F4EF4F836C741533EDEE862E7C10AABF6615326F1E2F14FC5071A401257D9125BDD10C23A4D6E17C8BB37633030FBF&DeviceToken=866962020250787&UserId=4085");
        //wv.loadUrl("http://item.app.ymatou.com/forYmatouApp/product/pid?pid=f1579879-4738-40b9-b94a-0caed64f308e&price=134&shareTitle=%E6%9D%AD%E5%B7%9E%E4%BF%9D%E7%A8%8E%E4%BB%93%E7%8E%B0%E8%B4%A7%20%E5%BE%B7%E5%9B%BD%E7%88%B1%E4%BB%96%E7%BE%8EAptamil%E5%A5%B6%E7%B2%891+%20%E5%8E%9F%E8%A3%85%20600g/%E7%9B%92%20%E5%A9%B4%E5%84%BF%E5%A5%B6%E7%B2%89&shareUrl=http://matouapp.ymatou.com/forYmatouApp/product/jump/f1579879-4738-40b9-b94a-0caed64f308e&sharePicUrl=http://p5.img.ymatou.com/upload/product/small/128e6b67c3f5492bb63e7b15f2182c1f_s.jpg&shareTip=undefined&title=%E5%95%86%E5%93%81%E8%AF%A6%E6%83%85");
        wv.loadUrl(url);
        openPage();
        long endTime = System.currentTimeMillis();
        long result = (MainActivity.startTime - endTime) / 1000;
        if (isX5Core()) {
            Toast.makeText(this, "X5:" + result, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Default" + result, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        wv.destroy();
        wv = null;
        finish();
    }

    public boolean isX5Core() {
        if (wv == null) return false;
        //if (wv.getX5WebViewExtension() != null) return true;
        return false;
    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Toast.makeText(X5Activity.this, description, Toast.LENGTH_SHORT).show();
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            super.onReceivedSslError(webView, sslErrorHandler, sslError);
            sslErrorHandler.proceed();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    private void openPage() {
        Button button = (Button) findViewById(R.id.button);
        final EditText etUrl = (EditText) findViewById(R.id.etUrl);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wv.loadUrl(url);
            }
        });
    }
}
