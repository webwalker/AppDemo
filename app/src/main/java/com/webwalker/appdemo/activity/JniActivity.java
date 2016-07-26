package com.webwalker.appdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.webwalker.appdemo.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class JniActivity extends AppCompatActivity {
    @InjectView(R.id.tvResult)
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jni);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.btnJni)
    public void clickJni() {
        String result = TestEntry.getStrFromC();
        tvResult.setText(result);
    }
}
