package com.example.navigator;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String s="西南财经大学";
        @SuppressLint("HandlerLeak") Handler handler=new Handler(){
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(msg.what==1){
                    ListView listView=findViewById(R.id.mylist1);
                    ArrayList<String> list=(ArrayList<String>)msg.obj;
                    ListAdapter adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_expandable_list_item_1,list);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(MainActivity.this);
                }
            }


        };
        HotThread hotThread=new HotThread();
        hotThread.setHandler(handler);
        hotThread.start();
        TextView textView=findViewById(R.id.main_search);
        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    showListPopulWindow();
                }
            }
        });
    }
    public void search(View view){
        //Intent intent=new Intent(this,)
    }

    public void click(View view){
        Spinner spinner=findViewById(R.id.spinner);
        String search_engine=spinner.getSelectedItem().toString();
        EditText editText=findViewById(R.id.main_search);
        String search=editText.getText().toString();
        Intent intent=new Intent(this,SearchBaidu.class);
        SharedPreferences sharedPreferences=getSharedPreferences("main_search",SearchBaidu.MODE_PRIVATE);
        SharedPreferences.Editor ed=sharedPreferences.edit();
        ed.putString("search",search);
        ed.putString("search_engine",search_engine);
        ed.apply();

        //保存搜索记录
        SharedPreferences sp=getSharedPreferences("search_record",MainActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        Set search_record=null;
        search_record= sp.getStringSet("search_record", new HashSet());
        /*search_record.add(search);
        editor.putStringSet("search_record",search_record);*/
        Set new_set=new HashSet();
        new_set.addAll(search_record);
        new_set.add(search);
        editor.putStringSet("search_record",new_set);
        editor.commit();
        Log.i("set", "click: "+sp.getStringSet("search_record", new HashSet()));
        startActivityForResult(intent,1);
    }
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ListView listView=findViewById(R.id.mylist1);
        Object itemAt=listView.getItemAtPosition(i);
        String search= (String) itemAt;
        //Log.i("click hot string", "onItemClick: "+search);
        Intent intent=new Intent(this,SearchBaidu.class);
        SharedPreferences sharedPreferences=getSharedPreferences("main_search",SearchBaidu.MODE_PRIVATE);
        SharedPreferences.Editor ed=sharedPreferences.edit();
        ed.putString("search",search);
        ed.putString("search_engine","百度");
        ed.commit();
        ed.apply();
        startActivityForResult(intent,1);
    }

    private void showListPopulWindow() {
        SharedPreferences sp=getSharedPreferences("search_record",MainActivity.MODE_PRIVATE);
        Set search_record=null;
        search_record= sp.getStringSet("search_record", new HashSet());
        Log.i("len", "showListPopulWindow: "+search_record);
        List<String> s_r=new ArrayList<String>(search_record);
        s_r.add("搜索记录");
        if(s_r.size()>=5){
            s_r=s_r.subList(0,5);
        }
        final ListPopupWindow listPopupWindow;
        listPopupWindow = new ListPopupWindow(MainActivity.this);
        listPopupWindow.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,s_r));//用android内置布局，或设计自己的样式
        listPopupWindow.setAnchorView(findViewById(R.id.main_search));//以哪个控件为基准，在该处以logId为基准
        listPopupWindow.setModal(true);

        List<String> finalS_r = s_r;
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {//设置项点击监听
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((EditText)findViewById(R.id.main_search)).setText(finalS_r.get(i));//把选择的选项内容展示在EditText上
                listPopupWindow.dismiss();//如果已经选择了，隐藏起来
            }
        });
        listPopupWindow.show();//把ListPopWindow展示出来
    }
    public void History(View view){
        Intent intent=new Intent(this,History.class);
        startActivityForResult(intent,1);
    }
    public void Shoucang(View view){
        Intent intent=new Intent(this,shoucang.class);
        startActivityForResult(intent,1);
    }
}