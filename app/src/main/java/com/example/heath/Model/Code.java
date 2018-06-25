package com.example.heath.Model;

/**
 * Created by 丽丽超可爱 on 2018/4/18.
 */

public class Code {

    private String code;
    private String message;
    private ResultData data;
    public ResultData getData() {
        return data;
    }

    public void setData(ResultData data) {
        this.data = data;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
