package com.example.heath.Register;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.heath.MyApplication;
import com.example.heath.R;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.phone_num)
    EditText phoneNum;
    @BindView(R.id.send_v_code)
    Button sendVCode;
    @BindView(R.id.et_vcode)
    EditText etVcode;
    @BindView(R.id.btn_next)
    Button btnNext;
    private NetworkChangeReceiver networkChangeReceiver;
    private MyApplication myApplication;

    @OnClick({R.id.send_v_code, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_v_code:  //发送验证码
                check();
                break;
            case R.id.btn_next:     //验证成功下一步
                Intent i = getIntent();
                Bundle b = i.getExtras();
                phone = phoneNum.getText().toString().trim().replaceAll("/s", "");
                myApplication.setName(phone);
                if (phoneNum.getText().length() > 0 && etVcode.getText().length() > 0) {
                    if (b.getInt("TAG") == 1) {

                        String code = etVcode.getText().toString().replaceAll("/s", "");
                        if (!TextUtils.isEmpty(code)) {//判断验证码是否为空
                            //验证
                            SMSSDK.submitVerificationCode(country, phone, code);
                            Bundle bundle = new Bundle();
                            bundle.putString("phone", phone);
                            Intent intent = new Intent(RegisterActivity.this, Set_password.class);
                            startActivity(intent);

                        } else {//如果用户输入的内容为空，提醒用户
                            toast("请输入正确的验证码");
                        }
                    }
                } else {
                    toast("请输入正确的验证码");
                }

                break;
            default:
                break;
        }
    }

    private TimerTask tt;
    private Timer tm;
    private int TIME = 60;//倒计时60s这里应该多设置些因为mob后台需要60s,我们前端会有差异的建议设置90，100或者120
    public String country = "86";//这是中国区号，如果需要其他国家列表，可以使用getSupportedCountries();获得国家区号
    private String phone;
    private static final int CODE_REPEAT = 1; //重新发送
    private FloatingActionButton fab;
    private CardView cvAdd;
    private Button go;     //


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_on);
        ShowEnterAnimation();
        MyApplication.getInstance().addActivity(this);
        initView();
        ButterKnife.bind(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });
    }

    private void initView() {
        myApplication = (MyApplication) getApplication();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        //动态注册
        registerReceiver(networkChangeReceiver, intentFilter);
        go = (Button) findViewById(R.id.bt_go);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        cvAdd = (CardView) findViewById(R.id.cv_add);
    }

    Handler hd = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == CODE_REPEAT) {
                btnNext.setEnabled(true);
                sendVCode.setEnabled(true);
                tm.cancel();//取消任务
                tt.cancel();//取消任务
                TIME = 60;//时间重置
                sendVCode.setText("重新发送验证码");
            } else {
                sendVCode.setText(TIME + "秒");
                sendVCode.setEnabled(false);
            }
        }
    };
    //回调
    EventHandler eh = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {       //获取验证码成功


                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {//如果你调用了获取国家区号类表会在这里回调
                    //返回支持发送验证码的国家列表


                }
            } else {//错误等在这里（包括验证失败）
                //错误码请参照http://wiki.mob.com/android-api-错误码参考/这里我就不再继续写了
                ((Throwable) data).printStackTrace();
                String str = data.toString();
                toast(str);
            }
        }
    };

    //吐司的一个小方法
    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, str, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void check() {
        phone = phoneNum.getText().toString().trim().replaceAll("/s", "");
        myApplication.setName(phone);
        if (!TextUtils.isEmpty(phone)) {
            //定义需要匹配的正则表达式的规则
            String REGEX_MOBILE_SIMPLE = "[1][358]\\d{9}";
            //把正则表达式的规则编译成模板
            Pattern pattern = Pattern.compile(REGEX_MOBILE_SIMPLE);
            //把需要匹配的字符给模板匹配，获得匹配器
            Matcher matcher = pattern.matcher(phone);
            // 通过匹配器查找是否有该字符，不可重复调用重复调用matcher.find()
            if (matcher.find()) {   //匹配手机号是否存在
                alterWarning();
            } else {
                toast("请输入正确的手机号");
            }
        } else {
            toast("请先输入手机号");
        }

    }


    //弹窗确认下发
    private void alterWarning() {
        // 2. 通过sdk发送短信验证
        AlertDialog.Builder builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("我们将要发送验证码到+86 " + phone + ""); //设置内容
        builder.setIcon(R.mipmap.notice);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss(); //关闭dialog
                // 2. 通过sdk发送短信验证（请求获取短信验证码，在监听（eh）中返回）
                SMSSDK.getVerificationCode(country, phone);
                //做倒计时操作
                tm = new Timer();
                tt = new TimerTask() {
                    @Override
                    public void run() {
                        hd.sendEmptyMessage(TIME--);
                    }
                };
                tm.schedule(tt, 0, 1000);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }

    protected void onDestroy() {
        super.onDestroy();
        // 注销回调接口registerEventHandler必须和unregisterEventHandler配套使用，否则可能造成内存泄漏。
        SMSSDK.unregisterEventHandler(eh);
        unregisterReceiver(networkChangeReceiver);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //得到网络连接管理器
            ConnectivityManager connectionManager = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            //通过管理器得到网络实例
            NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
            //判断是否连接
            if (networkInfo != null && networkInfo.isAvailable()) {

                Toast.makeText(context, "当前网络已连接!",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "当前网络已断开!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
