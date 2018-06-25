package com.example.heath.Model;

/**
 * Created by 丽丽超可爱 on 2018/3/26.
 */

public class Con_person {
    public String name;
    public String phone;

    public Con_person(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }
    public Con_person(){

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Con_person{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
    
}
