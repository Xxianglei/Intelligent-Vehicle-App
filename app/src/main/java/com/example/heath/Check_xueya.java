package com.example.heath;

import android.bluetooth.BluetoothAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.example.heath.Datebase.DataBaseManager;
import com.example.heath.Datebase.XueyaModle;
import com.example.heath.HttpUtils.OkNetRequest;
import com.example.heath.utils.TimeUtils;
import com.example.heath.view.DiscView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

public class Check_xueya extends AppCompatActivity implements View.OnClickListener {


    private DiscView mDiscView;
    private ImageView down;
    private View include;
    private boolean tag;
    private Button start;
    private DataBaseManager dataBaseManager;
    private TextView high_tv;
    private TextView low_tv;
    private TextView zhuangtai;
    private TextView date;
    private List<XueyaModle> list;
    private ImageView mPoint;
    private PopupWindow loadingWindow;
    private BluetoothAdapter bluetoothAdapter;
    private String url = "http://47.94.21.55/houtai/addtj.php";
    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_xueya);
        initView();
        initEvent();
        resImg();
        preLoad();

    }

    private void resImg() {
        down.setImageResource(R.mipmap.down2);
        tag = true;
    }


    private void initView() {
        myApplication = (MyApplication)getApplication();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        dataBaseManager = new DataBaseManager();
        down = (ImageView) findViewById(R.id.down);
        mDiscView = (DiscView) findViewById(R.id.score);
        include = findViewById(R.id.doctor_include);
        start = (Button) findViewById(R.id.start);
        high_tv = (TextView) findViewById(R.id.high);
        low_tv = (TextView) findViewById(R.id.low);
        zhuangtai = (TextView) findViewById(R.id.zhuangtai);
        date = (TextView) findViewById(R.id.date);
        mDiscView.setHeadText("健康得分");
        if (bluetoothAdapter.isEnabled()) {
            start.setEnabled(true);
        } else{
            start.setEnabled(false);
            Toast.makeText(this, "您还未连接设备!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initEvent() {
        start.setOnClickListener(this);
        down.setOnClickListener(this);
        include.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.down:
                if (tag) {
                    down.setImageResource(R.mipmap.up2);
                    include.setVisibility(View.VISIBLE);
                    tag = false;
                } else {
                    down.setImageResource(R.mipmap.down2);
                    include.setVisibility(View.GONE);
                    tag = true;
                }
                break;
            case R.id.start:
                showPopupWindow();

                break;
        }
    }

    private void check() {
        // 保存血压数据
        int randomNum_high = (int) (80 + Math.random() * 100);
        int randomNum_low = (int) (50 + Math.random() * 100);
        String time=TimeUtils.dateToString2();
        dataBaseManager.saveSingle(null, time, null, null, null, 0, randomNum_high, randomNum_low, 0, 0, 0);
        // 上传数据
        upload(myApplication.getName().toString(),url,String.valueOf(randomNum_high),String.valueOf(randomNum_low),time);
        date.setText("测量时间" + TimeUtils.dateToString());
        judge(randomNum_high, randomNum_low);

    }

    private void preLoad() {
        list = dataBaseManager.readxyList();
        if (list.size() > 0) {
            judge(list.get(list.size() - 1).getHigh_data(), list.get(list.size() - 1).getLow_data());
            date.setText("测量时间" + list.get(list.size() - 1).getDate());
        } else {
            mDiscView.setValue(90);
        }
    }

    private void judge(int high, int low) {
        high_tv.setText(high + "");
        low_tv.setText(low + "");
        if ((140 < high && low >= 90) || high > 140 || low >= 90) {
            zhuangtai.setText("血压偏高");
            int tag = (int) (30 + Math.random() * 50);
            mDiscView.setValue(tag);
            mDiscView.setbottomText("不理想");
        }
        if ((90 <= high && high <= 120) || (60 <= low && low <= 80)) {
            zhuangtai.setText("血压正常");
            int tag = (int) (80 + Math.random() * 20);
            mDiscView.setValue(tag);
            mDiscView.setbottomText("良好");
        }
        if ((high < 90 && low < 60) || low < 60 || high < 90) {
            zhuangtai.setText("血压偏低");
            int tag = (int) (30 + Math.random() * 50);
            mDiscView.setValue(tag);
            mDiscView.setbottomText("不理想");
        }
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
                try {
                    sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        check();
                        mPoint.clearAnimation();
                        loadingWindow.dismiss();
                    }
                });
            }
        }.start();
    }

    private void upload(String user, String url, String data1, String data2, String date) {

        HashMap<String, String> params = new HashMap<>();
        // 添加请求参数
        params.put("user", user);
        params.put("atime", date);
        params.put("xueya_gao", data1);
        params.put("xueya_di", data2);
        OkNetRequest.postFormRequest(url, params, new OkNetRequest.DataCallBack() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void requestSuccess(Response response,String result) throws Exception {
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
