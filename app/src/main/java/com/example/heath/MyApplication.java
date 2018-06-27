package com.example.heath;

/**
 * Created by 丽丽超可爱 on 2018/3/13.
 */

import android.app.Application;
import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowManager;


/**
 * Created by As on 2017/8/7.
 */

public class MyApplication extends Application {

    private static Context context;
    private String name;
    private String city;
    private String phone;
    private String image_String;
    private String blu="";

    public String getMinlin() {
        return minlin;
    }

    public void setMinlin(String minlin) {
        this.minlin = minlin;
    }

    private String minlin;

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    private double time;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        FlowManager.init(this);

    }

    public static Context getContext() {
        return context;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImage_String() {
        return image_String;
    }

    public void setImage_String(String image_String) {
        this.image_String = image_String;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}