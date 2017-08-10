package com.skyland.teotwsweibo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.skyland.teotwsweibo.R;


/**
 * Created by skyland on 2017/7/12
 */

public abstract class BaseSubActivity extends BaseActivity{

//    public AppComponent getAppComponent(){
//        return ((App)getApplication()).getAppComponent();
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(R.anim.lib_common_slide_in_right, R.anim.lib_common_slide_out_left);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.lib_common_slide_in_left, R.anim.lib_common_slide_out_right);
    }
}
