package com.webwalker.appdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.tencent.smtt.sdk.QbSdk;
import com.webwalker.appdemo.activity.AesActivity;
import com.webwalker.appdemo.activity.AesRsaActivity;
import com.webwalker.appdemo.activity.AnimationActivity;
import com.webwalker.appdemo.activity.BaseActivity;
import com.webwalker.appdemo.activity.CoordinatorActivity;
import com.webwalker.appdemo.activity.DragActivity;
import com.webwalker.appdemo.activity.JniActivity;
import com.webwalker.appdemo.activity.PaintActivity;
import com.webwalker.appdemo.activity.PatchActivity;
import com.webwalker.appdemo.activity.RecycleViewActivity;
import com.webwalker.appdemo.activity.SlideDragActivity;
import com.webwalker.appdemo.activity.SnackbarActivity;
import com.webwalker.appdemo.activity.TextViewActivity;
import com.webwalker.appdemo.activity.UIActivity;
import com.webwalker.appdemo.activity.WebTestActivity;
import com.webwalker.appdemo.activity.YoutubeActivity;
import com.webwalker.appdemo.adapter.MyRecycleAdapter;
import com.webwalker.appdemo.common.Params;
import com.webwalker.appdemo.views.TestItem;
import com.webwalker.appdemo.views.TestItem2;
import com.webwalker.appdemo.views.TestView;
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

        //proguard test
        TestView tv = new TestView(this);
        TestItem ti = new TestItem();
        TestItem.TestItem2 item2 = new TestItem.TestItem2();
        TestItem.TestItem2.TestItem3 item3 = new TestItem.TestItem2.TestItem3();
        TestItem.TestItem2.TestItem3.TestItem4 item4 = new TestItem.TestItem2.TestItem3.TestItem4();
        TestItem2 t2 = new TestItem2();

        init();
    }

    public void init() {
        recyclerview.setAdapter(new MyRecycleAdapter(this, getActivities()));
        recyclerview.setLayoutManager(new GridLayoutManager(this, 3));
    }

    public List<BaseActivity> getActivities() {
        List<BaseActivity> list = new ArrayList<>();
        list.add(new AesActivity());
        list.add(new AesRsaActivity());

        list.add(new CoordinatorActivity());
        list.add(new X5Activity());
        list.add(new JniActivity());
        list.add(new RecycleViewActivity());
        list.add(new WebTestActivity());
        list.add(new PatchActivity());
        list.add(new TextViewActivity());
        list.add(new DragActivity(Params.get().label("水平拖拽").params("horizontal", true)));
        list.add(new DragActivity(Params.get().label("垂直拖拽").params("vertical", true)));
        list.add(new DragActivity(Params.get().label("拖拽边缘").params("edge", true)));
        list.add(new DragActivity(Params.get().label("拖拽对象").params("capture", true)));
        list.add(new DragActivity(Params.get().label("自由拖拽").params("any", true)));
        list.add(new DragActivity(Params.get().label("一起拖拽")
                .params("any", true)
                .params("moveTogether", true)));
        list.add(new YoutubeActivity());
        list.add(new SlideDragActivity());
        list.add(new UIActivity(Params.get().label("下拉滑动").layout(R.layout.view_drag_layout)));
        list.add(new UIActivity(Params.get().label("Drag侧滑").layout(R.layout.drag_slide_view)));
        list.add(new SnackbarActivity());
        list.add(new PaintActivity(Params.get().label("绘图")));
        list.add(new AnimationActivity(Params.get().label("动画")));

        return list;
    }

    @Override
    public String getLabel() {
        return "";
    }
}
