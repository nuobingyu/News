package com.nby.news.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.nby.news.Bean.NewsBean;
import com.nby.news.Adapter.HotSpotListAdapter;
import com.nby.news.I_interface.OnItemClickListener;
import com.nby.news.R;
import com.nby.news.Activity.ShowNewsContentActivity;
import com.nby.news.StringPool;
import com.nby.news.unit.FileUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HotSpotFragment extends Fragment{

    private HotSpotListAdapter hotSpotListAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private List<NewsBean> newsList = new ArrayList<>();
    private List<NewsBean> newsBeanList = new ArrayList<>();
    private FileUnit fileUnit;
    static boolean isLoading = false;
    static boolean isRefreshing = false;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1001:
                    if(newsList.size()<19)
                        return;
                    int size = newsBeanList.size();
                    for(int i = size+19 ; i>=size; i--){
                        newsBeanList.add(newsList.get(i));
                    }
                    for(int i = 0 ;i< newsBeanList.size(); i++){
                        NewsBean newsBean = newsBeanList.get(i);
                        fileUnit.appendToTempFile(newsBean);
                    }
                    hotSpotListAdapter.notifyDataSetChanged();
                    break;
                case 1002:
                    hotSpotListAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotspot_layout,container ,false);
        if(newsList==null){
            Log.e("文件没有信息！"," ");
            firstRequest(StringPool.URL_HOTSPOT);
        }else{
            Log.e("这是从文件获取的内容！"," ");
        }
        fileUnit = new FileUnit(getContext());
        recyclerView = view.findViewById(R.id.hotspot_list);
        refreshLayout = view.findViewById(R.id.hotspot_refresh);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        newsList.addAll(fileUnit.readFromTempFile());
        for(int i = 0 ;i< 20 ;i++){
            newsBeanList.add(newsList.get(i));
        }
        hotSpotListAdapter = new HotSpotListAdapter(getContext( ),newsBeanList ,new OnItemClickListener( ) {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity() , ShowNewsContentActivity.class);
                intent.putExtra("NewsUrl",newsBeanList.get(position).getUrl() ); //跳转并传递新闻的Url
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(hotSpotListAdapter);
        hotSpotListAdapter.notifyDataSetChanged();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener( ) {
            @Override
            public void onRefresh() {
                int size = newsBeanList.size();
                if(isLoading|| isRefreshing||newsList.size()< size+9)
                    return;
                isRefreshing = true;
                refreshLayout.setRefreshing(true);
                //加入10条
                for(int i = size+9 ; i>=size; i--){
                    newsBeanList.add(newsList.get(i));
                }
                refreshLayout.setRefreshing(false);
                isRefreshing = false;
                hotSpotListAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    public void firstRequest(String mUrl)  {
        isLoading = true;
        new Thread(new Runnable(){
            @Override
            public void run() {
                Document doc = null;

                try {
                    doc = Jsoup.connect(mUrl).get( );
                }catch (IOException e){
                    e.printStackTrace();
                }
                if (doc != null) {
                    Elements elements = doc.getElementsByClass("container");
                    for (Element element1 : elements) {
                        Elements news = element1.getElementsByAttributeValue("class", "Q-tpList");
                        for (Element e : news) {
                            NewsBean newsBean = new NewsBean();
                            Elements pics = e.getElementsByClass("picto");
                            for (Element pic : pics) {
                                String imgUrl ="";
                                imgUrl = pic.attr("src");
                                if(imgUrl.length()>2){
                                    if(!imgUrl.contains("https://")){
                                        imgUrl = "http:"+imgUrl;
                                    }
                                    Log.e("新闻pic", imgUrl);
                                    newsBean.imgUrls.add(imgUrl);
                                }
                            }
                            Elements links = e.getElementsByClass("linkto");
                            for (Element link : links) {
                                String linkStr =  link.attr("href");
                                newsBean.url = linkStr;
                                Log.e("新闻link（正文链接）",linkStr);
                                String title = link.text();
                                newsBean.title = title;
                                Log.e("标题", title);
                            }
                            newsList.add(newsBean);
                        }
                        Message msg = new Message();
                        msg.what= 1001;
                        handler.sendMessage(msg);
                        isLoading = false;
                    }
                }
            }
        }).start();
    }
}