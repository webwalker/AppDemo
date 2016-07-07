package com.webwalker.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by xujian on 2016/7/6.
 */
public class MainClassTest {
    public static void main(String[] args) {
        start();
    }

    public static void start() {
        final List<String> domains = new ArrayList<>();
        domains.add("www.ymatou.com");
        domains.add("app.ymatou.com");
        domains.add("ymtlog.ymatou.com");
        domains.add("s1.ymatou.com");
        domains.add("img.ymatou.com");

        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                for (int j = 0; j < domains.size(); j++) {
                    TimerDnsPolicy policy = new TimerDnsPolicy();
                    policy.resolve(domains.get(j));
                }
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }
}
