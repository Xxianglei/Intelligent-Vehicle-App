package com.example.heath;

/**
 * Created by 丽丽超可爱 on 2018/3/13.
 */

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.ArrayList;
import java.util.List;


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

    public boolean isTag1() {
        return tag1;
    }

    public void setTag1(boolean tag1) {
        this.tag1 = tag1;
    }

    public boolean isTag2() {
        return tag2;
    }

    public void setTag2(boolean tag2) {
        this.tag2 = tag2;
    }

    public boolean isTag3() {
        return tag3;
    }

    public void setTag3(boolean tag3) {
        this.tag3 = tag3;
    }

    private boolean tag1=false;
    private boolean tag2=false;
    private boolean tag3=false;
    public double getaDouble() {
        return aDouble;
    }

    public void setaDouble(double aDouble) {
        this.aDouble = aDouble;
    }

    public double getbDouble() {
        return bDouble;
    }

    public void setbDouble(double bDouble) {
        this.bDouble = bDouble;
    }

    private double aDouble;
    private double bDouble;

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

    private double time=0.0;
    /**打开的activity**/
    private List<Activity> activities = new ArrayList<Activity>();
    /**应用实例**/
    private static MyApplication instance;
    /**
     *  获得实例
     * @return
     */
    public static MyApplication getInstance(){
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        FlowManager.init(this);
        instance = this;
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

    public void addActivity(Activity activity){
        activities.add(activity);
    }
    /**
     *  结束指定的Activity
     * @param activity
     */
    public void finishActivity(Activity activity){
        if (activity!=null) {
            this.activities.remove(activity);
            activity.finish();
            activity = null;
        }
    }
    /**
     * 应用退出，结束所有的activity
     */
    public void exit(){
        for (Activity activity : activities) {
            if (activity!=null) {
                activity.finish();
            }
        }
        System.exit(0);
    }
    /**
     * 关闭Activity列表中的所有Activity*/
    public void finishActivity(){
        for (Activity activity : activities) {
            if (null != activity) {
                activity.finish();
            }
        }
        //杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}