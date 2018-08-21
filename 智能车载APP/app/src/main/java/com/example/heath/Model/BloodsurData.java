package com.example.heath.Model;

/**
 * Created by 丽丽超可爱 on 2018/4/15.
 */

/**
 * {
 "code": "0000",
 "msg": "操作成功",
 "phone": {
 "result": "您好,您2月28日11:33检测空腹血糖6.9mmol/L。血糖偏高，合理的饮食结构有助血糖稳定在理想水平不至过高，建议您尽量规律饮食，每天少食多餐，以每日4-6餐为宜，每次定时定量，不过饥过饱，避免血糖过高。",
 "glucoseValue": 6.9,
 "glucoseType": 1,
 "level": 1,
 "testTime": "2016-02-28 11:33:12",
 "describe": "血糖偏高",
 "suggest": "合理的饮食结构有助血糖稳定在理想水平不至过高，建议您尽量规律饮食，每天少食多餐，以每日4-6餐为宜，每次定时定量，不过饥过饱，避免血糖过高。"
 }
 }
 */


public class BloodsurData {

    private String result;
    private String glucoseValue;
    private String glucoseType;
    private String testTime;
    private String describe;
    private String level;
    private String suggest;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getGlucoseValue() {
        return glucoseValue;
    }

    public void setGlucoseValue(String glucoseValue) {
        this.glucoseValue = glucoseValue;
    }

    public String getGlucoseType() {
        return glucoseType;
    }

    public void setGlucoseType(String glucoseType) {
        this.glucoseType = glucoseType;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    @Override
    public String toString() {
        return "BloodsurData{" +
                "result='" + result + '\'' +
                ", glucoseValue='" + glucoseValue + '\'' +
                ", glucoseType='" + glucoseType + '\'' +
                ", testTime='" + testTime + '\'' +
                ", describe='" + describe + '\'' +
                ", level='" + level + '\'' +
                ", suggest='" + suggest + '\'' +
                '}';
    }
}
