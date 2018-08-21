package com.example.heath.Datebase;

import android.content.Context;
import android.util.Log;

import com.example.heath.MyApplication;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * Created by 丽丽超可爱 on 2018/5/25.
 */

public class DataBaseManager {

    /**
     * 
     */
    private TiwenModle tiwenModle;
    private XinlvModle xinlvModle;
    private  XueyaModle xueyaModle;
    private  XueyangModle xueyangModle;
    private  TizhongModle tizhong;
    private  UserModle userModle;
    private  CardModle cardModle;
    private  AirModle airModle;
    private ConnectModle connectModle;


    public DataBaseManager() {
        Context mContext = MyApplication.getContext();
        tizhong = new TizhongModle();
        tiwenModle = new TiwenModle();
        xinlvModle = new XinlvModle();
        xueyaModle = new XueyaModle();
        xueyangModle = new XueyangModle();
        userModle = new UserModle();
        cardModle = new CardModle();
        airModle = new AirModle();
        connectModle = new ConnectModle();
    }

    public void saveSingle(String date1, String date2, String date3, String date4, String date5, int data1, int highdata, int lowdata, int data3, float data4, int data5) {
        xueyangModle.setDate(date1);
        xueyangModle.setData(data1);

        xueyaModle.setDate(date2);
        xueyaModle.setHigh_data(highdata);
        xueyaModle.setLow_data(lowdata);

        xinlvModle.setDate(date3);
        xinlvModle.setData(data3);

        tiwenModle.setDate(date4);
        tiwenModle.setData(data4);

        tizhong.setDate(date5);
        tizhong.setData(data5);
        if (data1 != 0)
            xueyangModle.save();
        if (highdata != 0 && lowdata != 0)
            xueyaModle.save();
        if (data3 != 0)
            xinlvModle.save();
        if (data4 != 0)
            tiwenModle.save();
        if (data5 != 0)
            tizhong.save();
        if (tizhong.save())
            Log.e("data5", data5 + "");

    }

    public void saveUser(String name, String xingbie, int age, int high, int weight) {
        userModle.setName(name);
        userModle.setXingbie(xingbie);
        userModle.setAge(age);
        userModle.setHigh(high);
        userModle.setWeight(weight);
        if (weight != 0)
            userModle.save();
    }

    public void saveCard(String name, String high, String weight, String his, String birth, String blood, String react, String hobbit) {
        cardModle.setName(name);
        cardModle.setHigh(high);
        cardModle.setWeight(weight);
        cardModle.setHis(his);
        cardModle.setBirth(birth);
        cardModle.setBlood(blood);
        cardModle.setReact(react);
        cardModle.setHobbit(hobbit);
        cardModle.save();

    }

    public void saveAir(String date, int data) {
        airModle.setDate(date);
        airModle.setData(data);
        airModle.save();
    }

    public void saveConn(String name, String phone) {
        connectModle.setName(name);
        connectModle.setPhone(phone);

        if (connectModle.save())
            Log.e("********", name + phone);
        else Log.e("********", "------");
    }

    public TizhongModle readtzSingle() {
        TizhongModle tizhongModle = SQLite.select().
                from(TizhongModle.class).querySingle();
        return tizhongModle;
    }

    /**
     * 保存多条
     */
    public void saveList(List<TizhongModle> SourceList) {
        FlowManager.getDatabase(AppDatabase.class)
                .getTransactionManager()
                .getSaveQueue()
                .addAll2(SourceList);
        //还需要执行下面操作
        FlowManager.getDatabase(AppDatabase.class).getTransactionManager().getSaveQueue().purgeQueue();
    }

    /**
     * 读取多条体重
     */
    public List readtzList() {
        List<TizhongModle> contentBeanList = SQLite.select().
                from(TizhongModle.class).queryList();
        Log.e("读取多条体重",contentBeanList.size()+"");
        return contentBeanList;
    }

    public List readconnList() {
        List<ConnectModle> contentBeanList = SQLite.select().
                from(ConnectModle.class).queryList();
        return contentBeanList;
    }

    public List readcardList() {
        List<CardModle> contentBeanList = SQLite.select().
                from(CardModle.class).queryList();
        return contentBeanList;
    }

    public List readuserList() {
        List<UserModle> contentBeanList = SQLite.select().
                from(UserModle.class).queryList();
        return contentBeanList;
    }

    public List readairList() {
        List<AirModle> contentBeanList = SQLite.select().
                from(AirModle.class).queryList();
        return contentBeanList;
    }

    /**
     * 读取多条心率
     */
    public List readxlList() {
        List<XinlvModle> contentBeanList = SQLite.select().
                from(XinlvModle.class).queryList();
        return contentBeanList;
    }

    public List readxyangList() {
        List<XueyangModle> contentBeanList = SQLite.select().
                from(XueyangModle.class).queryList();
        return contentBeanList;
    }

    public List readxyList() {
        List<XueyaModle> contentBeanList = SQLite.select().
                from(XueyaModle.class).queryList();
        return contentBeanList;
    }

    public List readtwList() {
        List<TiwenModle> contentBeanList = SQLite.select().
                from(TiwenModle.class).queryList();
        return contentBeanList;
    }

    public boolean deletConnSingle(String name) {
        Log.e("jjj",name.toString());
        connectModle = SQLite.select()
                .from(ConnectModle.class)
                .where(ConnectModle_Table.name.eq(name))
                .querySingle();
        if ( connectModle!=null){
            connectModle.delete();
            return true;
        }
        else
            return false;

    }
    public void clearAll(){
        new Delete().from(TiwenModle.class).execute();
        new Delete().from(XinlvModle.class).execute();
        new Delete().from(XueyaModle.class).execute();
        new Delete().from(XueyangModle.class).execute();
        new Delete().from(TizhongModle.class).execute();
        new Delete().from(UserModle.class).execute();
        new Delete().from(CardModle.class).execute();
        new Delete().from(AirModle.class).execute();
        new Delete().from(ConnectModle.class).execute();
    }
}
