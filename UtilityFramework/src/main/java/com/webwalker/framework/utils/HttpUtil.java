package com.webwalker.framework.utils;

import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

/**
 * @author xu.jian
 */
public class HttpUtil {
    private static final String TAG = "HttpUtils";
    public static final int HTTP_REQUEST_TYPE_GET = 0;
    public static final int HTTP_REQUEST_TYPE_POST = 1;
    private static final String CHARSET = "UTF-8";
    private static final int TIME_OUT = 10 * 10000000; // 超时时间
    public static final String SUCCESS = "1";
    public static final String FAILURE = "0";

    public static boolean connectUrl(String url, int timeout) {
        boolean ret = false;
        HttpURLConnection httpUrl = null;
        long beginTime = SystemClock.uptimeMillis();
        long endTime = beginTime;

        while ((endTime - beginTime) < timeout) {
            try {
                httpUrl = (HttpURLConnection) new URL(url).openConnection();
                httpUrl.setConnectTimeout(timeout / 3);
                httpUrl.connect();
                ret = true;
                break;
            } catch (Throwable e) {
                e.printStackTrace();
            } finally {
                if (null != httpUrl) {
                    httpUrl.disconnect();
                }
                httpUrl = null;
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            endTime = SystemClock.uptimeMillis();
        }
        return ret;
    }


    /**
     * * android上传文件到服务器
     *
     * @param file       需要上传的文件
     * @param RequestURL 请求的rul
     * @return 返回响应的内容
     */
    public static String uploadFile(String RequestURL, String type,
                                    Map<String, String> maps, File file) {
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        try {
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET);
            // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
                    + BOUNDARY);
            conn.setRequestProperty("userId", maps.get("userId"));

            if (file != null) {
                /** * 当文件不为空，把文件包装并且上传 */
                OutputStream outputSteam = conn.getOutputStream();
                DataOutputStream dos = new DataOutputStream(outputSteam);
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */
                sb.append("Content-Disposition: form-data; name=\"" + type
                        + "\"; filename=\"" + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                        .getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应码 200=成功 当响应成功，获取响应的流
                 */
                int res = conn.getResponseCode();
                Log.e(TAG, "response code:" + res);
                if (res == 200) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(),
                                    "utf-8"));// 设置编码,否则中文乱码
                    StringBuilder resp = new StringBuilder();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        // line = new String(line.getBytes(), "utf-8");
                        resp.append(line);
                    }
                    reader.close();
                    return resp.toString();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FAILURE;
    }

    public static void uploadFile(String actionUrl, Bitmap bitmap) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            byte[] buffer = out.toByteArray();

            URL url = new URL(actionUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
			/* 设置传送的method=POST */
            con.setRequestMethod("POST");
			/* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
			/* 设置DataOutputStream */
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " + "name=\"file\";"
                    + end);
            ds.writeBytes(end);
            ds.write(buffer);
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            ds.flush();

            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            ds.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void uploadFile(String actionUrl, String uploadFilePath,
                                  String newName) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(actionUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
			/* 设置传送的method=POST */
            con.setRequestMethod("POST");
			/* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
			/* 设置DataOutputStream */
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; "
                    + "name=\"file\";filename=\"" + newName + "\"" + end);
            ds.writeBytes(end);
			/* 取得文件的FileInputStream */
            FileInputStream fStream = new FileInputStream(uploadFilePath);
			/* 设置每次写入1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
			/* 从文件读取数据至缓冲区 */
            while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
            fStream.close();
            ds.flush();
			/* 取得Response内容 */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
			/* 将Response显示于Dialog */
            System.out.println("上传成功" + b.toString().trim());
			/* 关闭DataOutputStream */
            ds.close();
        } catch (Exception e) {
            System.out.println("上传失败" + e);
        }
    }
}
