package com.example.heath;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.heath.Bluteooth.BindBlutooh;
import com.example.heath.Datebase.CardModle;
import com.example.heath.Datebase.ConnectModle;
import com.example.heath.Datebase.DataBaseManager;
import com.example.heath.HttpUtils.OkNetRequest;
import com.example.heath.Main_Fragment.Fragment1;
import com.example.heath.Main_Fragment.Fragment2;
import com.example.heath.Main_Fragment.Fragment3;
import com.example.heath.Main_Fragment.Fragment4;
import com.example.heath.Model.Weather_model;

import com.example.heath.Speech.IatBasicActivity;
import com.example.heath.utils.HttpDownloader;
import com.example.heath.utils.JsonUtil;
import com.example.heath.utils.ParseNowWeatherUtil;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;


import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;
import okhttp3.Request;
import okhttp3.Response;

import static com.xiasuhuei321.loadingdialog.view.LoadingDialog.Speed.SPEED_TWO;

public class MainActivity extends IatBasicActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AMapLocationListener, View.OnLongClickListener, View.OnTouchListener {

    //AMap是地图对象
    private static final int REFRESH_COMPLETE = 0X110;
    private SwipeRefreshLayout mSwipeLayout;
    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;
    //声明mListener对象，定位监听器
    private LinearLayout doctor_tab;
    private LinearLayout jiance_tab;
    private LinearLayout mian;
    private LinearLayout guide;
    private double lat;
    private double lon;
    private double recLen = 0;
    private ImageView doctor;
    private ImageView jiance;
    private String down_url = "http://47.94.21.55/houtai/select.php?";
    private String down_url2 = "http://47.94.21.55/houtai/select.php?";


    private Fragment1 mTab01;
    private Fragment2 mTab02;
    private Fragment3 mTab03;
    private Fragment4 mTab04;


    private TextView now_weather;
    private TextView Your_city;
    private NavigationView navigationView;
    private String city = "jilin";
    private StringBuffer buffer;
    private Weather_model nowWeather;

    private SharedPreferences Sp;
    private static int CODE = 0x00;
    private TextView name;
    private MyApplication myApplication;
    private View headerView;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ImageView headimage;
    private LinearLayout linearLayout;

