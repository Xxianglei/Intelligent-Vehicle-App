package com.example.heath.Datebase;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by 丽丽超可爱 on 2018/5/25.
 */

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public final class AppDatabase {
    //数据库名称
    public static final String NAME = "HealthDatabase";
    //数据库版本号
    public static final int VERSION = 8;

}