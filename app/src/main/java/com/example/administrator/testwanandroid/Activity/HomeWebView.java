package com.example.administrator.testwanandroid.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import com.example.administrator.testwanandroid.R;
import com.example.administrator.testwanandroid.Utils.L;

import java.security.PrivateKey;

public class HomeWebView extends AppCompatActivity {
    private WebView webView;
    private ProgressBar progress_bar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_home);
        initView();
    }

    private void initView() {
        webView=findViewById(R.id.webview_home);
        progress_bar=findViewById(R.id.progress_bar);
        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        String title=intent.getStringExtra("title");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        //加载网页逻辑
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        //接口回调
        webView.setWebChromeClient(new WebViewClient());
        webView.loadUrl(url);

    }
    public class  WebViewClient extends WebChromeClient{
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress==100){
                progress_bar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
