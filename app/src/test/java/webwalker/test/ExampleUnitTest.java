package webwalker.test;

import com.webwalker.framework.dns.TimerDnsPolicy;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
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