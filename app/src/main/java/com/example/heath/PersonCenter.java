package com.example.heath;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.Window;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.heath.Bluteooth.BindBlutooh;
import com.example.heath.Datebase.DataBaseManager;
import com.example.heath.Datebase.UserModle;
import com.example.heath.HttpUtils.OkNetRequest;
import com.example.heath.Model.Code;
import com.example.heath.Register.Log_in;
import com.example.heath.utils.ActivityManager;
import com.example.heath.utils.ImageUtils;
import com.example.heath.view.CircleImageView;
import com.example.heath.view.MyOneLineView;
import com.example.heath.view.ObservableScrollView;
import com.google.gson.Gson;
import com.qiushui.blurredview.BlurredView;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;


import okhttp3.Request;
import okhttp3.Response;

import static com.xiasuhuei321.loadingdialog.view.LoadingDialog.Speed.SPEED_TWO;

public class PersonCenter extends AppCompatActivity implements View.OnClickListener {



    @Override
    protected void onStop() {
        upload_data(params);
        super.onStop();
    }


    private ObservableScrollView mScrollView = null;
    private MaterialRefreshLayout materialRefreshLayout;


    private NumberPicker numberPicker_age;
    private NumberPicker numberPicker_weight;
    private NumberPicker numberPicker_high;

    private static int my_high;
    private static int my_tizong;
    private static int my_age;
    private static String my_xingbie;
    private static String item[] = {"男", "女"};
    private static int a = 0;
    private EditText set_name;
    private static final String TAG = "PersonCentter";

    private String url = "http://47.94.21.55/houtai/addUSer.php";

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private Window window;
    private MyApplication myApp;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private MyApplication application;
    private HashMap<String, String> params;

    private boolean bool;

    private int mScrollerY;
    private BlurredView blurredView;
    private int mAlpha = 1;
    private DataBaseManager dataBaseManager;
    private MyOneLineView oneItem;
    private MyOneLineView twoItem;
    private MyOneLineView thereItem;
    private MyOneLineView fourItem;
    private TextView name;
    private ImageView xingbie;
    private CircleImageView headImage;
    private TextView textView;
    private TextView age;
    private TextView tizong;
    private TextView high;
    private LinearLayout llhigh;
    private LinearLayout llAge;
    private LinearLayout llTizong;

    private Button button;
    private MyApplication myApplication;

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.xingbie:

