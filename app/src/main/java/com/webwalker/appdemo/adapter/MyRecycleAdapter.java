package com.webwalker.appdemo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.webwalker.appdemo.R;
import com.webwalker.appdemo.model.Product;
import com.webwalker.framework.utils.ImageUnity;

import java.util.List;

/**
 * Created by xujian on 2016/8/22.
 */
public class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.MyViewHolder> {
    private List<Product> products;

    public MyRecycleAdapter(List<Product> list) {
        products = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder view, int position) {
        Product product = products.get(position);
        ImageUnity.getLoader().displayImage(product.getImg(), view.imageView);
        view.textView.setText(product.getTitle());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.recycle_item_img);
            textView = (TextView) itemView.findViewById(R.id.recycle_item_title);
        }

    }

}