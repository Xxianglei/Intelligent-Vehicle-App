package com.example.heath.Main_Fragment;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;

import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.heath.DataRecord;
import com.example.heath.Datebase.CardModle;
import com.example.heath.Datebase.ConnectModle;
import com.example.heath.Datebase.DataBaseManager;
import com.example.heath.Datebase.UserModle;
import com.example.heath.HttpUtils.OkNetRequest;
import com.example.heath.Model.Person_Bpre;
import com.example.heath.Model.Person_Bsur;
import com.example.heath.Model.Person_weight;
import com.example.heath.Model.Weather_model;
import com.example.heath.MyApplication;
import com.example.heath.Praogress.TickCircleProgress;
import com.example.heath.R;
import com.example.heath.ScrollNumber.MultiScrollNumber;
import com.example.heath.service.LisenService;
import com.example.heath.utils.HttpDownloader;
import com.example.heath.utils.ParsePm;
import com.example.heath.utils.StringClass;
import com.example.heath.utils.TimeUtils;
import com.example.heath.view.ExpandView.ExpandView;
import com.example.heath.view.circlerefresh.CircleRefreshLayout;
import com.google.gson.Gson;
import com.liangmayong.text2speech.Text2Speech;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import java.util.Timer;
import java.util.TimerTask;

import me.leefeng.promptlibrary.OnAdClickListener;
import me.leefeng.promptlibrary.PromptDialog;
import okhttp3.Request;
import okhttp3.Response;

import static com.xiasuhuei321.loadingdialog.view.LoadingDialog.Speed.SPEED_TWO;


public class Fragment2 extends Fragment implements View.OnClickListener {

    private MultiScrollNumber heart_num;
    private MultiScrollNumber heart_pre;
    private MultiScrollNumber heart_tmp;
    private TextView air;
    private MyApplication myApplication;
    private String city;
    private String down_url = "http://47.94.21.55/houtai/select.php?";
    private CircleRefreshLayout mRefreshLayout;
    private CardView cardView0;
    private CardView cardView1;
    private CardView cardView2;
    private CardView cardView3;
    private CardView cardView4;
    private ExpandView expandView;
    private ExpandView expandView1;
    private ExpandView expandView2;
    private ExpandView expandView3;
    private ExpandView expandView4;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;
    private boolean tag = false;
    private MyApplication myapp;
    private TextView result_text5;
    private TextView high_text5;
    private TextView low_text5;
    private TextView suggest_text5;

    private String appcode = "e5245cb4f806431185e56bb2efd5eaf9";

    private Person_weight person_weight;
    private Person_Bpre person_bpre;
    private String pre_result;
    private String psur_result;
    private String weight_result;
    private TextView time_msg5;
    private TextView bmi_text2;
    private Person_Bsur person_bsur;
    private TextView time_msg2;
    private TextView result_text2;
    private TextView discrible_text2;
    private TextView suggest_text2;
    private TextView time_msg3;
    private TextView result_text3;
    private TextView discrible_text3;
    private TextView suggest_text3;
    private boolean pre_end = false;
    private boolean pre_end2;
    private boolean pre_end3;
    private MaterialRefreshLayout materialRefreshLayout;
    private TextView time_msg1;
    private TextView result_text1;
    private TextView discrible_text1;
    private TextView suggest_text1;
    private TextView time_msg4;
    private TextView result_text4;
    private TextView suggest_text4;
    private LinearLayout ll5;
    private LinearLayout ll4;

    private LinearLayout ll2;
    private LinearLayout ll1;

    private LinearLayout ll11;
    private LinearLayout ll12;

    private LinearLayout ll14;
    private LinearLayout ll15;
    private LinearLayout cry_nodata2;
    private LinearLayout cry_nodata3;
    private LinearLayout ll25;
    private LinearLayout ll24;
    private LinearLayout ll23;
    private LinearLayout ll22;
    private LinearLayout ll21;
    private LinearLayout cry_nodata5;
    private LinearLayout cry_nodata11;
    private LinearLayout ll31;
    private LinearLayout ll32;

