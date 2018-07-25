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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heath.Datebase.ConnectModle;
import com.example.heath.Datebase.DataBaseManager;
import com.example.heath.HttpUtils.OkNetRequest;
import com.example.heath.Model.Contact;
import com.example.heath.adpter.ContactAdapter;
import com.example.heath.presenter.ContactPresenter;
import com.example.heath.view.IContactView;
import com.king.view.slidebar.SlideBar;
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

public class ConnActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener , IContactView{

    private boolean Tag = false;
    private DataBaseManager database;


    private MyApplication myApplication;
    private NetworkInfo info;
    private SlideBar slideBar;

    private TextView tvLetter;

    private ListView listView;

    private ContactAdapter contactAdapter;

    private ContactPresenter presenter;

    private Context context;
    private List<Contact> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_person);
        initView();
        MyApplication.getInstance().addActivity(this);
        LoadLocalData();

    }




    private void LoadLocalData() {
        listView.setAdapter(contactAdapter);
    }

    private void initView() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        info = manager.getActiveNetworkInfo();
        database = new DataBaseManager();
        context = this;
        slideBar = (SlideBar) findViewById(R.id.slideBar);
        tvLetter = (TextView) findViewById(R.id.tvLetter);
        listView = (ListView) findViewById(R.id.listview);
        list = new ArrayList<Contact>();
        List<ConnectModle> list1 = database.readconnList();
        if (list1.size() >= 1) {
            for (int i = list1.size() - 1; i >= 0; i--) {
                Contact c = new Contact();
                c.setName(list1.get((i)).getName().toString());
                c.setNumber(list1.get((i)).getPhone().toString());
                list.add(c);
                Log.e("电话***", list1.get((i)).getName() + list1.get((i)).getPhone());
            }
        }
        contactAdapter = new ContactAdapter(context, list);
        presenter = new ContactPresenter(context,  this);
        ImageView imageView = findViewById(R.id.add);
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



        slideBar.setOnTouchLetterChangeListenner(new SlideBar.OnTouchLetterChangeListenner() {
            @Override
            public void onTouchLetterChange(boolean isTouch, String letter) {
                int pos = contactAdapter.getPositionByLetter(letter);
                listView.setSelection(pos);
                presenter.showLetter(letter);

            }
        });

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
                            Contact c = new Contact();
                            c.setName(name.getText().toString());
                            c.setNumber(phone.getText().toString());
                            list.add(c);
                            contactAdapter = new ContactAdapter(context,list);
                            listView.setAdapter(contactAdapter);
                            contactAdapter.notifyDataSetInvalidated();
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
                    intentToCall(list.get(position).getNumber().toString());
                    Log.e("拨打电话", list.get(position).getNumber().toString());
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + list.get(position).getNumber().toString()));
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
                Log.e("删除某人", list.get(position).getName().toString());
                Log.e("position", position + "");
                if (database.deletConnSingle(list.get(position).getName().toString())) {
                    Toast.makeText(ConnActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    list.remove(position);
                    contactAdapter = new ContactAdapter(context,list);
                    listView.setAdapter(contactAdapter);
                    contactAdapter.notifyDataSetInvalidated();
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

        String url = "http://47.94.21.55/houtai/addlink.php";
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

    @Override
    public void getListContact(List<Contact> list) {
        contactAdapter.setListData(list);
        contactAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLetter(String letter) {
        tvLetter.setText(letter);
        if(tvLetter.getVisibility()!=View.VISIBLE){
            tvLetter.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void hideLetter() {
        tvLetter.setVisibility(View.GONE);
        tvLetter.setText("");
    }

}