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
import android.widget.Toast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.skyland.teotwsweibo.R;
import com.skyland.teotwsweibo.base.BaseSubActivity;
import com.skyland.teotwsweibo.utils.AccountUtils;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by skyland on 2017/7/12
 */

public class AccountManagerActivity extends BaseSubActivity implements AccountContract.View{
    private static final String TAG = "Account";

    private AccountContract.Presenter presenter;

    private RecyclerView recyclerView;
    private Button btnAddAccount;
    private ProgressDialog progressDialog;

    private AccountListAdapter adapter;
    private LinearLayoutManager layoutManager;

    private List<AccountInfo> accountList = new ArrayList<>();

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
        recyclerView = findViewById(R.id.recycler_account_manager);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new AccountListAdapter(accountList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                presenter.onClickAccountItem(accountList.get(position));
            }
        });

        btnAddAccount = findViewById(R.id.btn_add_account);
        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.startOauthActivity(AccountManagerActivity.this);
            }
        });
    }

    @Override
    protected void initLogic() {
        presenter = new AccountPresenter(this, this);
        presenter.start();
    }

    @Override
    public void setPresenter(AccountContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showToast(String setMessage) {
        Toast.makeText(this, setMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void refreshAccounts() {
        accountList.clear();
        accountList.addAll(AccountUtils.getDefault().getAccountList(this));
        Log.e(TAG, "refreshAccountList: " + accountList.size() );
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showProgress() {
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在获取用户信息.......");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            presenter.onResult(requestCode, data);
        }
    }
}
