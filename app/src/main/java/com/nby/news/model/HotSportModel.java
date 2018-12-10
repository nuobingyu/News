package com.nby.news.model;

import android.content.Context;

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

public class HotSportModel {
    private static String mUrl= StringPool.URL_HOTSPOT;

    private Context mContext;

    public HotSportModel(Context context){
        mContext = context;
    }

    public void requestDate(IUpdateNewsDate iUpdateDate)  {
        List<NewsBean> newsBeanList = new ArrayList<>();
        new Thread(new Runnable(){
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(mUrl).get( );
                }catch (IOException e){
                    e.printStackTrace();
                }
                assert doc!=null;
            Elements elements = doc.getElementsByClass("Q-tpList");
                for (Element e : elements) {
                    NewsBean newsBean = new NewsBean();
                    Elements pics = e.getElementsByClass("picto");
                    for (Element pic : pics) {
                        String imgUrl = pic.attr("src");
                        if (imgUrl != null && imgUrl.length( ) > 2) {
                            if (!imgUrl.contains("https://")) {
                                imgUrl = "http:" + imgUrl;
                            }
                            //  Log.e("新闻pic", imgUrl);
                            newsBean.imgUrls.add(imgUrl);
                        }
                    }
                    Elements link = e.getElementsByClass("linkto");
                    newsBean.setUrl(link.attr("href"));
                    newsBean.setTitle(link.text());
                    //  Log.e("新闻link（正文链接）",linkStr);
                    newsBeanList.add(newsBean);
                    new SharedPreferencesUnit(mContext).putNewsBean(newsBean.title,newsBean);
                    //  fileUnit.appendToTempFile(newsBean);
                }
                iUpdateDate.update(newsBeanList);
            }
        }).start();
    }
}
