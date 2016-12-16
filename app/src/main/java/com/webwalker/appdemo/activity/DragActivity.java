package com.webwalker.appdemo.activity;

import android.os.Bundle;
import android.view.View;

import com.webwalker.appdemo.R;
import com.webwalker.appdemo.common.Params;
import com.webwalker.framework.utils.MessageUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;
import webwalker.frameworkui.drag.DragLayout;

/**
 * 参考http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2014/0911/1680.html
 * <p>
 * ViewDragHelper实现QQ5.0侧滑并处理与ViewPager的滑动冲突 http://www.w2bc.com/Article/54439
 * ViewDragHelper实现QQ6.0http://blog.csdn.net/z240336124/article/details/53003337?ref=myrecommend
 * 侧滑 http://blog.csdn.net/u012551350/article/details/51601985
 */
public class DragActivity extends BaseActivity {
    public DragActivity() {
    }

    public DragActivity(Params params) {
        super(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);
        ButterKnife.bind(this);

        DragLayout dragLayout = (DragLayout) findViewById(R.id.dragLayout);
        if (params.getBoolean("horizontal")) {
            dragLayout.setDragHorizontal(true);
        }
        if (params.getBoolean("vertical")) {
            dragLayout.setDragVertical(true);
        }
        if (params.getBoolean("edge")) {
            dragLayout.setDragEdge(true);
        }
        if (params.getBoolean("capture")) {
            dragLayout.setDragCapture(true);
        }
        if (params.getBoolean("any")) {
            dragLayout.setAnyDrag(true);
        }
        if (params.getBoolean("moveTogether")) {
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
        return super.getLabels();
    }
}
