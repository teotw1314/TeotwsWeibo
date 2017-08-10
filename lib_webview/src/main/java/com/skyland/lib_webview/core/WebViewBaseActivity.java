package com.skyland.lib_webview.core;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.support.v7.widget.Toolbar;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.skyland.lib_webview.R;
import com.skyland.lib_webview.widget.WebViewPro;

/**
 * Created by skyland on 2017/7/8
 */

public abstract class WebViewBaseActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    protected static final String TAG = "WebViewBaseActivity";
    protected static final String KEY_TITLE = "key_title";
    protected static final String KEY_URL = "key_url";
    protected static final String KEY_IS_AUTHO = "key_autho";

    public static final int REQUEST_CODE = 101;
    public static final String RESULT_CODE_KEY = "result_code";

    private static final String WEIBO_CALLBACK_URL = "https://api.weibo.com/oauth2/default.html";
    private static final String WEIBO_CALLBACK_PARMENT_NAME = "code";

    protected Toolbar toolbar;
    protected ProgressBar progressBar;
    protected WebViewPro webViewPro;

    protected String mTitle;
    protected String mUrl;
    protected boolean isAutho;
    protected String currentUrl;

    protected abstract void initToolbar();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.lib_webview_activity);
        initIntent();
        initView();
        initWebView();
    }

    private void initIntent() {
        Intent intent = getIntent();
        mTitle = intent.getStringExtra(KEY_TITLE);
        mUrl = intent.getStringExtra(KEY_URL);
        isAutho = intent.getBooleanExtra(KEY_IS_AUTHO, true);
        currentUrl = mUrl;
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_lib_webview_activity);
        progressBar = (ProgressBar) findViewById(R.id.progress_lib_webview_activity);
        webViewPro = (WebViewPro) findViewById(R.id.webview_lib_webview_activity);

        initToolbar();
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewBaseActivity.this.finish();
            }
        });
        if(!isAutho){
            toolbar.inflateMenu(R.menu.lib_webview_toolbar);

        }
    }

    private void initWebView() {
        webViewPro.setWebViewClient(webViewClient);
        webViewPro.setWebChromeClient(webChromeClient);
        webViewPro.loadUrl(mUrl);
    }

    /**
     * web view client
     */
    WebViewClient webViewClient = new WebViewClient() {

        private boolean onError = false;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.e(TAG, "onPageStarted: " + url);
            toolbar.setSubtitle(Uri.parse(url).getScheme() + "://" + Uri.parse(url).getHost());
            progressBar.setVisibility(View.VISIBLE);
            currentUrl = url;
            if (url.startsWith(WEIBO_CALLBACK_URL)) {
                weiboCallBack(url);
                return;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            toolbar.setTitle(TextUtils.isEmpty(mTitle) ? view.getTitle() : mTitle);
            progressBar.setVisibility(View.GONE);

            if (onError) {
                Log.e(TAG, "onPageFinished: load error --  " + url);

            } else {
                Log.e(TAG, "onPageFinished: load success" + url);

            }
            onError = false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.e(TAG, "onReceivedError-- " + "errorCode:" + errorCode + "   description:" + description + "   failedUrl:" + failingUrl);
            onError = true;
        }
    };

    /**
     * Web Chrome Client
     */
    WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {  //progress最大值为100
            super.onProgressChanged(view, newProgress);
            progressBar.setProgress(newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            toolbar.setTitle(TextUtils.isEmpty(mTitle) ? view.getTitle() : mTitle);
        }
    };

    /**
     * Toolbar Listener
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int meanId = item.getItemId();
        if (meanId == R.id.action_copy_url) {
            copyUrl();
        } else if (meanId == R.id.action_open_with_browser) {
            openWithBrowser();
        }

        return true;
    }

    /**
     * 复制网页链接
     */
    private void copyUrl() {
        ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("url", currentUrl);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 使用浏览器打开
     */
    private void openWithBrowser() {
        Uri uri = Uri.parse(currentUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void weiboCallBack(String url) {
        String code = Uri.parse(url).getQueryParameter(WEIBO_CALLBACK_PARMENT_NAME);
        Log.e(TAG, "weiboCallBack: " + code);
        Intent intent = new Intent();
        intent.putExtra(RESULT_CODE_KEY, code);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (webViewPro.canGoBack()) {
            webViewPro.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        webViewPro.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webViewPro.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防止内存泄露
        if (webViewPro != null) {
            webViewPro.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webViewPro.clearHistory();
            ((ViewGroup) webViewPro.getParent()).removeView(webViewPro);
            webViewPro.destroy();
            webViewPro = null;
        }
    }
}

