package com.lt.hm.wovideo.ui;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.lt.hm.wovideo.R;
import com.lt.hm.wovideo.base.BaseActivity;
import com.lt.hm.wovideo.utils.TLog;
import com.lt.hm.wovideo.widget.SecondTopbar;

import butterknife.BindView;


/**
 * @author leonardo
 * @version 1.0
 * @create_date 16/1/3
 */
public class ThirdActivity extends BaseActivity implements SecondTopbar.myTopbarClicklistenter {

    @BindView(R.id.web_topbar)
    SecondTopbar webTopbar;
    @BindView(R.id.web_container)
    WebView web_container;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private String url;


    @Override
    protected int getLayoutId() {
        return R.layout.layout_web_page;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("url");

        if (!url.startsWith("http:") && !url.startsWith("https:")) {
            url = "http://" + url;
            TLog.log("url"+url);
        }
    }

    @Override
    public void initViews() {
        webTopbar.setOnTopbarClickListenter(this);
    }

    @Override
    public void initDatas() {
        WebSettings webSettings = web_container.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAppCacheEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        web_container.setSaveEnabled(false);
        //显示 移除  SearchBox 接口
//        if (Build.VERSION.SDK_INT <= 17) {
//            web_container.removeJavascriptInterface("searchBoxJavaBridge_");
//            web_container.removeJavascriptInterface("accessibility");
//            web_container.removeJavascriptInterface("accessibilityTraversal");
//        }


        webSettings.setSupportZoom(true);          //支持缩放
        webSettings.setBuiltInZoomControls(true);  //启用内置缩放装置
        web_container.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                TLog.log("progress"+newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    if (progressBar.getVisibility() ==View. GONE)
                        progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });


        web_container.setWebViewClient(new webViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                TLog.log("current_request" + url);
                view.loadUrl(url);   //在当前的webview中跳转到新的url
                return true;
            }
        });
        web_container.loadUrl(url);

    }

    @Override
    public void leftClick() {
        this.finish();
    }

    @Override
    public void rightClick() {

    }


    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            TLog.log("fail" + failingUrl + description + "::code" + errorCode);
        }
    }
}
