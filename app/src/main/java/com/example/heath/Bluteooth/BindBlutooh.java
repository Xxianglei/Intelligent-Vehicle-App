package com.example.heath.Bluteooth;

import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.example.heath.R;
import com.example.heath.utils.Utils;
import com.skyfishjy.library.RippleBackground;

public class BindBlutooh extends Activity {
    protected static final int REQUEST_DISCOVERABLE = 0;
    private ImageView imageView;
    private RippleBackground rippleBackground;
    private RelativeLayout re;
    private ListView mListView;
    private ArrayList<Bluetooth.SiriListItem> list;
    ChatListAdapter mAdapter;
    Context mContext;
    broad mReceiver;
    String address1 = "98:D3:51:FD:7F:4E";
    String address2 = "43:34:18:04:03:26";
    String address3 = "00:21:13:00:B2:FF";
    private BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    private TextView sug;

    @Override
    public void onStart() {
        super.onStart();

        if (!mBtAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, 3);

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        setContentView(R.layout.devices);
        mContext = this;
        init();
        initEvent();
    }

    private void initEvent() {
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.isFastClick()) {
                    Toast.makeText(BindBlutooh.this, "正在搜索蓝牙设备,请等待...", Toast.LENGTH_SHORT).show();
                    rippleBackground.startRippleAnimation();
                    results();
                }
            }
        });
    }

    private void results() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);

                    Message message = new Message();
                    message.what = 0x12;
                    mHandler.sendMessage(message);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void init() {
        rippleBackground = (RippleBackground) findViewById(R.id.content);
        imageView = (ImageView) findViewById(R.id.centerImage);
        imageView.setVisibility(View.VISIBLE);
        sug = (TextView) findViewById(R.id.suggesst);
        re = (RelativeLayout) findViewById(R.id.re);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("搜索设备");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        list = new ArrayList<>();
        mAdapter = new ChatListAdapter(mContext, list);
        mListView = (ListView) findViewById(R.id.list);
        mListView.setAdapter(mAdapter);
        mListView.setFastScrollEnabled(true);
        mListView.setOnItemClickListener(mDeviceClickListener);
        Log.e("aaa", "aaaaaaa");
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            re.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                if (device.getAddress().contains(address1)) {
                    list.add(new Bluetooth.SiriListItem("蜗行—空气质量传感器" + "\n" + "蓝牙地址：" + device.getAddress(), true));
                } else if (device.getAddress().contains(address2)) {
                    list.add(new Bluetooth.SiriListItem("蜗行—温度传感器" + "\n" + "蓝牙地址：" + device.getAddress(), true));
                } else if (device.getAddress().contains(address3)) {
                    list.add(new Bluetooth.SiriListItem("蜗行—心率传感器" + "\n" + "蓝牙地址：" + device.getAddress(), true));
                } else {
                    list.add(new Bluetooth.SiriListItem(device.getName() + "\n" + "蓝牙地址：" + device.getAddress(), true));
                }

                mAdapter.notifyDataSetChanged();
                mListView.setSelection(list.size() - 1);
            }
        } else {
            list.add(new Bluetooth.SiriListItem("No devices have been paired", true));
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(list.size() - 1);
        }

    }

    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            Bluetooth.SiriListItem item = list.get(arg2);
            final String info = item.message;
            final String address = info.substring(info.length() - 17);
            Bluetooth.BlueToothAddress = address;

            AlertDialog.Builder StopDialog = new AlertDialog.Builder(mContext);//定义一个弹出框对象
            StopDialog.setTitle("连接");//标题
            StopDialog.setMessage(item.message);
            StopDialog.setPositiveButton("连接", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    mBtAdapter.cancelDiscovery();
                    //开启服务
                    Intent intent = new Intent(BindBlutooh.this, ChatService.class);
                    intent.putExtra("address", address);
                    startService(intent);
                    Bluetooth.serviceOrCilent = Bluetooth.ServerOrCilent.CILENT;
                }
            });
            StopDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Bluetooth.BlueToothAddress = null;
                }
            });
            StopDialog.show();
        }
    };

    public class broad extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e("ccccc", "cccc");
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    list.add(new Bluetooth.SiriListItem(device.getName() + "\n" + device.getAddress(), false));
                    mAdapter.notifyDataSetChanged();
                    mListView.setSelection(list.size() - 1);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                if (mListView.getCount() == 0) {
                    list.add(new Bluetooth.SiriListItem("未搜索到蓝牙设备", false));
                    mAdapter.notifyDataSetChanged();
                    mListView.setSelection(list.size() - 1);
                }

            }
        }

    }


    public static class SiriListItem {
        String message;
        boolean isSiri;

        public SiriListItem(String msg, boolean siri) {
            message = msg;
            isSiri = siri;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }
        if (mReceiver != null)
            this.unregisterReceiver(mReceiver);
    }

    private android.os.Handler mHandler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 0x12) {
                Log.e("bbbb", "bbbb");
                // TODO Auto-generated method stub
                if (mBtAdapter.isDiscovering()) {
                    mBtAdapter.cancelDiscovery();

                } else {
                    mListView.setVisibility(View.VISIBLE);
                    list.clear();
                    mAdapter.notifyDataSetChanged();

                    Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
                    if (pairedDevices.size() > 0) {
                        for (BluetoothDevice device : pairedDevices) {
                            if (device.getAddress().contains(address1)) {
                                list.add(new Bluetooth.SiriListItem("蜗行—空气质量传感器" + "\n" + "蓝牙地址：" + device.getAddress(), true));
                            } else if (device.getAddress().contains(address2)) {
                                list.add(new Bluetooth.SiriListItem("蜗行—温度传感器" + "\n" + "蓝牙地址：" + device.getAddress(), true));
                            } else if (device.getAddress().contains(address3)) {
                                list.add(new Bluetooth.SiriListItem("蜗行—心率传感器" + "\n" + "蓝牙地址：" + device.getAddress(), true));
                            } else {
                                list.add(new Bluetooth.SiriListItem(device.getName() + "\n" + "蓝牙地址：" + device.getAddress(), true));
                            }
                            mAdapter.notifyDataSetChanged();
                            mListView.setSelection(list.size() - 1);
                        }
                    } else {
                        list.add(new Bluetooth.SiriListItem("No devices have been paired", true));
                        mAdapter.notifyDataSetChanged();
                        mListView.setSelection(list.size() - 1);
                    }
                    /* 开始搜索 */
                    mBtAdapter.startDiscovery();
                    mReceiver = new broad();
                    IntentFilter filter = new IntentFilter();
//发现设备
                    filter.addAction(BluetoothDevice.ACTION_FOUND);
//设备连接状态改变
                    filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//蓝牙设备状态改变
                    filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
                    registerReceiver(mReceiver, filter);

                }
                rippleBackground.stopRippleAnimation();
                re.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                Toast.makeText(BindBlutooh.this, "搜索到设备...", Toast.LENGTH_SHORT).show();
            }
        }
    };

}