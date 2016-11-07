package com.webwalker.appdemo.activity;

import android.os.Bundle;

import com.webwalker.appdemo.R;

public class CoordinatorActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_layout);

        init();
    }

    private void init() {
//        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "FAB", Snackbar.LENGTH_LONG)
//                        .setAction("cancel", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                //这里的单击事件代表点击消除Action后的响应事件
//                            }
//                        })
//                        .show();
//            }
//        });
    }

    @Override
    public String getLabel() {
        return "Coordinator";
    }
}
