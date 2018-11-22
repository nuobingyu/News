package com.nby.news.Service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;


import android.util.Log;

import com.nby.news.Bean.NewsBean;
import com.nby.news.StringPool;
import com.nby.news.unit.FileUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import android.os.*;

public class UpdateNewsService extends Service{
    static long delayedMills = 60*60*1000;
    private FileUnit fileUnit ;
    private List<NewsBean> newsBeanList = new ArrayList<>();
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 201:
                    removeDuplicate(newsBeanList);
                    Log.e("服务器获取个数",newsBeanList.size()+"");
                    for(int i = 0 ; i< newsBeanList.size(); i++){
                        fileUnit.appendToTempFile(newsBeanList.get(i));
                    }
                    break;
            }
        }
    };

    public static List removeDuplicate(List list) {
        HashSet<NewsBean> h = new HashSet<>(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate( );
        fileUnit = new FileUnit(getApplicationContext());
        Log.e("aaa","我活着啊！");
        CS();
        executeNetListen();
    }

    private void CS(){
        new Handler().postDelayed(new Runnable( ) {
            @Override
            public void run() {
                Log.e("Service","我还活着");
                CS();
            }
        },5000);
    }

    private void executeNetListen(){

        new Handler().postDelayed(new Runnable( ) {
            @Override
            public void run() {
                Log.e("service","执行任务.....");
                request_hotSpot(StringPool.URL_HOTSPOT,null );
                executeNetListen();
            }
        },delayedMills);
    }

    @Override
    public void onDestroy() {
        super.onDestroy( );
        Log.e("aaa","我死了a！");
    }

    public void request_hotSpot(String mUrl, Handler handler){
        new Thread(new Runnable( ) {
            @Override
            public void run() {
                try {
                    org.jsoup.nodes.Document doc = Jsoup.connect(mUrl).get( );
                    if (doc != null) {
                        Elements elements = doc.getElementsByClass("container");
                        if (elements != null) {
                            for (Element element1 : elements) {
                                Elements news = element1.getElementsByAttributeValue("class", "Q-tpList");
                                for (Element e : news) {
                                    NewsBean newsBean = new NewsBean();
                                    Elements pics = e.getElementsByClass("picto");
                                    Elements links = e.getElementsByClass("linkto");
                                    for (Element pic : pics) {
                                        String imgUrl = pic.attr("src");
                                        if(imgUrl!=null && imgUrl.length()>2){
                                            if(!imgUrl.contains("https://")){
                                                imgUrl = "http:"+imgUrl;
                                            }
                                            Log.e("新闻pic", imgUrl);
                                            newsBean.imgUrls.add(imgUrl);
                                        }
                                    }
                                    for (Element link : links) {
                                        Log.e("新闻link（正文链接）", link.attr("href"));
                                        newsBean.url = link.attr("href");
                                        Log.e("标题", link.text( ));
                                        newsBean.title = link.text();
                                    }
                                    Log.e("分割线", "------------------------------------------");
                                    newsBeanList.add(newsBean);
                                }
                            }
                            Message msg =Message.obtain();
                            msg.what = 201;
                            mHandler.sendMessage(msg);
                        }
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
