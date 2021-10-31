package com.example.navigator;

import android.os.Handler;
import android.util.Log;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class myThread_zhiwang extends Thread implements Runnable{
    String url="https://kns.cnki.net/kns8/defaultresult/index?txt_1_sel=SU%24%25%3D%7C&kw=";
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
    public void run() {
        trustEveryone();
        search=search.replaceAll("%","%25");
        search=search.replaceAll("&","%26");
        search=search.replaceAll("/+","%2B");
        search=search.replaceAll(" ","%20");
        search=search.replaceAll("#","%23");
        url+=search;
        Connection conn = Jsoup.connect(url);
        Log.i(TAG, "run: url"+url);
        Document document= null;
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);              // 启用JS解释器，默认为true
        webClient.getOptions().setCssEnabled(false);                    // 禁用css支持
        webClient.getOptions().setThrowExceptionOnScriptError(false);   // js运行错误时，是否抛出异常
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setTimeout(10 * 1000);                   // 设置连接超时时间
        HtmlPage page = null;
        try {
            //url是你要解析的页面路径
            page = webClient.getPage(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        webClient.waitForBackgroundJavaScript(30 * 1000);               // 等待js后台执行30秒

        //下面要根据不同的页面进行解析
        String pageAsXml = page.asXml();
        document = Jsoup.parse(pageAsXml);
        Log.i(TAG, "run: "+document.toString());

        /*try {
            *//*document = conn.timeout(3000).userAgent("Mozilla/5.0 (Linux; U; Android 2.3.7; en-us; Nexus One Build/FRF91) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1").get();
            Log.i(TAG, "run: title"+document.title()+"page="+page);
            ArrayList<Item> list=new ArrayList<Item>();
            Elements table=document.getElementsByTag("table");
            Log.i(TAG, "run: "+table.size());*//*

        } catch (IOException e) {
            e.printStackTrace();
        }*/


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
