package com.webwalker.appdemo.activity;

import android.os.Bundle;
import android.view.View;

import com.webwalker.appdemo.R;
import com.webwalker.framework.utils.MessageUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;
import webwalker.frameworkui.drag.DragLayout;

/**
 * 参考http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2014/0911/1680.html
 *
 * ViewDragHelper实现QQ5.0侧滑并处理与ViewPager的滑动冲突 http://www.w2bc.com/Article/54439
 * ViewDragHelper实现QQ6.0http://blog.csdn.net/z240336124/article/details/53003337?ref=myrecommend
 * 侧滑 http://blog.csdn.net/u012551350/article/details/51601985
 */
public class DragActivity extends BaseActivity {
    private static int index = 0;

    public DragActivity() {
    }

    public DragActivity(Bundle params, String label) {
        super(params);
        this.label = label;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
        ButterKnife.bind(this);

        DragLayout dragLayout = (DragLayout) findViewById(R.id.dragLayout);
        if (getIntent().getBooleanExtra("horizontal", false)) {
            dragLayout.setDragHorizontal(true);
        }
        if (getIntent().getBooleanExtra("vertical", false)) {
            dragLayout.setDragVertical(true);
        }
        if (getIntent().getBooleanExtra("edge", false)) {
            dragLayout.setDragEdge(true);
        }
        if (getIntent().getBooleanExtra("capture", false)) {
            dragLayout.setDragCapture(true);
        }
        if (getIntent().getBooleanExtra("any", false)) {
            dragLayout.setAnyDrag(true);
        }
        if (getIntent().getBooleanExtra("moveTogether", false)) {
            dragLayout.setMoveTogether(true);
        }
    }

    @OnClick({R.id.drag1})
    public void click(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.drag1:
                MessageUtil.shortToast(this, "click me1");
                break;
            case R.id.drag2:
                MessageUtil.shortToast(this, "click me2");
                break;
        }
    }

    @Override
    public String getLabel() {
        return label;
    }
}
