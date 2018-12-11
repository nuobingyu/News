package com.nby.news.model;

import android.content.Context;
import android.util.Log;

import com.nby.news.Bean.AddressBean;
import com.nby.news.json.Weather;
import com.nby.news.Interface.IGetWeather;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherModel {

    private Context mContext;
    private IUpdateWeather mIUpdateWeather;

    public WeatherModel(Context context ,IUpdateWeather iUpdateWeather){
        mContext = context;
        mIUpdateWeather = iUpdateWeather;
    }

    public interface IUpdateWeather{
        void updateWeatherDate(Weather weather);
    }

    //进行网络请求，获取天气api返回的数据
    public void requestWeatherAPI(AddressBean mAddress){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://v.juhe.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final IGetWeather request = retrofit.create(IGetWeather.class);
        Call<Weather> call = request.getWeatherData(mAddress.getCity(),"json"
                ,1,"82be4d603e75b19c337b57d30b319951");
        call.enqueue(new Callback<Weather>( ) {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                Log.e("onResponse"," ");
                mIUpdateWeather.updateWeatherDate(response.body());
            }
            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.e("onFailure",""+t.getMessage());
            }
        });
    }
}
