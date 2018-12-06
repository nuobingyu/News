package com.nby.news.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;

import com.nby.news.Adapter.CultureRcvAdapter;
import com.nby.news.Bean.NewsBean;
import com.nby.news.Interface.IUpdateDate;
import com.nby.news.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CultureFragment extends Fragment{
    private static String mUrl = "http://www.egouz.com/culture/";
    private RecyclerView recyclerView;
    private CultureRcvAdapter rcvAdapter;
    private List<NewsBean> newsBeans;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 2001:
                    rcvAdapter = new CultureRcvAdapter(getContext(),newsBeans);
                    recyclerView.setAdapter(rcvAdapter);
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_culture_layout,container ,false);
        recyclerView = view.findViewById(R.id.rcv_culture);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration( ) {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = 10;
                outRect.right = 10;
                outRect.top = 10;
            }
        });
        requestData(new IUpdateDate( ) {
            @Override
            public void update(List<NewsBean> dataList) {
                newsBeans = dataList;
                mHandler.sendEmptyMessage(2001);
            }
        });
        return view;
    }

    private void requestData(IUpdateDate iUpdateDate){
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
                }
                iUpdateDate.update(dataList);
            }
        }).start();
    }
}
