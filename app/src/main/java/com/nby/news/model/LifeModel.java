package com.nby.news.model;

import android.content.Context;

import com.nby.news.Bean.NewsBean;
import com.nby.news.Interface.IUpdateNewsData;
import com.nby.news.StringPool;
import com.nby.news.unit.SharedPreferencesUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LifeModel {
    private static String mUrl = StringPool.URL_LIFE;

    private Context mContext;

    public LifeModel(Context context){
        mContext = context;
    }

    public void requestLifeDate(IUpdateNewsData iUpdateDate, String mUrl){
        List<NewsBean> beanList = new ArrayList<>();
        new Thread(new Runnable( ) {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(mUrl).get( );
                } catch (IOException e) {
                    e.printStackTrace( );
                }
                assert doc != null;
                Elements list = doc.getElementsByClass("list");
                for (Element e : list) {
                    NewsBean newsBean = new NewsBean( );
                    String pic = e.getElementsByTag("a")
                            .get(0)
                            .getElementsByTag("img")
                            .attr("src");
                    if(pic.length()<5){
                        continue;
                    }
                    String link = e.getElementsByTag("a").attr("href");
                    String title = e.getElementsByTag("a").attr("title");
                    String text = e.getElementsByTag("p").text( );
                    newsBean.setTitle(title);
                    newsBean.setpText(text);
                    newsBean.setUrl(link);
                    newsBean.setPic(pic);
                    beanList.add(newsBean);
                    new SharedPreferencesUnit(mContext).putNewsBean(newsBean.title,newsBean);
                }
                iUpdateDate.update(beanList);
            }
        }).start();
    }


}
