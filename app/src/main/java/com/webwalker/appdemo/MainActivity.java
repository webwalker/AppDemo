package com.webwalker.appdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tencent.smtt.sdk.QbSdk;
import com.webwalker.appdemo.activity.BaseActivity;
import com.webwalker.appdemo.activity.CoordinatorActivity;
import com.webwalker.appdemo.activity.JniActivity;
import com.webwalker.appdemo.activity.RecycleViewActivity;
import com.webwalker.appdemo.activity.WebTestActivity;
import com.webwalker.appdemo.adapter.MyRecycleAdapter;
import com.webwalker.appdemo.web.FirstLoadingX5Service;
import com.webwalker.appdemo.web.X5Activity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;

    public static long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        startTime = System.currentTimeMillis();
        if (!QbSdk.isTbsCoreInited()) {
            Intent intent = new Intent(this, FirstLoadingX5Service.class);
            startService(intent);
        }

        init();
    }

    public void init() {
        recyclerview.setAdapter(new MyRecycleAdapter(this, getActivities()));
        recyclerview.setLayoutManager(new GridLayoutManager(this, 3));
    }

    public List<BaseActivity> getActivities() {
        List<BaseActivity> list = new ArrayList<>();
        list.add(new CoordinatorActivity());
        list.add(new X5Activity());
        list.add(new JniActivity());
        list.add(new RecycleViewActivity());
        list.add(new WebTestActivity());

        return list;
    }

    @Override
    public String getLabel() {
        return "";
    }
}
