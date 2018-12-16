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

public class CultureModel {

    private Context mContext;

    public CultureModel(Context context){
        mContext = context;
    }

    private static String mUrl = StringPool.URL_CULTURE;
    public void requestData(IUpdateNewsData iUpdateDate ){
        new Thread(new Runnable( ) {
            List<NewsBean> dataList = new ArrayList<>();
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(mUrl).get();
                } catch (IOException e) {
                    e.printStackTrace( );
                }
                Elements elements = doc.getElementsByClass("item-list")
                        .get(0)
                        .getElementsByClass("item");
                for(Element e :elements){
                    NewsBean bean = new NewsBean();
                    bean.setTitle(e.getElementsByTag("img").attr("title"));
                    bean.setPic(e.getElementsByTag("img").attr("src"));
                    bean.setpText(e.getElementsByTag("p").text());
                    dataList.add(bean);
                    new SharedPreferencesUnit(mContext).putNewsBean(bean.title,bean);
                }
                iUpdateDate.update(dataList);
            }
        }).start();
    }
}
