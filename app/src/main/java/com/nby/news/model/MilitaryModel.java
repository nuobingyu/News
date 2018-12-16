package com.nby.news.model;

import android.content.Context;

import com.nby.news.Bean.NewsBean;
import com.nby.news.Interface.IUpdateNewsData;
import com.nby.news.unit.SharedPreferencesUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MilitaryModel {
    private static String PAGE_DATA_URL = "https://new.qq.com/ch/milite/";
    private static String JX_DATA_URL = "http://mil.huanqiu.com/world/";
    private static String YW_DATA_URL = "http://mil.huanqiu.com/strategysituation/";

    private Context mContext;

    public MilitaryModel(Context context){
        mContext = context;
    }

    public void requestViewPagerDate(IUpdateNewsData iUpdateDate){
        List<NewsBean> list = new ArrayList<>();
        new Thread(new Runnable( ) {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(PAGE_DATA_URL).get();
                } catch (IOException e) {
                    e.printStackTrace( );
                }
                assert doc != null;
                Elements elements = doc.getElementsByClass("react-swipe-container")
                        .get(0)
                        .getElementsByClass("focus-item");
                for(int i = 0 ; i< 3 &&elements.size()>=3; i++){
                    NewsBean newsBean = new NewsBean();
                    Element element = elements.get(i);
                    String imgUrl = element.getElementsByTag("img").attr("src");
                    if(!imgUrl.contains("http:")){
                        imgUrl= "http:"+imgUrl;
                    }
                    newsBean.setPic(imgUrl);
                    newsBean.setUrl(element.attr("href"));
                    newsBean.setTitle(element.getElementsByTag("h2").text());
//                    Log.e("news",newsBean.getTitle());
                    list.add(newsBean);
                }
                iUpdateDate.update(list);
            }
        }).start();
    }

    public void requestNews_JX(IUpdateNewsData iUpdateDate){
        List<NewsBean> list = new ArrayList<>();
        new Thread(new Runnable( ) {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(JX_DATA_URL).get();
                } catch (IOException e) {
                    e.printStackTrace( );
                }
                Elements elements = doc.getElementsByClass("listPicBox")
                        .get(0)
                        .getElementsByClass("item");
                for(Element e: elements){
                    NewsBean newsBean = new NewsBean();
                    newsBean.setUrl(e.getElementsByTag("a").attr("href"));
                    newsBean.setTitle(e.getElementsByTag("a").attr("title"));
                    newsBean.setPic(e.getElementsByTag("img").attr("src"));
                    newsBean.setpText(e.getElementsByTag("h5").text());
                    newsBean.setTime(e.getElementsByTag("h6").text());
                    list.add(newsBean);
                    new SharedPreferencesUnit(mContext).putNewsBean(newsBean.title,newsBean);
                }
                iUpdateDate.update(list);
            }
        }).start();
    }

    public void requestNews_YW(IUpdateNewsData iUpdateDate){
        List<NewsBean> list = new ArrayList<>();
        new Thread(new Runnable( ) {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(YW_DATA_URL).get();
                } catch (IOException e) {
                    e.printStackTrace( );
                }
                Elements elements = doc.getElementsByClass("listPicBox")
                        .get(0)
                        .getElementsByClass("item");
                for(Element e: elements){
                    NewsBean newsBean = new NewsBean();
                    newsBean.setUrl(e.getElementsByTag("a").attr("href"));
                    newsBean.setTitle(e.getElementsByTag("a").attr("title"));
                    newsBean.setPic(e.getElementsByTag("img").attr("src"));
                    newsBean.setpText(e.getElementsByTag("h5").text());
                    newsBean.setTime(e.getElementsByTag("h6").text());
                    list.add(newsBean);
                    new SharedPreferencesUnit(mContext).putNewsBean(newsBean.title,newsBean);
                }
                iUpdateDate.update(list);
            }
        }).start();
    }
}
