package com.example.heath.Main_Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.example.heath.R;

import com.example.heath.Guide.RouteNaviActivity;
import com.example.heath.Guide.overlay.PoiOverlay;
import com.example.heath.Guide.util.Utils;

/**
 * Created by Administrator on 2017/8/8.
 */
public class Fragment3 extends Fragment implements AMapLocationListener, AMap.InfoWindowAdapter, AMap.OnInfoWindowClickListener, PoiSearch.OnPoiSearchListener, AMap.OnMarkerClickListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private AMap mMap;
    private PoiSearch mPoiSearch;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private Marker mLocationMarker;
    private Circle mLocationCircle;
    private PoiOverlay mPoiOverlay;
    private AMapLocation mCurrentLocation;
    private String hospital = "医院";
    private EditText search;
    private ImageButton search_img;
    private ImageButton lukuang;
    private ImageButton heibai;
    private boolean tag1;
    private boolean tag2;
    private double x;
    private double y;
    private ImageButton location;
    private Spinner spinner;
    private  int r=2000;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3, container, false);
        setUpMapIfNeeded();
        initView(view);
        initLocation();
        return view;
    }

    private void initView(View view) {
        location = view.findViewById(R.id.location);
        search = view.findViewById(R.id.search_tv);
        search_img = view.findViewById(R.id.search_img);
        lukuang = view.findViewById(R.id.lukuang);
        heibai = view.findViewById(R.id.heibai);
        spinner = view.findViewById(R.id.spinner);
        search_img.setOnClickListener(this);
        lukuang.setOnClickListener(this);
        heibai.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);
        location.setOnClickListener(this);

        tag1 = true;
        tag2 = true;
    }

    @Override
    public void onClick(View v) {
        mLocationClient.startLocation();
        switch (v.getId()) {
            case R.id.search_img:
                hospital = search.getText().toString();
                if (search.getText()!= null) {
                    PoiSearch.Query poiQuery = new PoiSearch.Query("", hospital);
                    LatLonPoint centerPoint = new LatLonPoint(x, y);
                    PoiSearch.SearchBound searchBound = new PoiSearch.SearchBound(centerPoint, r);
                    mPoiSearch = new PoiSearch(getActivity().getApplicationContext(), poiQuery);
                    mPoiSearch.setBound(searchBound);
                    mPoiSearch.setOnPoiSearchListener(this);
                    mPoiSearch.searchPOIAsyn();
                } else {
                    Toast.makeText(getActivity(), "请输入正确的目的地", Toast.LENGTH_SHORT).show();
                }


                //mMap.reloadMap();
                break;
            case R.id.heibai:
                if (tag1) {
                    //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
                    mMap.setMapType(AMap.MAP_TYPE_NIGHT);
                    heibai.setImageResource(R.mipmap.moon);
                    tag1 = false;
                } else {
                    mMap.setMapType(AMap.MAP_TYPE_NORMAL);
                    heibai.setImageResource(R.mipmap.sun);
                    tag1 = true;
                }

                break;
            case R.id.lukuang:
                if (tag2) {
                    mMap.setTrafficEnabled(true);    // 显示实时交通状况
                    tag2 = false;
                } else {
                    mMap.setTrafficEnabled(false);    // 显示实时交通状况
                    tag2 = true;
                }

                break;
            case R.id.location:
                mLocationClient.startLocation();
                Toast.makeText(getActivity(),"定位更新",Toast.LENGTH_SHORT).show();
                break;


        }

    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyLocation();
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            mMap.setMyLocationEnabled(false);
            mMap.setOnMarkerClickListener(this);
            mMap.setOnInfoWindowClickListener(this);
            mMap.setInfoWindowAdapter(this);
        }
    }

    /**
     * 进行poi搜索
     *
     * @param lat
     * @param lon
     */
    private void initPoiSearch(double lat, double lon) {
        if (mPoiSearch == null) {
            PoiSearch.Query poiQuery = new PoiSearch.Query("", hospital);
            Log.e("hs", hospital);
            LatLonPoint centerPoint = new LatLonPoint(lat, lon);
            PoiSearch.SearchBound searchBound = new PoiSearch.SearchBound(centerPoint, r);
            mPoiSearch = new PoiSearch(getActivity().getApplicationContext(), poiQuery);
            mPoiSearch.setBound(searchBound);
            mPoiSearch.setOnPoiSearchListener(this);
            mPoiSearch.searchPOIAsyn();
        }
    }


    private void destroyLocation() {
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(this);
            mLocationClient.onDestroy();
        }
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(true);
        mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
        mLocationClient.setLocationListener(this);
        mLocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        if (aMapLocation == null || aMapLocation.getErrorCode() != AMapLocation.LOCATION_SUCCESS) {
            Toast.makeText(getActivity(), aMapLocation.getErrorInfo() + "  " + aMapLocation.getErrorCode(), Toast.LENGTH_LONG).show();
            return;
        }
        mCurrentLocation = aMapLocation;
        LatLng curLatLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
        if (mLocationMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(curLatLng);
            markerOptions.anchor(0.5f, 0.5f);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.navi_map_gps_locked));
            mLocationMarker = mMap.addMarker(markerOptions);
        }
        if (mLocationCircle == null) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(curLatLng);
            circleOptions.radius(aMapLocation.getAccuracy());
            circleOptions.strokeWidth(2);
            circleOptions.strokeColor(getResources().getColor(R.color.stroke));
            circleOptions.fillColor(getResources().getColor(R.color.fill));
            mLocationCircle = mMap.addCircle(circleOptions);
        }
        x = aMapLocation.getLatitude();
        y = aMapLocation.getLongitude();
        initPoiSearch(aMapLocation.getLatitude(), aMapLocation.getLongitude());
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (i != AMapException.CODE_AMAP_SUCCESS || poiResult == null) {
            return;
        }
        if (mPoiOverlay != null) {
            mPoiOverlay.removeFromMap();
        }
        mPoiOverlay = new PoiOverlay(mMap, poiResult.getPois());
        mPoiOverlay.addToMap();
        mPoiOverlay.zoomToSpan();
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (mLocationMarker == marker) {
            return false;
        }

        return false;
    }

    /**
     * 自定义marker点击弹窗内容
     *
     * @param marker
     * @return
     */
    @Override
    public View getInfoWindow(final Marker marker) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.poikeyword,
                null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(marker.getTitle());
        TextView snippet = (TextView) view.findViewById(R.id.snippet);
        int index = mPoiOverlay.getPoiIndex(marker);
        float distance = mPoiOverlay.getDistance(index);
        String showDistance = Utils.getFriendlyDistance((int) distance);
        snippet.setText("距当前位置" + showDistance);
        ImageButton button = (ImageButton) view
                .findViewById(R.id.start_amap_app);
        // 调起导航
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAMapNavi(marker);
            }
        });
        return view;
    }

    /**
     * 点击一键导航按钮跳转到导航页面
     *
     * @param marker
     */
    private void startAMapNavi(Marker marker) {
        if (mCurrentLocation == null) {
            return;
        }
        Intent intent = new Intent(getActivity(), RouteNaviActivity.class);
        intent.putExtra("gps", false);
        intent.putExtra("start", new NaviLatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
        intent.putExtra("end", new NaviLatLng(marker.getPosition().latitude, marker.getPosition().longitude));
        startActivity(intent);
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                r=2000;
                break;
            case 1:
                r=2500;
                break;
            case 2:
                r=3000;
                break;
            case 3:
                r=3500;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
     Toast.makeText(getActivity(),"您未选择搜索半径",Toast.LENGTH_SHORT).show();
    }
}