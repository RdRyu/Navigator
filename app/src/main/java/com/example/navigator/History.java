package com.example.navigator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class History extends AppCompatActivity implements AdapterView.OnItemClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        SharedPreferences sharedPreferences=getSharedPreferences("history",History.MODE_PRIVATE);
        int len=sharedPreferences.getInt("len",0);
        ArrayList<Item> items=new ArrayList<>();
        for(int i=0;i<len;i++){
            Item item=new Item();
            item.setDetail(sharedPreferences.getString("detail"+i,""));
            item.setUrl(sharedPreferences.getString("url"+i,""));
            item.setTitle(sharedPreferences.getString("title"+i,""));
            items.add(item);
            Log.i("show", "onCreate: "+item.getDetail());
        }
        ListView listView = findViewById(R.id.history_view);
        myAdapter myAdapter = new myAdapter(History.this, R.layout.list_item, items);
        listView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
        ListView lw=findViewById(R.id.history_view);
        lw.setOnItemClickListener(this);
    }
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ListView listView=findViewById(R.id.history_view);
        Object itemAt=listView.getItemAtPosition(i);
        Item item= (Item) itemAt;
        Toast.makeText(History.this,"click",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,Web_activity.class);
        SharedPreferences sharedPreferences=getSharedPreferences("open_web",SearchBaidu.MODE_PRIVATE);
        SharedPreferences.Editor ed=sharedPreferences.edit();
        ed.putString("url",item.getUrl());
        ed.commit();
        ed.apply();
        startActivityForResult(intent,2);
    }
    public void back(View view){
        finish();
    }
}