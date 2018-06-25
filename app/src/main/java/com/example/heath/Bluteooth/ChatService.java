package com.example.heath.Bluteooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.example.heath.Bluteooth.BindBlutooh.BlueToothAddress;
import static com.example.heath.Bluteooth.BindBlutooh.isOpen;
import static com.example.heath.Bluteooth.BindBlutooh.serviceOrCilent;

/**
 * Created by lenovo on 2018/6/14.
 */

@RequiresApi(api = Build.VERSION_CODES.ECLAIR)
public class ChatService extends Service{

    public static final String PROTOCOL_SCHEME_RFCOMM = "btspp";


    private BluetoothServerSocket mserverSocket = null;
    private ServerThread startServerThread = null;
    private clientThread clientConnectThread = null;
    private BluetoothSocket socket = null;
    private BluetoothDevice device = null;
    private readThread mreadThread = null;;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    public void onCreate() {
        super.onCreate();
        connect();
    }





    private void connect(){
        Log.e("connect","run");
        if(isOpen)
        {

            return;
        }
        if(serviceOrCilent== BindBlutooh.ServerOrCilent.CILENT)
        {
            String address = BlueToothAddress;
            if(!address.equals("null"))
            {
                device = mBluetoothAdapter.getRemoteDevice(address);
                clientConnectThread = new clientThread();
                clientConnectThread.start();
               isOpen = true;
            }
            else
            {

            }
        }
        else if(serviceOrCilent== BindBlutooh.ServerOrCilent.SERVICE)
        {
            startServerThread = new ServerThread();
            startServerThread.start();
            isOpen = true;
        }
    }


    //?????????
    private class clientThread extends Thread {

        public void run() {
            try {
                Log.e("client","run");

                socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

                socket.connect();

                mreadThread = new readThread();
                mreadThread.start();
            }
            catch (IOException e)
            {
                Log.e("connect", "", e);

            }
        }
    };

    //??????????
    private class ServerThread extends Thread {
        public void run() {

            try {
                Log.e("service","run");
                /** ?????????????????
                 * ?????????????????UUID	 */
                mserverSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(PROTOCOL_SCHEME_RFCOMM,
                        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));

                Log.d("server", "wait cilent connect...");



				/* ????????????????? */
                socket = mserverSocket.accept();
                Log.d("server", "accept success !");



                mreadThread = new readThread();
                mreadThread.start();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
    /**
     *???????? */
    private void shutdownServer() {
        new Thread() {
            public void run() {
                if(startServerThread != null)
                {
                    startServerThread.interrupt();
                    startServerThread = null;
                }
                if(mreadThread != null)
                {
                    mreadThread.interrupt();
                    mreadThread = null;
                }
                try {
                    if(socket != null)
                    {
                        socket.close();
                        socket = null;
                    }
                    if (mserverSocket != null)
                    {
                        mserverSocket.close();/* ???????? */
                        mserverSocket = null;
                    }
                } catch (IOException e) {
                    Log.e("server", "mserverSocket.close()", e);
                }
            };
        }.start();
    }
    /* ??????????? */
    private void shutdownClient() {
        new Thread() {
            public void run() {
                if(clientConnectThread!=null)
                {
                    clientConnectThread.interrupt();
                    clientConnectThread= null;
                }
                if(mreadThread != null)
                {
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
            };
        }.start();
    }

    //???????
    private class readThread extends Thread {
        public void run() {
            Log.e("read","run");
            byte[] buffer = new byte[1024];
            int bytes;
            InputStream mmInStream = null;

            try {
                mmInStream = socket.getInputStream();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            while (true) {
                try {
                    // Read from the InputStream
                    if( (bytes = mmInStream.read(buffer)) > 0 )
                    {
                        byte[] buf_data = new byte[bytes];
                        for(int i=0; i<bytes; i++)
                        {
                            buf_data[i] = buffer[i];
                        }
                        String s = new String(buf_data);
                        Log.e("MESSAGE",s);

                        sendContentBroadcast(s);


                        Thread.sleep(4500);

                    }
                } catch (IOException e) {
                    try {
                        mmInStream.close();
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //?????
    protected void sendContentBroadcast(String name) {
        // TODO Auto-generated method stub
        Intent intent=new Intent();
        intent.setAction("com.example.servicecallback.content");
        intent.putExtra("name", name);
        sendBroadcast(intent);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        if (serviceOrCilent == BindBlutooh.ServerOrCilent.CILENT)
        {
            shutdownClient();
        }
        else if (serviceOrCilent == BindBlutooh.ServerOrCilent.SERVICE)
        {
            shutdownServer();
        }
        isOpen = false;
        serviceOrCilent = BindBlutooh.ServerOrCilent.NONE;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
