package com.nby.news.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nby.news.Adapter.LifeRecyclerViewAdapter;
import com.nby.news.Bean.NewsBean;
import com.nby.news.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.StaggeredGridLayoutManager.*;

public class LifeFragment extends Fragment{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
//    private StaggeredGridLayoutManager manager;
    private LifeRecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private List<NewsBean> newsBeanList = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){

            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_life_layout,container ,false);
        for(int i = 1 ; i<= 10; i++){
            String mUrl = "http://www.360changshi.com/ys/shipu/";
            if(i!=1){
                mUrl+="list_"+i+".html";
            }
            requestData(mUrl);
        }

        recyclerView = view.findViewById(R.id.life_recycler);
//        manager = new StaggeredGridLayoutManager(3, VERTICAL);
        manager = new LinearLayoutManager(getContext());
        adapter = new LifeRecyclerViewAdapter(getContext() ,newsBeanList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        refreshLayout = view.findViewById(R.id.life_refresh);
        return view;
    }

    private void requestData(String mUrl){
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
//                    Log.e("list","pic="+pic+"\n"+"link="+link+"\n"
//                            +"text="+text+"\n"+"title="+title);
                    newsBeanList.add(newsBean);
                }
                getActivity( ).runOnUiThread(new Runnable( ) {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged( );
                    }
                });
            }

        }).start();
    }
}
