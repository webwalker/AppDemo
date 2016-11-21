//package com.webwalker.framework.http;
//
//import com.ymatou.shop.reconstract.base.constants.UrlConstants;
//import com.ymt.framework.app.App;
//
//import java.io.File;
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.util.HashMap;
//import java.util.concurrent.TimeUnit;
//
//import okhttp3.Cache;
//import okhttp3.Interceptor;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;
//
///**
// * Retrofit, RxJava 简单封装，保留原始面貌
// *
// * @Url 替换url
// * @Query@QueryMap 替换url中查询参数
// * @Header 替换header
// * @Field@FieldMap 替换post请求body中参数
// * @Part@PartMap
// * @FormUrlEncoded post请求需要加的方法注解
// * @POST() 标示该方法为post请求
// * @GET() 标示该方法为get请求
// * @BODY() Created by xujian on 2016/11/9.
// */
//public class RetrofitUtils {
//    private static final int DEFAULT_TIMEOUT = 20;
//    private Retrofit retrofit;
//    private static RetrofitUtils instance;
//    private HashMap<String, Object> services;
//
//    private RetrofitUtils() {
//        initRetrofit();
//    }
//
//    public static synchronized RetrofitUtils getInstance() {
//        if (instance == null) {
//            instance = new RetrofitUtils();
//        }
//        return instance;
//    }
//
//    private void initRetrofit() {
//        services = new HashMap<>();
//        OkHttpClient client = createHttpClient();
//        retrofit = new Retrofit.Builder()
//                .baseUrl(UrlConstants.BaseUrl)
//                //非Json采用ScalarsConverterFactory.create()
//                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(StringConverterFactory.create())
//                //为支持rxjava,需添加下面这个把Retrofit转成RxJava可用的适配类
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .client(client)
//                .build();
//    }
//
//    public <T> T create(Class<T> clzss) {
//        if (services.containsKey(clzss.getName())) {
//            return (T) services.get(clzss.getName());
//        } else {
//            Object service = retrofit.create(clzss);
//            services.put(clzss.getName(), service);
//            return (T) service;
//        }
//    }
//
//    public <T> T create(Class<T> clzss, OkHttpClient client) {
//        if (services.containsKey(clzss.getName())) {
//            return (T) services.get(clzss.getName());
//        } else {
//            Object service = createService(clzss, client);
//            services.put(clzss.getName(), service);
//            return (T) service;
//        }
//    }
//
//    private <T> T createService(Class<T> clzss, OkHttpClient client) {
//        String baseUrl = "";
//        try {
//            Field field1 = clzss.getField("BaseUrl");
//            baseUrl = (String) field1.get(clzss);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.getMessage();
//            e.printStackTrace();
//        }
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .client(client)
//                .build();
//        return retrofit.create(clzss);
//    }
//
//    //default okhttp client
//    public OkHttpClient createHttpClient() {
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        File cacheDirectory = new File(App.get().getCacheDir().getAbsolutePath(), "HttpCache");
//        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
//        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
//        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
//        builder.retryOnConnectionFailure(true);
//        builder.cache(new Cache(cacheDirectory, 10 * 1024 * 1024));
//        builder.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Interceptor.Chain chain) throws IOException {
//                Request request = chain.request().newBuilder()
//                        //.addHeader("version", "1.0.0")
//                        .header("Connection", "close")
//                        .header("retrofit", "2.1.0") //测试使用
//                        .build();
//                return chain.proceed(request);
//            }
//        });
//        //http log interceptor
//        /*if (AppConfig.getInstance().isDebug()) {
//            HttpLoggingInterceptor httpLog = new HttpLoggingInterceptor();
//            httpLog.setLevel(HttpLoggingInterceptor.Level.BODY);
//            builder.addInterceptor(httpLog);
//        }*/
//        //builder.addNetworkInterceptor()
//        return builder.build();
//    }
//}
