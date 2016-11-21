//package com.webwalker.framework.http;
//
//import android.content.Intent;
//import android.support.v4.content.LocalBroadcastManager;
//import android.text.TextUtils;
//
//import com.ymatou.shop.reconstract.base.YMTAPIRequestUtil;
//import com.ymt.framework.app.App;
//import com.ymt.framework.http.model.NewBaseResult;
//import com.ymt.framework.http.volley.YMTAPIStatus;
//import com.ymt.framework.http.volley.YMTApiCallback;
//import com.ymt.framework.okhttp.OkNetCore;
//import com.ymt.framework.utils.JsonUtil;
//
//import java.io.IOException;
//
//import retrofit2.Response;
//import rx.Observable;
//import rx.Observer;
//import rx.Subscriber;
//import rx.Subscription;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.schedulers.Schedulers;
//
///**
// * 此类没法继承Observable重写使用，因RxJavaCallAdapterFactory中做了限定:rawType != Observable.class
// * Created by xujian on 2016/11/9.
// */
//public class YmtObservable<T> {
//    private YMTApiCallback callback;
//    private Subscription subscription;
//    private Observable<Response<T>> observable;
//
//    public YmtObservable(Observable<Response<T>> observable) {
//        this.observable = observable;
//    }
//
//    public YmtObservable call(Subscriber subscriber) {
//        this.subscription = observable.subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
//        return this;
//    }
//
//    public YmtObservable get(final YMTApiCallback callback) {
//        return call(callback);
//    }
//
//    public YmtObservable post(final YMTApiCallback callback) {
//        return call(callback);
//    }
//
//    public YmtObservable call(final YMTApiCallback callback) {
//        if (!preCheck(callback)) return null;
//        this.callback = callback;
//        this.subscription = observable.subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Response<T>>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        onResponseError(e);
//                    }
//
//                    @Override
//                    public void onNext(Response<T> t) {
//                        onResponse(t);
//                    }
//                });
//        return this;
//    }
//
//    public Observable<Response<T>> getObservable() {
//        return observable;
//    }
//
//    /**
//     * 用于取消一个任务，或者封装一个SerialSubscription来管理URL和subscription的关系
//     * 再通过查找进行任务取消
//     */
//    public Subscription getSubscription() {
//        return subscription;
//    }
//
//    private boolean preCheck(YMTApiCallback callback) {
//        return !YMTAPIRequestUtil.disposeNoNetworkRequest(callback);
//    }
//
//    private void onResponseError(Throwable e) {
//        e.printStackTrace();
//        final YMTAPIStatus status = new YMTAPIStatus(500, "请求网络失败");
//        callback.onFailed(status);
//    }
//
//    private boolean onResponse(Response<T> response) {
//        OkNetCore.newInstance().addNativePointHttpRequest(response.raw());
//        int code = response.code();
//        T data = response.body();
//        if (data == null) {
//            throw (new NullPointerException());
//        }
//        if (response.isSuccessful() && code == 200) {
//            Intent intent;
//            if (data instanceof NewBaseResult) {
//                NewBaseResult result = (NewBaseResult) data;
//                if (result.status == 401) {
//                    intent = new Intent("401");
//                    LocalBroadcastManager.getInstance(App.get()).sendBroadcast(intent);
//                }
//            } else if (data instanceof com.ymt.framework.http.model.BaseResult) {
//                com.ymt.framework.http.model.BaseResult result = (com.ymt.framework.http.model.BaseResult) data;
//                if (result.Status == 401) {
//                    intent = new Intent("401");
//                    LocalBroadcastManager.getInstance(App.get()).sendBroadcast(intent);
//                }
//            }
//            callback.onSuccess(data);
//        } else {
//            OkNetCore.ErrModel errModel = null;
//            try {
//                String errorResult = response.errorBody().string();
//                errModel = JsonUtil.fromJson(errorResult, OkNetCore.ErrModel.class);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            String msg;
//            if (errModel != null) {
//                msg = TextUtils.isEmpty(errModel.Message) ? "请求网络失败" : errModel.Message;
//            } else {
//                msg = "请求网络失败";
//            }
//            YMTAPIStatus status = new YMTAPIStatus(code, msg);
//            callback.onFailed(status);
//        }
//        return true;
//    }
//}
