package com.webwalker.appdemo.views;

import java.io.Serializable;

/**
 * Created by xujian on 2016/12/8.
 */

public class TestItem implements Serializable {
    public String name;

    public static class TestItem2 {
        public String name;

        public static class TestItem3 {
            public String name;

            public static class TestItem4 {
                public String name;
            }
        }
    }
}
