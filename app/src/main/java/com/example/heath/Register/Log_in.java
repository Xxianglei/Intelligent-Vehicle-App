package com.example.heath.Register;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heath.HttpUtils.OkNetRequest;
import com.example.heath.MainActivity;
import com.example.heath.Model.Code;
import com.example.heath.MyApplication;
import com.example.heath.R;
import com.google.gson.Gson;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Request;
import okhttp3.Response;

import static com.xiasuhuei321.loadingdialog.view.LoadingDialog.Speed.SPEED_TWO;

public class Log_in extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btGo;
    private TextView forgot_key;
    private FloatingActionButton fab;
    private String name;
    private String password;
    private Code code;
    private SharedPreferences.Editor edit;
    private NetworkChangeReceiver networkChangeReceiver;
    private MyApplication myApplication;
    private LoadingDialog ld;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        initView();
        MyApplication.getInstance().addActivity(this);
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(ld!=null)
            ld=null;
        unregisterReceiver(networkChangeReceiver);
    }

    private void initView() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        //动态注册
        registerReceiver(networkChangeReceiver, intentFilter);
        SharedPreferences sharedPreferences = getSharedPreferences("log_tag", MODE_PRIVATE);
        forgot_key = (TextView) findViewById(R.id.forgot_key);  // 忘记密码
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        btGo = (Button) findViewById(R.id.bt_go);     //登录
        btGo.setClickable(false);
        CardView cv = (CardView) findViewById(R.id.cv);
        fab = (FloatingActionButton) findViewById(R.id.fab);  // 注册
        code = new Code();
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkbox);
        myApplication = (MyApplication) getApplication();
    }

    private void setListener() {

        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 登录
                name = etUsername.getText().toString();
                password = etPassword.getText().toString();
                ld = new LoadingDialog(Log_in.this);
                if (name.length()>0 && password.length()>0) {

                    myApplication.setName(name);
                    ld.setLoadingText("正在登录中...")
                            .setSuccessText("登录成功")//显示加载成功时的文字
                            .setFailedText("登录失败")
                            .setLoadSpeed(SPEED_TWO)
                            .show();
                    GLog_in(name, password);
                } else {
                    ld.loadFailed();
                    Toast.makeText(Log_in.this, "账号或密码为空", Toast.LENGTH_SHORT).show();
                }


            }
        });
        // 注册
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("TAG", 1);
                Intent intent = new Intent(Log_in.this, RegisterActivity.class);
                intent.putExtras(bundle);
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Log_in.this, fab, fab.getTransitionName());
                startActivity(intent, options.toBundle());
            }
        });
        // 忘记密码
        forgot_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("TAG", 1);
                Intent intent_forgot_key = new Intent(Log_in.this, RegisterActivity.class);
                intent_forgot_key.putExtras(bundle);
                startActivity(intent_forgot_key);
            }
        });

    }

    @Override
    protected void onPause() {
        ld.close();
        super.onPause();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    private void GLog_in(String name, final String password) {

        HashMap<String, String> params = new HashMap<>();
        // 添加请求参数
        params.put("phone", name);
        params.put("password", password);

        String url = "http://47.94.21.55/houtai/login.php";
        OkNetRequest.postFormRequest(url, params, new OkNetRequest.DataCallBack() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void requestSuccess(Response response, String result) throws Exception {


                if (response.body().contentLength() != 0) {
                    // 请求成功的回调
                    Log.e("login", result.toString());
                    Gson gson = new Gson();
                    String deal_result = result.toString();
                    deal_result = deal_result.replace("连接成功", "");
                    code = gson.fromJson(deal_result, Code.class);
                    int a = Integer.parseInt(code.getCode());
                    if (a == 201) {

                        startActivity(new Intent(Log_in.this, MainActivity.class));
                        Log_in.this.finish();

                    } else {
                        ld.loadFailed();
                        Toast.makeText(Log_in.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void progressSuccess(Response response) throws Exception {

            }

            @Override
            public void requestFailure(Request request, IOException e) {
                // 请求失败的回调
                ld.loadFailed();
                Toast.makeText(Log_in.this, "登录失败", Toast.LENGTH_SHORT).show();
                Log.e("login--------------", request.body().toString());
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        fab.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.setVisibility(View.VISIBLE);
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
                btGo.setClickable(true);
            } else {
                btGo.setClickable(false);
                Toast.makeText(context, "当前网络已断开!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