                AlertDialog.Builder builder4 = new AlertDialog.Builder(PersonCenter.this);
                builder4.setIcon(R.mipmap.set);
                builder4.setTitle("性别");
                builder4.setSingleChoiceItems(item, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        my_xingbie = item[i];
                        a = i;
                        editor.putInt("TAG", a);

                    }
                });
                builder4.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //  修改ui信息
                        textView.setText(my_xingbie);
                        editor.putString("SEX", my_xingbie);
                        if (a != 0) {
                            xingbie.setImageResource(R.mipmap.girl);
                        } else {
                            xingbie.setImageResource(R.mipmap.boy);
                        }

                        params.put("sex", my_xingbie);


                        editor.commit();

                    }
                });
                builder4.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PersonCenter.this, "取消", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog4 = builder4.create();

                window = dialog4.getWindow();
                //为Window设置动画
                window.setWindowAnimations(R.style.animTranslate);
                dialog4.show();


                break;

            case R.id.head_image:
                showChoosePicDialog();

                break;
            case R.id.ll_high:
                //解决两次点击出现dialog时候崩溃
                //原因：Dialog第二次打开报错是出现在自定义的Dialog中，
                // Dialog在被第二次打开的时候其试图已经存在，而你再次打开它，
                // Dialog会认为此视图已有了父容器而你还要为人家添加一个父容器
                View myView3 = LayoutInflater.from(PersonCenter.this).inflate(R.layout.choosehigh, null);
                numberPicker_high = myView3.findViewById(R.id._high);
                numberPicker_high.setMaxValue(250);
                numberPicker_high.setMinValue(140);
                numberPicker_high.setValue(170);
                numberPicker_high.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        my_high = i1;
                    }
                });

                AlertDialog.Builder builder2 = new AlertDialog.Builder(PersonCenter.this);
                builder2.setIcon(R.mipmap.set);
                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  修改ui信息
                        editor.putString("HIGH", my_high + "");
                        high.setText(my_high + "cm");

                        params.put("height", String.valueOf(my_high));


                        editor.commit();

                    }
                });
                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PersonCenter.this, "取消", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog2 = builder2.create();
                dialog2.setTitle("身高");
                dialog2.setView(myView3);
                window = dialog2.getWindow();
                //为Window设置动画
                window.setWindowAnimations(R.style.animTranslate);
                dialog2.show();
                break;
            case R.id.ll_tizong:

                View myView2 = LayoutInflater.from(PersonCenter.this).inflate(R.layout.chooseweight, null);
                numberPicker_weight = myView2.findViewById(R.id.my_weight);
                numberPicker_weight.setMaxValue(200);
                numberPicker_weight.setMinValue(30);
                numberPicker_weight.setValue(70);
                numberPicker_weight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        my_tizong = i1;
                    }
                });
                AlertDialog.Builder builder3 = new AlertDialog.Builder(PersonCenter.this);
                builder3.setIcon(R.mipmap.set);
                builder3.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putString("WEIGHT", my_tizong + "");
                        tizong.setText(my_tizong + "kg");

                        params.put("weight", String.valueOf(my_tizong));

                        editor.commit();

                    }
                });
                builder3.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PersonCenter.this, "取消", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog3 = builder3.create();
                dialog3.setTitle("体重");
                dialog3.setView(myView2);

                //为Window设置动画
                window = dialog3.getWindow();
                window.setWindowAnimations(R.style.animTranslate);
                dialog3.show();
                break;

            case R.id.ll_age:

                View myView = LayoutInflater.from(PersonCenter.this).inflate(R.layout.chooseage, null);
                numberPicker_age = myView.findViewById(R.id._age);
                numberPicker_age.setMaxValue(120);
                numberPicker_age.setMinValue(0);
                numberPicker_age.setValue(25);
                numberPicker_age.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        my_age = i1;
                    }
                });
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonCenter.this);
                builder.setIcon(R.mipmap.set);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editor.putString("AGE", my_age + "");
                        age.setText(my_age + "");
                        params.put("age", String.valueOf(my_age));
                        editor.commit();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PersonCenter.this, "取消", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setTitle("年龄");
                dialog.setView(myView);

                //为Window设置动画
                window = dialog.getWindow();
                window.setWindowAnimations(R.style.animTranslate);
                dialog.show();
                break;
            case R.id.name:
                View myView6 = LayoutInflater.from(PersonCenter.this).inflate(R.layout.setname, null);
                set_name = myView6.findViewById(R.id.set_name);

                AlertDialog.Builder builder6 = new AlertDialog.Builder(PersonCenter.this);
                builder6.setIcon(R.mipmap.set);
                builder6.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  修改ui信息

                        name.setText(set_name.getText().toString());
                        //保存偏好设置 写入全局变量  ...  多出调用
                        editor.putString("NAME", set_name.getText().toString());
                        editor.commit();

                        params.put("userName", set_name.getText().toString());


                        Log.i("测试", set_name.getText().toString());
                        Log.i("偏好", sp.getString("NAME", null).toString());

                        myApp.setPhone(set_name.getText().toString());

                    }
                });
                builder6.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PersonCenter.this, "取消", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog6 = builder6.create();
                dialog6.setTitle("昵称");
                dialog6.setView(myView6);

                //为Window设置动画
                window = dialog6.getWindow();
                window.setWindowAnimations(R.style.animTranslate);
                dialog6.show();
                break;
            case R.id.one_item:
                startActivity(new Intent(this, BindBlutooh.class));
                break;
            case R.id.two_item:
                startActivity(new Intent(this, ConnActivity.class));
                break;
            case R.id.there_item:
                startActivity(new Intent(this, ReportActivity.class));
                break;
            case R.id.four_item:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.log_out:

                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setIcon(R.mipmap.delete);
                builder1.setTitle("提示");
                builder1.setMessage("您确定要退出登陆吗");
                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  清除数据库  清除偏好设

                        DataBaseManager dataBaseManager = new DataBaseManager();
                        dataBaseManager.clearAll();
                        /**
                         *
                         * ActivityManager.addActivity(this,"MainActivity");
                         * Activity activity = ActivityManager.getActivity("MainActivity");
                         * activity.finish();
                         */
                        // 关掉所有的Activity，退出App时使用
                        ActivityManager.removeAllActivity();
                    }


                });

                builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PersonCenter.this, "取消", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog1 = builder1.create();
                dialog1.show();

                break;

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_con);
        getWindow().setBackgroundDrawable(null);
        initViews();
        params.put("user",myApplication.getName().toString());
        // 获取偏好头像
        getBitmapFromSharedPreferences();
        LoadLocal();
        // 刷新监听
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {

               // upload_data(params);
                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        materialRefreshLayout.finishRefresh();
                    }
                }, 2000);

            }
        });
    }

    //  头像
    public void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = {"选择本地照片", "拍照"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        //  郁闷这个错找半天   还好一下就解决了
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                "image/*");
                        // 调用剪切功能
                        startActivityForResult(intent, CHOOSE_PICTURE);
                        break;
                    case TAKE_PICTURE: // 拍照
                        takePicture();
                        break;
                }
            }
        });

        Window window7 = builder.create().getWindow();
        //为Window设置动画
        window7.setWindowAnimations(R.style.animTranslate);
        builder.create().show();
    }

    private void takePicture() {
        Intent openCameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        tempUri = Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), "image.jpg"));
        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }


    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     */
    protected void setImageToView(Intent data) {
        bool = false;
        Bundle extras = data.getExtras();
        Log.i(TAG, String.valueOf(data.getExtras().getParcelable("data")));
        if (extras != null) {
            bool = true;

            Bitmap photo = extras.getParcelable("data");
            Log.d(TAG, "setImageToView:" + photo);

            photo = ImageUtils.toRoundBitmap(photo);  // 这个时候的图片已经被处理成圆形的了

            //  保存偏好
            //第一步:将Bitmap压缩至字节数组输出流ByteArrayOutputStream
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
            //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
            //第三步:将String保持至SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("testSP", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("image", imageString);
            editor.commit();

            //加入全局变量 主页同步显示
            application = (MyApplication) getApplication();
            application.setImage_String(imageString);
            Log.e("头像全局保存", imageString.toString());

            headImage.setImageBitmap(photo);

            // 上传头像服务端
            params.put("img", imageString);


        }
    }

    // 获取头像偏好设置
    private void getBitmapFromSharedPreferences() {

        SharedPreferences sharedPreferences = getSharedPreferences("testSP", Context.MODE_PRIVATE);
        //第一步:取出字符串形式的Bitmap
        if (sharedPreferences.contains("image")) {
            String imageString = sharedPreferences.getString("image", "");
            //第二步:利用Base64将字符串转换为ByteArrayInputStream
            byte[] byteArray = Base64.decode(imageString, Base64.DEFAULT);
            if (byteArray.length == 0) {
                headImage.setImageResource(R.mipmap.wodetoux);
            } else {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
                //第三步:利用ByteArrayInputStream生成Bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
                headImage.setImageBitmap(bitmap);
            }
        } else {
            headImage.setImageResource(R.mipmap.wodetoux);
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {
            // 没有获取 到权限，从新请求，或者关闭app
            Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show();
        }
    }


    private void initViews() {
        myApplication = (MyApplication)getApplication();
        button = (Button) findViewById(R.id.log_out);
        name = (TextView) findViewById(R.id.name);
        llhigh = (LinearLayout) findViewById(R.id.ll_high);
        llAge = (LinearLayout) findViewById(R.id.ll_age);
        llTizong = (LinearLayout) findViewById(R.id.ll_tizong);
        high = (TextView) findViewById(R.id.high);
        tizong = (TextView) findViewById(R.id.tizong);
        age = (TextView) findViewById(R.id.age);
        textView = (TextView) findViewById(R.id.textView);
        headImage = (CircleImageView) findViewById(R.id.head_image);
        xingbie = (ImageView) findViewById(R.id.xingbie);
        headImage.setOnClickListener(this);
        llAge.setOnClickListener(this);
        llTizong.setOnClickListener(this);
        llhigh.setOnClickListener(this);
        name.setOnClickListener(this);
        xingbie.setOnClickListener(this);
        button.setOnClickListener(this);

        oneItem = (MyOneLineView) findViewById(R.id.one_item);
        twoItem = (MyOneLineView) findViewById(R.id.two_item);
        thereItem = (MyOneLineView) findViewById(R.id.there_item);
        fourItem = (MyOneLineView) findViewById(R.id.four_item);
        headImage = (CircleImageView) findViewById(R.id.head_image);
        // 初始化item
        oneItem.initMine(R.mipmap.cen_shezhi, "我的设备", "", true);
        twoItem.initMine(R.mipmap.cen_lianxiren, "我的联系人", "", true);
        thereItem.initMine(R.mipmap.cen_rep, "健康报告", "", true);
        fourItem.initMine(R.mipmap.cen_more, "更多", "", true);
        // 设置监听

        oneItem.setOnClickListener(this);
        twoItem.setOnClickListener(this);
        thereItem.setOnClickListener(this);
        fourItem.setOnClickListener(this);

        dataBaseManager = new DataBaseManager();
        blurredView = (BlurredView) findViewById(R.id.mohu);
        materialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh);
        mScrollView = (ObservableScrollView) findViewById(R.id.id_scrollview);
        // 滚动监听
        mScrollView.setOnScrollListener(new ObservableScrollView.OnScrollChangedListener() {

            @Override
            public void onScrollChanged(int x, int y, int oldX, int oldY) {
                Log.e("wxpsc", " x = " + x + " y = " + y + " oldX = " + oldX + " oldY = " + oldY);
                mScrollerY = y;

                if (Math.abs(mScrollerY) > 100) {
                    blurredView.setBlurredTop(100);
                    mAlpha = 100;
                } else {
                    blurredView.setBlurredTop(mScrollerY / 2);
                    mAlpha = Math.abs(mScrollerY) / 2;
                }
                blurredView.setBlurredLevel(mAlpha);
            }


        });
        //当时缺了个PersonCenter.this
        sp = PersonCenter.this.getSharedPreferences("gerencenter", MODE_PRIVATE);
        editor = sp.edit();
        // 获取全局化对象
        myApp = (MyApplication) getApplication();
        params = new HashMap<>();

        // 判断是否含有   避免 获取空对象 cash
        if (sp.contains("AGE") && sp.contains("HIGH") && sp.contains("SEX") && sp.contains("NAME") && sp.contains("TAG")) {
            Log.i("偏好", sp.getString("AGE", null).toString());
            age.setText((sp.getString("AGE", null).toString()));
            high.setText((sp.getString("HIGH", null).toString()) + "cm");
            tizong.setText((sp.getString("WEIGHT", null).toString()) + "kg");
            textView.setText(sp.getString("SEX", null).toString());
            name.setText(sp.getString("NAME", "蜗行").toString());


            // 存入数据库
             dataBaseManager.saveUser(sp.getString("NAME", null).toString(),sp.getString("AGE", null).toString(),Integer.parseInt(sp.getString("AGE", null).toString()),Integer.parseInt(sp.getString("HIGH", null).toString()),Integer.parseInt(sp.getString("WEIGHT", null).toString()));


            if (sp.getInt("TAG", 0) != 0) {
                xingbie.setImageResource(R.mipmap.girl);
            } else {
                xingbie.setImageResource(R.mipmap.boy);
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("个人中心");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void upload_data(HashMap<String, String> params) {
        params.put("user",myApplication.getName().toString());
        OkNetRequest.postFormRequest(url, params, new OkNetRequest.DataCallBack() {
            @Override
            public void requestSuccess(Response response,String result) throws Exception {
                // 请求成功的回调
                Log.e("同步成功", result.toString());
                Gson gson = new Gson();
                String deal_result = result.toString();
                deal_result = deal_result.replace("连接成功", "");
                Code code = gson.fromJson(deal_result, Code.class);
                int a = Integer.parseInt(code.getCode());
                if (a == 301) {
                    Toast.makeText(PersonCenter.this, "信息同步成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PersonCenter.this, "信息同步失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void progressSuccess(Response response) throws Exception {

            }

            @Override
            public void requestFailure(Request request, IOException e) {
                // 请求失败的回调
                Log.e("同步失败", request.body().toString());

            }
        });

    }
   /* private TextView name;
    private ImageView xingbie;

    private TextView textView;
    private TextView age;
    private TextView tizong;
    private TextView high;*/
private void  LoadLocal(){
    List<UserModle>list=  dataBaseManager.readuserList();
    if (list.size()>0){
        name.setText(list.get(list.size()-1).getName());
        age.setText(list.get(list.size()-1).getAge()+"岁");
        tizong.setText(list.get(list.size()-1).getWeight()+"kg");
        high.setText(list.get(list.size()-1).getHigh()+"cm");
        if (list.get(list.size()-1).getXingbie().equals("女")){
            xingbie.setImageResource(R.mipmap.girl);
            textView.setText("女");
        }
        else {
            xingbie.setImageResource(R.mipmap.boy);
            textView.setText("男");
        }

    }

}

}
