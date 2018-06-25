package com.example.heath.Model;

/**
 * Created by SparkklyYS on 2017/7/20.
 * Weather bean包含天气预报的各项信息
 */

public class Weather_model {
    private String temperature;     //温度
    private String Air;
    public String getTemperature() {
        return temperature;
    }
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "Weather_model{" +
                "temperature='" + temperature + '\'' +
                '}';
    }

    public String getAir() {
        return Air;
    }

    public void setAir(String air) {
        Air = air;
    }
}
