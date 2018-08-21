package com.example.heath.Datebase;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by 丽丽超可爱 on 2018/5/25.
 */

@Table(database = AppDatabase.class)
public class UserModle extends BaseModel{
    //DBFlow会根据你的类名自动生成一个表明
    //这个类对应的表名为：XueyaModle_Table
    //自增ID
    @Column
    @PrimaryKey(autoincrement = true)
    public Long id=(long)0;
    @Column
    public String  name;
    @Column
    public String xingbie;
    @Column
    public int  age;
    @Column
    public int  high;
    @Column
    public int  weight;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getXingbie() {
        return xingbie;
    }

    public void setXingbie(String xingbie) {
        this.xingbie = xingbie;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
