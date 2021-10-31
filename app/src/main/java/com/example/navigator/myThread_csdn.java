package com.example.navigator;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class myThread_csdn extends Thread implements Runnable{
    private static final String TAG = "Thread_csdn";
    Handler handler;
    public void setHandler(Handler handler){
        this.handler=handler;
    }
    String search=null;
    public void setSearch(String search){
        this.search=search;
    }
    int page=1;
    public void setPage(int page){this.page=page;}
    String url="https://search.gitee.com/?skin=rec&type=repository&q=";
    public void run(){
        try{
            trustEveryone();
            search=search.replaceAll("%","%25");
            search=search.replaceAll("&","%26");
            search=search.replaceAll("/+","%2B");
            search=search.replaceAll(" ","%20");
            search=search.replaceAll("#","%23");
            url+=search;
            String cur_page="&pageno="+page;
            url+=cur_page;

            Connection conn = Jsoup.connect(url);
            Log.i(TAG, "run: url"+url);
            Document document=conn.timeout(3000).userAgent("Mozilla/5.0 (Linux; U; Android 2.3.7; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1").get();
            Log.i(TAG, "run: title"+document.title()+"page="+page);
            ArrayList<Item> list=new ArrayList<Item>();
            Elements item_list=document.getElementsByClass("hits-list").get(1).getElementsByClass("item");
            for (Element element:item_list){
                Item item=new Item();
                item.setUrl(element.getElementsByTag("a").attr("href"));
                item.setTitle(element.getElementsByTag("a").text());
                item.setDetail(element.getElementsByClass("desc").text());
                list.add(item);
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

