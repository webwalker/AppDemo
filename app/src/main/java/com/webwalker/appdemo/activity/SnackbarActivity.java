package com.webwalker.appdemo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import com.webwalker.appdemo.R;

public class SnackbarActivity extends BaseActivity {
    CoordinatorLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snackbar);
        container = (CoordinatorLayout) findViewById(R.id.container);
    }

    public void createSnackbar(View v) {
        Snackbar snackbar =
                Snackbar.make(container, "SnackbarTest", Snackbar.LENGTH_SHORT).setAction("点击我", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar snackbar =
                                Snackbar.make(container, "我被点击了", Snackbar.LENGTH_SHORT);
                        setSnackbarMessageTextColor(snackbar, Color.parseColor("#FF0000"));
                        snackbar.show();
                    }
                });
        setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
        snackbar.show();
    }

    public static void setSnackbarMessageTextColor(Snackbar snackbar, int color) {
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }

    @Override
    public String getLabel() {
        return "Snackbar";
    }
}
