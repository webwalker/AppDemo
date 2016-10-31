package com.webwalker.appdemo.model;

/**
 * Created by xujian on 2016/8/22.
 */
public class Product {
    private String img;
    private String title;

    public Product(String img, String title) {
        this.img = img;
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}