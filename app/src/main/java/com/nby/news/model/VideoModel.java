package com.nby.news.model;

import android.content.Context;

import com.nby.news.Bean.VideoBean;
import com.nby.news.StringPool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoModel {
    private static String mUrl = StringPool.URL_VIDEO;

    private Context mContext;

    public VideoModel(Context context){
        mContext = context;
    }

    public interface IUpdateVideoModel{
        void upDateVideoDate(List<VideoBean> dataList);
    }


    public void requestVideoDate(IUpdateVideoModel iUpdateVideoModel){
        List<VideoBean> videoBeanList = new ArrayList<>();
        new Thread(new Runnable(){
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(mUrl).get();
                } catch (IOException e) {
                    e.printStackTrace( );
                }
                assert doc != null;
                Elements elements = doc.getElementsByClass("mod_bd");

                for(Element ele :elements){
                    Elements list = ele.getElementsByClass("list_item");
                    for(Element item : list){
                        VideoBean videoBean = new VideoBean();
                        videoBean.setId(elements.attr("data-id"));
                        Elements content = item.getElementsByClass("figure_pic");
                        String title = content.attr("alt");
                        videoBean.setTitle(title);
                        String src = "";
                        src = content.attr("lz_next");
                        if(src.equals("")){
                            src = content.attr("src");
                        }
                        if(!src.contains("http:")){
                            src="http:"+src;
                        }
                        if(src.equals("")||src.length()<5){
                            continue;
                        }
                        videoBean.setImgUrl(src);
                        String miaoShu = item.getElementsByClass("figure_count").text();
                        if(miaoShu.length()<=0)
                            continue;
                        videoBean.setMiaoshu(miaoShu);
                        String link = item.getElementsByClass("figure").attr("href");
                        if(link== null ||link.equals("")||link.length()<4)
                            continue;
                        videoBean.setVideo_link(link);
                        videoBeanList.add(videoBean);
                        //new SharedPreferencesUnit(mContext).putBean(videoBean.getTitle(),videoBean);
                    }
                }
                iUpdateVideoModel.upDateVideoDate(videoBeanList);
            }
        }).start();
    }
}
