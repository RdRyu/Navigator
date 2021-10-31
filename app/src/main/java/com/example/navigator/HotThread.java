package com.example.navigator;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class HotThread extends Thread implements Runnable{
    private static final String TAG = "hotthread run";
    Handler handler;
    public void setHandler(Handler handler){
        this.handler=handler;
    }
    ArrayList<String> list=new ArrayList<String>();
    public void run(){
        String url="https://www.baidu.com/";
        try {
            Document document = Jsoup.connect(url).get();
            Elements tables=document.getElementsByClass("title-content-title");
            for (Element el:tables) {
                //Log.i(TAG, "run: " +el.text());
                list.add(el.text());
            }
            Message msg=handler.obtainMessage();
            msg.obj=list;
            msg.what=1;
            handler.sendMessage(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
