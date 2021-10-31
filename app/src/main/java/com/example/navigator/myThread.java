package com.example.navigator;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class myThread extends Thread implements Runnable{
    private static final String TAG = "mythread";
    Handler handler;
    public void setHandler(Handler handler){
        this.handler=handler;
    }
    String search=null;
    public void setSearch(String search){
        this.search=search;
    }
    int page=0;
    public void setPage(int page){this.page=page;}
    @Override
    public void run() {
        try{
            trustEveryone();
            String url="https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&tn=baidu&wd=";
            search=search.replaceAll("%","%25");
            search=search.replaceAll("&","%26");
            search=search.replaceAll("/+","%2B");
            search=search.replaceAll(" ","%20");
            search=search.replaceAll("#","%23");


            url+=search;
            String cur_page="&pn="+page+0;
            url+=cur_page;
            Connection conn = Jsoup.connect(url);

            //conn.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            /*conn.header("Accept-Encoding", "gzip, deflate, br");
            conn.header("Accept-Language", "zh-CN,zh;q=0.9");
            conn.header("Cache-Control", "max-age=0");
            conn.header("Connection", "keep-alive");
            conn.header("Host", "blog.maxleap.cn");
            conn.header("Upgrade-Insecure-Requests", "1");
            conn.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.186 Safari/537.36");
            */

            Document document= null;
            Log.i(TAG, "run: url"+url);
            //Connection conheader = connect.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");
            document=conn.timeout(30000).userAgent("Mozilla/5.0 (Linux; U; Android 2.3.7; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1").get();
            Log.i(TAG, "run: title"+document.title()+"page="+page);
            if(document.title().equals("百度安全验证")){
                document=conn.timeout(1000).userAgent("MQQBrowser/26 Mozilla/5.0 (Linux; U; Android 2.3.7; zh-cn; MB200 Build/GRJ22; CyanogenMod-7) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1").proxy("120.133.60.92",80).get();
            }
            ArrayList<Item> list=new ArrayList<Item>();
            Elements baike=document.getElementsByClass("result-op c-container new-pmd");
            //Log.i(TAG, "run: size"+baike.size());
            //置顶百度百科
            for (Element bk:baike){
                if (bk.attr("srcid").equals("37024")){
                    //Log.i(TAG, "run: 37024"+bk.text());
                    Item item=new Item();
                    try{
                        item.setTitle(bk.getElementsByClass("t").get(0).getElementsByTag("a").text());
                        item.setUrl(bk.getElementsByClass("t").get(0).getElementsByTag("a").attr("href"));
                        item.setDetail(bk.getElementsByClass("c-span9 c-span-last op-bk-polysemy-piccontent").text());
                        Log.i(TAG, "run: baike"+item.getDetail());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            Elements elements=document.getElementsByClass("result c-container new-pmd");
            //Log.i(TAG, "run: "+elements.get(0).getElementsByClass("t").get(0).getElementsByTag("a").attr("href"));
            for (Element element:elements){
                Item item=new Item();
                item.setTitle(element.getElementsByClass("t").get(0).getElementsByTag("a").text());
                item.setUrl(element.getElementsByClass("t").get(0).getElementsByTag("a").attr("href"));
                item.setDetail(element.getElementsByClass("c-abstract").text());
                list.add(item);
                //Log.i(TAG, "run11: "+item.getDetail());
            }
            Message msg=handler.obtainMessage();
            msg.what=1;
            msg.obj=list;
            handler.sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[] { new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            } }, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }
}
