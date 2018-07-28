package com.example.heath.Bluteooth;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;


import com.example.heath.MyApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lenovo on 2018/6/14.
 */

public class ChatService extends Service {

    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";


    private BluetoothServerSocket mserverSocket = null;
    private clientThread clientConnectThread = null;
    private BluetoothSocket socket = null;
    private BluetoothDevice device = null;
    private readThread mreadThread = null;
    String address = "null";
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    String address1 = "98:D3:51:FD:7F:4E";
    String address2 = "43:34:18:04:03:26";
    String address3 = "00:21:13:00:B2:FF";
    int i = 0;
    private boolean a;
    private boolean b;
    private boolean c;

    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        address = intent.getStringExtra("address");
        Log.e("address", address);
        if (Bluetooth.serviceOrCilent == Bluetooth.ServerOrCilent.CILENT) {

            if (!address.equals("null")) {
                MyApplication myApplication = (MyApplication) getApplication();
                /**
                 *    标志所有设备已经连接
                 */

                if (address.contains(address1)) {
                    i = 1;
                    myApplication.setTag1(true);
                } else if (address.contains(address2)) {
                    i = 2;
                    myApplication.setTag2(true);
                } else if (address.contains(address3)) {
                    i = 3;
                    myApplication.setTag3(true);
                }

                /**
                 * 开启对应线程
                 */
                clientConnectThread = new clientThread(mBluetoothAdapter.getRemoteDevice(address));
                clientConnectThread.start();

            }
        }

        return super.onStartCommand(intent, flags, startId);

    }


    //开启客户端
    private class clientThread extends Thread {
        public clientThread(BluetoothDevice remoteDevice) {
            device = remoteDevice;

        }

        public void run() {
            try {
                Log.e("client", "run");
                //创建一个Socket连接：只需要服务器在注册时的UUID号
                // socket = device.createRfcommSocketToServiceRecord(BluetoothProtocols.OBEX_OBJECT_PUSH_PROTOCOL_UUID);
                socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                //连接
                socket.connect();
                //启动接受数据

                Thread read1 = new Thread(new readThread(socket));
                read1.start();


            } catch (IOException e) {
                Log.e("connect", "", e);

            }
        }
    }

    ;


    /* 停止客户端连接 */
    private void shutdownClient() {
        new Thread() {
            public void run() {
                if (clientConnectThread != null) {
                    clientConnectThread.interrupt();
                    clientConnectThread = null;
                }
                if (mreadThread != null) {
                    mreadThread.interrupt();
                    mreadThread = null;
                }

                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    socket = null;
                }
            }
        }.start();
    }

    //读取数据
    private class readThread extends Thread {
        BluetoothSocket socket;

        public readThread(BluetoothSocket socket) {
            this.socket = socket;
        }

        public void run() {
            Log.e("read", "run");
            byte[] buffer = new byte[1024];
            byte[] buffer2 = new byte[22];
            int bytes;
            int count = 0;
            int data[] = new int[14];
            float pinjun = 0.0f;
            float fangcha = 0.0f;
            int sum = 0;
            float fenzi = 0.0f;
            int last = 0;
            InputStream mmInStream = null;

            try {
                mmInStream = socket.getInputStream();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            while (!isInterrupted()) {
                try {
                    String ad = String.valueOf(socket.getRemoteDevice());
                    // Read from the InputStream
                    if (ad.contains(address1)) {
                        if ((bytes = mmInStream.read(buffer)) > 0) {

                            byte[] buf_data = new byte[bytes];
                            for (int i = 0; i < bytes; i++) {
                                buf_data[i] = buffer[i];
                            }
                            String s = new String(buf_data);
                            Log.e("MESSAGE1", s);
                            sendContentBroadcast(s, 1);
                            Log.e("MESSAGE1", s);


                            //应该在4-5s之间还需进一步调试
                            Thread.sleep(4500);

                        }
                    } else if (ad.contains(address2)) {
                        while (!isInterrupted()) {

                            int num = mmInStream.read(buffer);
                            Log.e("MESSAGE2", String.valueOf(buffer[0]));
                            String s = new String(String.valueOf(buffer[0]));
                            sendContentBroadcast(s, 2);
                            Thread.sleep(5000);

                        }
                    } else if (ad.contains(address3)) {
                        //  取出14组  4.9秒发送一次
                        if ((mmInStream.read(buffer2, 0, 21)) > 0) {

                            String s = new String(buffer2);
                            //  正则提取数字
                            String regEx = "[^0-9]";
                            Pattern p = Pattern.compile(regEx);
                            Matcher m = p.matcher(s);
                            s = m.replaceAll("").trim();
                            Log.e("MESSAGE1", s);
                            if (isNumeric(s)) {
                                int xinlv = Integer.parseInt(s);
                                //  s  xinlv  都代表心率数据
                                if (xinlv >= 55 && xinlv <= 150) {
                                    data[count] = xinlv;      //  心率数据存入14个元素的数组
                                    count++;
                                    Log.e("count:", count + "");
                                    if (count == 14) {
                                        for (int i = 0; i <= 13; i++) {
                                            sum = data[i] + sum;
                                        }
                                        Log.e("sum", sum + "");
                                        pinjun = (float) (sum / 14.0);
                                        Log.e("pinjun", pinjun + "");
                                        for (int i = 0; i <= 13; i++) {
                                            fenzi = (float) (Math.pow((data[i] - pinjun), 2) + fenzi);
                                        }
                                        Log.e("fenzi", fenzi + "");
                                        fangcha = (float) (fenzi / 14.0);
                                        Log.e("fangcha:", fangcha + "");
                                        Log.e("处理后", s);
                                        if (fangcha <= 100) {
                                            int send = (int) pinjun;
                                            sendContentBroadcast(String.valueOf(send), 3);
                                            last = send;
                                        } else {
                                            sendContentBroadcast(String.valueOf(last), 3);
                                        }
                                        sum = 0;
                                        count = 0;
                                        fenzi = 0.0f;
                                    }
                                } else {
                                    if (last != 0)
                                        sendContentBroadcast(String.valueOf(last), 3);
                                    else
                                        sendContentBroadcast(String.valueOf(81), 3);
                                }
                                //应该在4-5s之间还需进一步调试
                                Thread.sleep(350);
                            }
                        }
                    }


                } catch (
                        IOException e)

                {
                    try {
                        mmInStream.close();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    break;
                } catch (
                        InterruptedException e)

                {
                    e.printStackTrace();
                    break;
                }

            }
            try {
                mmInStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    //发送广播
    protected void sendContentBroadcast(String name, int i) {
        // TODO Auto-generated method stub

        Intent intent = new Intent();
        intent.setAction("com.example.servicecallback.content");
        intent.putExtra("name", name);
        intent.putExtra("type", i);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (Bluetooth.serviceOrCilent == Bluetooth.ServerOrCilent.CILENT) {
            shutdownClient();
        }

        Bluetooth.serviceOrCilent = Bluetooth.ServerOrCilent.NONE;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            Log.e("数字", "OK");
            return true;
        } catch (NumberFormatException e) {
            Log.e("数字", "NO");
            return false;
        }
    }
}
