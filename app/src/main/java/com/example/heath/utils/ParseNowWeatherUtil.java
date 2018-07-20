package com.example.heath.utils;


import com.example.heath.Model.Weather_model;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by SparklyYS on 2017/7/20.
 * 解析返回的JSON字符串工具类
 * 解析实时天气
 */

        public class ParseNowWeatherUtil {

            /**
             * @param jsonString
             * @return
             * @throws Exception
             */
            public Weather_model getInfomation(String jsonString) throws Exception {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray resultJsonArray = jsonObject.getJSONArray("HeWeather5");
                JSONObject jsonObject1 = resultJsonArray.getJSONObject(0);
                JSONObject now = jsonObject1.getJSONObject("now");
                Weather_model nowWeather = new Weather_model();

                nowWeather.setTemperature(now.getString("tmp"));


                return nowWeather;
            }


        }

