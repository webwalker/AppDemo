package com.webwalker.test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xujian on 2016/7/6.
 */
public class TimerDnsPolicy {
    private int resolveInterval = 1 * 60 * 1000; //10分钟
    private static final ConcurrentHashMap<String, DnsDomain> domains = new ConcurrentHashMap<>();

    public static String formatCurrentDate(String format) {
        String date = new SimpleDateFormat(format).format(new Date());
        return date;
    }

    public void resolve(String domainName) {
        Log("--------------waiting<" + domainName + ">----------------");
        if (!domains.containsKey(domainName)) {
            Log("init dns >>> [" + domainName + "]" + formatCurrentDate("yyyy-MM-dd HH:mm:ss"));
            parseDomain(domainName);
            return;
        }
        DnsDomain domain = domains.get(domainName);
        long timeDiff = System.currentTimeMillis() - domain.lastResolveTime;
        if (timeDiff >= resolveInterval) {
            Log("update dns >>> [" + domainName + "]" + formatCurrentDate("yyyy-MM-dd HH:mm:ss"));
            parseDomain(domainName);
        }
    }

    private void parseDomain(String domainName) {
        InetAddress address = getDomainAddress(domainName);
        if (address != null) {
            DnsDomain domain = new DnsDomain();
            domain.name = domainName;
            domain.ip = address.getHostAddress();
            domain.lastResolveTime = System.currentTimeMillis();
            domains.put(domainName, domain);
        }
    }

    private InetAddress getDomainAddress(String domainName) {
        try {
            Log("getDomainAddress [" + domainName + "]");
            InetAddress myServer = InetAddress.getByName(domainName);
            return (myServer);
        } catch (UnknownHostException e) {
        }
        return null;
    }

    // 取得LOCALHOST的IP地址
    private InetAddress getLocalIP() {
        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
        }
        return null;
    }

    private void Log(String msg) {
        System.out.print(msg);
        System.out.println();
        //Log.d(TAG, msg);
    }

    public static class DnsDomain {
        public String name;
        public long lastResolveTime;
        public String ip;
    }
}
