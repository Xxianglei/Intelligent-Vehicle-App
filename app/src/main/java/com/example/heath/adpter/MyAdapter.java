package com.example.heath.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;

import com.example.heath.R;
import com.example.heath.Model.Con_person;

import java.util.List;

public class MyAdapter extends BaseAdapter {

    private List<Con_person> List;
    private LayoutInflater inflater;
    public MyAdapter() {}

    public MyAdapter(List<Con_person> List, Context context) {
        this.List = List;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return List==null?0:List.size();
    }

    @Override
    public Con_person getItem(int position) {
        return List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //加载布局为一个视图
        View view=inflater.inflate(R.layout.list_demo,null);
        Con_person person=getItem(position);
        //在view视图中查找id为image_photo的控件
        TextView name=(TextView)view.findViewById(R.id.name);
        TextView phone=(TextView) view.findViewById(R.id.phone);

        name.setText(person.getName());
        phone.setText(person.getPhone());
        return view;
    }
}