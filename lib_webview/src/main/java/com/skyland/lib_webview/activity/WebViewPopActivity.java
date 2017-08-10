package com.skyland.lib_webview.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.skyland.lib_webview.R;
import com.skyland.lib_webview.core.WebViewBaseActivity;

/**
 * Created by skyland on 2017/7/11
 */

public class WebViewPopActivity extends WebViewBaseActivity {

    public static void startActivity(Activity setActivity, String title, String url, boolean setIsAutho) {
        Intent intent = new Intent(setActivity, WebViewPopActivity.class);
        intent.putExtra(KEY_TITLE, title);
        intent.putExtra(KEY_URL, url);
        intent.putExtra(KEY_IS_AUTHO, setIsAutho);
        setActivity.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(R.anim.lib_webview_slide_in_bottom, R.anim.lib_webview_slide_out_top);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition( R.anim.lib_webview_slide_in_top, R.anim.lib_webview_slide_out_bottom);
    }

    @Override
    protected void initToolbar() {
        toolbar.setNavigationIcon(R.mipmap.ic_navigation_close);
    }
}
