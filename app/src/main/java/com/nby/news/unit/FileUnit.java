package com.nby.news.unit;

import android.content.Context;
import android.util.Log;

import com.nby.news.Bean.NewsBean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FileUnit {

    static String tempFileName = "nby_temp.txt";
    static String dataFileName = "nby_data.txt";
    static HashSet<NewsBean> newsBeanList = new HashSet<>();
    private FileOutputStream os;
    private FileInputStream is;
    private Context mContext;

    public FileUnit(Context context){
        mContext = context;
    }

    public void writeToTempFile(NewsBean newsBean){
        String data = "";
        File file = new File(mContext.getExternalFilesDir(null).getAbsolutePath(),tempFileName);
        File parentFile = file.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        data = newsBean.title+"==nby=="
                +newsBean.url;
        for(int i = 0 ; i <newsBean.imgUrls.size();i++){
            data = data+"==nby=="+newsBean.imgUrls.get(i);
        }
        data += "==end==";
        try {
            os = new FileOutputStream(file,false);
            if(data.equals("==end==")){
                os.close();
                return;
            }
            os.write(data.getBytes("UTF-8"));
            //Log.e("写数据进入内存",data);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace( );
        } catch (IOException e) {
            e.printStackTrace( );
        }
    }

    public HashSet<NewsBean> readFromTempFile(){
        File file = new File(mContext.getExternalFilesDir(null).getAbsolutePath(),tempFileName);
        if(!file.exists()){
            return null;
        }
        try {
            is = new FileInputStream(file);
            int l;
            byte[] b = new byte[2048];
            String s ="";
            while((l =is.read(b))!=-1){
                s += new String(b,0,l);
            }

            String[] newsStrings = s.split("==end==");

            if(newsStrings.length!=0){
                for(int i = 0 ; i< newsStrings.length; i++){
                  //  Log.e("从内存读取到的内容",newsStrings[i]);
                    String[] news = newsStrings[i].split("==nby==");
                    if(news.length>0){
                        NewsBean newsBean = new NewsBean();
                        newsBean.title = news[0];
                        newsBean.url = news[1];
                        for(int j = 2 ; j< news.length ;j++){
                            newsBean.imgUrls.add(news[j]);
                        }
                        newsBeanList.add(newsBean);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace( );
        } catch (IOException e) {
            e.printStackTrace( );
        }

        return newsBeanList;
    }

    public void appendToTempFile(NewsBean newsBean){
        String data = "";
        File file = new File(mContext.getExternalFilesDir(null).getAbsolutePath(),tempFileName);
        File parentFile = file.getParentFile();
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        data = newsBean.title+"==nby==" +newsBean.url;
        for(int i = 0 ; i <newsBean.imgUrls.size();i++){
            data = data +"==nby=="+newsBean.imgUrls.get(i);
        }
        data += "==end==";

        try {
            os = new FileOutputStream(file,true);
            if(data.equals("==end==")){
                os.close();
                return;
            }
            os.write(data.getBytes("UTF-8"));
            //Log.e("追加数据进入内存",data);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace( );
        } catch (IOException e) {
            e.printStackTrace( );
        }
    }

    public void clearAndDelectedFile(){
        File file = new File(mContext.getExternalFilesDir(null).getAbsolutePath(),tempFileName);
        if(file.exists()){
            file.delete();
        }
    }

}
