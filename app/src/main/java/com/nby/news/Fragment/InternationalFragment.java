package com.nby.news.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.nby.news.Adapter.InternationalRecyclerViewAdapter;
import com.nby.news.Bean.NewsBean;
import com.nby.news.I_interface.OnItemClickListener;
import com.nby.news.R;
import com.nby.news.StringPool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.os.*;
import android.widget.GridLayout;
import android.widget.Toast;

public class InternationalFragment extends Fragment{

    private List<NewsBean> newsBeanList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private InternationalRecyclerViewAdapter adapter;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1001:
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_internation_layout,container ,false);
        requestData(StringPool.URL_INTERNATIONAL);
        recyclerView = view.findViewById(R.id.international_recyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new InternationalRecyclerViewAdapter(getContext() ,newsBeanList
                ,new OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                Toast("跳转页面到："+newsBeanList.get(position).url);
            }
        });
        recyclerView.setAdapter(adapter);
        refreshLayout = view.findViewById(R.id.international_refresh);
        return view;
    }

    private void requestData(String mUrl){
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
                            Log.e("title",title);
                            Log.e("link",link);
                            Log.e("img", imgUrl);
                            Log.e("time", time);
                            Log.e("p", p);
                            newsBeanList.add(newsBean);
                        }else{
                            Log.e("小于7"," ");
                        }
                    }
                    handler.sendEmptyMessage(1001);
                } catch (IOException e) {
                    e.printStackTrace( );
                }
            }
        }).start();
    }

    public void Toast(String s){
        Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
    }
}
