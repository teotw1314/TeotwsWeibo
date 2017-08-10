package com.skyland.sky_data.network;


import android.animation.ObjectAnimator;

import com.skyland.sky_data.bean.AccessTokenInfo;
import com.skyland.sky_data.bean.StatusInfo;
import com.skyland.sky_data.bean.UserInfo;
import com.skyland.sky_data.result.StatusResult;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by skyland on 2017/7/10
 */

public interface NetworkService {

    @FormUrlEncoded
    @POST("oauth2/access_token")
    Observable<AccessTokenInfo> getAccessToken(
            @Field("client_id") String setAppKey,
            @Field("client_secret") String setAppSecret,
            @Field("grant_type") String setType,
            @Field("code") String setCode,
            @Field("redirect_uri") String setCallBackUrl
    );

    @GET("2/users/show.json")
    Observable<UserInfo> getUserInfo(
            @Query("access_token") String setAccessToken,
            @Query("uid") String setUid
    );

    @GET("2/statuses/home_timeline.json")
    Observable<StatusResult> getTimeline(
            @Query("access_token") String setAccessToken,
            @Query("since_id") String setSinceId,
            @Query("max_id") String setMaxId,
            @Query("count") int setCount,
            @Query("page") int setPage,
            @Query("trim_user") int setTrimUser
    );


}
