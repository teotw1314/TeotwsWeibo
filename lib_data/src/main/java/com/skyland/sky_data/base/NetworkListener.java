package com.skyland.sky_data.base;

/**
 * Created by skyland on 2017/7/12
 */

public interface NetworkListener<T> {
    void onError(Throwable e);

    void onSuccess(T t);
}
