package com.nby.news.unit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.nby.news.Bean.NewsBean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

public class SharedPreferencesUnit {

    private Context mContext;

    public SharedPreferencesUnit(Context context){
        mContext = context;
    }

    public void putNewsBean(String key, Object obj){
        @SuppressLint("CommitPrefEdits")
        SharedPreferences.Editor editor =
                mContext.getSharedPreferences("News",Context.MODE_PRIVATE).edit();

        if(obj instanceof Serializable){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(baos);
                oos.writeObject(obj);
            } catch (IOException e) {
                e.printStackTrace( );
            }
            String string64 = new String(Base64.encode(baos.toByteArray(), 0));
            editor.putString(key, string64).apply();
        }
    }

    public Object getNewsBean(String key) {
        Object obj = null;
        try {
            SharedPreferences sp = mContext.getSharedPreferences("News",Context.MODE_PRIVATE);
            Map<String ,NewsBean> map = (Map<String, NewsBean>) sp.getAll();
            for(Map.Entry<String ,NewsBean> entry: map.entrySet()){
                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            }
            String base64 = sp.getString(key, "");

            if (base64.equals("")) {
                return null;
            }
            byte[] base64Bytes = Base64.decode(base64.getBytes(), 1);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

}
