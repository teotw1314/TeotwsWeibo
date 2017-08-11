package com.skyland.teotwsweibo.module.account;

import android.app.Activity;
import android.content.Intent;

import com.skyland.teotwsweibo.BasePresenter;
import com.skyland.teotwsweibo.BaseView;

/**
 * Created by skyland on 2017/8/10
 */

@SuppressWarnings("unused")
public interface AccountContract {

    interface View extends BaseView<Presenter>{

        void showToast(String setMessage);

        void refreshAccounts();

        void showProgress();

        void hideProgress();
    }


    interface Presenter extends BasePresenter{
        void startOauthActivity(Activity setActivity);

        void getAccessToken(String setCode);

        void getUserInfo(String setAccessToken, String setUid);

        void onResult(int requestCode, Intent data);

        void onClickAccountItem(AccountInfo accountInfo);
    }



}
