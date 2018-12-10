package com.nby.news.model;

import android.content.Context;
import android.util.Log;

import com.nby.news.Bean.NewsBean;
import com.nby.news.Interface.IUpdateNewsDate;
import com.nby.news.StringPool;
import com.nby.news.unit.SharedPreferencesUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InternationalModel {
    private static String mUrl = StringPool.URL_INTERNATIONAL;

    private Context mContext;

    public InternationalModel(Context context){
        mContext = context;
    }

    public void requestData(IUpdateNewsDate iUpdateDate){
        List<NewsBean> newsBeanList = new ArrayList<>();
        new Thread(new Runnable( ) {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(mUrl).get();
                    Elements list = doc.getElementById("eData").getElementsByTag("dl");
                    for(Element item :list){
                        NewsBean newsBean = new NewsBean();
                        String title = item.getElementsByTag("h3").text();
                        String link = item.getElementsByTag("a").attr("href");
                        Elements dds = item.getElementsByTag("dd");
                        if(dds.size() >=7) {
                            String imgUrl = dds.get(2).text();
                            String time = dds.get(4).text();
                            String p = dds.get(6).text();
                            newsBean.title = title;
                            newsBean.url = link;
                            newsBean.imgUrls.add(imgUrl);
                            newsBean.time = time;
                            newsBean.pText = p;
                            newsBeanList.add(newsBean);
                            new SharedPreferencesUnit(mContext).putNewsBean(newsBean.title,newsBean);
                        }else{
                            Log.e("小于7"," ");
                        }
                    }
                    iUpdateDate.update(newsBeanList);
                } catch (IOException e) {
                    e.printStackTrace( );
                }
            }
        }).start();
    }
}
