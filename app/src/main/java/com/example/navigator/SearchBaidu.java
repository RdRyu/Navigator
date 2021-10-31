package com.example.navigator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchBaidu extends AppCompatActivity implements AdapterView.OnItemClickListener{
    String search=new String();
    String search_engine=new String();
    int page=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_baidu);
        Intent intent=getIntent();
        SharedPreferences sp=getSharedPreferences("main_search",SearchBaidu.MODE_PRIVATE);
        PreferenceManager.getDefaultSharedPreferences(this);

        search=sp.getString("search","google");
        search_engine=sp.getString("search_engine","百度");
        EditText ed=findViewById(R.id.search_bar);
        ed.setText(search);

        @SuppressLint("HandlerLeak") Handler handler=new Handler(){
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ArrayList<Item> itemlist = (ArrayList<Item>) msg.obj;
                if (msg.what == 1) {
                    ListView listView = findViewById(R.id.list_baidu);
                    //Log.i("get", "handleMessage: "+itemlist.get(0));
                    myAdapter myAdapter = new myAdapter(SearchBaidu.this, R.layout.list_item, itemlist);
                    listView.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();
                }
            }
        };
        if(search_engine.equals("百度")){
            myThread myThread=new myThread();
            myThread.setSearch(search);
            myThread.setHandler(handler);
            myThread.start();
        }
        else if(search_engine.equals("Gitee")){
            myThread_csdn myThread_csdn=new myThread_csdn();
            myThread_csdn.setSearch(search);
            myThread_csdn.setHandler(handler);
            myThread_csdn.start();
        }
        else{
            Intent in=new Intent(this,Web_activity.class);
            SharedPreferences sharedPreferences=getSharedPreferences("open_web",SearchBaidu.MODE_PRIVATE);
            SharedPreferences.Editor ed1=sharedPreferences.edit();
            ed1.putString("url","https://kns.cnki.net/kns8/defaultresult/index?txt_1_sel=SU%24%25%3D%7C&kw="+search);
            ed1.commit();
            ed1.apply();
            startActivityForResult(in,2);
        }

        Button btn_back=findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button btn_search=findViewById(R.id.button_baidu);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner spinner=findViewById(R.id.spinner_baidu);
                search_engine=spinner.getSelectedItem().toString();
                EditText editText=findViewById(R.id.search_bar);
                search=editText.getText().toString();
                if(search_engine.equals("百度")){
                    myThread myThread1=new myThread();
                    myThread1.setSearch(search);
                    myThread1.setHandler(handler);
                    myThread1.start();
                }
                else if(search_engine.equals("Gitee")){
                    page+=1;
                    myThread_csdn myThread1=new myThread_csdn();
                    myThread1.setSearch(search);
                    myThread1.setHandler(handler);
                    myThread1.start();
                }
                else{
                    Intent in=new Intent(SearchBaidu.this,Web_activity.class);
                    SharedPreferences sharedPreferences=getSharedPreferences("open_web",SearchBaidu.MODE_PRIVATE);
                    SharedPreferences.Editor ed1=sharedPreferences.edit();
                    ed1.putString("url","https://kns.cnki.net/kns8/defaultresult/index?txt_1_sel=SU%24%25%3D%7C&kw="+search);
                    ed1.commit();
                    ed1.apply();
                    startActivityForResult(in,2);
                }

            }
        });
        Button btn_page_up=findViewById(R.id.page_previous);
        btn_page_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page==0){
                    Toast.makeText(SearchBaidu.this,"已在第一页了",Toast.LENGTH_LONG).show();
                    return;
                }
                if(search_engine.equals("百度")){
                    myThread myThread1=new myThread();
                    myThread1.setHandler(handler);
                    myThread1.setPage(page-1);
                    page-=1;
                    myThread1.setSearch(search);
                    myThread1.start();
                }
                else if(search_engine.equals("Gitee")){
                    myThread_csdn myThread1=new myThread_csdn();
                    myThread1.setHandler(handler);
                    myThread1.setPage(page-1);
                    page-=1;
                    myThread1.setSearch(search);
                    myThread1.start();
                }
            }
        });
        Button btn_page_next=findViewById(R.id.page_next);
        btn_page_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(search_engine.equals("百度")){
                    myThread myThread1=new myThread();
                    myThread1.setHandler(handler);
                    myThread1.setPage(page+1);
                    page+=1;
                    myThread1.setSearch(search);
                    myThread1.start();
                }
                else if(search_engine.equals("Gitee")){
                    myThread_csdn myThread1=new myThread_csdn();
                    myThread1.setHandler(handler);
                    myThread1.setPage(page+1);
                    page+=1;
                    myThread1.setSearch(search);
                    myThread1.start();
                }
            }
        });
        ListView listView=findViewById(R.id.list_baidu);
        listView.setOnItemClickListener(SearchBaidu.this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ListView listView=findViewById(R.id.list_baidu);
        Object itemAt=listView.getItemAtPosition(i);
        Item item= (Item) itemAt;
        //Toast.makeText(SearchBaidu.this,"click",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,Web_activity.class);
        SharedPreferences sharedPreferences=getSharedPreferences("open_web",SearchBaidu.MODE_PRIVATE);
        SharedPreferences.Editor ed=sharedPreferences.edit();
        ed.putString("url",item.getUrl());
        ed.commit();
        ed.apply();
        //记录历史
        SharedPreferences sp=getSharedPreferences("history",0);
        SharedPreferences.Editor editor=sp.edit();
        int len=sp.getInt("len",0);
        editor.putString("title"+len,item.getTitle());
        editor.putString("url"+len,item.getUrl());
        editor.putString("detail"+len,item.getDetail());
        editor.putInt("len",len+1);
        editor.apply();
        startActivityForResult(intent,2);
    }
}