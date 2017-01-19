package com.webwalker.framework.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.webwalker.framework.App;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 设备信息工具类
 */
public class DeviceUtil {
    private static int screenWidth = 0;

    /**
     * 获得应用类型 android, ios
     *
     * @return android
     */
    public static String getDeviceType() {
        return "android";
    }

    /**
     * 获得国际移动设备身份码
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /**
     * 获得国际移动用户识别码
     *
     * @param context
     * @return
     */
    public static String getIMSI(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
    }

    /**
     * 获得设备屏幕矩形区域范围
     *
     * @param context
     * @return
     */
    public static Rect getScreenRect(Context context) {
        if (context == null) {
            return new Rect(0, 0, 0, 0);
        }
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int w = display.getWidth();
        int h = display.getHeight();
        return new Rect(0, 0, w, h);
    }

    public static int getScreenHeight(Context context) {
        if (context == null) {
            return 800;
        }
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return display.getHeight();
    }

    public static int getScreenWidth(Context context) {
        if (screenWidth > 0) return screenWidth;
        if (context == null) return 0;
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        screenWidth = display.getWidth();
        return screenWidth;
    }

    /**
     * 获得设备屏幕密度
     */
    public static float getScreenDensity(Context context) {
        DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
        return metrics.density;
    }

    public static int getScreenDensityDpi(Context context) {
        DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
        return (int) (metrics.density * 160);
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = App.get().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * @param spValue
     * @return sp转为px
     */
    public static int sp2px(float spValue) {
        final float fontScale = App.get().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获得android_id
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    /**
     * 获得deviceId
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        return getIMEI(context);
    }

    /**
     * 获得屏幕尺寸
     *
     * @param context
     * @return
     */
    public static String getResolution(Context context) {
        Rect rect = getScreenRect(context);
        return rect.right + "x" + rect.bottom;
    }

    public static String getSerialNumber() {
        String serialNumber = "";

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serialNumber = (String) get.invoke(c, "ro.serialno");

            if (serialNumber.equals("")) {
                serialNumber = "?";
            }
        } catch (Exception e) {
            if (serialNumber.equals("")) {
                serialNumber = "?";
            }
        }

        return serialNumber;
    }

    public static PackageInfo getPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getClass().getPackage().getName(), PackageManager.GET_CONFIGURATIONS);
        } catch (NameNotFoundException e) {
        }
        return null;
    }

    public static boolean isOpenGPS(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        return locationManager != null
                && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    /**
     * Get height of status bar
     *
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        Rect outRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        if (outRect.top > 0)
            return outRect.top;

        int dpi = getScreenDensityDpi(activity);
        if (dpi == DisplayMetrics.DENSITY_LOW) {
            return 24;
        } else if (dpi == DisplayMetrics.DENSITY_MEDIUM) {
            return 32;
        } else {
            return 48;
        }
    }

    public static String int2ip(int ipInt) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipInt & 0xFF).append(".");
        sb.append((ipInt >> 8) & 0xFF).append(".");
        sb.append((ipInt >> 16) & 0xFF).append(".");
        sb.append((ipInt >> 24) & 0xFF);
        return sb.toString();
    }

    /**
     * 获取当前ip地址
     *
     * @param context
     * @return
     */
    public static String getLocalIpAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (wifiInfo != null) {
                    int i = wifiInfo.getIpAddress();
                    return int2ip(i);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";

    }

    /**
     * 获取本机的mac地址
     *
     * @param context
     * @return
     */
    public static String getLocalMacAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (wifiInfo != null) {
                    return wifiInfo.getMacAddress();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 获取手机所连路由器mac地址
     *
     * @param context
     * @return
     */
    public static String getRouterMacAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (wifiInfo != null) {
                    return wifiInfo.getBSSID();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 判断当前应用是否在前台运行
     *
     * @return
     */
    public static boolean isRunningForeground() {
        Context appContext = App.get();
        String packageName = appContext.getPackageName();
        String topActivityClassName = getTopActivityName(appContext);
        return packageName != null && topActivityClassName != null && (topActivityClassName.startsWith(packageName) || topActivityClassName.startsWith("com.vipshop.cart"));
    }

    public static String getTopActivityName(Context context) {
        String topActivityClassName = null;
        ActivityManager activityManager =
                (ActivityManager) (context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topActivityClassName = f.getClassName();
        }
        return topActivityClassName;
    }


    public static String getNetworkType() {
        ConnectivityManager cm = (ConnectivityManager) App.get()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo info = cm.getActiveNetworkInfo();
        if (null == info)
            return "";
        if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            return "wifi";
        } else {
            return "3g";
        }
    }

    public static String getCustomChannelName() {
        ApplicationInfo appinfo = App.get().getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        String key = "META-INF/" + "ymtb";
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.startsWith(key)) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String[] split = ret.split("\\.");
        if (split != null && split.length >= 2) {
            return ret;
        } else {
            return "";
        }
    }

    private static String customChannelId = null;

    public static String getCustomChannelId() {
        if (null == customChannelId) {
            String fullChannelName = getCustomChannelName();
            if (TextUtils.isEmpty(fullChannelName)) {
                customChannelId = "";
            } else {
                String[] split = fullChannelName.split("\\.");
                if (null != split && split.length > 1) {
                    customChannelId = split[1];
                } else {
                    customChannelId = "";
                }
            }
        }
        return customChannelId;
    }
}