    private EditText mContent;
    private WebView webView;
    private ImageView guide_image;
    private TextView doctor_tv;
    private TextView check_tv;
    private TextView guide_tv;
    private ImageView main_img;
    private TextView mian_tv;
    private NetworkChangeReceiver networkChangeReceiver;
    private DrawerLayout mDrawerLayout;
    private String blu;
    private Timer timer;
    private BluetoothAdapter mBluetoothAdapter;
    private double a;
    private double b;
    private float speed;
    private String street;
    private String s;
    private float sp;
    private List<ConnectModle> con_persons;
    private boolean tag;
    private HashMap<String, String> params;
    private LoadingDialog ld;
    private boolean tag1;
    private DrawerLayout drawer;
    private Timer timer4;
    private String minlin;
    private boolean a1=false;
    private DataBaseManager dataBaseManager;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        MyApplication.getInstance().addActivity(this);
        ld = new LoadingDialog(MainActivity.this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ld.setLoadingText("正在拉取数据...")
                        .setSuccessText("拉取成功")//显示加载成功时的文字
                        .setFailedText("拉取失败")
                        .setLoadSpeed(SPEED_TWO)
                        .show();
            }
        });
        params = new HashMap<>();
        myApplication = (MyApplication) getApplication();
        params.put("user", "13166991256");
        tag1 = false;
        down_data(params);
        down_data2(params);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        /***
         * 优化统一权限申请
         */

        List<PermissionItem> permissions = new ArrayList<PermissionItem>();

        permissions.add(new PermissionItem(Manifest.permission.ACCESS_FINE_LOCATION,
                "定位",
                R.drawable.permission_ic_location));
        permissions.add(new PermissionItem(Manifest.permission.RECORD_AUDIO,
                "麦克风",
                R.drawable.permission_ic_micro_phone));
        permissions.add(new PermissionItem(Manifest.permission.CALL_PHONE,
                "电话",
                R.drawable.permission_ic_phone));
        HiPermission.create(MainActivity.this)
                .permissions(permissions)
                .style(R.style.PermissionDefaultGreenStyle)
                .checkMutiPermission(new PermissionCallback() {
                    @Override
                    public void onClose() {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onDeny(String permission, int position) {

                    }

                    @Override
                    public void onGuarantee(String permission, int position) {

                    }
                });


        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!mBluetoothAdapter.isEnabled()) //未打开蓝牙，才需要打开蓝牙
                {
                    // 提示用户开启蓝牙
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivity(intent);
                    //会以Dialog样式显示一个Activity
                }
            }
        }, 10000*30, 24 * 60 * 60 * 1000);
        // 注册蓝牙监听
        registerReceiver(blueStateBroadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        registerReceiver(blueStateBroadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED));
        //  注册网络状态监听
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        //动态注册
        registerReceiver(networkChangeReceiver, intentFilter);

        init_location();
        //Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        initView();
        initIatData(mContent);
        initEvent();
        setSelect(1);


    }

    private boolean XunFeigned() {

        if (minlin != null) {

            return true;
        }

        return false;
    }


    // 返回桌面不关闭程序
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.person) {
            new Runnable() {
                @Override
                public void run() {

                    startActivity(new Intent(MainActivity.this, PersonCenter.class));

                }
            }.run();


        } else if (id == R.id.dat) {
            new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this, DataRecord.class));

                }
            }.run();
            drawer.closeDrawer(GravityCompat.START);
            drawer.openDrawer(Gravity.LEFT);
        } else if (id == R.id.report) {
            new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this, ReportActivity.class));

                }
            }.run();
            drawer.closeDrawer(GravityCompat.START);
            drawer.openDrawer(Gravity.LEFT);
        } else if (id == R.id.doctor) {
            new Runnable() {
                @Override
                public void run() {

                    startActivity(new Intent(MainActivity.this, SaveCard.class));

                }
            }.run();
            drawer.closeDrawer(GravityCompat.START);
            drawer.openDrawer(Gravity.LEFT);//侧滑打开  不设置则不会默认打开
        } else if (id == R.id.blu) {
            new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this, BindBlutooh.class));

                }
            }.run();
            drawer.closeDrawer(GravityCompat.START);
            drawer.openDrawer(Gravity.LEFT);
        } else if (id == R.id.about) {
            new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                }
            }.run();
            drawer.closeDrawer(GravityCompat.START);
            drawer.openDrawer(Gravity.LEFT);
        } else if (id == R.id.log_out) {
            new Runnable() {
                @Override
                public void run() {
                    log_out();

                }
            }.run();
            drawer.closeDrawer(GravityCompat.START);
            drawer.openDrawer(Gravity.LEFT);

        }


        return true;
    }

    private void log_out() {
        //  注销
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setIcon(R.mipmap.delete);
        builder1.setTitle("提示");
        builder1.setMessage("您确定要退出登陆吗");
        builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  清除数据库  清除偏好设
                //  清除本地偏好设置
                Sp = getSharedPreferences("CITY", Context.MODE_PRIVATE);
                editor = Sp.edit();
                editor.clear();
                editor.commit();
                DataBaseManager dataBaseManager = new DataBaseManager();
                dataBaseManager.clearAll();
                MainActivity.this.finish();
            }


        });

        builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog1 = builder1.create();
        dialog1.show();


    }

    private void initEvent() {
        doctor_tab.setOnClickListener(this);
        jiance_tab.setOnClickListener(this);
        guide.setOnClickListener(this);
        mian.setOnLongClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        headimage.setOnClickListener(this);
        now_weather.setOnClickListener(this);
        mian.setOnClickListener(this);
        mian.setOnTouchListener(this);

    }

    private void initView() {
        // 设置昵称

        mContent = (EditText) findViewById(R.id.et_content);

        mian = (LinearLayout) findViewById(R.id.main);
        guide = (LinearLayout) findViewById(R.id.guide);
        doctor_tab = (LinearLayout) findViewById(R.id.doctor);
        jiance_tab = (LinearLayout) findViewById(R.id.jiance);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        doctor = (ImageView) findViewById(R.id.doctor_img);
        jiance = (ImageView) findViewById(R.id.jiance_img);
        main_img = (ImageView) findViewById(R.id.main_img);

        guide_image = (ImageView) findViewById(R.id.guide_img);

        doctor_tv = (TextView) findViewById(R.id.doctor_tv);
        check_tv = (TextView) findViewById(R.id.check_tv);
        guide_tv = (TextView) findViewById(R.id.guide_tv);
        mian_tv = (TextView) findViewById(R.id.mian_tv);

        dataBaseManager = new DataBaseManager();
        // 找到天气控件
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        now_weather = headerView.findViewById(R.id.now_weather);
        Your_city = headerView.findViewById(R.id.Your_city);
        name = headerView.findViewById(R.id.my_name);
        headimage = headerView.findViewById(R.id.head_image);

        SharedPreferences sharedPreferences2 = getSharedPreferences("P_NAME", Context.MODE_PRIVATE);
        SharedPreferences sharedPreferences3 = getSharedPreferences("headimage", Context.MODE_PRIVATE);


        if (sharedPreferences3.contains("image")) {

            String imageString = sharedPreferences3.getString("image", null);
            //第二步:利用Base64将字符串转换为ByteArrayInputStream

            byte[] byteArray = Base64.decode(imageString, Base64.DEFAULT);
            if (byteArray.length == 0) {

                headimage.setImageResource(R.mipmap.wodetoux);
            } else {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
                //第三步:利用ByteArrayInputStream生成Bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
                headimage.setImageBitmap(bitmap);
                Log.e("TEST", imageString);
            }
        } else {
            headimage.setImageResource(R.mipmap.wodetoux);
        }
        Log.i("TAG+++++++++++++", name.getText().toString());


    }

    public void onClick(View view) {
        resetImgs();

        switch (view.getId()) {
            case R.id.doctor:
                setSelect(0);    //医生
                doctor_tv.setTextColor(R.color.tv_color);
                break;
            case R.id.main:
                setSelect(1);  //首页
                a1=false;
                mian_tv.setTextColor(R.color.tv_color);
                break;
            case R.id.guide:
                setSelect(2);   //导航
                guide_tv.setTextColor(R.color.tv_color);
                break;
            case R.id.jiance:
                setSelect(4);     //检测
                check_tv.setTextColor(R.color.tv_color);
                break;
            case R.id.head_image:
                startActivity(new Intent(MainActivity.this, PersonCenter.class));
                break;
            case R.id.name:
                startActivity(new Intent(MainActivity.this, PersonCenter.class));
                break;
            case R.id.now_weather:

                Intent intent = new Intent(MainActivity.this, Week_Report.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", "http://m.weather.com.cn/d/town/index?lat=" + lat + "&" + "lon=" + lon);
                Log.e("url", "http://m.weather.com.cn/d/town/index?lat=" + lat + "&" + "lon=" + lon);
                intent.putExtras(bundle);
                startActivity(intent);

                break;


        }

    }


    /*
    重置图片
     */
    private void resetImgs() {
        doctor.setImageResource(R.mipmap.doctor_no);
        jiance.setImageResource(R.mipmap.check_no);
        guide_image.setImageResource(R.mipmap.guide_no);
        main_img.setImageResource(R.mipmap.mian_no);
        doctor_tv.setTextColor(R.color.font_color);
        check_tv.setTextColor(R.color.font_color);
        guide_tv.setTextColor(R.color.font_color);
        mian_tv.setTextColor(R.color.font_color);
    }

    /*
    点亮图片
     */
    private void setSelect(int i) {
        //把图片设置为亮
        //切换内容区域

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case 0:      //医生
                if (mTab01 == null) {
                    mTab01 = new Fragment1();
                    transaction.add(R.id.id_content, mTab01);
                } else {
                    transaction.show(mTab01);
                }
                doctor.setImageResource(R.mipmap.doctor_yes);
                break;
            case 1:       //首页

                if (mTab04 == null) {
                    mTab04 = new Fragment4();
                    transaction.add(R.id.id_content, mTab04);
                } else {
                    transaction.show(mTab04);
                }
                main_img.setImageResource(R.mipmap.mian_yes);
                break;
            case 2:   //导航

                if (mTab03 == null) {
                    mTab03 = new Fragment3();
                    transaction.add(R.id.id_content, mTab03);
                } else {
                    transaction.show(mTab03);
                }

                guide_image.setImageResource(R.mipmap.guide_yes);
                break;
            case 4:   //监测

                if (mTab02 == null) {
                    mTab02 = new Fragment2();
                    transaction.add(R.id.id_content, mTab02);
                } else {
                    transaction.show(mTab02);
                }
                jiance.setImageResource(R.mipmap.check_yes);
                break;
        }
        transaction.commit();

    }

    private void hideFragment(FragmentTransaction transaction) {
        if (mTab01 != null) {
            transaction.hide(mTab01);
        }
        if (mTab02 != null) {
            transaction.hide(mTab02);
        }
        if (mTab03 != null) {
            transaction.hide(mTab03);
        }
        if (mTab04 != null) {
            transaction.hide(mTab04);
        }


    }


    // 定位天气服务头
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                //   aMapLocation.getCountry();//国家信息
                // aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息
                lat = aMapLocation.getLatitude();//获取纬度
                //街道信息
                tag = true;
                street = aMapLocation.getStreet();
                speed = aMapLocation.getSpeed();
                if (tag) {
                    if (s != street || speed >= 5) {
                        //   车子开始移动    隔两秒刷新一次    传给全局变量
                        recLen = 0.0;
                        handler.postDelayed(runnable, 1000);

                        tag = false;
                    }
                }
                s = street;
                sp = speed;
                Log.e("lat", lat + "");

                lon = aMapLocation.getLongitude();//获取经度
                Log.e("lon", lon + "");

                buffer = new StringBuffer();
                buffer.append(aMapLocation.getCity() + "");
                city = buffer.toString();
                myApplication.setCity(buffer.toString());
                Log.e("定位", "xx" + buffer.toString());
                //设置城市 保存当前位置
                Your_city.setText(buffer.toString());
                new Thread(new now_WeatherThread(buffer.toString())).start(); // 实况
                SharedPreferences.Editor editor = Sp.edit();
                editor.putString("CITY", buffer.toString());

                editor.commit();
                Log.e("天气", "123456");
                mLocationClient.stopLocation(); //停止定位
            } else {
                //显示错误信息ErrCode是错误码，详见错误码表。errInfo是错误信息，
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());

            }
        }
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            recLen++;
            myApplication.setTime(recLen);
            handler.postDelayed(this, 1000);
        }
    };

    private void init_location() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(5000);
        //给对定位客户端象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }

    //activity 再次回到前台
    protected void onResume() {

        if ((myApplication.getPhone()) != null) {
            name.setText((myApplication.getPhone()).toString());
            sharedPreferences = MainActivity.this.getSharedPreferences("P_NAME", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("NAME", name.getText().toString());
            editor.commit();
        }
        if ((myApplication.getImage_String()) != null) {
            // 保存偏好头像
            String imageString = myApplication.getImage_String();
            SharedPreferences sharedPreferences = getSharedPreferences("headimage", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("image", imageString);
            editor.commit();
            Log.e("存入头像", imageString);
            //第二步:利用Base64将字符串转换为ByteArrayInputStream
            byte[] byteArray = Base64.decode(imageString, Base64.DEFAULT);
            if (byteArray.length == 0) {
                headimage.setImageResource(R.mipmap.wodetoux);
            } else {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
                //第三步:利用ByteArrayInputStream生成Bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
                headimage.setImageBitmap(bitmap);
            }
        }
        // 和oncreate方法冲突  在oncreate方法后会执行onstart和onresume
        /** else

         {
         headimage.setImageResource(R.mipmap.wodetoux);
         }*/
        super.onResume();

    }

    @Override
    protected void onPause() {
        ld.close();
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();//停止定位
        mLocationClient.onDestroy();//销毁定位客户端。
        //解除广播
        unregisterReceiver(networkChangeReceiver);
        unregisterReceiver(blueStateBroadcastReceiver);
        if (timer != null) {
            timer.cancel();
            // 一定设置为null，否则定时器不会被回收
            timer = null;
        }
        if (timer4 != null) {
            timer4.cancel();
            // 一定设置为null，否则定时器不会被回收
            timer4 = null;
        }

    }

    @Override
    public boolean onLongClick(View v) {
        clickMethod();
        a1 = true;
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                break;

            case MotionEvent.ACTION_UP:

                if (a1) {
                    if (myApplication.getMinlin() != null) {
                        Log.e("获取语音信息", myApplication.getMinlin().toString());
                        minlin = myApplication.getMinlin().toString();
                    }
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            if (XunFeigned()) {
                                Message message = new Message();
                                message.what = 0x17;
                                mHandler.sendMessage(message);
                            }

                        }
                    }).start();
                }
                a1=false;
                break;


        }
        return false;
    }


    private class now_WeatherThread implements Runnable {

        private String city = "jilin";

        public now_WeatherThread(String city) {
            this.city = city;
        }

        public void run() {
            try {
                Log.e("获取天气", "xx" + buffer.toString());
                city = URLEncoder.encode(city, "UTF-8");
                Log.e("转码", city);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();

            }
            String address = "https://free-api.heweather.com/v5/now?city=" + city + "&key=f75021d48c674f89b3928c2524644ac8";
            HttpDownloader httpDownloader = new HttpDownloader();
            String jsonString = httpDownloader.download(address);
            //打印weather info
            Log.e("cn", jsonString);
            Message message = Message.obtain();
            message.obj = jsonString;
            message.what = 0x12;
            mHandler.sendMessage(message);

        }

    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x12) {   //实况
                try {
                    String jsonString = (String) msg.obj;
                    ParseNowWeatherUtil parseNowWeatherUtil = new ParseNowWeatherUtil();
                    nowWeather = parseNowWeatherUtil.getInfomation(jsonString);
                    Log.e("天气", nowWeather.getTemperature());
                    now_weather.setText(nowWeather.getTemperature() + "℃");

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            if (msg.what == 0x17) {

                Log.e("back", minlin);
                if (minlin.equals("医院") || minlin.equals("寻找医院") || minlin.equals("去医院") || minlin.equals("我要去医院") || minlin.equals("立即上医院") || minlin.equals("导航") || minlin.equals("紧急导航")) {
                    // 切换导航界面
                    resetImgs();
                    setSelect(2);         //导航
                    guide_tv.setTextColor(R.color.tv_color);

                }
                if (minlin.equals("电话") || minlin.equals("拨打电话") || minlin.equals("立即拨打电话") || minlin.equals("打电话") || minlin.equals("我要打电话") || minlin.equals("求救") || minlin.equals("拨打紧急联系人")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        DataBaseManager dataBaseManager = new DataBaseManager();
                        con_persons = dataBaseManager.readconnList();
                        if (con_persons.size() > 0) {
                            // 6.0以上权限申请
                            intentToCall(con_persons.get(con_persons.size() - 1).getPhone().toString());
                            Log.e("拨打电话", con_persons.get(con_persons.size() - 1).getPhone().toString());
                            Toast.makeText(MainActivity.this,"拨打电话"+con_persons.get(con_persons.size() - 1).getPhone().toString(),Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(MainActivity.this, "您还没有添加联系人", Toast.LENGTH_SHORT).show();

                    } else {
                        if (con_persons.size() > 0) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + con_persons.get(con_persons.size() - 1).getPhone().toString()));
                            startActivity(intent);
                        } else
                            Toast.makeText(MainActivity.this, "您还没有添加联系人", Toast.LENGTH_SHORT).show();
                    }

                }
            }
            if (msg.what == 0x18) {

            }

        }
    };

    //定位天气服务尾



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

            } else {
                Toast.makeText(context, "当前网络已断开!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 直接拨打电话
     */

    public boolean intentToCall(final String phoneNumber) {
        if (phoneNumber.isEmpty()) {
            Toast.makeText(MainActivity.this, "拨打失败", Toast.LENGTH_SHORT).show();
            return false;
        }
        //6.0权限处理
        Acp.getInstance(this).request(new AcpOptions.Builder().setPermissions(
                Manifest.permission.CALL_PHONE).build(), new AcpListener() {
            @Override
            public void onGranted() {
                Uri u = Uri.parse("tel:" + phoneNumber);
                Intent it = new Intent(Intent.ACTION_CALL, u);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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

    BroadcastReceiver blueStateBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
            switch (blueState) {
                case BluetoothAdapter.STATE_OFF:
                    Log.i("TAG", "blueState: STATE_OFF");
                    blu = "您的蓝牙已经关闭!";
                    Toast.makeText(MainActivity.this, blu, Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    Log.i("TAG", "blueState: STATE_TURNING_ON");
                    blu = "您的蓝牙正在打开!";

                    break;
                case BluetoothAdapter.STATE_ON:
                    Log.i("TAG", "blueState: STATE_ON");
                    blu = "您的蓝牙已经打开!";
                    Toast.makeText(MainActivity.this, blu, Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Log.i("TAG", "blueState: STATE_TURNING_OFF");
                    blu = "您的蓝牙正在关闭!";

                    break;
                default:
                    break;
            }
            int blueconState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, 0);
            switch (blueconState) {
                case BluetoothAdapter.STATE_CONNECTED:
                    Log.i("TAG", "STATE_CONNECTED");
                    blu = "您的蓝牙已经连接!";

                    break;
                case BluetoothAdapter.STATE_CONNECTING:
                    Log.i("TAG", "STATE_CONNECTING");
                    blu = "您的蓝牙正在开启连接!";

                    break;
                case BluetoothAdapter.STATE_DISCONNECTED:
                    Log.i("TAG", "STATE_DISCONNECTED");
                    blu = "您的蓝牙已经断开!";

                    break;
                case BluetoothAdapter.STATE_DISCONNECTING:
                    Log.i("TAG", "STATE_DISCONNECTING");
                    blu = "您的蓝牙正在断开连接!";
                    break;
                default:
                    break;
            }
        }
    };

    private void down_data(HashMap<String, String> params) {
        down_url = down_url + "biao=xinxi";

        OkNetRequest.postFormRequest(down_url, params, new OkNetRequest.DataCallBack() {
            @Override
            public void requestSuccess(Response response, String result) throws Exception {

                // 请求成功的回调
                Log.e("下载同步成功", result.toString());
                String s=result.trim();

                JSONObject jsonObject = new JSONObject(JsonUtil.JSONTokener(s));
                JSONObject data = jsonObject.getJSONObject("data");


                Log.e("OK咯", data.getString("name").toString());

                name.setText(data.getString("name")+"");
                dataBaseManager.saveCard(data.getString("name"), data.getString("shengao"), data.getString("tizhong"), data.getString("bingshi"), data.getString("bron"), data.getString("xuexing"), data.getString("guoming"), data.getString("xiguan"));

                //存入数据库
                ld.loadSuccess();
            }

            @Override
            public void progressSuccess(Response response) throws Exception {

            }

            @Override
            public void requestFailure(Request request, IOException e) {
                ld.loadFailed();

                Log.e("下载同步失败", request.body().toString());
            }


        });

    }

    private void down_data2(HashMap<String, String> params) {
        down_url2 = down_url2 + "biao=user";
        final DataBaseManager dataBaseManager = new DataBaseManager();

        OkNetRequest.postFormRequest(down_url2, params, new OkNetRequest.DataCallBack() {
            @Override
            public void requestSuccess(Response response, String result) throws Exception {
                // 请求成功的回调

                Log.e("下载同步成功", result.toString());
                String s=result.trim();

                JSONObject jsonObject = new JSONObject(JsonUtil.JSONTokener(s));
                jsonObject.length();
                Log.d("data",  jsonObject.length()+"");
                JSONObject data = jsonObject.getJSONObject("data");


                //存入数据库

                Log.e("OK咯", data.getString("userName") + data.getString("sex") + Integer.parseInt(data.getString("age")) + Integer.parseInt(data.getString("height")) + Integer.parseInt(data.getString("weight")));
                dataBaseManager.saveUser(data.getString("userName"), data.getString("sex"), Integer.parseInt(data.getString("age")), Integer.parseInt(data.getString("height")), Integer.parseInt(data.getString("weight")));
                ld.loadSuccess();

            }

            @Override
            public void progressSuccess(Response response) throws Exception {

            }

            @Override
            public void requestFailure(Request request, IOException e) {
                // 请求失败的回调

                ld.loadFailed();

                Log.e("下载同步失败", request.body().toString());

            }
        });

    }

    class user {
        private String code;
        private String message;
        private Data1 data1;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }


        public Data1 getData1() {
            return data1;
        }

        public void setData1(Data1 data1) {
            this.data1 = data1;
        }
    }


    class Data1 {
        private String userName;
        private String sex;
        private String height;
        private String weight;
        private String age;
        private String img;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        private String id;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }


}
