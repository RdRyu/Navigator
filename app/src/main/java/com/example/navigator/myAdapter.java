package com.example.navigator;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class myAdapter extends ArrayAdapter {
    public myAdapter(@NonNull Context context, int resource, ArrayList<Item> itemlist) {
        super(context, resource,itemlist);
    }
    public View getView(int pos, View convertView, ViewGroup parent){
        View itemView=convertView;
        if(itemView==null){
            itemView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        TextView title=itemView.findViewById(R.id.title);
        TextView url=itemView.findViewById(R.id.url);
        TextView brief=itemView.findViewById(R.id.brief);
        Item item= (Item) getItem(pos);
        title.setText(item.getTitle());
        url.setText(item.getUrl());
        brief.setText(item.getDetail());
        notifyDataSetChanged();
        Button button=itemView.findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences= getContext().getSharedPreferences("shoucang",0);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                int len=sharedPreferences.getInt("len",0);
                editor.putString("title"+len,item.getTitle());
                editor.putString("url"+len,item.getUrl());
                editor.putString("detail"+len,item.getDetail());
                editor.putInt("len",len+1);
                Toast.makeText(getContext().getApplicationContext(), "收藏成功",Toast.LENGTH_LONG).show();
                editor.apply();
            }
        });
        return itemView;
    }
}
