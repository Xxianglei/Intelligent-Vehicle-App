package com.example.heath.service;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.heath.MyApplication;

import java.text.NumberFormat;

/**
 * Created by Administrator on 2018/6/9.
 */

public class LisenService extends Service {

    private UpdateThread updateThread;
    private MyApplication myApplication;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e("监听服务", "服务关闭");
        updateThread.interrupt();
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        Log.e("监听服务", "服务开启");
        updateThread = new UpdateThread();
        updateThread.start();
        super.onCreate();
        myApplication = (MyApplication)getApplication();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class UpdateThread extends Thread {

        private int xueya = 0;
        private int xinlv = 0;
        private float tiwen;
        private Intent intent;
        private int ddd=0;

        @Override
        public void run() {
            super.run();

            while (!isInterrupted())

            {
                try {

                    Thread.sleep(5000 * 2);
                    intent = new Intent();
                    xueya = (int) (75 + Math.random() * 45);
                    xinlv = (int) (50 + Math.random() * 50);
                    tiwen = (float) (35.1 + Math.random() * 3.1);
                    int i = (int) (tiwen * 10);
                    // 转回float类型,然后将乘上的数重新除去。
                    tiwen = (float) i / 10;
                    intent.setAction("com.example.heath.service.LisenService");  //用隐式意图来启动广播
                    intent.putExtra("xueya", xueya);
                    intent.putExtra("xinlv", xinlv);
                    intent.putExtra("tiwen", tiwen);
                    if (myApplication.getTime()== 8 * 1000 * 60 * 60)
                    intent.putExtra("time",myApplication.getTime());
                    if (myApplication.getTime()==16 * 1000 * 60 * 60)
                        intent.putExtra("time",myApplication.getTime());

                    Log.e("xxxxxxx", tiwen + "");
                    if (xinlv < 60 && (tiwen>38||tiwen < 36) && (xueya < 80 || xueya > 115)) {
                        ddd++;
                        if (ddd>=5)
                        intent.putExtra("warning",0);
                        ddd=0;
                    }
                    else intent.putExtra("warning",2);
                    sendBroadcast(intent);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    break;                          //捕获到异常之后，执行break跳出循环。
                }


            }
        }
    }


}
