package com.webwalker.appdemo.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xujian on 2016/12/16.
 */
public class Params implements Serializable {
    private static final String layoutId = "layoutId";
    private static final String label = "label";
    private Map<String, Object> params = new HashMap<>();

    public static Params get() {
        return new Params();
    }

    public Params params(String key, Object value) {
        params.put(key, value);
        return this;
    }

    public int getLayout() {
        return getParams(layoutId);
    }

    public String getLabel() {
        return getParams(label);
    }

    public Params layout(int value) {
        params(layoutId, value);
        return this;
    }

    public Params label(String value) {
        params(label, value);
        return this;
    }

    public <T> T getParams(String key) {
        if (params.containsKey(key)) {
            return (T) params.get(key);
        }
        return null;
    }

    public boolean getBoolean(String key) {
        if (params.containsKey(key)) {
            return (boolean) params.get(key);
        }
        return false;
    }

    public int getInt(String key) {
        if (params.containsKey(key)) {
            return (int) params.get(key);
        }
        return -1;
    }

    public String getString(String key) {
        if (params.containsKey(key)) {
            return (String) params.get(key);
        }
        return null;
    }
}
