package com.skyland.teotwsweibo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.skyland.teotwsweibo.module.account.AccountManagerActivity;
import com.skyland.teotwsweibo.module.home.HomeActivity;
import com.skyland.teotwsweibo.utils.AccountUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        AccountManagerActivity.startActivity(this);
        entry();
    }

    private void entry() {
        if (AccountUtils.getDefault().getCurrentAccount(this) == null) {
            AccountManagerActivity.startActivity(this);
        } else {
            HomeActivity.startActivity(this, null);
        }
    }
}
