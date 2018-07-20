package com.example.heath;

import android.bluetooth.BluetoothAdapter;
import android.os.Build;
import android.os.Bundle;
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
import com.example.heath.Datebase.XinlvModle;
import com.example.heath.HttpUtils.OkNetRequest;
import com.example.heath.utils.TimeUtils;
import com.example.heath.view.heartview.DigitalGroupView;
import com.example.heath.view.heartview.DividerItemDecoration;
import com.example.heath.view.heartview.HeartbeatView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Check_xinlv extends AppCompatActivity {
    private HeartbeatView mHeartbeatView;
    private RecyclerView mHeartbeatRecycler;
    private List<HeartbeatEntity> mData = new ArrayList<>();
    private HeartbeatAdapter mAdapter;
    private DigitalGroupView mDigiResult;
    private TextView mTextUnit;
    private DataBaseManager dataBaseManager;
    private boolean Tag = false;
    private View emptyView;
    private BluetoothAdapter bluetoothAdapter;
    private String url="http://47.94.21.55/houtai/addtj.php";
    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_inlv);
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

    private void LoadYunData() {

    }


    private void initView() {
        ImageView imageView=(ImageView)findViewById(R.id.back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myApplication = (MyApplication)getApplication();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        emptyView = findViewById(R.id.cry_nodata);
        mHeartbeatView = (HeartbeatView) findViewById(R.id.heartbeat);
        mDigiResult = (DigitalGroupView) findViewById(R.id.digi_heartbeat_result);
        mTextUnit = (TextView) findViewById(R.id.text_unit);
        mHeartbeatRecycler = (RecyclerView) findViewById(R.id.recycler_heartbeat);
        mHeartbeatRecycler.setLayoutManager(new LinearLayoutManager(this));
        mHeartbeatRecycler.setItemAnimator(new DefaultItemAnimator());
        mHeartbeatRecycler.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter = new HeartbeatAdapter();
        mHeartbeatRecycler.setAdapter(mAdapter);

        if (bluetoothAdapter.isEnabled()){
            mHeartbeatView.setEnabled(true);
        }
        else {
            mHeartbeatView.setEnabled(false);
            Toast.makeText(this,"您还未连接设备!",Toast.LENGTH_SHORT).show();
        }
    }

    private void initEvent() {
        mHeartbeatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHeartbeatView.startAnim();
                hideResult();
            }
        });
        mHeartbeatView.setHeartBeatAnimListener(new HeartbeatView.HeartBeatAnimImpl() {
            @Override
            public void onAnimFinished() {
                DataBaseManager dataBaseManager = new DataBaseManager();
                int randomNum = (int) (50 + Math.random() * 50);
                HeartbeatEntity e = new HeartbeatEntity();
                e.date = TimeUtils.dateToString2();
                e.datum = String.valueOf(randomNum);
                // 数据存入数据库
                String time=TimeUtils.dateToString2();
                upload(myApplication.getName().toString(),url,String.valueOf(randomNum),time);
                dataBaseManager.saveSingle(null, null, time, null, null, 0, 0, 0, randomNum, 0, 0);
                mData.add(0, e);
                mAdapter.notifyItemInserted(0);
                mHeartbeatRecycler.scrollToPosition(0);
                showResult();
                mDigiResult.setDigits(randomNum);
            }
        });
    }

    private boolean LoadLocalData() {
        mData.clear();
        DataBaseManager dataBaseManager = new DataBaseManager();
        List<XinlvModle> list = dataBaseManager.readxlList();
        if (list.size() > 0) {
            for (int i = list.size(); i > 0; i--) {
                HeartbeatEntity e = new HeartbeatEntity();
                e.date = list.get(i - 1).getDate();
                e.datum = (String.valueOf(list.get(i - 1).getData()));
                mData.add(e);

            }
            return true;
        } else {
            // 显示数据为空
            Log.e("心率数据", "*****无*****");

            return false;
        }
    }

    class HeartbeatAdapter extends RecyclerView.Adapter<HeartbeatAdapter.HeartbeatHolder> {
        @Override
        public HeartbeatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            HeartbeatHolder holder = new HeartbeatHolder(LayoutInflater.from(Check_xinlv.this).
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

        class HeartbeatHolder extends RecyclerView.ViewHolder {
            TextView textDate;
            TextView textDatum;

            public HeartbeatHolder(View itemView) {
                super(itemView);
                textDate = (TextView) itemView.findViewById(R.id.date);
                textDatum = (TextView) itemView.findViewById(R.id.datum);
            }
        }
    }

    private void hideResult() {
        AlphaAnimation mHiddenAction = new AlphaAnimation(1f, 0f);
        mHiddenAction.setDuration(400);
        mTextUnit.setAnimation(mHiddenAction);
        mDigiResult.setAnimation(mHiddenAction);
        mTextUnit.setVisibility(GONE);
        mDigiResult.setVisibility(GONE);
    }

    private void showResult() {
        mTextUnit.setVisibility(VISIBLE);
        mDigiResult.setVisibility(VISIBLE);
        emptyView.setVisibility(GONE);
        mHeartbeatRecycler.setVisibility(VISIBLE);
    }

    class HeartbeatEntity {
        String date;
        String datum;
    }
    private void upload(String user, String url, String data, String date) {

        HashMap<String, String> params = new HashMap<>();
        // 添加请求参数
        params.put("user", user);
        params.put("atime", date);
        params.put("xinlv", data);

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
