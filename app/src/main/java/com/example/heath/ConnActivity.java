package com.example.heath;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.UiThread;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.heath.Datebase.ConnectModle;
import com.example.heath.Datebase.DataBaseManager;
import com.example.heath.HttpUtils.OkNetRequest;
import com.example.heath.Model.Code;
import com.example.heath.Model.Con_person;
import com.example.heath.Register.Log_in;
import com.example.heath.adpter.MyAdapter;
import com.google.gson.Gson;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by 丽丽超可爱 on 2018/4/30.
 */

public class ConnActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView listView;
    private boolean Tag = false;
    private DataBaseManager database;
    private MyAdapter adapter;
    private List<Con_person> con_persons = new ArrayList<Con_person>();

    private ImageView imageView;
    private List<ConnectModle> list1;
    private String url = "http://47.94.21.55/houtai/addlink.php";
    private MyApplication myApplication;
    private ConnectivityManager manager;
    private NetworkInfo info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_person);
        initView();

        if (LoadLocalData() == false) {
            LoadYunData();
        } else {
            LoadLocalData();
        }
    }


    private void LoadYunData() {

    }

    private boolean LoadLocalData() {

        list1 = database.readconnList();
        con_persons.clear();
        if (list1.size() >= 1) {
            for (int i = list1.size() - 1; i >= 0; i--) {
                Con_person con_person = new Con_person();
                con_person.setName(list1.get((i)).getName());
                con_person.setPhone(list1.get((i)).getPhone());
                con_persons.add(con_person);
                Log.e("电话***", list1.get((i)).getName() + list1.get((i)).getPhone());
            }
            listView.setAdapter(adapter);
            return true;
        } else
            return false;
    }

    private void initView() {
        manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        info = manager.getActiveNetworkInfo();
        adapter = new MyAdapter(con_persons, this);
        database = new DataBaseManager();
        listView = findViewById(R.id.list_item);
        imageView = findViewById(R.id.add);
        imageView.setOnClickListener(this);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        //设置空列表的时候，显示为一张图片
        View emptyView = findViewById(R.id.image);
        listView.setEmptyView(emptyView);
        myApplication = (MyApplication) getApplication();
        if (info != null && info.isConnected()) {
        }
        else {
            Toast.makeText(ConnActivity.this, "请打开网络!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                View myView6 = LayoutInflater.from(ConnActivity.this).inflate(R.layout.list_demo2, null);
                final EditText name = myView6.findViewById(R.id.name);
                final EditText phone = myView6.findViewById(R.id.phone);

                AlertDialog.Builder builder6 = new AlertDialog.Builder(this);
                builder6.setIcon(R.mipmap.set);
                builder6.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e("姓名***1", name.getText().toString() + phone.getText().toString());
                        if (name.getText().toString() != null && phone.getText().toString() != null) {
                            database.saveConn(name.getText().toString(), phone.getText().toString());
                            Con_person con_person = new Con_person();
                            con_person.setName(name.getText().toString());
                            con_person.setPhone(phone.getText().toString());
                            Log.e("姓名***2", name.getText().toString() + phone.getText().toString());
                            con_persons.add(0, con_person);
                            adapter.notifyDataSetChanged();
                            adapter.notifyDataSetInvalidated();
                            listView.setAdapter(adapter);
                            //网络状态存在并且是已连接状态
                            if (info != null && info.isConnected()) {
                                Upload(myApplication.getName(), name.getText().toString(), phone.getText().toString());
                            }
                            else {
                                Toast.makeText(ConnActivity.this, "请打开网络!", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(ConnActivity.this, "请输入完整!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder6.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ConnActivity.this, "取消", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog6 = builder6.create();
                dialog6.setTitle("昵称");
                dialog6.setView(myView6);
                //为Window设置动画
                Window window = dialog6.getWindow();
                window.setWindowAnimations(R.style.animTranslate);
                dialog6.show();
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.delete);
        builder.setTitle("提示");
        builder.setMessage("您确定拨打电话吗");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 6.0以上权限申请
                    intentToCall(con_persons.get(position).getPhone().toString());
                    Log.e("拨打电话", con_persons.get(position).getPhone().toString());
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + con_persons.get(position).getPhone().toString()));
                    startActivity(intent);
                }
            }


        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ConnActivity.this, "取消", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.delete);
        builder.setTitle("提示");
        builder.setMessage("您确定要删除吗");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("删除某人", con_persons.get(position).getName().toString());
                Log.e("position", position + "");
                if (database.deletConnSingle(con_persons.get(position).getName().toString())) {
                    Toast.makeText(ConnActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    con_persons.remove(position);
                    adapter.notifyDataSetChanged();
                    adapter.notifyDataSetInvalidated();
                } else
                    Toast.makeText(ConnActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ConnActivity.this, "取消", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    /**
     * 直接拨打电话
     */
    public boolean intentToCall(final String phoneNumber) {
        if (phoneNumber.isEmpty()) {
            Toast.makeText(ConnActivity.this, "拨打失败", Toast.LENGTH_SHORT).show();
            return false;
        }
        //6.0权限处理
        Acp.getInstance(this).request(new AcpOptions.Builder().setPermissions(
                Manifest.permission.CALL_PHONE).build(), new AcpListener() {
            @Override
            public void onGranted() {
                Uri u = Uri.parse("tel:" + phoneNumber);
                Intent it = new Intent(Intent.ACTION_CALL, u);
                if (ActivityCompat.checkSelfPermission(ConnActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    startActivity(it);
                    return;
                }

            }

            @Override
            public void onDenied(List<String> permissions) {

            }
        });

        return true;

    }

    private void Upload(String user, String name, String phone) {

        HashMap<String, String> params = new HashMap<>();
        // 添加请求参数
        params.put("user", user);
        params.put("name", name);
        params.put("phone", phone);

        OkNetRequest.postFormRequest(url, params, new OkNetRequest.DataCallBack() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void requestSuccess(Response response,String result) throws Exception {
                // 请求成功的回调
                Log.e("success", result.toString());
                Toast.makeText(ConnActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void progressSuccess(Response response) throws Exception {

            }

            @Override
            public void requestFailure(Request request, IOException e) {
                // 请求失败的回调
                Toast.makeText(ConnActivity.this, "同步失败", Toast.LENGTH_SHORT).show();
            }
        });
    }



}