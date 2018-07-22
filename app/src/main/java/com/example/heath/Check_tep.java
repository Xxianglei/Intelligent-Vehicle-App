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
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heath.Datebase.DataBaseManager;
import com.example.heath.Datebase.TiwenModle;

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


public class Check_tep extends AppCompatActivity {

    private StepView mSvStep;
    private Handler mHandler = new Handler();

    private RecyclerView mHeartbeatRecycler;
    private List<HeartbeatEntity> mData = new ArrayList<>();
    private HeartbeatAdapter mAdapter;
    private View emptyView;
    private String url = "http://47.94.21.55/houtai/addtj.php";
    private MyApplication myApplication;
    private boolean stopThread = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_tep);
        initView();
        MyApplication.getInstance().addActivity(this);
        initEvent();
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
        mSvStep.setmMaxValueint(50);
        if (bluetoothAdapter.isEnabled()) {
            mSvStep.setEnabled(true);
        } else {
            mSvStep.setEnabled(false);
            Toast.makeText(this, "您还未连接设备!", Toast.LENGTH_SHORT).show();
        }
        myApplication = (MyApplication) getApplication();
    }


    private class HeartbeatAdapter extends RecyclerView.Adapter<HeartbeatAdapter.HeartbeatHolder> {
        @Override
        public HeartbeatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            HeartbeatHolder holder = new HeartbeatHolder(LayoutInflater.from(Check_tep.this).
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
                text_unit.setText("℃");
            }
        }
    }

    private void hideResult() {
        AlphaAnimation mHiddenAction = new AlphaAnimation(1f, 0f);
        mHiddenAction.setDuration(400);

    }

    private void showResult() {

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
                            if(!stopThread) {
                                try {
                                    Thread.sleep(5000);
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            emptyView.setVisibility(GONE);
                                            mHeartbeatRecycler.setVisibility(VISIBLE);
                                            DataBaseManager dataBaseManager = new DataBaseManager();
                                            float randomNum = (float) (35.1 + Math.random() * 3.2);
                                            int i = (int) (randomNum * 10);
                                            // 转回float类型,然后将乘上的数重新除去。
                                            randomNum = (float) i / 10;
                                            HeartbeatEntity e = new HeartbeatEntity();
                                            e.date = TimeUtils.dateToString2();
                                            e.datum = String.valueOf(randomNum);
                                            //  数据存入数据库
                                            String time = TimeUtils.dateToString2();
                                            upload(myApplication.getName().toString(), url, String.valueOf(randomNum), time);
                                            dataBaseManager.saveSingle(null, null, null, time, null, 0, 0, 0, 0, randomNum, 0);
                                            mData.add(0, e);
                                            mAdapter.notifyItemInserted(0);
                                            mHeartbeatRecycler.scrollToPosition(0);
                                            showResult();
                                            if (mSvStep != null) {
                                                mSvStep.setmCurrentValue(randomNum);
                                                mSvStep.finish();
                                            }
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }
            }
        });

    }


    private boolean LoadLocalData() {
        mData.clear();
        DataBaseManager dataBaseManager = new DataBaseManager();
        List<TiwenModle> list = dataBaseManager.readtwList();
        if (list.size() > 0) {
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

    private void upload(String user, String url, String data, String date) {

        HashMap<String, String> params = new HashMap<>();
        // 添加请求参数
        params.put("user", user);
        params.put("atime", date);
        params.put("tiwen", data);

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
