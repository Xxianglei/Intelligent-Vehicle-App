package com.example.heath.Bluteooth;

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
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.example.heath.PersonCenter;
import com.example.heath.R;
import com.example.heath.utils.ParseNowWeatherUtil;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static android.R.attr.handle;
import static android.R.attr.x;

@RequiresApi(api = Build.VERSION_CODES.ECLAIR)
public class BindBlutooh extends AppCompatActivity {
    protected static final int REQUEST_DISCOVERABLE = 0;
    private ImageView imageView;
    private RippleBackground rippleBackground;
    private ListView mListView;
    private ArrayList<SiriListItem> list;
    ChatListAdapter mAdapter;
    private RelativeLayout re;
    private IntentFilter discoveryFilter;
    private IntentFilter foundFilter;
    private Intent intent;
    private TextView sug;

    enum ServerOrCilent {
        NONE,
        SERVICE,
        CILENT
    }

    private Context mContext;
    static String BlueToothAddress = "null";
    static ServerOrCilent serviceOrCilent = ServerOrCilent.NONE;
    static boolean isOpen = false;
    private BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    public void onStart() {
        super.onStart();
        if (!mBtAdapter.isEnabled()) {
            imageView.setClickable(false);
            sug.setVisibility(View.VISIBLE);
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, 3);
        }
        else {
            imageView.setClickable(true);
            sug.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.devices);
        mContext = this;
        init();
        initEvent();
    }

    private void init() {
        intent = new Intent(BindBlutooh.this, ChatService.class);
        rippleBackground = (RippleBackground) findViewById(R.id.content);
        imageView = (ImageView) findViewById(R.id.centerImage);
        imageView.setVisibility(View.VISIBLE);
        sug = (TextView) findViewById(R.id.suggesst);
        re =(RelativeLayout) findViewById(R.id.re);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("搜索设备");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.mipmap.back);
        //取代原本的actionbar
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        list = new ArrayList<SiriListItem>();
        mAdapter = new ChatListAdapter(mContext, list);
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        mListView = (ListView) findViewById(R.id.list);

        mListView.setAdapter(mAdapter);
        mListView.setFastScrollEnabled(true);
        mListView.setOnItemClickListener(mDeviceClickListener);
        mListView.setOnItemLongClickListener(mDeviceClickListener2);

        if (pairedDevices.size() > 0) {
            mListView.setVisibility(View.VISIBLE);
            re.setVisibility(View.GONE);
            for (BluetoothDevice device : pairedDevices) {
                list.add(new SiriListItem(device.getName() + "\n" + device.getAddress(), true));
                mAdapter.notifyDataSetChanged();
                mListView.setSelection(list.size() - 1);
            }
        } else {
            list.add(new SiriListItem("No devices have been paired", true));
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(list.size() - 1);
            mListView.setVisibility(View.GONE);
            re.setVisibility(View.VISIBLE);
        }


    }

    private void initEvent() {

        //  注册广播
        discoveryFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, discoveryFilter);
        foundFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, foundFilter);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(BindBlutooh.this, "正在搜索蓝牙设备,请等待...", Toast.LENGTH_SHORT).show();
                rippleBackground.startRippleAnimation();
                results();
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

    private AdapterView.OnItemLongClickListener mDeviceClickListener2 = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mListView.setClickable(false);
            AlertDialog.Builder StopDialog = new AlertDialog.Builder(mContext);
            StopDialog.setTitle("断开连接");//标题
            StopDialog.setMessage("您是否要断开蓝牙设备?");
            StopDialog.setPositiveButton("断开", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    //关闭服务
                    //关闭广播
                    unregisterReceiver(mReceiver);
                    stopService(intent);
                    Log.e("***", "已断开!");
                    Toast.makeText(BindBlutooh.this, "断开成功!", Toast.LENGTH_SHORT).show();
                }
            });
            StopDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    BlueToothAddress = null;
                }
            });
            StopDialog.show();
            return true;
        }

    };
    private OnItemClickListener mDeviceClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            SiriListItem item = list.get(arg2);
            String info = item.message;
            String address = info.substring(info.length() - 17);
            BlueToothAddress = address;

            AlertDialog.Builder StopDialog = new AlertDialog.Builder(mContext);//定义一个弹出框对象
            StopDialog.setTitle("连接");//标题
            StopDialog.setMessage(item.message);
            StopDialog.setPositiveButton("连接", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    mBtAdapter.cancelDiscovery();
                    //开启服务
                    startService(intent);
                    serviceOrCilent = ServerOrCilent.CILENT;
                    Toast.makeText(BindBlutooh.this, "连接成功!", Toast.LENGTH_SHORT).show();
                }
            });
            StopDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    BlueToothAddress = null;
                }
            });
            StopDialog.show();
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    list.add(new SiriListItem(device.getName() + "\n" + device.getAddress(), false));
                    mAdapter.notifyDataSetChanged();
                    mListView.setSelection(list.size() - 1);
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                if (mListView.getCount() == 0) {
                    list.add(new SiriListItem("未搜索到蓝牙设备", false));
                    mAdapter.notifyDataSetChanged();
                    mListView.setSelection(list.size() - 1);
                }
            }
        }
    };

    public class SiriListItem {
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
        if (mReceiver!=null) {
            this.unregisterReceiver(mReceiver);
        }
    }

    private android.os.Handler mHandler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x12) {
                if (mBtAdapter.isDiscovering()) {
                    mBtAdapter.cancelDiscovery();
                } else {
                    list.clear();
                    mAdapter.notifyDataSetChanged();
                    Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
                    if (pairedDevices.size() > 0) {
                        for (BluetoothDevice device : pairedDevices) {
                            list.add(new SiriListItem(device.getName() + "\n" + device.getAddress(), true));
                            mAdapter.notifyDataSetChanged();
                            mListView.setSelection(list.size() - 1);
                        }
                    } else {
                        list.add(new SiriListItem("No devices have been paired", true));
                        mAdapter.notifyDataSetChanged();
                        mListView.setSelection(list.size() - 1);
                    }
                    /* 开始搜索 */
                    mBtAdapter.startDiscovery();
                }
            }
            re.setVisibility(View.GONE);
            Toast.makeText(BindBlutooh.this, "搜索到设备...", Toast.LENGTH_SHORT).show();
        }
    };

}