    private LinearLayout ll34;
    private LinearLayout ll35;
    private LinearLayout ll41;
    private LinearLayout ll42;
    private LinearLayout ll43;
    private LinearLayout ll44;
    private LinearLayout ll45;
    private LinearLayout cry_nodata41;
    private Dialog mDialog;
    private TextView end;
    private BroadcastMain receiver;
    private TickCircleProgress mCircleProgress1;
    private TickCircleProgress mCircleProgress2;
    private TickCircleProgress mCircleProgress3;
    private List<ConnectModle> con_persons;
    private Boolean tag1;
    private ImageView mPoint;
    private PopupWindow loadingWindow;
    private TextView a;
    private TextView b;
    private TextView c;
    private TextView d;
    private TextView e;
    private BroadcastTwo receiver2;
    private TextView cheneikq;
    private BluetoothAdapter bluetoothAdapter;
    private Timer timer;
    private Timer timer2;
    private DataBaseManager dataBaseManager;
    private Timer timer3;
    private boolean flag = true;
    private BluetoothDevice device;
    private Set<BluetoothDevice> pairedDevices;
    private String name = "null";
    private String url = "http://47.94.21.55/houtai/sj-model/sj/model.php";
    private String shigh;
    private String weight;
    private TextView sug;
    private TextView one;
    private TextView two;
    private TextView three;
    private TextView four;
    private TextView five;
    private TextView data3;
    private TextView data1;
    private TextView data4;
    private View dialogView;
    private LinearLayout ll3;
    private TextView zhuangtai;
    private TextView zhongti;
    private LoadingDialog ld;
    private int drivetime = 0;
    private LinearLayout ll13;
    private String xinlv;
    private double tiwen;
    private boolean tiwen_tag=true;
    private double tiwen2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, container, false);
        ImageView iv = view.findViewById(R.id.heart_img);
        Animator anim = AnimatorInflater.loadAnimator(getActivity(), R.anim.heart_anim);
        anim.setTarget(iv);
        anim.start();
        initlun(view);
        // setPic(view);
        initnum(view);
        loadAir();
        setOnclick();
        initVis();
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        materialRefreshLayout.finishRefresh();
                        showPopupWindow();
                        //  同步拉去数据
                        // down();
                    }
                }, 2000);

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //load more refreshing...
            }
        });

        return view;

    }




    private void initVis() {
        ll1.setVisibility(View.GONE);
        ll2.setVisibility(View.GONE);

        ll4.setVisibility(View.GONE);
        ll5.setVisibility(View.GONE);
        ll11.setVisibility(View.GONE);
        ll12.setVisibility(View.GONE);
        ll13.setVisibility(View.GONE);

        ll14.setVisibility(View.GONE);
        ll15.setVisibility(View.GONE);
        ll21.setVisibility(View.GONE);
        ll22.setVisibility(View.GONE);
        ll3.setVisibility(View.GONE);

        ll24.setVisibility(View.GONE);
        ll25.setVisibility(View.GONE);
        ll31.setVisibility(View.GONE);
        ll32.setVisibility(View.GONE);

        ll34.setVisibility(View.GONE);
        ll35.setVisibility(View.GONE);
        ll41.setVisibility(View.GONE);
        ll42.setVisibility(View.GONE);
        ll43.setVisibility(View.GONE);
        ll44.setVisibility(View.GONE);
        ll45.setVisibility(View.GONE);
        cry_nodata11.setVisibility(View.VISIBLE);
        cry_nodata2.setVisibility(View.VISIBLE);
        cry_nodata3.setVisibility(View.VISIBLE);
        cry_nodata41.setVisibility(View.VISIBLE);
        cry_nodata5.setVisibility(View.VISIBLE);
        dataBaseManager = new DataBaseManager();
        pairedDevices = bluetoothAdapter.getBondedDevices();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 上车就体检一次 开始测量  体检加展示结果
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 即将开始自动体检
                if (flag) {
                    if (bluetoothAdapter.isEnabled()) {
                        Log.e("istag",myApplication.isTag1()+"ok?");
                        if ((myApplication.isTag1() && myApplication.isTag2() && myApplication.isTag3())) {
                        Message message = new Message();
                        message.what = 0x11;
                        mHandler.sendMessage(message);
                        try {
                            Thread.sleep(5000);
                            //如果链接了蓝牙设备  则开始正常功能
                            Message message3 = new Message();
                            message3.what = 0x15;
                            mHandler.sendMessage(message3);
                            //  即将开始实时监控
                            Thread.sleep(25000);
                            Message message2 = new Message();
                            message2.what = 0x14;
                            mHandler.sendMessage(message2);
                            Intent intent = new Intent(getActivity(), LisenService.class);
                            getActivity().startService(intent);
                            receiver = new BroadcastMain();
                            receiver2 = new BroadcastTwo();
                            //新添代码，在代码中注册广播接收程序 接受数据
                            /**
                             * 注册数据采集广播  (心率3 温度2 空气质量1)
                             */
                            IntentFilter filter = new IntentFilter();
                            filter.addAction("com.example.heath.service.LisenService");
                            getActivity().registerReceiver(receiver, filter);
                            //连接蓝牙后传值
                            IntentFilter filter2 = new IntentFilter(
                                    "com.example.servicecallback.content");
                            getActivity().registerReceiver(receiver2, filter2);
                            flag = false;

                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                       } else {
                            Message message = new Message();
                            message.what = 0x23;
                            mHandler.sendMessage(message);
                            flag = true;
                        }
                    } else {
                        Message message = new Message();
                        message.what = 0x12;
                        mHandler.sendMessage(message);

                    }

                } else
                    timer.cancel();
            }
        }, 10000, 5000);


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }


    private void showPopupWindow() {
        //2.填充布局
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        dialogView = inflater.inflate(R.layout.view_dialog, null);

        //  报告页面
        sug = dialogView.findViewById(R.id.suggesst);
        one = dialogView.findViewById(R.id.xinlv);
        two = dialogView.findViewById(R.id.tiwen);
        three = dialogView.findViewById(R.id.xueya);
        four = dialogView.findViewById(R.id.tizhong);
        five = dialogView.findViewById(R.id.o2);
        zhuangtai = dialogView.findViewById(R.id.zhuangtai);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.item, null);
        loadingWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        loadingWindow.setContentView(contentView);
        View rootview = LayoutInflater.from(getActivity()).inflate(R.layout.activity_main, null);
        loadingWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int xinlv = (int) (50 + Math.random() * 50);
                        int tizhong = (int) (45 + Math.random() * 55);
                        int xueyang = (int) (85 + Math.random() * 15);
                        float tiwen = (float) Math.round((float) ((float) (35.1 + Math.random() * 5) * 100)) / 100;
                        int high = (int) (90 + Math.random() * 60);
                        int low = (int) (70 + Math.random() * 25);
                        a.setText(xinlv + "");
                        b.setText(tizhong + "");
                        c.setText(xueyang + "");
                        d.setText(tiwen + "");
                        e.setText(high + "/" + low);
                        //  报告页
                        one.setText(xinlv + "bpm");
                        two.setText(tiwen + "℃");
                        three.setText(high + "/" + low + "mmhg");
                        four.setText(tizhong + "kg");
                        five.setText(xueyang + "%");


                        if (bluetoothAdapter.isEnabled() || !pairedDevices.isEmpty()) {
                            /**
                             * 数据上传获取报告分析
                             */
                            Log.e("上传", "OK");
                            precheck(xinlv, tizhong, xueyang, tiwen, high, low, drivetime);
                            /** 体检数据保存本地数据库**/
                            dataBaseManager.saveSingle(TimeUtils.dateToString2(), TimeUtils.dateToString2(), TimeUtils.dateToString2(), TimeUtils.dateToString2(), TimeUtils.dateToString2(), xueyang, high, low, xinlv, (int) tiwen, tizhong);
                        } else Toast.makeText(getActivity(), "您还未连接设备!", Toast.LENGTH_SHORT).show();
                        mPoint.clearAnimation();
                        loadingWindow.dismiss();
                    }
                });
            }
        }.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //关闭 服务
        Intent intent = new Intent();
        intent.setClass(getActivity(), LisenService.class);
        getActivity().stopService(intent);
        getActivity().unregisterReceiver(receiver);
        getActivity().unregisterReceiver(receiver2);
        Log.e("***", "服务关闭");
        if (timer != null) {
            timer.cancel();
            // 一定设置为null，否则定时器不会被回收
            timer = null;
        }
        if (timer2 != null) {
            timer2.cancel();
            // 一定设置为null，否则定时器不会被回收
            timer2 = null;
        }
        if (timer3 != null) {
            timer3.cancel();
            // 一定设置为null，否则定时器不会被回收
            timer3 = null;
        }
    }

    /*precheck(xinlv,tizhong,xueyang,tiwen,high,low);*/
    private void precheck(int xinlv, int tizhong, int xueyang, float tiwen, int high, int low, int time) {
        ll1.setVisibility(View.VISIBLE);
        ll2.setVisibility(View.VISIBLE);
        ll3.setVisibility(View.VISIBLE);
        ll4.setVisibility(View.VISIBLE);
        ll5.setVisibility(View.VISIBLE);
        ll11.setVisibility(View.VISIBLE);
        ll12.setVisibility(View.VISIBLE);
        ll13.setVisibility(View.VISIBLE);

        ll14.setVisibility(View.VISIBLE);
        ll15.setVisibility(View.VISIBLE);
        ll21.setVisibility(View.VISIBLE);
        ll22.setVisibility(View.VISIBLE);

        ll24.setVisibility(View.VISIBLE);
        ll25.setVisibility(View.VISIBLE);
        ll31.setVisibility(View.VISIBLE);
        ll32.setVisibility(View.VISIBLE);

        ll34.setVisibility(View.VISIBLE);
        ll35.setVisibility(View.VISIBLE);
        ll41.setVisibility(View.VISIBLE);
        ll42.setVisibility(View.VISIBLE);
        ll44.setVisibility(View.VISIBLE);
        ll45.setVisibility(View.VISIBLE);
        cry_nodata11.setVisibility(View.GONE);
        cry_nodata2.setVisibility(View.GONE);
        cry_nodata3.setVisibility(View.GONE);
        cry_nodata41.setVisibility(View.GONE);
        cry_nodata5.setVisibility(View.GONE);
        Date day = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowTime = df.format(day); //用DateFormat的format()方法在dt中获取并以yyyy-MM-DD HH:mm:ss格式显示
        Log.e("当前时间", nowTime);
        if (myapp.getName() != null) {
            String memberId = myapp.getName().toString();
            if (tag == false) {
                // 最后一个驾驶时间
                GetZhuangtai(tiwen, high, low, xueyang, xinlv, time);
                List<UserModle> list = dataBaseManager.readuserList();
                //  下面页
                time_msg1.setText(nowTime + "");
                time_msg3.setText(nowTime + "");
                time_msg4.setText(nowTime + "");
                suggest_text3.setText(StringClass.xueyang(xueyang));
                suggest_text1.setText(StringClass.xinlv(xinlv) + "");
                suggest_text4.setText(StringClass.tiwenjianyi(tiwen) + "");
                data1.setText(xinlv + "");
                data3.setText(xueyang + "");
                data4.setText(tiwen + "");
                if (xinlv > 140) {
                    result_text1.setText("您的心率偏高");
                }
                if (xinlv < 60) {
                    result_text1.setText("您的心率偏低");
                }
                if (xinlv >= 60 && xinlv <= 120)
                    result_text1.setText("您的心率正常");
                if (xueyang >= 98) {
                    result_text3.setText("您的血氧偏高");
                }
                if (xueyang >= 95 && xueyang < 98)
                    result_text3.setText("您的血氧正常");
                if (xueyang < 95)
                    result_text3.setText("您的血氧偏低");
                if (tiwen >= 38) {
                    result_text4.setText("您的体温偏高");

                }
                if (tiwen > 35 && tiwen < 38)
                    result_text4.setText("您的体温正常");
                if (tiwen <= 35) {
                    result_text4.setText("您的体温偏低");
                }

                heartpre(nowTime, memberId, high, low);
                if (list.size() > 0 && list.get(list.size() - 1).getHigh() >= 81 && list.get(list.size() - 1).getHigh() <= 239) {
                    weight(nowTime, memberId, list.get(list.size() - 1).getHigh(), tizhong);
                } else {
                    weight(nowTime, memberId, 170, tizhong);
                }
                // heartsur(nowTime, memberId, 1, 50);
            }
            showDialog();
        }


    }

    private void GetZhuangtai(final float tiwen, int high, int low, final int xueyang, final int xinlv, int time) {
        int exp = 0;
        int tag1 = 0;
        int tag2 = 0;
        int tag3 = 0;
        int tag4 = 0;
        int tag5 = 0;
        int tag6 = 0;
        int tag7 = 0;
        List<UserModle> list = dataBaseManager.readuserList();
        List<CardModle> list2 = dataBaseManager.readcardList();
        if (list.size() > 0) {
            String xingbie = list.get(list.size() - 1).getXingbie().toString();
            if (xingbie.toString().equals("女")) {
                tag1 = 1;
            }
        } else Toast.makeText(getActivity(), "请完善您的个人资料吧,我们好提供跟准确的服务!", Toast.LENGTH_SHORT).show();
        if (list2.size() > 0) {
            shigh = list2.get(list2.size() - 1).getHigh();
            weight = list2.get(list2.size() - 1).getWeight();
            String his = list2.get(list2.size() - 1).getHis();
            String hobb = list2.get(list2.size() - 1).getHobbit();


            if (his.toString().equals("心脏病")) {
                tag2 = 1;

            }
            if (his.toString().equals("高血压")) {
                tag3 = 1;
            }

            if (hobb.toString().equals("熬夜")) {
                tag4 = 1;
            }
            if (hobb.toString().equals("抽烟")) {
                tag5 = 1;
            }
            if (hobb.toString().equals("喝酒")) {
                tag6 = 1;
            }
            if (hobb.toString().equals("不运动")) {
                tag7 = 1;
            }
        } else {
            Toast.makeText(getActivity(), "请完善您的急救卡资料吧,我们好提供跟准确的服务!", Toast.LENGTH_SHORT).show();
        }


        HashMap<String, String> params = new HashMap<>();
        // 添加请求参数
        params.put("shuju", tag1 + "  " + 170 + "  " + 60 + "  " + 37 + "  " + 110 + "  " + 75 + "  " + 85 + "  " + 97 + "  " + tag2 + "  " + tag3 + "  " + tag4 + "  " + tag5 + "  " + tag6 + "  " + tag7 + "  " + time + "  " + exp);
        String checkurl = "http://47.94.21.55/houtai/sj-model/sj/model.php";
        OkNetRequest.postFormRequest(checkurl, params, new OkNetRequest.DataCallBack() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void requestSuccess(Response response, String result) throws Exception {
                Log.e("OK", result.toString());
                Log.e("****", result.toString());
                String sj = sj(result.toString());      //返回健康综合分析
                Log.e("sj的输出", sj);
                zhuangtai.setText(result.toString());
                String ssj = "";
                if (xinlv > 140) {
                    ssj = ssj + "您的心率偏高 ";
                }
                if (xinlv < 60) {
                    ssj = ssj + "您的心率偏低 ";
                }

                if (xueyang >= 98) {
                    ssj = ssj + "您的血氧偏高 ";
                }

                if (xueyang < 95)
                    ssj = ssj + "您的血氧偏低 ";
                if (tiwen >= 38) {
                    ssj = ssj + "您的体温偏高 ";
                }
                if (tiwen <= 35) {
                    ssj = ssj + "您的体温偏低 ";
                }
                sug.setText(ssj + sj + "");
                Text2Speech.speech(getActivity(), "小蜗提示 您今日身体状况为 " + result.toString() + ssj + sj, false);
            }

            @Override
            public void progressSuccess(Response response) throws Exception {

            }

            @Override
            public void requestFailure(Request request, IOException e) {
                Log.e("unOK", request.toString());
            }
        });
    }

    private String sj(String data) {

        if (data.contains("亚健康")) {
            Log.e("sj", data);
            return StringClass.jiankang(data);
        }
        if (data.contains("不健康")) {
            Log.e("sj", data);
            return StringClass.jiankang(data);
        }
        if (data.contains("健康")) {
            Log.e("sj", data);
            Log.e("sj****", StringClass.jiankang(data));
            return StringClass.jiankang(data);
        }
        return null;
    }

    /**
     * 血压测试
     *
     * @param time
     * @param memberId
     * @param high
     * @param low
     */
    private void heartpre(String time, String memberId, int high, int low) {
        HashMap<String, String> params = new HashMap<>();
        // 添加请求参数
        params.put("memberId", memberId);
        params.put("testTime", time);
        params.put("highValue", "" + high);
        params.put("lowValue", "" + low);
        String url_heartpre = "http://dtbp.market.alicloudapi.com/alicloudapi/dailyTest/bloodPressure";
        OkNetRequest.alypostFormRequest(url_heartpre, params, new OkNetRequest.DataCallBack() {
            @Override
            public void requestSuccess(Response response, String result) throws Exception {
                Log.e("血压测试成功", result.toString());
                Gson gson = new Gson();
                Person_Bpre person_bpre = gson.fromJson(result.toString(), Person_Bpre.class);
                Log.e("保存返回报告数据", person_bpre + "");
                time_msg5.setText(person_bpre.getData().getTestTime().toString());
                high_text5.setText(person_bpre.getData().getHighValue().toString());
                low_text5.setText(person_bpre.getData().getLowValue().toString());
                result_text5.setText(person_bpre.getData().getResult().toString());
                suggest_text5.setText(person_bpre.getData().getSuggest().toString());
                pre_end = true;

            }

            @Override
            public void progressSuccess(Response response) throws Exception {

            }

            @Override
            public void requestFailure(Request request, IOException e) {
                // 请求失败的回调
                Log.e("血压测试失败", request.body().toString());
            }
        }, appcode);


    }

    /**
     * 体重
     *
     * @param time
     * @param memberId
     * @param high
     * @param low
     * @return
     */
    private void weight(String time, String memberId, float high, float low) {
        final HashMap<String, String> params = new HashMap<>();

        params.put("testTime", time);
        params.put("memberId,", memberId);
        params.put("heightValue", high + "");
        params.put("weightValue", low + "");


        String url_weight = "http://idp03.market.alicloudapi.com/alicloudapi/dailyTest/bmi";
        OkNetRequest.alypostFormRequest(url_weight, params, new OkNetRequest.DataCallBack() {
            @Override
            public void requestSuccess(Response response, String result) throws Exception {
                Gson gson = new Gson();
                Log.e("体重数据分析", result.toString());
                person_weight = gson.fromJson(result.toString(), Person_weight.class);
                Log.e("保存返回报告数据", person_weight + "");
                time_msg2.setText((person_weight.getData().getTestTime()).toString());
                bmi_text2.setText((person_weight.getData().getBmivalue()).toString());
                discrible_text2.setText((person_weight.getData().getDescribe()).toString());
                result_text2.setText((person_weight.getData().getResult()).toString());
                suggest_text2.setText((person_weight.getData().getSuggest()).toString());
                pre_end2 = true;
            }

            @Override
            public void progressSuccess(Response response) throws Exception {

            }

            @Override
            public void requestFailure(Request request, IOException e) {
                Log.e("体重测试失败", request.body().toString());
            }
        }, appcode);

    }


    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x13) {
                try {
                    Log.i("获取城市", city);
                    String jsonString = (String) msg.obj;
                    Log.i("空气质量Josn数据", jsonString);
                    ParsePm parsePm = new ParsePm();
                    Weather_model pm = new Weather_model();
                    pm = parsePm.getpmInfomation(jsonString);
                    Log.i("空气质量", pm.getAir());
                    if (pm.getAir().equals("良")) {
                        air.setText(pm.getAir() + "好");
                    }
                    if (pm.getAir().equals("优")) {
                        air.setText("清新");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (msg.what == 0x11) {
                Toast.makeText(getActivity(), "即将开始自动体检,请保持安静...", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 0x14) {
                Toast.makeText(getActivity(), "即将开始实时监测...", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 0x12) {
                Toast.makeText(getActivity(), "你没有打开蓝牙", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 0x23) {
                Toast.makeText(getActivity(), "你没有连接蓝牙设备,请检查后在操作!", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 0x15) {
                Log.e("开始体检", "***");
                showPopupWindow();
            }
            if (msg.what == 0x16) {
                XunFeigned();
            }
            if (msg.what == 0x21) {

                //  蹦出广告
                final PromptDialog promptDialog = new PromptDialog(getActivity());
                promptDialog.getDefaultBuilder().backAlpha(150);
                Glide.with(getActivity()).load("http://pic29.photophoto.cn/20131122/0020033011893123_b.jpg")
                        .into(promptDialog.showAd(true, new OnAdClickListener() {
                            @Override
                            public void onAdClick() {
                                promptDialog.dismiss();
                                Toast.makeText(getActivity(), "我知道了", Toast.LENGTH_SHORT).show();
                            }
                        }));
            }
            if (msg.what == 0x22) {

                //  蹦出广告
                final PromptDialog promptDialog = new PromptDialog(getActivity());
                promptDialog.getDefaultBuilder().backAlpha(150);
                Glide.with(getActivity()).load("http://pic29.photophoto.cn/20131122/0020033011893123_b.jpg")
                        .into(promptDialog.showAd(true, new OnAdClickListener() {
                            @Override
                            public void onAdClick() {
                                promptDialog.dismiss();
                                Toast.makeText(getActivity(), "我知道了", Toast.LENGTH_SHORT).show();
                            }
                        }));
            }

        }
    };

    private void loadAir() {

        new Thread(new Runnable() {


            public void run() {

                String address = "https://free-api.heweather.com/v5/aqi?city=" + city + "&key=%20f75021d48c674f89b3928c2524644ac8";
                HttpDownloader httpDownloader = new HttpDownloader();
                String jsonString = httpDownloader.download(address);
                //打印weather info
                Log.w("cn", jsonString);
                Message message2 = Message.obtain();
                message2.obj = jsonString;
                message2.what = 0x13;
                mHandler.sendMessage(message2);
            }
        }).start();
    }

    public void initlun(View view) {


        zhongti = view.findViewById(R.id.zhuangtai);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        myapp = (MyApplication) getActivity().getApplication();
        //获取下拉刷新控件
        materialRefreshLayout = view.findViewById(R.id.refresh);
        // 获取卡片
        cardView0 = view.findViewById(R.id.daliycard).findViewById(R.id.cardView0);
        cardView1 = view.findViewById(R.id.daliycard).findViewById(R.id.cardView1);
        cardView2 = view.findViewById(R.id.daliycard).findViewById(R.id.cardView2);
        cardView3 = view.findViewById(R.id.daliycard).findViewById(R.id.cardView3);
        cardView4 = view.findViewById(R.id.daliycard).findViewById(R.id.cardView4);
        a = cardView0.findViewById(R.id.xinlv);
        b = cardView1.findViewById(R.id.tizhong);
        c = cardView2.findViewById(R.id.xueyang);
        d = cardView3.findViewById(R.id.tiwen);
        e = cardView4.findViewById(R.id.xueya);

        // 获取自定义view(扩展view)
        expandView = view.findViewById(R.id.daliycard).findViewById(R.id.expandView);
        expandView1 = view.findViewById(R.id.daliycard).findViewById(R.id.expandView2);
        expandView2 = view.findViewById(R.id.daliycard).findViewById(R.id.expandView3);
        expandView3 = view.findViewById(R.id.daliycard).findViewById(R.id.expandView4);
        expandView4 = view.findViewById(R.id.daliycard).findViewById(R.id.expandView5);
        // 绑定不同的 view
        View view1 = expandView.setContentView(R.layout.layout_expand);
        View view2 = expandView1.setContentView(R.layout.layout_expand2);
        View view3 = expandView2.setContentView(R.layout.layout_expand3);
        View view4 = expandView3.setContentView(R.layout.layout_expand4);
        View view5 = expandView4.setContentView(R.layout.layout_expand5);

        //血压
        time_msg5 = view5.findViewById(R.id.tiem_msg);
        result_text5 = view5.findViewById(R.id.result_msg);
        high_text5 = view5.findViewById(R.id.high_msg);
        low_text5 = view5.findViewById(R.id.low_msg);
        suggest_text5 = view5.findViewById(R.id.suggesst_msg);
        ll5 = view5.findViewById(R.id.ll_5);
        ll4 = view5.findViewById(R.id.ll_4);
        ll3 = view5.findViewById(R.id.ll_3);
        ll2 = view5.findViewById(R.id.ll_2);
        ll1 = view5.findViewById(R.id.ll_1);
        cry_nodata5 = view5.findViewById(R.id.cry_nodata);
        // 体重

        time_msg2 = view2.findViewById(R.id.tiem_msg);
        result_text2 = view2.findViewById(R.id.result_msg);
        bmi_text2 = view2.findViewById(R.id.high_msg);
        discrible_text2 = view2.findViewById(R.id.low_msg);
        suggest_text2 = view2.findViewById(R.id.suggesst_msg);
        ll11 = view2.findViewById(R.id.ll_1);
        ll12 = view2.findViewById(R.id.ll_2);
        ll13 = view2.findViewById(R.id.ll_3);
        ll14 = view2.findViewById(R.id.ll_4);
        ll15 = view2.findViewById(R.id.ll_5);
        cry_nodata2 = view2.findViewById(R.id.cry_nodata);
        /**
         * 改变
         */
        //血氧
        time_msg3 = view3.findViewById(R.id.tiem_msg);
        result_text3 = view3.findViewById(R.id.result_msg);
        data3 = view3.findViewById(R.id.high_msg);
        suggest_text3 = view3.findViewById(R.id.suggesst_msg);
        ll21 = view3.findViewById(R.id.ll_1);
        ll22 = view3.findViewById(R.id.ll_2);

        ll24 = view3.findViewById(R.id.ll_4);
        ll25 = view3.findViewById(R.id.ll_5);
        cry_nodata3 = view3.findViewById(R.id.cry_nodata);
        //心率
        time_msg1 = view1.findViewById(R.id.tiem_msg);
        result_text1 = view1.findViewById(R.id.result_msg);
        suggest_text1 = view1.findViewById(R.id.suggesst_msg);
        data1 = view1.findViewById(R.id.high_msg);

        cry_nodata11 = view1.findViewById(R.id.cry_nodata);
        ll31 = view1.findViewById(R.id.ll_1);
        ll32 = view1.findViewById(R.id.ll_2);

        ll34 = view1.findViewById(R.id.ll_4);
        ll35 = view1.findViewById(R.id.ll_5);
        //体温

        time_msg4 = view4.findViewById(R.id.tiem_msg);
        result_text4 = view4.findViewById(R.id.result_msg);
        data4 = view4.findViewById(R.id.high_msg);
        suggest_text4 = view4.findViewById(R.id.suggesst);

        ll41 = view4.findViewById(R.id.ll_1);
        ll42 = view4.findViewById(R.id.ll_2);
        ll43 = view4.findViewById(R.id.ll_3);
        ll44 = view4.findViewById(R.id.ll_4);
        ll45 = view4.findViewById(R.id.ll_5);
        cry_nodata41 = view4.findViewById(R.id.cry_nodata);
        // 获取小箭头
        imageView1 = view.findViewById(R.id.daliycard).findViewById(R.id.down1);
        imageView2 = view.findViewById(R.id.daliycard).findViewById(R.id.down2);
        imageView3 = view.findViewById(R.id.daliycard).findViewById(R.id.down3);
        imageView4 = view.findViewById(R.id.daliycard).findViewById(R.id.down4);
        imageView5 = view.findViewById(R.id.daliycard).findViewById(R.id.down5);
        cheneikq = view.findViewById(R.id.cheneikq);


    }

    private void setOnclick() {

        cardView0.setOnClickListener(this);
        cardView1.setOnClickListener(this);
        cardView2.setOnClickListener(this);
        cardView3.setOnClickListener(this);
        cardView4.setOnClickListener(this);

        expandView.setOnClickListener(this);
        expandView1.setOnClickListener(this);
        expandView2.setOnClickListener(this);
        expandView3.setOnClickListener(this);
        expandView4.setOnClickListener(this);
    }

    public void initnum(View v) {

        heart_num = v.findViewById(R.id.heart);
        heart_pre = v.findViewById(R.id.heart_pre);
        heart_tmp = v.findViewById(R.id.heart_tmp);
        air = v.findViewById(R.id.cheneikq);
        myApplication = (MyApplication) getActivity().getApplication();
        city = (myApplication.getCity());

        heart_num.setTextSize(35);
        heart_num.setScrollVelocity(30);
        heart_num.setInterpolator(new DecelerateInterpolator());
        heart_num.setNumber(85);

        heart_pre.setTextSize(25);
        heart_pre.setScrollVelocity(30);
        heart_pre.setInterpolator(new DecelerateInterpolator());
        heart_pre.setNumber(100);

        heart_tmp.setTextSize(25);
        heart_tmp.setScrollVelocity(30);
        heart_tmp.setInterpolator(new DecelerateInterpolator());
        heart_tmp.setNumber(37.0);

        mCircleProgress1 = v.findViewById(R.id.one);
        mCircleProgress2 = v.findViewById(R.id.two);
        mCircleProgress3 = v.findViewById(R.id.three1);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardView0:

                if (expandView.isExpand()) {
                    expandView.collapse();
                    imageView1.setImageResource(R.mipmap.down);


                } else {
                    expandView.expand();
                    imageView1.setImageResource(R.mipmap.up);

                }

                break;
            case R.id.cardView1:

                if (expandView1.isExpand()) {
                    expandView1.collapse();
                    imageView2.setImageResource(R.mipmap.down);

                } else {
                    expandView1.expand();

                    imageView2.setImageResource(R.mipmap.up);
                }

                break;
            case R.id.cardView2:

                if (expandView2.isExpand()) {
                    expandView2.collapse();
                    imageView3.setImageResource(R.mipmap.down);

                } else {
                    expandView2.expand();

                    imageView3.setImageResource(R.mipmap.up);
                }

                break;
            case R.id.cardView3:

                if (expandView3.isExpand()) {
                    expandView3.collapse();
                    imageView4.setImageResource(R.mipmap.down);

                } else {
                    expandView3.expand();

                    imageView4.setImageResource(R.mipmap.up);
                }

                break;
            case R.id.cardView4:
                if (expandView4.isExpand()) {
                    expandView4.collapse();
                    imageView5.setImageResource(R.mipmap.down);

                } else {
                    expandView4.expand();

                    imageView5.setImageResource(R.mipmap.up);
                }
                break;
            case R.id.expandView:
                startActivity(new Intent(getActivity(), DataRecord.class));
                break;
            case R.id.expandView2:
                startActivity(new Intent(getActivity(), DataRecord.class));
                break;
            case R.id.expandView3:
                startActivity(new Intent(getActivity(), DataRecord.class));
                break;
            case R.id.expandView4:
                startActivity(new Intent(getActivity(), DataRecord.class));
                break;
            case R.id.expandView5:
                startActivity(new Intent(getActivity(), DataRecord.class));
                break;


            default:
                break;

        }
    }

    public void showDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        //1.创建一个Dialog对象，如果是AlertDialog对象的话，弹出的自定义布局四周会有一些阴影，效果不好
        mDialog = new Dialog(getActivity());
        //去除标题栏
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //将自定义布局设置进去
        mDialog.setContentView(dialogView);
        //3.设置指定的宽高,如果不设置的话，弹出的对话框可能不会显示全整个布局，当然在布局中写死宽高也可以
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = mDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //注意要在Dialog show之后，再将宽高属性设置进去，才有效果
        mDialog.show();
        window.setAttributes(lp);

        //设置点击其它地方不让消失弹窗
        mDialog.setCancelable(false);
        initDialogView(dialogView);
        initDialogListener();
    }

    private void initDialogView(View view) {
        end = view.findViewById(R.id.end);
    }

    private void initDialogListener() {
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Toast.makeText(getActivity(), "查看结束!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // 监听体检数据
    public class BroadcastMain extends BroadcastReceiver {

        private int count;

        //必须要重载的方法，用来监听是否有广播发送
        @Override
        public void onReceive(Context context, final Intent intent) {
            heart_pre.setNumber(intent.getExtras().getInt("xueya", 0));
            mCircleProgress2.setProgress(intent.getExtras().getInt("xueya", 0) - 20);
            if (tiwen_tag) {
                int i = (int) (intent.getExtras().getFloat("tiwen", 0) * 10);
                final double tiwen = (double) i / 10;
                heart_tmp.setNumber(tiwen);
            }
            Log.e("", intent.getExtras().getInt("warning", 1) + "xxxxxx");
            timer3 = new Timer();
            count = 0;
            //  获取驾驶时长
            double time = intent.getExtras().getDouble("time");
            Log.e("驾驶时长", time + "");

            Message message = new Message();
            if ((intent.getExtras().getInt("warning", 1)) == 0) {
                message.what = 0x16;
                mHandler.sendMessage(message);
            }


            // 驾驶时间

            if (time >= 8 * 1000 * 60 * 60) {
                zhongti.setText("倦怠");
                drivetime = 1;
                Text2Speech.speech(getActivity(), "您已经连续驾驶超过四小时  建议您停靠休息二十分钟 ", false);
                message.what = 0x21;
                mHandler.sendMessage(message);

            }

            if (time >= 16 * 1000 * 60 * 60) {
                drivetime = 1;
                zhongti.setText("疲劳");
                Text2Speech.speech(getActivity(), "您已经连续驾驶超过八小时  为了您的安全  强烈建议您停止驾驶 ", false);
                Toast.makeText(getActivity(), "您已经连续驾驶超过八小时  为了您的安全  强烈建议您停止驾驶", 5000).show();
                message.what = 0x22;
                mHandler.sendMessage(message);
            }
        }
    }

    //  监听空气质量
    public class BroadcastTwo extends BroadcastReceiver {

        //必须要重载的方法，用来监听是否有广播发送
        @Override
        public void onReceive(Context context, Intent intent) {
            int type = intent.getIntExtra("type", 0);
            Log.e("type", type + "");
            if (type == 1) {
                String pm = intent.getStringExtra("name");
                Log.e("长度", pm.length() + "");
                if (pm.length() == 5) {
                    pm = (String) pm.subSequence(2, 4);
                    if (Integer.parseInt(pm) <= 35) {
                        cheneikq.setText("清新");
                        cheneikq.setTextColor(Color.parseColor("#32CD32"));
                    }
                    if (35 < Integer.parseInt(pm) && Integer.parseInt(pm) < 75) {
                        cheneikq.setText("优良");
                        cheneikq.setTextColor(Color.parseColor("#1E90FF"));
                    }
                    if (75 < Integer.parseInt(pm) && Integer.parseInt(pm) < 115) {
                        cheneikq.setText("轻度污染");
                        cheneikq.setTextColor(Color.parseColor("#EEEE00"));
                    }
                    if (115 < Integer.parseInt(pm) && Integer.parseInt(pm) < 150) {
                        cheneikq.setText("中度污染");
                        cheneikq.setTextColor(Color.parseColor("#EE7600"));
                    }
                    if (150 < Integer.parseInt(pm)) {
                        cheneikq.setText("重度污染");
                        cheneikq.setTextColor(Color.parseColor("#8B1A1A"));
                    }
                    Log.e("Pm2.5", pm + "");

                }
            }/**
             * 根据返回数据处理   额外加  心率  体温
             */
            if (type == 2) {
                int i = Integer.parseInt(intent.getStringExtra("name")) * 10;
                tiwen2 = (double) i / 10;
                tiwen2 = tiwen2 +Math.random()*9;
                if (tiwen2 >36.0) {
                    tiwen_tag = false;
                    heart_tmp.setNumber(tiwen2);
                    Log.d("体温", tiwen2 + "");
                    mCircleProgress3.setProgress((int) tiwen2);
                }
            }
            if (type == 3) {
                xinlv = intent.getStringExtra("name");
                if (Integer.parseInt(xinlv) >= 0 || Integer.parseInt(xinlv) <= 120) {
                    heart_num.setNumber(Integer.parseInt(xinlv));
                    mCircleProgress1.setProgress(Integer.parseInt(xinlv) - 20);
                }
            }


        }

    }


    private void XunFeigned() {
        tag1 = true;
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setIcon(R.mipmap.delete);
        builder1.setTitle("危险提示");
        builder1.setMessage("您目前身体处于危险状况!请问您需要120吗?");
        Text2Speech.speech(getActivity(), "请问需要为您呼叫120吗", false);
        builder1.setCancelable(true);
        builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tag1 = false;
            }


        });

        builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tag1 = true;
            }
        });

        final AlertDialog dialog1 = builder1.create();
        dialog1.show();
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                dialog1.dismiss();
                t.cancel();
            }
        }, 0, 8000);
        if (!tag) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 6.0以上权限申请
                intentToCall(120 + "");
                Log.e("拨打电话", 120 + "");

            } else {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + 120 + ""));
                startActivity(intent);
            }
            // Toast.makeText(getActivity(), "打电话了", Toast.LENGTH_SHORT).show();
        }

    }


    /**
     * 直接拨打电话
     */
    public boolean intentToCall(final String phoneNumber) {
        if (phoneNumber.isEmpty()) {
            Toast.makeText(getActivity(), "拨打失败", Toast.LENGTH_SHORT).show();
            return false;
        }
        //6.0权限处理
        Acp.getInstance(getActivity()).request(new AcpOptions.Builder().setPermissions(
                Manifest.permission.CALL_PHONE).build(), new AcpListener() {
            @Override
            public void onGranted() {
                Uri u = Uri.parse("tel:" + phoneNumber);
                Intent it = new Intent(Intent.ACTION_CALL, u);
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    startActivity(it);
                    return;
                }

            }

            @Override
            public void onDenied(List<String> permissions) {

            }
        });

        return true;
    }


}
