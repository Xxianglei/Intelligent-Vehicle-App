package com.example.heath.Model;

/**
 * Created by 丽丽超可爱 on 2018/4/15.
 */

/**

 "result": "您好,您2月28日11:33检测血压145/95mmHg：属于1级高血压，控制较不理想。鉴于您还有：高脂血症、糖尿病、冠心病，提示您：请坚持监测血压并清淡饮食、适量运动、远离烟酒、保持好心情等；如服药，还请规律用药。",
 "highValue": 145,
 "lowValue": 95,
 "level":1
 "testTime": "2016-02-28 11:33:12",
 "describe": "属于1级高血压，控制较不理想。",
 "suggest": "鉴于您还有：高脂血症、糖尿病、冠心病，请坚持检测血压并清淡饮食、适量运动、远离烟酒、保持好心情等；如服药，还请规律用药。"
 }
 }
 */
public class BloodpreData {

    private String result;
    private String highValue;
    private String lowValue;
    private String testTime;
    private String describe;
    private String suggest;


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getHighValue() {
        return highValue;
    }

    public void setHighValue(String  highValue) {
        this.highValue = highValue;
    }

    public String  getLowValue() {
        return lowValue;
    }

    public void setLowValue(String  lowValue) {
        this.lowValue = lowValue;
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

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    @Override
    public String toString() {
        return "BloodpreData{" +
                "result='" + result + '\'' +
                ", highValue='" + highValue + '\'' +
                ", lowValue='" + lowValue + '\'' +
                ", testTime='" + testTime + '\'' +
                ", describe='" + describe + '\'' +
                ", suggest='" + suggest + '\'' +
                '}';
    }
}
