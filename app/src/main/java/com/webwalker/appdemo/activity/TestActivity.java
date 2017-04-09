package com.webwalker.appdemo.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.webwalker.appdemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//临时测试使用
public class TestActivity extends Activity {
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.textView)
    TextView textView;

    private static Pattern domainPattern = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button1)
    public void clickDomain() {
        complieDomainRegex();
        boolean result = isForbiddenDomain(editText.getText().toString());
        textView.setText(result ? "禁止" : "放行");
    }

    private static void complieDomainRegex() {
        List<String> whiteList = new ArrayList<>();
        whiteList.add("ymatou.com");
        whiteList.add("baidu.com");
        whiteList.add("alipay.com");
        whiteList.add("shengpay.com");

        if (whiteList == null || whiteList.size() == 0) return;
        StringBuilder sb = new StringBuilder();
        for (String s : whiteList) {
            sb.append(s).append("|");
        }
        String domains = sb.toString();
        if (domains.endsWith("|")) {
            domains = domains.substring(0, domains.length() - 1);
        }
        domainPattern = Pattern.compile(".*(?:" + domains + ")$");
    }

    //check is forbidden url domain
    public static boolean isForbiddenDomain(String url) {
        if (TextUtils.isEmpty(url)) return false;
        Uri uri = Uri.parse(url);
        String host = uri.getHost().toLowerCase();
        if (host.indexOf("ymatou.com") > -1) return false;
        if (domainPattern == null) return false;

        Matcher matcher = domainPattern.matcher(host);
        if (!matcher.find()) return true;
        return false;
    }
}
