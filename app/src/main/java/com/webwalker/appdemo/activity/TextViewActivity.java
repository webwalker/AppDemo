package com.webwalker.appdemo.activity;

import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.webwalker.appdemo.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import webwalker.frameworkui.textview.ExpandTextView;

public class TextViewActivity extends BaseActivity {
    @Bind(R.id.tvMore)
    TextView tvMore;
    @Bind(R.id.ivMore)
    ImageView ivMore;
    @Bind(R.id.tv_content)
    ExpandTextView tvContent;

    private boolean isShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textview);
        ButterKnife.bind(this);

        textLines();
        expandTextView();
    }

    //textview 函数控制折叠显示与隐藏 http://www.cnblogs.com/zhy7104/p/5961411.html
    private void textLines() {
        tvMore.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int line = tvMore.getLineCount();
                if (line > 2 && !isShow) {
                    tvMore.setLines(2);
                    tvMore.setEllipsize(TextUtils.TruncateAt.END);
                    ivMore.setVisibility(View.VISIBLE);
                } else {
                    ivMore.setVisibility(View.INVISIBLE);
                }

                Layout layout = tvMore.getLayout();
                //layout.getLineEnd() 获得第一行文本 填充后的字符串索引位置
                if (layout.getEllipsisCount(line - 1) > 0) {
                    ivMore.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
    }

    //readme中有更好的实现
    private void expandTextView() {
        tvContent.setText(getString(R.string.text1));
        tvContent.setClose();
        tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvContent.isClose()) {
                    tvContent.setOpen();
                } else if (tvContent.isOpened()) {
                    tvContent.setClose();

                } else {
                    tvContent.setOpen();
                }

            }
        });
    }

    @Override
    public String getLabel() {
        return "TextView";
    }
}
