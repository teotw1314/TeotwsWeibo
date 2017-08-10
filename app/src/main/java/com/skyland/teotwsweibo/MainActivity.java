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
        
        entry();
    }

    private void entry() {
        if (AccountUtils.getDefault().getCurrentAccount(this) == null) {
            // TODO: 2017/8/10 account
            AccountManagerActivity.startActivity(this);
        } else {
            // TODO: 2017/8/10 home
            HomeActivity.startActivity(this, null);
        }
    }
}
