package com.example.heath.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.heath.Model.TimeData;
import com.example.heath.R;

import java.util.List;

/**
 * Created by 丽丽超可爱 on 2018/4/29.
 */

public class TimeAdapter extends BaseAdapter {

        //印章数据
        private List<TimeData> list;
        private LayoutInflater mInflater;

        public TimeAdapter(Context context, List<TimeData> list) {
            this.list = list;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.record_item, null);
            }
            ViewHolder holder = getViewHolder(convertView);
            TimeData kd = list.get(position);
            holder.tv_content.setText(kd.getContent());
            holder.tv_time.setText(kd.getTime());
            return convertView;
        }

        /**
         * 获得控件管理对象
         *
         * @param view
         * @return
         */
        private ViewHolder getViewHolder(View view) {
            ViewHolder holder = (ViewHolder) view.getTag();
            if (holder == null) {
                holder = new ViewHolder(view);
                view.setTag(holder);
            }
            return holder;
        }

        /**
         * 控件管理类
         */
        private class ViewHolder {
            private TextView tv_content, tv_time;

            ViewHolder(View view) {
                tv_content = (TextView) view.findViewById(R.id.tv_content);
                tv_time = (TextView) view.findViewById(R.id.tv_time);
            }
        }
    }

