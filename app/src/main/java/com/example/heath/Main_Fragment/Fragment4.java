package com.example.heath.Main_Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.heath.AddAlarmActivity;
import com.example.heath.Check_air;
import com.example.heath.Check_o2;
import com.example.heath.Check_tep;
import com.example.heath.Check_tizhong;
import com.example.heath.Check_xinlv;
import com.example.heath.DataRecord;
import com.example.heath.MainActivity_med;
import com.example.heath.R;
import com.example.heath.ReportActivity;
import com.example.heath.SaveCard;
import com.example.heath.TimeLineActivity;
import com.example.heath.Week_Report;
import com.example.heath.Check_xueya;
import com.oragee.banners.BannerView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/8.
 */
public class Fragment4 extends Fragment implements View.OnClickListener, AMapLocationListener {
    //AMap是地图对象
    private static final int REFRESH_COMPLETE = 0X110;
    private SwipeRefreshLayout mSwipeLayout;
    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;
    //声明mListener对象，定位监听器
    private double lat;
    private double lon;


    private int[] imgs = {R.mipmap.ban1, R.mipmap.ban2, R.mipmap.ban3, R.mipmap.ban4};
    private BannerView bannerView;
    private TextView weather;
    private TextView report;
    private TextView his_data;
    private LinearLayout heath_news;
    private LinearLayout womah_news;
    private LinearLayout lvyou;
    private LinearLayout remrn;
    private LinearLayout doctor;
    private Fragment1 mTab01;
    private Fragment4 mTab04;
    private LinearLayout savecard;
    private LinearLayout check_xinlv;
    private LinearLayout check_xueya;
    private LinearLayout check_tizhong;
    private LinearLayout check_air;
    private LinearLayout check_o2;
    private LinearLayout check_tmp;
    private BannerView bannerView1;
    private TextView medicine;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment4, container, false);
        bannerView = view.findViewById(R.id.banner);
        initBanner();
        bannerView.setOnClickListener(this);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init_location();
        initView();
        initEvent();
    }


    private void initEvent() {
        weather.setOnClickListener(this);
        report.setOnClickListener(this);
        his_data.setOnClickListener(this);
        heath_news.setOnClickListener(this);
        womah_news.setOnClickListener(this);
        lvyou.setOnClickListener(this);
        remrn.setOnClickListener(this);
        doctor.setOnClickListener(this);
        savecard.setOnClickListener(this);
        check_xinlv.setOnClickListener(this);
        check_xueya.setOnClickListener(this);
        check_tizhong.setOnClickListener(this);
        check_air.setOnClickListener(this);
        check_o2.setOnClickListener(this);
        check_tmp.setOnClickListener(this);
        medicine.setOnClickListener(this);

    }

    private void initView() {
        medicine = getActivity().findViewById(R.id.medicine);
        report = getActivity().findViewById(R.id.report);
        his_data = getActivity().findViewById(R.id.his_data);
        weather = getActivity().findViewById(R.id.weather);
        heath_news = getActivity().findViewById(R.id.health_news);
        womah_news = getActivity().findViewById(R.id.woman_news);
        lvyou = getActivity().findViewById(R.id.lvyou);
        remrn = getActivity().findViewById(R.id.remen);
        doctor = getActivity().findViewById(R.id.image8);
        savecard = getActivity().findViewById(R.id.image7);
        check_xinlv = getActivity().findViewById(R.id.image3);
        check_xueya = getActivity().findViewById(R.id.image1);
        check_tizhong = getActivity().findViewById(R.id.image6);
        check_air = getActivity().findViewById(R.id.image5);
        check_o2 = getActivity().findViewById(R.id.image4);
        check_tmp = getActivity().findViewById(R.id.image2);
    }

    private void initBanner() {
        ArrayList<View> viewList = new ArrayList<View>();
        for (int i = 0; i < imgs.length; i++) {
            ImageView image = new ImageView(getActivity());
            image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //设置显示格式
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            image.setImageResource(imgs[i]);
            viewList.add(image);
        }
        bannerView.setViewList(viewList);
        bannerView.startLoop(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weather:
                Intent intent = new Intent(getActivity(), Week_Report.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", "http://m.weather.com.cn/d/town/index?lat=" + lat + "&" + "lon=" + lon);
                Log.e("url", "http://m.weather.com.cn/d/town/index?lat=" + lat + "&" + "lon=" + lon);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.banner:
                Log.e("点击banner","");
                Intent intent2 = new Intent(getActivity(), Week_Report.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("url", "http://www.pcauto.com.cn/playcar/zt/2007/czjk/");
                intent2.putExtras(bundle2);
                startActivity(intent2);
                break;
            case R.id.report:
                startActivity(new Intent(getActivity(), ReportActivity.class));
                break;
            case R.id.his_data:
                startActivity(new Intent(getActivity(), DataRecord.class));
                break;
            case R.id.health_news:
                Intent intent3 = new Intent(getActivity(), Week_Report.class);
                Bundle bundle3 = new Bundle();
                bundle3.putString("url", "http://wap.huanqiu.com/#channel=health");
                intent3.putExtras(bundle3);
                startActivity(intent3);
                break;
            case R.id.woman_news:
                Intent intent4 = new Intent(getActivity(), Week_Report.class);
                Bundle bundle4 = new Bundle();
                bundle4.putString("url", "http://wap.huanqiu.com/#channel=women");
                intent4.putExtras(bundle4);
                startActivity(intent4);
                break;
            //https://top.sina.cn/hot_news.d.html
            case R.id.remen:
                Intent intent5 = new Intent(getActivity(), Week_Report.class);
                Bundle bundle5 = new Bundle();
                bundle5.putString("url", "http://wap.huanqiu.com/#channel=");
                intent5.putExtras(bundle5);
                startActivity(intent5);
                break;
            //http://travel.sina.cn/#
            case R.id.lvyou:
                Intent intent6 = new Intent(getActivity(), Week_Report.class);
                Bundle bundle6 = new Bundle();
                bundle6.putString("url", "http://wap.huanqiu.com/#channel=travel");
                intent6.putExtras(bundle6);
                startActivity(intent6);
                break;
            case R.id.image8:
                Intent intent7 = new Intent(getActivity(), Week_Report.class);
                Bundle bundle7 = new Bundle();
                bundle7.putString("url", "https://m.haodf.com/");
                intent7.putExtras(bundle7);
                startActivity(intent7);
                break;
            case R.id.image7:
                Intent intent8 = new Intent(getActivity(), SaveCard.class);
                startActivity(intent8);
                break;
            case R.id.image3:
                Intent intent9 = new Intent(getActivity(), Check_xinlv.class);
                startActivity(intent9);
                break;
            case R.id.image1:
                Intent intent10 = new Intent(getActivity(), Check_xueya.class);
                startActivity(intent10);
                break;
            case R.id.image6:
                Intent intent11 = new Intent(getActivity(), Check_tizhong.class);
                startActivity(intent11);
                break;
            case R.id.image5:
                Intent intent12 = new Intent(getActivity(), Check_air.class);
                startActivity(intent12);
                break;
            case R.id.image4:
                Intent intent13 = new Intent(getActivity(), Check_o2.class);
                startActivity(intent13);
                break;
            case R.id.image2 :
                Intent intent14 = new Intent(getActivity(), Check_tep.class);
                startActivity(intent14);
                break;
            case R.id.medicine:
                Intent intent15 = new Intent(getActivity(), MainActivity_med.class);
                startActivity(intent15);
                break;
        }

    }



    // 定位服务

    private void init_location() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(1000 * 60 * 60);
        //给对定位客户端象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

    }

    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                aMapLocation.getCity();//城市信息
                lat = aMapLocation.getLatitude();//获取纬度
                Log.e("lat", lat + "");
                lon = aMapLocation.getLongitude();//获取经度
                Log.e("lon", lon + "");
                mLocationClient.stopLocation(); //停止定位
            } else {
                //显示错误信息ErrCode是错误码，详见错误码表。errInfo是错误信息，
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();//停止定位
        mLocationClient.onDestroy();//销毁定位客户端。
    }


}

