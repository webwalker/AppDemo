package webwalker.test;

import com.webwalker.framework.dns.TimerDnsPolicy;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xujian on 2016/7/6.
 */
public class MainClassTest {
    public static void main(String[] args) {
        System.out.print(".....");

        final List<String> domains = new ArrayList<>();
        domains.add("www.ymatou.com");
        domains.add("app.ymatou.com");
        domains.add("ymtlog.ymatou.com");
        domains.add("s1.ymatou.com");
        domains.add("img.ymatou.com");

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5 * 60; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int j = 0; j < domains.size(); j++) {
                        TimerDnsPolicy policy = new TimerDnsPolicy();
                        policy.resolve(domains.get(j));
                    }
                }
            }
        }).start();
    }
}
