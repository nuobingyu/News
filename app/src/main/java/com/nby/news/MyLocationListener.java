package com.nby.news;

import android.util.Log;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.nby.news.Bean.AddressBean;
import android.os.*;

public class MyLocationListener extends BDAbstractLocationListener {
    private AddressBean address;
    private Handler mHandler;

    public MyLocationListener(AddressBean addr, Handler handler){
        address = addr;
        mHandler = handler;
    }

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
        if(address != null && address.getAddress()!=null){
            mHandler.sendEmptyMessage(10001);
            Log.e("address",address.getAddress());
        }
    }
}
