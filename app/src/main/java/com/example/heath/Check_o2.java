package com.example.heath;

import android.bluetooth.BluetoothAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.heath.Datebase.DataBaseManager;
import com.example.heath.Datebase.XueyangModle;
import com.example.heath.HttpUtils.OkNetRequest;
import com.example.heath.utils.TimeUtils;
import com.example.heath.utils.Utils;
import com.example.heath.view.StepView;
import com.example.heath.view.heartview.DividerItemDecoration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class Check_o2 extends AppCompatActivity {

    private StepView mSvStep;
    private Handler mHandler = new Handler();

    private RecyclerView mHeartbeatRecycler;
    private List<HeartbeatEntity> mData = new ArrayList<>();
    private HeartbeatAdapter mAdapter;
    private View emptyView;
    private MyApplication myApplication;
    private String url = "http://47.94.21.55/houtai/addtj.php";
    private boolean stopThread = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_o2);
        MyApplication.getInstance().addActivity(this);
        initView();
        initEvent();
        //hideResult();
        // showResult();
        if (LoadLocalData() == false) {
            LoadYunData();
        } else {
            LoadLocalData();
        }
        if (mAdapter.getItemCount() == 0) {
            emptyView.setVisibility(VISIBLE);
            mHeartbeatRecycler.setVisibility(GONE);
        } else {
            emptyView.setVisibility(GONE);
            mHeartbeatRecycler.setVisibility(VISIBLE);
        }

    }

    @Override
    protected void onDestroy() {
        /**
         * 修复退出crash bug  及时关闭线程
         */
        stopThread = true;
        if (mSvStep != null)
            mSvStep = null;
        super.onDestroy();
    }

    private void LoadYunData() {

    }

    private void initView() {
        ImageView imageView = (ImageView) findViewById(R.id.back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        emptyView = findViewById(R.id.cry_nodata);
        mHeartbeatRecycler = (RecyclerView) findViewById(R.id.recycler_heartbeat);
        mHeartbeatRecycler.setLayoutManager(new LinearLayoutManager(this));
        mHeartbeatRecycler.setItemAnimator(new DefaultItemAnimator());
        mHeartbeatRecycler.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter = new HeartbeatAdapter();
        mHeartbeatRecycler.setAdapter(mAdapter);
        mSvStep = (StepView) findViewById(R.id.sv_step);
        mSvStep.setmMaxValueint(100);

        if (bluetoothAdapter.isEnabled()) {
            mSvStep.setEnabled(true);
        } else {
            mSvStep.setEnabled(false);
            Toast.makeText(this, "您还未连接设备!", Toast.LENGTH_SHORT).show();
        }
        myApplication = (MyApplication) getApplication();
    }


    private boolean LoadLocalData() {
        mData.clear();
        DataBaseManager dataBaseManager = new DataBaseManager();
        List<XueyangModle> list = dataBaseManager.readxyangList();
        if (list.size() > 0) {
            Log.e("list.size()", list.size() + "");
            for (int i = list.size(); i > 0; i--) {
                HeartbeatEntity e = new HeartbeatEntity();
                e.date = list.get(i - 1).getDate();
                e.datum = (String.valueOf(list.get(i - 1).getData()));
                mData.add(e);
            }

            mSvStep.setmCurrentValue(list.get(list.size() - 1).getData());
            mSvStep.finish();
            return true;
        } else {
            // 显示数据为空
            Log.e("心率数据", "*****无*****");
            mSvStep.setmCurrentValue(0);
            mSvStep.finish();
            return false;
        }
    }

    private class HeartbeatAdapter extends RecyclerView.Adapter<HeartbeatAdapter.HeartbeatHolder> {
        @Override
        public HeartbeatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            HeartbeatHolder holder = new HeartbeatHolder(LayoutInflater.from(Check_o2.this).
                    inflate(R.layout.item_heartbeat, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(HeartbeatHolder holder, int position) {
            holder.textDate.setText(mData.get(position).date);
            holder.textDatum.setText(mData.get(position).datum);
        }

        @Override
        public int getItemCount() {
            Log.e("mData.size()", mData.size() + "");
            return mData.size();
        }

        private class HeartbeatHolder extends RecyclerView.ViewHolder {
            TextView textDate;
            TextView textDatum;
            TextView text_unit;

            public HeartbeatHolder(View itemView) {
                super(itemView);
                textDate = (TextView) itemView.findViewById(R.id.date);
                textDatum = (TextView) itemView.findViewById(R.id.datum);
                text_unit = (TextView) itemView.findViewById(R.id.text_unit);
                text_unit.setText("%");
            }
        }
    }

    private class HeartbeatEntity {
        String date;
        String datum;

    }

    private void initEvent() {

        // 进行点击事件后的逻辑操作


        mSvStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isFastClick()) {
                    mSvStep.loading();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (!stopThread) {
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        emptyView.setVisibility(GONE);
                                        mHeartbeatRecycler.setVisibility(VISIBLE);
                                        int randomNum = (int) (80 + Math.random() * 20);
                                        HeartbeatEntity e = new HeartbeatEntity();
                                        e.date = TimeUtils.dateToString2();
                                        e.datum = String.valueOf(randomNum);
                                        String time = TimeUtils.dateToString2();
                                        DataBaseManager dataBaseManager = new DataBaseManager();
                                        // 数据存入数据库
                                        dataBaseManager.saveSingle(time, null, null, null, null, randomNum, 0, 0, 0, 0, 0);
                                        upload(myApplication.getName().toString(), url, String.valueOf(randomNum), time);
                                        mData.add(0, e);
                                        mAdapter.notifyItemInserted(0);
                                        mHeartbeatRecycler.scrollToPosition(0);
                                        if (mSvStep != null) {
                                            mSvStep.setmCurrentValue(randomNum);
                                            mSvStep.finish();
                                        }
                                    }
                                });
                            }
                        }
                    }).start();
                }
            }
        });

    }

    private void upload(String user, String url, String data, String date) {

        HashMap<String, String> params = new HashMap<>();
        // 添加请求参数
        params.put("user", user);
        params.put("atime", date);
        params.put("xueyang", data);

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
