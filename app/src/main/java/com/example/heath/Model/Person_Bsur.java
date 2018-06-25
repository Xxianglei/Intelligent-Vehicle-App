package com.example.heath.Model;

/**
 * Created by 丽丽超可爱 on 2018/4/15.
 */

public class Person_Bsur {
    // 返回分析数据
    private BloodsurData data;
    private String code;
    private String msg;

    public BloodsurData getData() {
        return data;
    }

    public void setData(BloodsurData data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Person_Bsur{" +
                "phone=" + data +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
