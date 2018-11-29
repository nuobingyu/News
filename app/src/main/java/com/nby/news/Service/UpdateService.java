package com.nby.news.Service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.os.*;

public class UpdateService extends Service{
    static long delayedMills = 60*60*1000;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 10001:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate( );
        //executeLBSListen();
    }

    private void executeLBSListen(){

        new Handler().postDelayed(new Runnable( ) {
            @Override
            public void run() {
                Log.e("service","执行任务.....");
                executeLBSListen();
            }
        },delayedMills);
    }

}
