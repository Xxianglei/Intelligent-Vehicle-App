package com.example.heath.Main_Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.heath.Check_xinlv;
import com.example.heath.Datebase.DataBaseManager;
import com.example.heath.Datebase.XinlvModle;
import com.example.heath.R;
import com.example.heath.utils.BarChartManager;
import com.example.heath.view.MyScrollView;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class ScrollFragment2 extends HeaderViewPagerFragment {

    @BindView(R.id.scrollView)
    NestedScrollView mScrollView;
    private BarChart barChart;
    private List<XinlvModle> list;
    private TextView zhi;
    private TextView date;
    private MyScrollView end;
    private LinearLayout ll;

    public static ScrollFragment2 newInstance() {
        return new ScrollFragment2();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.fragment_scroll2, container, false);
        initView(view);
        initData();
        return view ;
    }

    private void initData() {
        if (list.size()>0){
            zhi.setText(list.get(list.size()-1).getData()+"");
            date.setText(list.get(list.size()-1).getDate()+"");
        }
    }

    private void initView(View view) {
        //绑定控件
        DataBaseManager dataBaseManager=new DataBaseManager();
        list = dataBaseManager.readxlList();
        barChart = view.findViewById(R.id.BarChart);
        ll = view.findViewById(R.id.cry_nodata);
        if (list.size()>=7){
            ll.setVisibility(View.GONE);
            barChart.setVisibility(View.VISIBLE);
        }
        end = view.findViewById(R.id.end);
        RelativeLayout relativeLayout=view.findViewById(R.id.his_data);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getActivity(), Check_xinlv.class));
            }
        });
        zhi = view.findViewById(R.id.zhi);
        date = view.findViewById(R.id.date);


        BarChartManager barChartManager1 = new BarChartManager(barChart);
        NestedScrollView scrollview= view.findViewById(R.id.scrollView);

        //设置x轴的数据
         if (list.size()>=7) {
             ArrayList<Float> xValues = new ArrayList<>();
             for (int i = 0; i < 7; i++) {
                 xValues.add((float) i);
             }
             //设置y轴的数据()
             List<List<Float>> yValues = new ArrayList<>();
             for (int i = 0; i < 1; i++) {
                 List<Float> yValue = new ArrayList<>();
                 for (int j = list.size()-7; j <=list.size()-1; j++) {
                     yValue.add((float) list.get(j).getData());
                 }
                 yValues.add(yValue);
             }

             //颜色集合
             List<Integer> colours = new ArrayList<>();
             colours.add(Color.RED);

             //线的名字集合
             List<String> names = new ArrayList<>();
             names.add("心率（bpm）");


             //创建柱状图
             barChartManager1.showBarChart(xValues, yValues.get(0), names.get(0), colours.get(0));
             barChartManager1.setYAxis(140, 0, 6);
             barChartManager1.setDescription("心率值");
         }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialViewPagerHelper.registerScrollView(getActivity(), mScrollView);
    }

    @Override
    public View getScrollableView() {
        return end;
    }
}
