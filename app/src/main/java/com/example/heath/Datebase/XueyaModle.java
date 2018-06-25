package com.example.heath.Datebase;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by 丽丽超可爱 on 2018/5/25.
 */

@Table(database = AppDatabase.class)
public class XueyaModle extends BaseModel{
    //DBFlow会根据你的类名自动生成一个表明
    //这个类对应的表名为：XueyaModle_Table
    //自增ID
    @Column
    @PrimaryKey(autoincrement = true)
    public Long id=(long)0;
    @Column
    public String date;
    @Column
    public int high_data;
    @Column
    public int low_data;
    @Column
    public int score;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getHigh_data() {
        return high_data;
    }

    public void setHigh_data(int high_data) {
        this.high_data = high_data;
    }

    public int getLow_data() {
        return low_data;
    }

    public void setLow_data(int low_data) {
        this.low_data = low_data;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
