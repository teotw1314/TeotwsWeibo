package com.skyland.sky_data.base;

import rx.Subscriber;

/**
 * Created by skyland on 2017/7/12
 */

public class NetworkSubscriber<T> extends Subscriber<T> {

    private NetworkListener listener;

    public NetworkSubscriber(NetworkListener setListener){
        this.listener = setListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        listener.onError(e);
    }

    @Override
    public void onNext(T t) {
        listener.onSuccess(t);
    }
}
