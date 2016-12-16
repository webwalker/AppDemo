package com.webwalker.appdemo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webwalker.appdemo.R;
import com.webwalker.appdemo.activity.BaseActivity;

import java.util.List;

/**
 * Created by xujian on 2016/8/22.
 */
public class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.MyViewHolder> {
    private List<BaseActivity> items;
    private Context context;

    public MyRecycleAdapter(Context context, List<BaseActivity> list) {
        this.context = context;
        this.items = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder view, int position) {
        final BaseActivity item = items.get(position);
        view.textView.setText(item.getLabel());
        view.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, item.getClass());
                if (item.params != null) {
                    intent.putExtra("params", item.params);
                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.recycle_item);
        }
    }
}