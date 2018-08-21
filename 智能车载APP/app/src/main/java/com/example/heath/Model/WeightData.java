package com.example.heath.Model;

/**
 * Created by 丽丽超可爱 on 2018/4/15.
 */
/*{
        "code": "0000",
        "msg": "操作成功",
        "phone": {
        "result": "您好,您2月28日11:33BMI（体重指数）检测结果：22.5，体重正常。体重过低与免疫力低下、骨质疏松、贫血、抑郁等病症有关；而超重或肥胖又与一系列慢性疾病的发生发展密切相联。可见，体重过低或过高都不利于健康。您目前的体重就刚刚好，请继续保持哦！",
        "bmivalue": 22.5,
        "level": 1,
        "testTime": "2016-02-28 11:33:12",
        "describe": "体重正常",
        "suggest": "体重过低与免疫力低下、骨质疏松、贫血、抑郁等病症有关；而超重或肥胖又与一系列慢性疾病的发生发展密切相联。可见，体重过低或过高都不利于健康。您目前的体重就刚刚好，请继续保持哦！"
        }
        }*/
public class WeightData {
    private String result;
    private String bmivalue;
    private String testTime;
    private String describe;
    private String suggest;
    private float height;
    private float weight;
    private String level;
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getBmivalue() {
        return bmivalue;
    }

    public void setBmivalue(String bmivalue) {
        this.bmivalue = bmivalue;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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
        return "WeightData{" +
                "result='" + result + '\'' +
                ", bmivalue='" + bmivalue + '\'' +
                ", level='" + level + '\'' +
                ", testTime='" + testTime + '\'' +
                ", describe='" + describe + '\'' +
                ", suggest='" + suggest + '\'' +
                '}';
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
