package com.nby.news.Interface;

import android.view.View;

import com.nby.news.json.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IGetWeather{

    @GET("weather/index")
    Call<Weather> getWeatherData(@Query("cityname") String cityName
            ,@Query("dtype")String type,@Query("format")int format,@Query("key")String key);
}
