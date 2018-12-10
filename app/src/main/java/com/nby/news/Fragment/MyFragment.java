package com.nby.news.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.nby.news.Bean.AddressBean;
import com.nby.news.Bean.UserBean;
import com.nby.news.Bean.Weather;
import com.nby.news.R;
import com.nby.news.View.LoginPopWindow;
import com.nby.news.model.WeatherModel;

import java.util.Objects;

public class MyFragment extends Fragment{
    private Context mContext;
    private Button loginButton;
    private AddressBean address;
    private UserBean mUser;
    private FrameLayout frameLayout;
    private TextView weatherToday,weatherCk,userName, AddressText;
    private ImageView userImg;
    private Weather mWeather;
    private WeatherModel weatherModel;
    public LocationClient mLocationClient = null;
    public MyLocationListener myListener = new MyLocationListener();
    public LocationClientOption option = new LocationClientOption();
    private boolean isLogin = false;

    @SuppressLint("SetTextI18n")
    public void reInitMyFragment(View view){
        userName = view.findViewById(R.id.user_name_text);
        userImg = view.findViewById(R.id.user_tx);
        AddressText = view.findViewById(R.id.adress_text);
        weatherToday = view.findViewById(R.id.weather_jr);
        weatherCk = view.findViewById(R.id.weather_ck);
        userName.setText(mUser.getUserName());
        if(mWeather != null && mWeather.getTemperature()!=null){
           updateUi();
        }
    }

    @SuppressLint("SetTextI18n")
    public void updateUi(){
        AddressText.setText(address.getAddress());
        weatherToday.setText(mWeather.getWeatherStr()+" "
                +mWeather.getTemperature()+" "
                +mWeather.getWind());
        weatherCk.setText("此刻 "+mWeather.getCKTemprature()+"℃ "
                + mWeather.getCKWind()+" "
                +mWeather.getCKWindLevel());
    }


    //先执行onCreate再执行onCreateView
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWeather = new Weather();
        address = new AddressBean();

        //检查权限
        if(Build.VERSION.SDK_INT >= 24){
            int check = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
            if(check == PackageManager.PERMISSION_GRANTED){
                initLBS();
            }else{
                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity())
                        ,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},2);
            }
        }
        weatherModel = new WeatherModel(mContext, new WeatherModel.IUpdateWeather() {
            @Override
            public void updateWeatherDate(Weather weather) {
                mWeather = weather;
                if(isLogin){
                    updateUi();
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wode_layout,container ,false);
        frameLayout = view.findViewById(R.id.wode_frame);
        loginButton = view.findViewById(R.id.login_btn_wode);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LoginPopWindow loginPopWindow = new LoginPopWindow(mContext, new LoginPopWindow.ILoginStatusListener( ) {
                    @Override
                    public void onSuccess(UserBean userBean) {
                        mUser = userBean;
                        isLogin = true;
                        if(mUser == null || mUser.getUserName().equals("")){
                            Toast.makeText(mContext,"信息失效，请重新登录！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        frameLayout.removeAllViews();
                        View view = LayoutInflater
                                .from(mContext)
                                .inflate(R.layout.logined_head_wode,null);
                        frameLayout.addView(view);
                        reInitMyFragment(view);
                    }

                    @Override
                    public void onFail(String errorString) {
                        Toast.makeText(mContext,errorString,Toast.LENGTH_SHORT).show();
                    }
                });
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    loginPopWindow.showAsDropDown(view);
                }
            }
        });
        return view;
    }


    private void initLBS(){
        //BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口
        //原有BDLocationListener接口暂时同步保留。具体介绍请参考后文中的说明
        //声明LocationClient类
        mLocationClient = new LocationClient(mContext.getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(10000);
        //可选，设置发起定位请求的间隔，int类型，单位ms,如果设置为0，则代表单次定位，即仅定位一次，默认为0,如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false,使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(true);
        //可选，定位SDK内部是一个service，并放到了独立进程。设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(true);
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5*60*1000);
        //可选，V7.2版本新增能力,如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        mLocationClient.setLocOption(option);
        //把配置好的option设置给LocationClientOption对象

        option.setIsNeedAddress(true);//需要显示定位必须为true,不然回调为null
        mLocationClient.start();
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        //定位成功回调方法
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            address.setAddress(location.getAddrStr());    //获取详细地址信息
            address.setCountry(location.getCountry());    //获取国家
            address.setProvince(location.getProvince());  //获取省份
            address.setCity(location.getCity());          //获取城市
            address.setDistrict(location.getDistrict());  //获取区县
            address.setStreet(location.getStreet());      //获取街道信息

            //address会为null
            if(address != null && address.getAddress()!=null){
                weatherModel.requestWeatherAPI(address);
                mLocationClient.stop();
                Log.e("address",address.getAddress());
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}