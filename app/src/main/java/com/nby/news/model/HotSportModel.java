package com.nby.news.model;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.nby.news.Bean.NewsBean;
import com.nby.news.Interface.IUpdateNewsData;
import com.nby.news.StringPool;
import com.nby.news.db.DBHelper;
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
    private DBHelper dbHelper;

    public HotSportModel(Context context){
        mContext = context;
        dbHelper = new DBHelper(mContext,"DBHelper",null,1);
    }

    public void requestDate(IUpdateNewsData iUpdateDate)  {
        List<NewsBean> newsBeanList = new ArrayList<>();
        new Thread(new Runnable(){
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(mUrl).get();
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
                        ContentValues contentValues =new ContentValues();
                        contentValues.put("title",newsBean.title);
                        contentValues.put("url",newsBean.url);
                        dbHelper.getWritableDatabase()
                                .insert("search",null,contentValues);
                        new SharedPreferencesUnit(mContext).putNewsBean(newsBean.title,newsBean);
                    }
                Log.e("hotSportModel",""+newsBeanList);
                iUpdateDate.update(newsBeanList);
            }
        }).start();
    }
}
