package com.skyland.teotwsweibo.module.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.skyland.lib_webview.activity.WebViewSubActivity;
import com.skyland.lib_webview.core.WebViewBaseActivity;
import com.skyland.sky_data.base.NetworkListener;
import com.skyland.sky_data.bean.AccessTokenInfo;
import com.skyland.sky_data.bean.UserInfo;
import com.skyland.sky_data.network.SkyNetwork;
import com.skyland.teotwsweibo.module.home.HomeActivity;
import com.skyland.teotwsweibo.utils.AccountUtils;
/**
 * Created by skyland on 2017/8/10
 */

public class AccountPresenter implements AccountContract.Presenter {

    private static final String TAG = "account presenter";

    private Context context;
    private AccountContract.View accountView;

    private String accessToken;
    private String uid;

    public AccountPresenter(@NonNull Context context, @NonNull AccountContract.View accountView) {
        this.accountView = accountView;
        this.context = context;
        this.accountView.setPresenter(this);
    }

    @Override
    public void start() {
        accountView.refreshAccounts();
    }

    @Override
    public void startOauthActivity(Activity setActivity) {
        String oauthUrl = "https://api.weibo.com/oauth2/authorize"
                + "?client_id=" + SkyNetwork.getDefault().getAppKey()
                + "&redirect_uri=" + SkyNetwork.getDefault().getCallbackUrl()
                + "&response_type=code"
                + "&display=mobile/";
        WebViewSubActivity.startActivity(setActivity, null, oauthUrl, true);
    }

    @Override
    public void getAccessToken(String setCode) {
        accountView.showProgress();
        SkyNetwork.getDefault().getAccessToken(setCode, new NetworkListener<AccessTokenInfo>() {
            @Override
            public void onError(Throwable e) {
                // TODO: 2017/8/11 get access token on error
            }

            @Override
            public void onSuccess(AccessTokenInfo accessTokenInfo) {
                Log.e(TAG, "onSuccess: " + accessTokenInfo.uid);
                accessToken = accessTokenInfo.access_token;
                uid = accessTokenInfo.uid;
                getUserInfo(accessTokenInfo.access_token, accessTokenInfo.uid);
            }
        });
    }

    @Override
    public void getUserInfo(String setAccessToken, String setUid) {
        SkyNetwork.getDefault().getUserInfo(setAccessToken, setUid, new NetworkListener<UserInfo>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(UserInfo userInfo) {
                getUserInfoSuccess(userInfo);
            }
        });
    }

    @Override
    public void onResult(int requestCode, Intent data) {
        if (requestCode == WebViewBaseActivity.REQUEST_CODE) {
            String code = data.getStringExtra(WebViewBaseActivity.RESULT_CODE_KEY);
            Log.e(TAG, "onActivityResult: code:" + code);
            getAccessToken(code);
        }
    }

    @Override
    public void onClickAccountItem(AccountInfo accountInfo) {
        AccountUtils.getDefault().setCurrentAccount(context, accountInfo);
        HomeActivity.startActivity(context, accountInfo.uid);
        accountView.finishView();
    }


    private void getUserInfoSuccess(UserInfo userInfo) {
        Log.e(TAG, "getUserInfoSuccess: " + userInfo.name);
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.refresh_token = this.accessToken;
        accountInfo.uid = this.uid;
        accountInfo.name = userInfo.name;
        accountInfo.header_url = userInfo.avatar_hd;
        AccountUtils.getDefault().addAccount(context, accountInfo);
        accountView.refreshAccounts();
        accountView.hideProgress();
    }
}
