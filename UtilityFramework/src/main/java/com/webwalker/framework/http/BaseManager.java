//package com.webwalker.framework.http;
//
//import com.ymatou.shop.reconstract.base.YMTRequestUtil;
//import com.ymatou.shop.reconstract.base.utils.YMTAPIParamsUtil;
//
//import org.json.JSONObject;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import okhttp3.MediaType;
//import okhttp3.RequestBody;
//
//import static com.ymt.framework.okhttp.OkNetCore.encodeParameters;
//
///**
// * Created by xujian on 2016/11/14.
// */
//public class BaseManager<T> {
//    public T service;
//    private String ENCODE_TYPE = "UTF-8";
//    public static MediaType OCTET_TYPE = MediaType.parse("application/octet-stream");
//    public static MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
//    public static MediaType FORM_URLEN = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
//
//    public BaseManager() {
//    }
//
//    //一个manager对应一个service时
//    public BaseManager(Class<T> clzss) {
//        service = (T) RetrofitUtils.getInstance().create(clzss);
//    }
//
//    //不限定一一对应
//    public <T> T getService(Class<T> clzss) {
//        return (T) RetrofitUtils.getInstance().create(clzss);
//    }
//
//    public Map<String, String> params() {
//        return new HashMap<>();
//    }
//
//    public Map<String, String> oldParams(Map<String, String> param) {
//        return YMTRequestUtil.getBaseParams(param);
//    }
//
//    //兼容老的header
//    public Map<String, String> oldheader(Map<String, String> param) {
//        return YMTAPIParamsUtil.getRetrofitHeaders(param, "");
//    }
//
//    public Map<String, String> header(String version) {
//        return header(new HashMap<String, String>(), version);
//    }
//
//    //get header
//    public Map<String, String> header(Map<String, String> param, String version) {
//        return YMTAPIParamsUtil.getRetrofitHeaders(param, version);
//    }
//
//    //post header
//    public Map<String, String> header(JSONObject jsonParams) {
//        return header(jsonParams, "");
//    }
//
//    public Map<String, String> header(JSONObject jsonParams, String version) {
//        return header(jsonParams, null, version);
//    }
//
//    //带签名对象的头
//    public Map<String, String> header(JSONObject jsonParams, JSONObject signObject, String version) {
//        return YMTAPIParamsUtil.getRetrofitPostHeaders(null == signObject ? jsonParams : signObject, version);
//    }
//
//    public RequestBody body(JSONObject json) {
//        return RequestBody.create(JSON_TYPE, json.toString());
//    }
//
//    public RequestBody body() {
//        return RequestBody.create(null, "");
//    }
//
//    public RequestBody formBody(Map<String, String> params) {
//        return RequestBody.create(FORM_URLEN, encodeParameters(params));
//    }
//
//    public RequestBody body(String json) {
//        return RequestBody.create(MediaType.parse("application/json"), json);
//    }
//
//    public RequestBody body(Map<String, String> params) {
//        return RequestBody.create(MediaType.parse("application/json"), encodeParameters(params));
//    }
//}
