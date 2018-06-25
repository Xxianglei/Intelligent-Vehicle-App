package com.example.heath.Main_Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.heath.Check_xueya;
import com.example.heath.Datebase.DataBaseManager;
import com.example.heath.Datebase.XueyaModle;
import com.example.heath.R;

import com.example.heath.utils.LineChartManager;
import com.example.heath.view.MyScrollView;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.mikephil.charting.charts.LineChart;



import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class ScrollFragment extends HeaderViewPagerFragment {
    private LineChart lineChart;// 声明图表控件
    @BindView(R.id.scrollView)
    NestedScrollView mScrollView;
    private Context context;

    private NestedScrollView scrollview;
    private List<XueyaModle> list;
    private TextView zhi;
    private TextView date;
    private MyScrollView end;
    private LinearLayout ll;


    public static ScrollFragment newInstance() {
        return new ScrollFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scroll, container, false);
        initView(view);
        context=getActivity();
        initData();
        return view;
    }

    private void initView(View v) {
        DataBaseManager dataBaseManager=new DataBaseManager();
        list = dataBaseManager.readxyList();
        ll = v.findViewById(R.id.cry_nodata);
        lineChart = v.findViewById(R.id.lineChart);//绑定控件
        if (list.size()>=7){
            ll.setVisibility(View.GONE);
            lineChart.setVisibility(View.VISIBLE);
        }
        zhi = v.findViewById(R.id.zhi);
        date = v.findViewById(R.id.date);
        end = v.findViewById(R.id.end);
        RelativeLayout relativeLayout=v.findViewById(R.id.his_data);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity( new Intent(getActivity(), Check_xueya.class));
            }
        });

        scrollview= v.findViewById(R.id.scrollView);
        Log.e("大小",list.size()+"");
        LineChartManager lineChartManager1 = new LineChartManager(lineChart);

        //设置x轴的数据
        ArrayList<Float> xValues = new ArrayList<>();
        if (list.size()>=7) {
            for (int i = 0; i < 7; i++) {
                xValues.add((float) i);
            }
            //设置y轴的数据()
            boolean tag=true;
            List<List<Float>> yValues = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                List<Float> yValue = new ArrayList<>();
                for (int j = list.size()-7; j <=list.size()-1; j++) {
                    Log.e("大小2",list.size()+"");
                    if (tag){
                        yValue.add((float) list.get(j).getHigh_data());
                        tag=false;
                    }
                    else {
                        yValue.add((float) list.get(j).getLow_data());
                        tag=true;
                    }



                }
                yValues.add(yValue);
            }

            //颜色集合
            List<Integer> colours = new ArrayList<>();
            colours.add(Color.GREEN);
            colours.add(Color.BLUE);


            //线的名字集合
            List<String> names = new ArrayList<>();
            names.add("收缩压（mmHg）");
            names.add("舒张压（mmHg）");

            //创建多条折线的图表
            lineChartManager1.showLineChart(xValues, yValues, names, colours, false, false);
            lineChartManager1.setDescription("血压值");
            lineChartManager1.setYAxis(200, 0, 11);
            lineChartManager1.setHightLimitLine(140, "血压预警", Color.RED);
            // MyMarkerView myMarkerView=new MyMarkerView(context);
            // lineChart.setMarkerView(myMarkerView);
        }
    }


    private void initData() {
        if (list.size()>0) {
            zhi.setText(list.get(list.size() - 1).getHigh_data() + "/" + list.get(list.size() - 1).getLow_data());
            date.setText(list.get(list.size() - 1).getDate());
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
