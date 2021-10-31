package com.example.navigator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.ProxyConfig;
import androidx.webkit.ProxyController;
import androidx.webkit.WebViewFeature;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

public class Web_activity extends AppCompatActivity {
    private static int account=1;
    public WebView webView=null;
    String url=null;
    public void setUrl(String url){this.url=url;}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Intent intent=getIntent();
        SharedPreferences sp=getSharedPreferences("open_web",SearchBaidu.MODE_PRIVATE);
        PreferenceManager.getDefaultSharedPreferences(this);
        url=sp.getString("url","https://www.baidu.com");
        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setUserAgentString("User-Agent: MQQBrowser/26 Mozilla/5.0 (linux; U; Android 2.3.7; zh-cn; MB200 Build/GRJ22; CyanogenMod-7) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);//开启DOM
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(url);
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    //按返回键操作并且能回退网页
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                        //后退
                        webView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });



    }
    class MyWebViewClient extends WebViewClient {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        /*@Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }*/
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if( url.startsWith("http:") || url.startsWith("https:") ) {
                if(url.startsWith("https://wx.tenpay.com")){//H5使用微信支付
                    Map<String, String> extraHeaders = new HashMap<>();
                    extraHeaders.put("Referer","refer");
                    view.loadUrl(url, extraHeaders);
                } else{

                    view.loadUrl(url);
                }

            }else{ //非http或者https的网络请求拦截，用action_view启动。可能报错。

                try {
                    Uri uri = Uri.parse(url);
                    Intent intent =new Intent(Intent.ACTION_VIEW, uri);
                    view.getContext().startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    if (url.startsWith("alipay")){
                        Toast.makeText(view.getContext(), "请确认是否安装支付宝",Toast.LENGTH_SHORT).show();
                    }else if (url.startsWith("mqqwpa")){
                        Toast.makeText(view.getContext(), "请确认是否安装QQ",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            return true;
        }

        /*@Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            if (account++ == 1) {
                webView.loadUrl("javascript:alert('友情提示:账号为学号,密码为出生年月日,eg:账号：201607881 密码：19980101')");
            }
        }*/

        //页面结束后发生的操作
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

    }
}