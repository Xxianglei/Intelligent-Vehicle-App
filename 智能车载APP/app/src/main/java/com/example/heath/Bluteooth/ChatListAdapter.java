package com.example.heath.Bluteooth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.heath.R;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {
    private ArrayList<Bluetooth.SiriListItem> list;
    private LayoutInflater mInflater;

    public ChatListAdapter(Context context, ArrayList<Bluetooth.SiriListItem> list2) {
        list = list2;
        mInflater = LayoutInflater.from(context);
    }


    public int getCount() {
        return list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public int getItemViewType(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        Bluetooth.SiriListItem item = list.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder(
                    (View) convertView.findViewById(R.id.list_child),
                    (TextView) convertView.findViewById(R.id.chat_msg)
            );
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (item.isSiri) {
            viewHolder.child.setBackgroundResource(R.drawable.surface_bg);
        }
        viewHolder.msg.setText(item.message);

        return convertView;
    }

    class ViewHolder {
        protected View child;
        protected TextView msg;

        public ViewHolder(View child, TextView msg) {
            this.child = child;
            this.msg = msg;

        }
    }
}
