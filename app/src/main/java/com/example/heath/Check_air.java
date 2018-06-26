package com.example.heath;

import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.heath.Datebase.AirModle;
import com.example.heath.Datebase.DataBaseManager;
import com.example.heath.utils.TimeUtils;
import com.example.heath.view.StepArcView;
import com.example.heath.view.WindPath;

import java.util.List;


public class Check_air extends AppCompatActivity implements View.OnClickListener {

    private WindPath mBigWindMill;
    private WindPath mSmallWindMill;
    private StepArcView sv;
    private DataBaseManager dataBaseManager;
    private List<AirModle> list;
    private Button start;
    private ImageView mPoint;
    private PopupWindow loadingWindow;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_air);
        initView();
        initEvent();
        dataBaseManager = new DataBaseManager();
        list = dataBaseManager.readairList();
        preLoad();
    }


    private void initView() {
        ImageView imageView=(ImageView)findViewById(R.id.back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBigWindMill = (WindPath) findViewById(R.id.id_wind);
        mSmallWindMill = (WindPath) findViewById(R.id.id_windsmall);
        sv = (StepArcView) findViewById(R.id.sv);
        start = (Button)findViewById(R.id.start);

        if (bluetoothAdapter.isEnabled()){
            sv.setClickable(true);
        }
        else{
            sv.setClickable(false);
            Toast.makeText(this,"您还未连接设备!",Toast.LENGTH_SHORT).show();
        }

    }

    private void initEvent() {

        start.setOnClickListener(this);
        mBigWindMill.startAnim();
        mSmallWindMill.startAnim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
                showPopupWindow();

                break;
            default:
                break;

        }
    }

    private void check(String date, int data) {
        dataBaseManager.saveAir(date, data);
    }

    private void preLoad() {
        if (list.size() > 0) {
            sv.setCurrentCount(200,list.get(list.size()-1).getData());
        }else   sv.setCurrentCount(200,0);
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
                        int data = (int)(Math.random()*50+80);
                        check(TimeUtils.dateToString2(), data);
                        sv.setCurrentCount(200, data);
                        mPoint.clearAnimation();
                        loadingWindow.dismiss();
                    }
                });
            }
        }.start();
    }
}
