
package com.example.heath;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heath.Datebase.DataBaseManager;
import com.example.heath.Datebase.TizhongModle;
import com.example.heath.Datebase.UserModle;
import com.example.heath.HttpUtils.OkNetRequest;
import com.example.heath.utils.LineChartManager;
import com.example.heath.utils.TimeUtils;
import com.example.heath.view.indicator.LineIndicator;
import com.github.mikephil.charting.charts.LineChart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

public class Check_tizhong extends Activity implements View.OnClickListener {

    LineIndicator liProgress;
    private TextView tips;
    private ImageView add;
    private Button start;
    private int weight;
    private int start_weight;
    private int end_weight;
    private NumberPicker numberPicker_weight;
    private Window window;
    private TextView weight_tv;
    private DataBaseManager dataBaseManager;
    private List<TizhongModle> list2;
    private TextView bmi;
    private TextView std;
    private LineChartManager lineChartManager1;
    private ArrayList<Float> xValues;
    private List<List<Float>> yValues;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String leftAlert;
    private String rightAlert;
    private float losseweight;
    private PopupWindow loadingWindow;
    private ImageView mPoint;
    private BluetoothAdapter bluetoothAdapter;
    private String url = "http://47.94.21.55/houtai/addtj.php";
    private MyApplication myApplication;
    private boolean stopThread = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.check_tizhong);
        initView();

        MyApplication.getInstance().addActivity(this);
        initEvent();

    }


    private void initView() {
        ImageView image = (ImageView) findViewById(R.id.back);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myApplication = (MyApplication) getApplication();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        sp = getSharedPreferences("goal_weight", Context.MODE_PRIVATE);
        editor = sp.edit();
        weight_tv = findViewById(R.id.weight);
        liProgress = findViewById(R.id.li_progress);
        add = findViewById(R.id.add);
        tips = findViewById(R.id.tips);
        start = findViewById(R.id.start);
        bmi = findViewById(R.id.BMI);
        std = findViewById(R.id.zhuangtai);
        dataBaseManager = new DataBaseManager();
        MyApplication myApplication = (MyApplication) getApplication();
        if (bluetoothAdapter.isEnabled() && (myApplication.isTag1() && myApplication.isTag2() && myApplication.isTag3())) {
            start.setEnabled(true);
        } else {
            start.setEnabled(false);
            Toast.makeText(this, "您还未连接设备!", Toast.LENGTH_SHORT).show();
        }
        LineChart lineChart = findViewById(R.id.lineChart);//绑定控件
        lineChartManager1 = new LineChartManager(lineChart);
        list2 = dataBaseManager.readtzList();
        if (list2.size() > 0) {
            // 设置Bmi
            String bmi_tv = Bmi(list2.get(list2.size() - 1).getData());
            std.setText(bmi_tv);
            bmi.setText(Bmi_Value(list2.get(list2.size() - 1).getData()) + "");
            weight_tv.setText(list2.get(list2.size() - 1).getData() + "");
        }
        if (list2.size() >= 7)
            LocalData();
        else {
            Toast.makeText(this, "您的数据太少了稍后再看吧！", Toast.LENGTH_SHORT).show();
        }
        testLineProgress();

    }


    private void LocalData() {
        List<TizhongModle> list3 = new DataBaseManager().readtzList();
        Log.e("list3大小改变", list3.size() + "");
        //设置x轴的数据
        xValues = new ArrayList<>();
        if (list3.size() >= 7) {
            for (int i = 0; i < 7; i++) {
                xValues.add((float) i);
            }
            //设置y轴的数据()
            yValues = new ArrayList<>();
            for (int i = 1; i > 0; i--) {
                List<Float> yValue = new ArrayList<>();
                if (list3.size() >= 7) {
                    for (int j = list3.size() - 7; j <= list3.size() - 1; j++) {
                        yValue.add(Float.valueOf(list3.get(j).getData()));
                    }
                }
                yValues.add(yValue);
            }
            initLine();
        } else {
            Toast.makeText(this, "您的数据太少了稍后再看吧！", Toast.LENGTH_SHORT).show();
        }

    }

    private void initLine() {
        //颜色集合
        List<Integer> colours = new ArrayList<>();
        colours.add(Color.RED);
        //线的名字集合
        List<String> names = new ArrayList<>();
        names.add("体重（kg）");
        //创建多条折线的图表
        lineChartManager1.showLineChart(xValues, yValues, names, colours, true, false);
        lineChartManager1.setDescription("体重值");
        lineChartManager1.setYAxis(120, 0, 11);
        lineChartManager1.setLowLimitLine(70, "理想体重", Color.GREEN);


    }

    @Override
    protected void onDestroy() {
        /**
         * 修复退出crash bug  及时关闭线程
         */
        stopThread = true;
        super.onDestroy();
    }
    private void initEvent() {

        tips.setOnClickListener(this);
        liProgress.setOnClickListener(this);
        add.setOnClickListener(this);
        start.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.li_progress:
                prechoose();
                testLineProgress();
                break;
            case R.id.tips:
                Toast.makeText(this, "小蜗提示：可别忘了适当运动保持身材哦！", Toast.LENGTH_SHORT).show();
                break;
            case R.id.add:// 手动添加记录
                View myView = LayoutInflater.from(this).inflate(R.layout.chooseweight, null);
                numberPicker_weight = myView.findViewById(R.id.my_weight);
                numberPicker_weight.setMaxValue(200);
                numberPicker_weight.setMinValue(30);
                numberPicker_weight.setValue(70);
                numberPicker_weight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        weight = i1;

                    }
                });
                AlertDialog.Builder builder3 = new AlertDialog.Builder(Check_tizhong.this);
                builder3.setIcon(R.mipmap.set);
                builder3.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String time = TimeUtils.dateToString2();
                        if (check(weight, time)) {
                            upload(myApplication.getName().toString(), url, String.valueOf(weight), time);
                            Log.e("weight", weight + "");
                            weight_tv.setText(weight + "");
                            bmi.setText(Bmi_Value(weight) + "");
                            std.setText(Bmi(weight));
                            testLineProgress();
                            //  添加数据到曲线图
                            LocalData();
                            Toast.makeText(Check_tizhong.this, "保存成功", Toast.LENGTH_SHORT).show();
                            //更新


                        } else {
                            Toast.makeText(Check_tizhong.this, "保存失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder3.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Check_tizhong.this, "取消", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog3 = builder3.create();
                dialog3.setTitle("体重");
                dialog3.setView(myView);
                //为Window设置动画
                window = dialog3.getWindow();
                window.setWindowAnimations(R.style.animTranslate);
                dialog3.show();

                break;
            case R.id.start:  // 自动体检
                if (bluetoothAdapter.isEnabled()) {
                    showPopupWindow();
                } else Toast.makeText(this, "您还未连接设备!", Toast.LENGTH_SHORT).show();

                break;
        }
    }

    private void testLineProgress() {
        leftAlert = "开始";
        rightAlert = "目标";
        if (sp.contains("start_weight") && sp.contains("end_weight")) {
            String leftContent = sp.getString("start_weight", null) + "公斤";
            String rightContent = sp.getString("end_weight", null) + "公斤";
            liProgress.setContent(leftAlert, leftContent, rightAlert, rightContent);
            if (list2.size() > 0) {
                float now_weight = Float.valueOf(list2.get(list2.size() - 1).getData());
                losseweight = now_weight - Float.parseFloat(sp.getString("start_weight", null));
                liProgress.setIndicator(Float.parseFloat(sp.getString("start_weight", null)), Float.parseFloat(sp.getString("end_weight", null)), now_weight, losseweight + "kg");
            } else {
                String leftContent1 = "60公斤";
                String rightContent1 = "50公斤";
                liProgress.setContent(leftAlert, leftContent1, rightAlert, rightContent1);
                liProgress.setIndicator(60f, 50.0f, 55);
            }
        } else {
            String leftContent = "60公斤";
            String rightContent = "50公斤";
            liProgress.setContent(leftAlert, leftContent, rightAlert, rightContent);
            liProgress.setIndicator(60f, 50.0f, 55);
        }
    }

    private void prechoose() {
        View myView = LayoutInflater.from(this).inflate(R.layout.doublechoose, null);
        NumberPicker numberPicker = myView.findViewById(R.id.my_weight2);
        numberPicker.setMaxValue(200);
        numberPicker.setMinValue(30);
        numberPicker.setValue(70);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                end_weight = newVal;
                editor.putString("end_weight", end_weight + "");
                editor.commit();
            }
        });
        numberPicker_weight = myView.findViewById(R.id.my_weight);
        numberPicker_weight.setMaxValue(200);
        numberPicker_weight.setMinValue(30);
        numberPicker_weight.setValue(70);
        numberPicker_weight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                start_weight = i1;
                editor.putString("start_weight", start_weight + "");
                editor.commit();
            }
        });


        AlertDialog.Builder builder3 = new AlertDialog.Builder(Check_tizhong.this);
        builder3.setIcon(R.mipmap.set);
        builder3.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (list2.size() > 0) {
                    liProgress.setContent(leftAlert, start_weight + "公斤", rightAlert, end_weight + "公斤");
                    liProgress.setIndicator(start_weight, Float.valueOf(list2.get(list2.size() - 1).getData()), end_weight, "体重改变" + losseweight + "kg");
                }
            }
        });
        builder3.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Check_tizhong.this, "取消", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog3 = builder3.create();
        dialog3.setTitle("体重");
        dialog3.setView(myView);
        //为Window设置动画
        window = dialog3.getWindow();
        window.setWindowAnimations(R.style.animTranslate);
        dialog3.show();

    }

    private boolean check(int weight, String time) {
        if (weight != 0) {
            dataBaseManager.saveSingle(null, null, null, null, time, 0, 0, 0, 0, 0, weight);
            return true;
        } else
            return false;

    }

    //BMI=体重（千克）/（身高（米）*身高（米））
    private String Bmi(int weight) {
        float high = 0f;
        List<UserModle> list = dataBaseManager.readuserList();
        Log.e("list", list.size() + "" + list.get(list.size() - 1).getHigh());
        if (list.size() > 0) {
            high = list.get(list.size() - 1).getHigh();
            high = (float) (high / 100.0);
        } else {
            high = 1.7f;
        }

        if (high > 1.3f) {
            float bmi = (float) weight / (high * high);
            if (bmi < 18.5)
                return "过轻";
            if (18.5 <= bmi && bmi < 24)
                return "正常";
            if (24 <= bmi && bmi < 27)
                return "过重";
            if (27 <= bmi && bmi < 30)
                return "轻度肥胖";
            if (30 <= bmi && bmi < 35)
                return "中度肥胖";
            if (35 <= bmi)
                return "重度肥胖";
        }

        return "正常";
    }

    private float Bmi_Value(int weight) {
        float high = 0f;
        float bmi = 0.00f;
        List<UserModle> list = dataBaseManager.readuserList();
        if (list.size() > 0) {
            high = list.get(list.size() - 1).getHigh();
            // 换算米
            high = (float) (high / 100.0);
        } else {
            high = 1.7f;
            Toast.makeText(Check_tizhong.this, "请完善您的个人资料!", Toast.LENGTH_SHORT).show();
        }

        if (high < 1.5) {
            return 0f;
        } else
            bmi = (float) weight / (high * high);


        return (float) Math.round(bmi * 100) / 100;
    }

    private void showPopupWindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.item, null);
        loadingWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        loadingWindow.setContentView(contentView);
        View rootview = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        loadingWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
        loadingWindow.setOutsideTouchable(false);

        mPoint = (ImageView) contentView.findViewById(R.id.point);
        RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_PARENT, 0.37f, Animation.RELATIVE_TO_PARENT, 0.37f);
        ra.setDuration(2000);
        ra.setRepeatCount(Animation.INFINITE);
        ra.setRepeatMode(Animation.RESTART);
        mPoint.startAnimation(ra);
        new Thread() {
            @Override
            public void run() {
                if (!stopThread){
                    try {
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String time = TimeUtils.dateToString2();
                        int w = (int) (60 + Math.random() * 40);
                        if (check(w, time)) {

                            upload(myApplication.getName().toString(), url, String.valueOf(w), time);
                            weight_tv.setText(w + "");
                            bmi.setText(Bmi_Value(w) + "");
                            std.setText(Bmi(w));
                            if (list2.size() >= 7)
                                LocalData();
                        }
                        testLineProgress();
                        mPoint.clearAnimation();
                        loadingWindow.dismiss();
                    }
                });
            }}
        }.start();
    }

    private void upload(String user, String url, String data, String date) {

        HashMap<String, String> params = new HashMap<>();
        // 添加请求参数
        params.put("user", user);
        params.put("atime", date);
        params.put("tizhong", data);

        OkNetRequest.postFormRequest(url, params, new OkNetRequest.DataCallBack() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void requestSuccess(Response response, String result) throws Exception {
                // 请求成功的回调
                Log.e("cccc", result.toString());

            }

            @Override
            public void progressSuccess(Response response) throws Exception {

            }

            @Override
            public void requestFailure(Request request, IOException e) {
                // 请求失败的回调

            }
        });
    }
}
