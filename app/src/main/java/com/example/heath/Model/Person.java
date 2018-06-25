package com.example.heath.Model;

/**
 * Created by 丽丽超可爱 on 2018/3/30.
 */

public class Person {
    // 个人中心数据
    private String name;
    private String sex;
    private int age;
    private int high;
    private int weight;
    private String image;

    // 需要分析的数据
    private int xinlv;
    private float tizhi;
    private float xuezhi;
    private int xueya_high;
    private int xueya_low;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHigh() {
        return high;
    }

    public void setHigh(int high) {
        this.high = high;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }




    public int getXinlv() {
        return xinlv;
    }

    public void setXinlv(int xinlv) {
        this.xinlv = xinlv;
    }

    public float getTizhi() {
        return tizhi;
    }

    public void setTizhi(float tizhi) {
        this.tizhi = tizhi;
    }

    public float getXuezhi() {
        return xuezhi;
    }

    public void setXuezhi(float xuezhi) {
        this.xuezhi = xuezhi;
    }

    public int getXueya_high() {
        return xueya_high;
    }

    public void setXueya_high(int xueya_high) {
        this.xueya_high = xueya_high;
    }

    public int getXueya_low() {
        return xueya_low;
    }

    public void setXueya_low(int xueya_low) {
        this.xueya_low = xueya_low;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", high=" + high +
                ", weight=" + weight +
                ", image='" + image + '\'' +
                ", xinlv=" + xinlv +
                ", tizhi=" + tizhi +
                ", xuezhi=" + xuezhi +
                ", xueya_high=" + xueya_high +
                ", xueya_low=" + xueya_low +
                '}';
    }
}
