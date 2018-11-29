package com.nby.news.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.nby.news.R;
import android.os.Handler;
import android.os.Message;

public class ShowNewsContentActivity extends Activity{
    @SuppressLint("HandlerLeak")
    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 201:
                    if(webView!=null){
                        webView.setVisibility(View.VISIBLE);
                    }
            }
        }
    };
    public String mUrl;
    public WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content_layout);
        mUrl = getIntent().getStringExtra("NewsUrl");
        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        webSettings.setDisplayZoomControls(true);
        webSettings.setDefaultFontSize(12);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });
       // webView.setVisibility(View.GONE);
        webView.loadUrl(mUrl);
       // mHandler.sendEmptyMessageDelayed(201,3000);


    }

    @Override
    protected void onDestroy() {

        super.onDestroy( );
    }
}
