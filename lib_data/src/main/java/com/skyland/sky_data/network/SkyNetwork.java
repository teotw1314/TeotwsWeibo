package com.skyland.sky_data.network;

import com.skyland.sky_data.base.C;
import com.skyland.sky_data.base.NetworkListener;
import com.skyland.sky_data.base.NetworkSubscriber;
import com.skyland.sky_data.bean.AccessTokenInfo;
import com.skyland.sky_data.bean.UserInfo;
import com.skyland.sky_data.result.StatusResult;

import java.util.logging.StreamHandler;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by skyland on 2017/7/10
 */

@SuppressWarnings("unused")
public class SkyNetwork {

    private static SkyNetwork instance;

    private NetworkService networkService;


    public static SkyNetwork getDefault() {
        if (instance == null) {
            instance = new SkyNetwork();
        }
        return instance;
    }

    public SkyNetwork() {
        /**
         * 日志输出
         */
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        /**
         * 构建
         */
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.weibo.com/")
                .build();
        networkService = retrofit.create(NetworkService.class);
    }

    public String getAppKey() {
        return C.WEIBO_APP_KEY;
    }

    public String getAppSecret() {
        return C.WEIBO_APP_SECRET;
    }

    public String getCallbackUrl() {
        return C.WEIBO_CALLBACK_URL;
    }

    /**
     * 插入观察者
     *
     * @param observable
     * @param <T>
     */
    private <T> void setSubscribeOn(Observable<T> observable, NetworkListener<T> listener) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NetworkSubscriber<T>(listener));
    }


    public void getAccessToken(String setCode, NetworkListener<AccessTokenInfo> listener) {
        Observable<AccessTokenInfo> observable = networkService.getAccessToken(C.WEIBO_APP_KEY, C.WEIBO_APP_SECRET, C.WEIBO_TYPE, setCode, C.WEIBO_CALLBACK_URL);
        setSubscribeOn(observable, listener);
    }

    public void getUserInfo(String setAccessToken, String setUid, NetworkListener<UserInfo> listener) {
        Observable<UserInfo> observable = networkService.getUserInfo(setAccessToken, setUid);
        setSubscribeOn(observable, listener);
    }


    public void getTimeline(String setAccessToken, String setSinceId, String setMaxId, int setCount, int setPage, NetworkListener<StatusResult> listener) {
        Observable<StatusResult> observable = networkService.getTimeline(setAccessToken, setSinceId, setMaxId, setCount, setPage, 0);
        setSubscribeOn(observable, listener);

    }




}
