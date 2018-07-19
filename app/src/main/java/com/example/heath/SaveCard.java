package com.example.heath;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.heath.Datebase.DataBaseManager;
import com.example.heath.Datebase.CardModle;
import com.example.heath.HttpUtils.OkNetRequest;
import com.example.heath.Model.Code;
import com.example.heath.Register.Log_in;
import com.example.heath.Register.Set_password;
import com.google.gson.Gson;
import com.pedaily.yc.ycdialoglib.selectDialog.CustomSelectDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.Request;
import okhttp3.Response;


public class SaveCard extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {


    private Toolbar toolbar;
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private TextView editText4;
    private TextView editText6;
    private TextView editText7;
    private TextView editText8;
    private TextView editText5;
    private String strDate;
    private Window window;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private CheckBox checkBox11;
    private CheckBox checkBox12;
    private CheckBox checkBox13;
    private CheckBox checkBox14;
    private CheckBox checkBox15;
    private CheckBox checkBox17;
    private CheckBox checkBox16;
    private CheckBox checkBox18;
    private View myView;
    private View myView1;
    private ArrayList<String> lists;
    private CheckBox checkBox5;
    private DataBaseManager dataBaseManager;
    private List<CardModle> save_list;
    private boolean tag = true;
    private String url = "http://47.94.21.55/houtai/addxinxi.php";
    private MyApplication myApplication;
    private boolean tag2 = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_card);
        dataBaseManager = new DataBaseManager();
        MyApplication.getInstance().addActivity(this);
        initview();
        initEvent();

        preLoad();
    }

    private void initview() {
        myApplication = (MyApplication) getApplication();
        lists = new ArrayList<String>();
        editText1 = (EditText) findViewById(R.id.text1);
        editText2 = (EditText) findViewById(R.id.text2);
        editText3 = (EditText) findViewById(R.id.text3);
        editText4 = (TextView) findViewById(R.id.text4);
        editText5 = (TextView) findViewById(R.id.text5);
        editText6 = (TextView) findViewById(R.id.text6);
        editText7 = (TextView) findViewById(R.id.text7);
        editText8 = (TextView) findViewById(R.id.text8);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("急救卡");
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
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.update_person) {
                    if (tag) {
                        editText1.setEnabled(true);
                        editText2.setEnabled(true);
                        editText3.setEnabled(true);
                        editText4.setEnabled(true);
                        editText5.setEnabled(true);
                        editText6.setEnabled(true);
                        editText7.setEnabled(true);
                        editText8.setEnabled(true);
                        tag = false;
                    } else {
                        if (editText1.getText() != null && editText2.getText() != null && editText3.getText() != null && editText4.getText() != null && editText5.getText() != null && editText6.getText() != null && editText7.getText() != null && editText8.getText() != null) {
                            save(editText1.getText().toString(), editText2.getText().toString(), editText3.getText().toString(), editText4.getText().toString(), editText5.getText().toString(), editText6.getText().toString(), editText7.getText().toString(), editText8.getText().toString());
                            Toast.makeText(SaveCard.this, "您的信息保存成功！", Toast.LENGTH_SHORT).show();
                            Upload(editText1.getText().toString(), editText2.getText().toString(), editText3.getText().toString(), editText4.getText().toString(), editText5.getText().toString(), editText6.getText().toString(), editText7.getText().toString(), editText8.getText().toString());
                        } else {
                            Toast.makeText(SaveCard.this, "请您将信息输完整，以便更好地为您服务！", Toast.LENGTH_SHORT).show();
                        }
                        editText1.setEnabled(false);
                        editText2.setEnabled(false);
                        editText3.setEnabled(false);
                        editText4.setEnabled(false);
                        editText5.setEnabled(false);
                        editText6.setEnabled(false);
                        editText7.setEnabled(false);
                        editText8.setEnabled(false);
                        tag = true;

                    }
                }
                return true;
            }
        });
    }

    private void save(String name, String high, String weight, String his, String birth, String blood, String react, String hobbit) {
        dataBaseManager.saveCard(name, high, weight, his, birth, blood, react, hobbit);
    }

    private void preLoad() {
        save_list = dataBaseManager.readcardList();
        if (save_list.size() > 0) {
            editText1.setText(save_list.get(save_list.size() - 1).getName());
            editText2.setText(save_list.get(save_list.size() - 1).getHigh());
            editText3.setText(save_list.get(save_list.size() - 1).getWeight());
            editText4.setText(save_list.get(save_list.size() - 1).getHis());
            editText5.setText(save_list.get(save_list.size() - 1).getBirth());
            editText6.setText(save_list.get(save_list.size() - 1).getBlood());
            editText7.setText(save_list.get(save_list.size() - 1).getReact());
            editText8.setText(save_list.get(save_list.size() - 1).getHobbit());
        }

    }

    private void initEvent() {
        editText5.setOnClickListener(this);
        editText6.setOnClickListener(this);
        editText7.setOnClickListener(this);
        editText8.setOnClickListener(this);
        editText4.setOnClickListener(this);
    }

    //如果有Menu,创建完后,系统会自动添加到ToolBar上
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_mess_menu, menu);
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text5:
                TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {

                        SimpleDateFormat lsdFormat = new SimpleDateFormat("yyyy-MM-dd ");
                        strDate = lsdFormat.format(date);
                        editText5.setText(strDate + "");
                    }
                }).build();
                pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                pvTime.show();
                break;
            case R.id.text4:
                final List<String> list1 = new ArrayList<>();
                list1.add("心脏病");
                list1.add("高血压");
                list1.add("无");
                showDialog(new CustomSelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        editText4.setText(list1.get(position));
                    }
                }, list1);
                break;
            case R.id.text6:
                final List<String> list = new ArrayList<>();
                list.add("A型");
                list.add("B型");
                list.add("AB型");
                list.add("O型");
                showDialog(new CustomSelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        editText6.setText(list.get(position));
                    }
                }, list);
                break;
            case R.id.text7:
                View myView = LayoutInflater.from(this).inflate(R.layout.react, null);
                checkBox11 = myView.findViewById(R.id.one);
                checkBox12 = myView.findViewById(R.id.one1);
                checkBox13 = myView.findViewById(R.id.one2);
                checkBox14 = myView.findViewById(R.id.one3);
                checkBox15 = myView.findViewById(R.id.one4);
                checkBox16 = myView.findViewById(R.id.one5);
                checkBox17 = myView.findViewById(R.id.one6);
                checkBox18 = myView.findViewById(R.id.one7);
                checkBox11.setOnCheckedChangeListener(this);
                checkBox12.setOnCheckedChangeListener(this);
                checkBox13.setOnCheckedChangeListener(this);
                checkBox14.setOnCheckedChangeListener(this);
                checkBox15.setOnCheckedChangeListener(this);
                checkBox16.setOnCheckedChangeListener(this);
                checkBox17.setOnCheckedChangeListener(this);
                checkBox18.setOnCheckedChangeListener(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder sb = new StringBuilder();
                        for (int a = 0; a <= lists.size() - 1; a++) {
                            sb.append(lists.get(a).toString() + " ");
                        }
                        editText7.setText(sb);
                        lists.clear();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SaveCard.this, "取消", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setTitle("过敏反应");
                dialog.setView(myView);
                //为Window设置动画
                window = dialog.getWindow();
                window.setWindowAnimations(R.style.animTranslate);
                dialog.show();
                break;
            case R.id.text8:
                View myView1 = LayoutInflater.from(this).inflate(R.layout.hobbies, null);
                checkBox1 = myView1.findViewById(R.id.one);
                checkBox2 = myView1.findViewById(R.id.one1);
                checkBox3 = myView1.findViewById(R.id.one2);
                checkBox4 = myView1.findViewById(R.id.one3);
                checkBox5 = myView1.findViewById(R.id.one4);
                checkBox1.setOnCheckedChangeListener(this);
                checkBox2.setOnCheckedChangeListener(this);
                checkBox3.setOnCheckedChangeListener(this);
                checkBox4.setOnCheckedChangeListener(this);
                checkBox5.setOnCheckedChangeListener(this);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);

                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder sb = new StringBuilder();
                        for (i = 0; i <= lists.size() - 1; i++) {
                            sb.append(lists.get(i).toString());
                        }
                        editText8.setText(sb);
                        lists.clear();

                    }
                });
                builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SaveCard.this, "取消", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog1 = builder1.create();
                dialog1.setTitle("生活习惯");
                dialog1.setView(myView1);
                //为Window设置动画
                window = dialog1.getWindow();
                window.setWindowAnimations(R.style.animTranslate);
                dialog1.show();
                break;

        }

    }

    private CustomSelectDialog showDialog(CustomSelectDialog.SelectDialogListener listener,
                                          List<String> names) {
        CustomSelectDialog dialog = new CustomSelectDialog(this,
                R.style.transparentFrameWindowStyle, listener, names);
        dialog.setItemColor(R.color.colorAccent, R.color.colorPrimary);
        //判断activity是否finish
        if (!this.isFinishing()) {
            dialog.show();
        }
        return dialog;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            //添加到爱好数组
            lists.add(buttonView.getText().toString().trim());

        } else {
            //从数组中移除
            lists.remove(buttonView.getText().toString().trim());
        }
    }

    private void Upload(String name, String shengao, String tizhong, String bingshi, String bron, String xuexing, String guoming, String xiguan) {

        HashMap<String, String> params = new HashMap<>();
        // 添加请求参数
        params.put("name", name);
        params.put("shengao", shengao);
        params.put("tizhong", tizhong);
        params.put("bingshi", bingshi);
        params.put("bron", bron);
        params.put("xuexing", xuexing);
        params.put("guoming", guoming);
        params.put("xiguan", xiguan);
        params.put("user", myApplication.getName().toString());

        OkNetRequest.postFormRequest(url, params, new OkNetRequest.DataCallBack() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void requestSuccess(Response response, String result) throws Exception {
                // 请求成功的回调
                Log.e("同步急救卡", result.toString());
                Gson gson = new Gson();
                String deal_result = result.toString();
                deal_result = deal_result.replace("连接成功", "");
                Toast.makeText(SaveCard.this, "同步成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void progressSuccess(Response response) throws Exception {

            }

            @Override
            public void requestFailure(Request request, IOException e) {
                // 请求失败的回调
                Toast.makeText(SaveCard.this, "同步失败", Toast.LENGTH_SHORT).show();

            }
        });
    }

    /**
     * private EditText editText1;
     * private EditText editText2;
     * private EditText editText3;
     * private TextView editText4;
     * private TextView editText6;
     * private TextView editText7;
     * private TextView editText8;
     * private TextView editText5;
     */

    private void LoadLocal() {
        List<CardModle> list = dataBaseManager.readcardList();
        if (list.size() > 0) {
            editText1.setText(list.get(list.size() - 1).getName());
            editText2.setText(list.get(list.size() - 1).getHigh() + "");
            editText3.setText(list.get(list.size() - 1).getWeight() + "");
            editText4.setText(list.get(list.size() - 1).getHis());
            editText5.setText(list.get(list.size() - 1).getBirth());
            editText6.setText(list.get(list.size() - 1).getBlood());
            editText7.setText(list.get(list.size() - 1).getReact());
            editText8.setText(list.get(list.size() - 1).getHobbit());
        }

    }

}