package com.skyland.lib_webview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by skyland on 2017/7/11
 */

public class WebViewPro extends WebView {


    public WebViewPro(Context context) {
        super(context);
        init();
    }

    public WebViewPro(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WebViewPro(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        WebSettings webSettings = getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setJavaScriptEnabled(true);     //开启js支持

        setWebViewClient(webViewClient);
    }

    WebViewClient webViewClient  = new WebViewClient(){
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);      //在当前webiew加载网页
            return true;
        }
    };

}
