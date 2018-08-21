package com.example.heath.Datebase;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by 丽丽超可爱 on 2018/5/25.
 */

@Table(database = AppDatabase.class)
public class CardModle extends BaseModel{
    //DBFlow会根据你的类名自动生成一个表明
    //这个类对应的表名为：XueyaModle_Table
    //自增ID
    @Column
    @PrimaryKey(autoincrement = true)
    public Long id=(long)0;
    @Column
    public String name;
    @Column
    public String high;
    @Column
    public String weight;
    @Column
    public String his;
    @Column
    public String birth;
    @Column
    public String blood;
    @Column
    public String react;
    @Column
    public String hobbit;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHis() {
        return his;
    }

    public void setHis(String his) {
        this.his = his;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getReact() {
        return react;
    }

    public void setReact(String react) {
        this.react = react;
    }

    public String getHobbit() {
        return hobbit;
    }

    public void setHobbit(String hobbit) {
        this.hobbit = hobbit;
    }
}
