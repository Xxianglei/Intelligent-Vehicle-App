package com.example.heath.HttpUtils;

/**
 * Created by 丽丽超可爱 on 2018/4/13.
 */

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * 网络请求工具类
 */
public class OkNetRequest {
    private static OkNetRequest okNetRequest;
    private static OkHttpClient okHttpClient; // OKHttp网络请求
    private Handler mHandler;

    private OkNetRequest() {
        // 初始化okhttp 创建一个OKHttpClient对象，一个app里最好实例化一个此对象
        okHttpClient = new OkHttpClient();
        okHttpClient.newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 单例模式  获取NetRequest实例
     *
     * @return okNetRequest
     */
    private static OkNetRequest getInstance() {
        if (okNetRequest == null) {
            okNetRequest = new OkNetRequest();
        }
        return okNetRequest;
    }

    //-------------对外提供的方法Start--------------------------------

    /**
     * 建立网络框架，获取网络数据，异步get请求（Form）
     *
     * @param url      url
     * @param params   key value
     * @param callBack phone
     */
    public static void getFormRequest(String url, Map<String, String> params, DataCallBack callBack) {
        getInstance().inner_getFormAsync(url, params, callBack);

    }

    public static void alygetFormRequest(String url, Map<String, String> params, DataCallBack callBack, String appcode) {
        getInstance().inner_getFormAsync2(url, params, callBack, appcode);

    }

    /**
     * 建立网络框架，获取网络数据，异步post请求（Form）
     *
     * @param url      url
     * @param params   key value
     * @param callBack phone
     */
    public static void postFormRequest(String url, Map<String, String> params, DataCallBack callBack) {
        getInstance().inner_postFormAsync(url, params, callBack);
    }


    /**
     * 建立网络框架，获取网络数据，异步post请求（json）
     *
     * @param url      url
     * @param params   key value
     * @param callBack phone
     */
    public static void postJsonRequest(String url, Map<String, String> params, DataCallBack callBack) {
        getInstance().inner_postJsonAsync(url, params, callBack);
    }

    /**
     * 建立网络框架，获取网络数据，异步post请求（json）
     *
     * @param url      url
     * @param params   key value
     * @param callBack phone
     * @param callBack phone
     */
    public static void alypostJsonRequest(String url, Map<String, String> params, DataCallBack callBack, String appcode) {
        getInstance().inner_alypostJsonAsync(url, params, callBack, appcode);
    }

    /**
     * ************************  血压         post json(嵌套)
     *
     * @param url
     * @param params
     * @param callBack
     * @param appcode
     */
    public static void alypostJsonRequest2(String url, Map<String, HashMap<String, String>> params, DataCallBack callBack, String appcode) {
        getInstance().inner_alypostJsonAsync2(url, params, callBack, appcode);
    }

    /**
     * ********************** 血糖体重     post form
     *
     * @param url
     * @param params
     * @param callBack
     * @param appcode
     */

    public static void alypostFormRequest(String url, Map<String, String> params, DataCallBack callBack, String appcode) {
        getInstance().alyinner_postFormAsync(url, params, callBack, appcode);
    }
    //-------------对外提供的方法End--------------------------------

    /**
     * 异步get请求（Form），内部实现方法
     *
     * @param url    url
     * @param params key value
     */
    private void inner_getFormAsync(String url, Map<String, String> params, final DataCallBack callBack) {
        if (params == null) {
            params = new HashMap<>();
        }
        // 请求url（baseUrl+参数）
        final String doUrl = urlJoint(url, params);
        // 新建一个请求
        final Request request = new Request.Builder().url(doUrl).build();
        //执行请求获得响应结果
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) { // 请求成功
                    //执行请求成功的操作
                    String result = response.body().string();
                    deliverDataSuccess(response,result, callBack);
                    Log.e("执行请求成功的操作", "请求成功");
                } else {
                    throw new IOException(response + "");
                }
            }
        });
    }

    private void inner_getFormAsync2(String url, Map<String, String> params, final DataCallBack callBack, String appcode) {
        if (params == null) {
            params = new HashMap<>();
        }
        // 请求url（baseUrl+参数）
        final String doUrl = urlJoint(url, params);
        // 新建一个请求
        final Request request = new Request
                .Builder()
                .url(doUrl)
                .addHeader("Authorization", "APPCODE " + appcode)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .build();
        //执行请求获得响应结果
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) { // 请求成功
                    //执行请求成功的操作
                    String result = response.body().string();
                    deliverDataSuccess(response,result, callBack);
                    Log.e("执行请求成功的操作", "请求成功");
                } else {
                    throw new IOException(response + "");
                }
            }
        });
    }

    /**
     * 异步post请求（Form）,内部实现方法
     *
     * @param url      url
     * @param params   params
     * @param callBack callBack
     */
    private void inner_postFormAsync(String url, Map<String, String> params, final DataCallBack callBack) {
        RequestBody requestBody;
        if (params == null) {
            params = new HashMap<>();
        }
        Log.e("登录表单数据-----》", params.toString());
        FormBody.Builder builder = new FormBody.Builder();
        /**
         * 在这对添加的参数进行遍历
         */
        for (Map.Entry<String, String> map : params.entrySet()) {
            String key = map.getKey();
            String value;
            /**
             * 判断值是否是空的
             */
            if (map.getValue() == null) {
                value = "";
            } else {
                value = map.getValue();
            }
            /**
             * 把key和value添加到formbody中
             */
            builder.add(key, value);
        }
        requestBody = builder.build();
        //结果返回
        final Request request = new Request.Builder().url(url).post(requestBody).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) { // 请求成功
                    //执行请求成功的操作
                    String result = response.body().string();
                    deliverDataSuccess(response,result, callBack);
                    deliverProgress(response, callBack);


                } else {
                    throw new IOException(response + "");
                }
            }
        });
    }

    /**
     * *********************** 血糖体重
     *
     * @param url
     * @param params
     * @param callBack
     * @param appcode
     */
    private void alyinner_postFormAsync(String url, Map<String, String> params, final DataCallBack callBack, String appcode) {
        RequestBody requestBody;
        if (params == null) {
            params = new HashMap<>();
        }
        FormBody.Builder builder = new FormBody.Builder();
        /**
         * 在这对添加的参数进行遍历
         */
        for (Map.Entry<String, String> map : params.entrySet()) {
            String key = map.getKey();
            String value;
            /**
             * 判断值是否是空的
             */
            if (map.getValue() == null) {
                value = "";
            } else {
                value = map.getValue();
            }
            /**
             * 把key和value添加到formbody中
             */
            builder.add(key, value);
        }
        requestBody = builder.build();
        //结果返回
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "APPCODE " + appcode)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) { // 请求成功
                    //执行请求成功的操作
                    String result = response.body().string();
                    deliverDataSuccess(response,result, callBack);
                } else {
                    throw new IOException(response + "");
                }
            }
        });
    }


    /**
     * post请求传json
     *
     * @param url      url
     * @param callBack 成功或失败回调
     * @param params   params
     */
    private void inner_postJsonAsync(String url, Map<String, String> params, final DataCallBack callBack) {

        // 将map转换成json,需要引入Gson包
        String mapToJson = new Gson().toJson(params);
        Log.e("JSON", mapToJson);
        final Request request = buildJsonPostRequest(url, mapToJson);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) { // 请求成功
                    //执行请求成功的操作
                    String result = response.body().string();
                    deliverDataSuccess(response,result, callBack);
                } else {
                    throw new IOException(response + "");
                }
            }
        });
    }

    /**
     * post请求传json                     ------------------------ aly
     *
     * @param url      url
     * @param callBack 成功或失败回调
     * @param params   params
     */
    private void inner_alypostJsonAsync(String url, Map<String, String> params, final DataCallBack callBack, String appcode) {

        // 将map转换成json,需要引入Gson包
        String mapToJson = new Gson().toJson(params);
        Log.e("JSON", mapToJson);
        // url json appcode
        final Request request = alybuildJsonPostRequest(url, mapToJson, appcode);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) { // 请求成功
                    //执行请求成功的操作
                    String result = response.body().string();
                    deliverDataSuccess(response,result, callBack);
                } else {
                    throw new IOException(response + "");
                }
            }
        });
    }

    private void inner_alypostJsonAsync2(String url, Map<String, HashMap<String, String>> params, final DataCallBack callBack, String appcode) {

        // 将map转换成json,需要引入Gson包
        String mapToJson = new Gson().toJson(params);
        Log.e("OkHttp___JSON", mapToJson);
        // url json appcode
        final Request request = alybuildJsonPostRequest(url, mapToJson, appcode);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) { // 请求成功
                    //执行请求成功的操作
                    String result = response.body().string();
                    deliverDataSuccess(response,result, callBack);
                } else {
                    throw new IOException(response + "");
                }
            }
        });
    }

    /**
     * Json_POST请求参数
     *
     * @param url  url
     * @param json json
     * @return requestBody
     */

    private Request buildJsonPostRequest(String url, String json) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return new Request.Builder().url(url).post(requestBody).build();
    }

    /**
     * ------------------------------------aly
     *
     * @param url
     * @param json
     * @param appcode
     * @return
     */
    private Request alybuildJsonPostRequest(String url, String json, String appcode) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return new Request.Builder()
                .url(url)
                .addHeader("Authorization", "APPCODE " + appcode)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .post(requestBody)
                .build();
    }


    /**
     * 分发失败的时候调用
     *
     * @param request  request
     * @param e        e
     * @param callBack callBack
     */
    private void deliverDataFailure(final Request request, final IOException e, final DataCallBack callBack) {
        /**
         * 在这里使用异步处理
         */
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.requestFailure(request, e);
                }
            }
        });
    }

    /**
     * 分发成功的时候调用
     *
     * @param result   result
     * @param callBack callBack
     */
    private void deliverDataSuccess(final Response response,final String result, final DataCallBack callBack) {
        /**
         * 在这里使用异步线程处理
         */
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.requestSuccess(response,result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void deliverProgress(final Response response, final DataCallBack callBack) {
        /**
         * 在这里使用异步线程处理
         */
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.progressSuccess(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 数据回调接口
     */
    public interface DataCallBack {

        void requestSuccess(Response response, String result) throws Exception;

        void progressSuccess(Response response) throws Exception;


        void requestFailure(Request request, IOException e);
    }

    /**
     * 拼接url和请求参数
     *
     * @param url    url
     * @param params key value
     * @return String url
     */
    private static String urlJoint(String url, Map<String, String> params) {
        StringBuilder endUrl = new StringBuilder(url);
        boolean isFirst = true;
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            if (isFirst && !url.contains("?")) {
                isFirst = false;
                endUrl.append("?");
            } else {
                endUrl.append("&");
            }
            endUrl.append(entry.getKey());
            endUrl.append("=");
            endUrl.append(entry.getValue());
        }
        return endUrl.toString();
    }
}