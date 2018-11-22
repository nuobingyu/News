package com.nby.news.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nby.news.Bean.NewsBean;
import com.nby.news.Bean.NewsContent;
import com.nby.news.R;
import com.nby.news.unit.FileUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import android.os.Message;

public class ShowNewsContentActivity extends Activity{

    private Context mContext = ShowNewsContentActivity.this;
    private NewsBean mNewsBean;
    private TextView titleTextView;
    private TextView fromTextView;
    private TextView dateTextView;
    private LinearLayout news_content_layout;
    private FileUnit fileUnit;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1001:
                    updateUi();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content_layout);

        mNewsBean = new NewsBean();
        fileUnit = new FileUnit(mContext);
        fileUnit.readFromTempFile();
        mNewsBean = (NewsBean) getIntent().getSerializableExtra("newsBean");


        titleTextView = findViewById(R.id.news_title);
        fromTextView = findViewById(R.id.news_content_from);
        dateTextView = findViewById(R.id.news_content_date);
        news_content_layout = findViewById(R.id.news_content_body);
        dateTextView = findViewById(R.id.news_content_date);
        fromTextView = findViewById(R.id.news_content_from);

        titleTextView.setText(mNewsBean.title);

        //请求
        requestAll();
    }

    public void requestAll(){
        Log.e("标题",mNewsBean.title);
        Log.e("url",mNewsBean.url);
        if(mNewsBean.url.contains("/omn/")){
            paQuTenXunNews_CMSN();
        }else if(mNewsBean.url.contains("/a/")){
            paQuTenXunNews_A();
        }else if(mNewsBean.url.contains("/cmsn/")){
            paQuTenXunNews_CMSN();
        }else if(mNewsBean.url.contains("/zt/template/")){

        }
    }


    public void paQuTenXunNews_CMSN(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(mNewsBean.url).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Elements content = doc.getElementsByClass("qq_conent clearfix");
                for(Element items : content) {
                    Elements item = items.getElementsByClass("LEFT");
                    for (Element e : item) {
                        //时间
                        Elements times = e.getElementsByClass("a-src-time");
                        for(Element ele: times){
                            Elements time = ele.getElementsByTag("a");
                            mNewsBean.setTime(time.text());
                            Log.e("时间",time.text());
                        }

                        //导语摘要
                        Elements daoyu = e.getElementsByClass("introduction");
                        NewsContent nc = new NewsContent();
                        nc.setTag(2);
                        nc.setZhaoYao(daoyu.text());
                        mNewsBean.newsContents.add(nc);

                        //图片或者文字
                        Elements TextAndImgs = e.getElementsByClass("one-p");
                        for (Element TextAndImg : TextAndImgs) {
                            Elements imgs = TextAndImg.getElementsByClass("content-picture");
                            String img = imgs.attr("src");
                            NewsContent nc1 = new NewsContent();
                            if (img != null && img.length( ) > 0) {
                                img = "http:" + img;
                                nc1.setTag(0);
                                nc1.setPicUrl(img);
                                mNewsBean.newsContents.add(nc1);
                                Log.e("图片", img);
                            } else {
                                String text = TextAndImg.text( );
                                nc1.setTag(1);
                                nc1.setText(text);
                                mNewsBean.newsContents.add(nc1);
                                Log.e("新闻段", text);
                            }
                        }
                    }
                }
                Message msg = Message.obtain();
                msg.what = 1001;
                handler.sendMessage(msg);
            }
        }).start();
    }


    public void paQuTenXunNews_A(){
        new Thread(new Runnable( ) {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(mNewsBean.url).get();
                } catch (IOException e) {
                    e.printStackTrace( );
                }
                Elements article = doc.getElementsByClass("qq_article");
                for(Element articleContent : article){
                    Elements fromClass = articleContent.getElementsByClass("a_Info");
                    for(Element from :fromClass){
                        Elements fromText = from.getElementsByClass("a_source");
                        Log.e("来自",fromText.text());
                        mNewsBean.from = fromText.text();

                        Elements timeText = from.getElementsByClass("a_time");
                        Log.e("发布时间",timeText.text());
                        mNewsBean.time = timeText.text();
                    }
                }

                Element element = doc.getElementById("Cnt-Main-Article-QQ");
                Elements zhaiYao = element.getElementsByClass( "titdd-Article");
                NewsContent nc2 = new NewsContent();
                nc2.setTag(2);
                nc2.setZhaoYao(zhaiYao.text());
                mNewsBean.newsContents.add(nc2);
                Log.e("摘要",zhaiYao.text());

                Elements imgs = element.getElementsByTag("img");
                for(Element img : imgs){
                    String src = img.attr("src");
                    src = "http:"+src;
                    NewsContent nc3 = new NewsContent();
                    nc3.setTag(0);
                    nc3.setPicUrl(src);
                    mNewsBean.newsContents.add(nc3);
                    Log.e("图片链接",src);
                }
                Elements tuTextElements = element.getElementsByClass("text image_desc");
                for(Element element1 : tuTextElements){
                    Log.e("图片文字",element1.text());
                    NewsContent nc4 = new NewsContent();
                    nc4.setTag(3);
                    nc4.setPicText(element1.text());
                    mNewsBean.newsContents.add(nc4);
                }

                Elements duans = element.getElementsByTag("p");
                for(Element newsContent : duans){
                    Elements newsText = newsContent.getElementsByClass("text");
                    String s = newsText.text();
                    NewsContent nc5 = new NewsContent();
                    nc5.setTag(1);
                    nc5.setText(s);
                    mNewsBean.newsContents.add(nc5);
                    Log.e("新闻段",s);
                }
                Message msg = Message.obtain();
                msg.what = 1001;
                handler.sendMessage(msg);
            }
        }).start();
    }

    public void updateUi(){
        if(!mNewsBean.from.equals("")){
            fromTextView.setText(mNewsBean.from);
        }else{
            fromTextView.setVisibility(View.GONE);
        }
        if(!mNewsBean.time.equals("")){
            dateTextView.setText(mNewsBean.time);
        }else{
            dateTextView.setVisibility(View.GONE);
        }
        for(int i = 0 ; i< mNewsBean.newsContents.size(); i++){
            NewsContent nc = mNewsBean.newsContents.get(i);
            switch (nc.getTag()){
                case 0:
                    ImageView iv = new ImageView(mContext);
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                            , RelativeLayout.LayoutParams.MATCH_PARENT);
                    lp.setMargins(0,10,0,10);
                    lp.addRule(Gravity.CENTER);
                    iv.setLayoutParams(lp);
                    news_content_layout.addView(iv);
                    Glide.with(getApplicationContext())
                            .load(nc.getPicUrl())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(iv);
                    break;
                case 1:
                    TextView tv = new TextView(mContext);
                    tv.setTextSize(16);
                    RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                            , RelativeLayout.LayoutParams.MATCH_PARENT);
                    lp1.setMargins(10,0,10,20);
                    tv.setLayoutParams(lp1);
                    news_content_layout.addView(tv);
                    tv.setText("        "+nc.getText());
                    break;
                case 2:
                    TextView tv1 = new TextView(mContext);
                    tv1.setTextColor(getResources().getColor(R.color.black_color));
                    tv1.setTypeface(Typeface.DEFAULT_BOLD);
                    tv1.setTextSize(16);
                    RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                            , RelativeLayout.LayoutParams.MATCH_PARENT);
                    lp2.setMargins(10,0,10,20);
                    tv1.setLayoutParams(lp2);
                    news_content_layout.addView(tv1);
                    tv1.setText("       "+nc.getZhaoYao());
                    break;
                case 3:
                    TextView tv2 = new TextView(mContext);
                    RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT
                            , RelativeLayout.LayoutParams.MATCH_PARENT);
                    lp3.setMargins(10,0,10,20);
                    tv2.setGravity(Gravity.CENTER_HORIZONTAL);
                    tv2.setLayoutParams(lp3);
                    news_content_layout.addView(tv2);
                    tv2.setText(nc.getPicText());
                    break;
                case 4:
                    //视频链接
                    break;
            }
        }
    }


    @Override
    protected void onDestroy() {

        super.onDestroy( );
    }
}
