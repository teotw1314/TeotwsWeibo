package com.skyland.teotwsweibo.module.account;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.skyland.lib_webview.activity.WebViewSubActivity;
import com.skyland.lib_webview.core.WebViewBaseActivity;
import com.skyland.sky_data.base.NetworkListener;
import com.skyland.sky_data.bean.AccessTokenInfo;
import com.skyland.sky_data.bean.UserInfo;
import com.skyland.sky_data.network.SkyNetwork;
import com.skyland.teotwsweibo.R;
import com.skyland.teotwsweibo.base.BaseSubActivity;
import com.skyland.teotwsweibo.module.home.HomeActivity;
import com.skyland.teotwsweibo.utils.AccountUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skyland on 2017/7/12
 */

public class AccountManagerActivity extends BaseSubActivity {
    private static final String TAG = "Account";

    private RecyclerView recyclerView;
    private Button btnAddAccount;
    private ProgressDialog progressDialog;

    private AccountListAdapter adapter;
    private LinearLayoutManager layoutManager;

    private List<AccountInfo> accountList = new ArrayList<>();
    private String accessToken;
    private String uid;

    public static void startActivity(Context setContext) {
        Intent intent = new Intent(setContext, AccountManagerActivity.class);
        setContext.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.lib_session_activity_account_manager);
    }

    @Override
    protected void initToolbar() {
        toolbar.setNavigationIcon(R.mipmap.lib_common_ic_navigation_back);
        toolbar.setTitle("账号管理");
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_account_manager);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new AccountListAdapter(accountList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                onClickAccountItem(accountList.get(position));
            }
        });

        btnAddAccount = (Button) findViewById(R.id.btn_add_account);
        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushOauthActivity();
            }
        });
    }

    @Override
    protected void initLogic() {
        refreshAccountList();
    }

    private void refreshAccountList() {
        accountList.clear();
        accountList.addAll(AccountUtils.getDefault().getAccountList(this));
        Log.e(TAG, "refreshAccountList: " + accountList.size() );
        adapter.notifyDataSetChanged();
    }

    private void onClickAccountItem(AccountInfo accountInfo) {
        AccountUtils.getDefault().setCurrentAccount(this, accountInfo);
        HomeActivity.startActivity(this, accountInfo.uid);
    }

    /**
     *
     */
    private void pushOauthActivity() {
        String oauthUrl = "https://api.weibo.com/oauth2/authorize"
                + "?client_id=" + SkyNetwork.getDefault().getAppKey()
                + "&redirect_uri=" + SkyNetwork.getDefault().getCallbackUrl()
                + "&response_type=code"
                + "&display=mobile/";
        WebViewSubActivity.startActivity(this, null, oauthUrl, true);
    }

    /**
     * 获取access token
     *
     * @param setCode
     */
    private void getAccessToken(String setCode) {
        showDialog();
        SkyNetwork.getDefault().getAccessToken(setCode, new NetworkListener<AccessTokenInfo>() {
            @Override
            public void onError(Throwable e) {

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

    /**
     * 获取用户信息
     *
     * @param setAccessToken
     * @param setUid
     */
    private void getUserInfo(String setAccessToken, String setUid) {
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

    private void getUserInfoSuccess(UserInfo userInfo) {
        Log.e(TAG, "getUserInfoSuccess: " + userInfo.name);
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.refresh_token = this.accessToken;
        accountInfo.uid = this.uid;
        accountInfo.name = userInfo.name;
        accountInfo.header_url = userInfo.avatar_hd;
        AccountUtils.getDefault().setAccount(this, accountInfo);
        refreshAccountList();
        hideProgress();
    }

    /**
     * 显示loading动画
     */
    private void showDialog() {
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在获取用户信息.......");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }

    /**
     * 隐藏loading动画
     */
    private void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == WebViewBaseActivity.REQUEST_CODE) {
                String code = data.getStringExtra(WebViewBaseActivity.RESULT_CODE_KEY);
                Log.e(TAG, "onActivityResult: code:" + code);
                getAccessToken(code);
            }
        }
    }
}
