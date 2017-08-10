package com.skyland.teotwsweibo.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skyland.teotwsweibo.module.account.AccountInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skyland on 2017/8/1
 */

@SuppressWarnings("unused")
public class AccountUtils {

    private static final String TAG = "account";

    private static final String DEFAULT_ACCOUNT = "kkkk";
    private static final String KEY_CURRENT_ACCOUNT = "current_account_key";
    private static final String KEY_ACCOUNT_LIST = "account_list_key";


    public static AccountUtils instance;

    public static AccountUtils getDefault() {
        if (instance == null) {
            instance = new AccountUtils();
        }
        return instance;
    }

    public List<AccountInfo> getAccountList(Context setContext) {
        String accountJson = (String) SkySharedPreferencesUtils.getDefault().getSharedPreferences(setContext, KEY_ACCOUNT_LIST, DEFAULT_ACCOUNT);
        if (!TextUtils.equals(accountJson, DEFAULT_ACCOUNT)) {
            ArrayList<AccountInfo> accountList = new Gson().fromJson(accountJson, new TypeToken<ArrayList<AccountInfo>>() {
            }.getType());
            if (accountList != null) {
                return accountList;
            }
        }
        return new ArrayList<AccountInfo>();
    }

    public void setAccount(Context setContext, AccountInfo setAccountInfo) {
        Log.e(TAG, "setAccount: " + setAccountInfo.uid + "__" + setAccountInfo.name );
        if (setAccountInfo == null || TextUtils.isEmpty(setAccountInfo.uid)) {
            return;
        }
        setCurrentAccount(setContext, setAccountInfo);
        List<AccountInfo> accountList = getAccountList(setContext);
        for (int i = 0; i < accountList.size(); i++) {
            if (TextUtils.equals(accountList.get(i).uid, setAccountInfo.uid)) {
                accountList.remove(i);
                break;
            }
        }
        accountList.add(0, setAccountInfo);
        String accountJson = new Gson().toJson(accountList);
        SkySharedPreferencesUtils.getDefault().setSharedPreferences(setContext, KEY_ACCOUNT_LIST, accountJson);
    }

    public void setCurrentAccount(Context setContext, AccountInfo setAccountInfo) {
        Gson gson = new Gson();
        String current = gson.toJson(setAccountInfo);
        SkySharedPreferencesUtils.getDefault().setSharedPreferences(setContext, KEY_CURRENT_ACCOUNT, current);
    }

    public AccountInfo getCurrentAccount(Context setContext) {
        String current = (String) SkySharedPreferencesUtils.getDefault().getSharedPreferences(setContext, KEY_CURRENT_ACCOUNT, DEFAULT_ACCOUNT);
        if (TextUtils.equals(current, DEFAULT_ACCOUNT)) {
            return null;
        } else {
            Gson gson = new Gson();
            return gson.fromJson(current, AccountInfo.class);
        }
    }


}
