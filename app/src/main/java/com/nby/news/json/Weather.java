package com.nby.news.json;

import android.util.Log;
public class Weather {
    public Result result;

    public void show() {
        Log.e("show", result.today.wind + "-"
                + result.today.temperature + "-"
                + result.today.weather);
    }
    public String getCKTemprature(){
        return result.sk.temp;
    }

    public String getCKWind(){
        return result.sk.wind_direction;
    }

    public String getCKWindLevel(){
        return result.sk.wind_strength;
    }

    public String getWind(){
        return result.today.wind;
    }

    public String getTemperature() {
        if(result !=null &&result.today.temperature!=null)
            return result.today.temperature;
        return null;
    }

    public String getWeatherStr() {
        return result.today.weather;
    }
}


