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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.nby.news.Activity.ShowNewsContentActivity;
import com.nby.news.Interface.OnItemClickListener;
import com.nby.news.R;
import com.nby.news.Bean.VideoBean;
import com.nby.news.Adapter.VideoRecyclerViewAdapter;
import com.nby.news.StringPool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends Fragment implements CompoundButton.OnCheckedChangeListener{

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private VideoRecyclerViewAdapter videoRecyclerViewAdapter;
    private List<VideoBean> videoDataList = new ArrayList<>();
    private List<VideoBean> videoBeanList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private CheckBox checkBox1,checkBox2,checkBox3;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1001:
                    videoBeanList.addAll(videoDataList);
                    videoRecyclerViewAdapter.notifyDataSetChanged();
                    break;
                case 1002:
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_layout,container,false);

        requestDate(StringPool.URL_VIDEO);
        requestVideoUrl();
        swipeRefreshLayout = view.findViewById(R.id.video_swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.video_recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        onItemClickListener = new OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                //点击视频播放操作
                Toast.makeText(getContext(),videoBeanList.get(position).getVideo_link()
                        ,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ShowNewsContentActivity.class);
                intent.putExtra("NewsUrl",videoBeanList.get(position).getVideo_link());
                intent.putExtra("tag","2");
                startActivity(intent);
            }
        };
        videoRecyclerViewAdapter = new VideoRecyclerViewAdapter(videoBeanList ,getContext(),onItemClickListener);
        recyclerView.setAdapter(videoRecyclerViewAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener( ) {
            @Override
            public void onRefresh() {
                // ...
            }
        });
        checkBox1 = view.findViewById(R.id.check1);
        checkBox2 = view.findViewById(R.id.check2);
        checkBox3 = view.findViewById(R.id.check3);
        checkBox1.setOnCheckedChangeListener(this);
        checkBox2.setOnCheckedChangeListener(this);
        checkBox3.setOnCheckedChangeListener(this);
        return view;
    }


    public void sort(){
        int checkedCount = 0;
        int isCheckedIds[] = new int[3];
        if(checkBox1.isChecked()){
            isCheckedIds[checkedCount] = R.id.check1;
            checkedCount++;
        }
        if(checkBox2.isChecked()){
            isCheckedIds[checkedCount] = R.id.check2;
            checkedCount++;
        }
        if(checkBox3.isChecked()){
            isCheckedIds[checkedCount] = R.id.check3;
            checkedCount++;
        }
        videoBeanList.clear();
        switch (checkedCount){
            case 0:
            case 3:
                videoBeanList.addAll(videoDataList);
                videoRecyclerViewAdapter.notifyDataSetChanged();
            case 1:
               addSelectData(isCheckedIds[0]);
                break;
            case 2:
                addSelectData(isCheckedIds[0]);
                addSelectData(isCheckedIds[1]);
                break;
        }


    }

    private void addSelectData(int id){
        switch(id){
            case R.id.check1:
                for(int i = 0 ; i< videoDataList.size() ;i++){
                    VideoBean videoBean = videoDataList.get(i);
                    if(videoBean.getMiaoshu().contains(":")|| videoBean.getMiaoshu().contains("集")){
                        continue;
                    }else{
                        videoBeanList.add(videoBean);
                    }
                }
                break;
            case R.id.check2:
                for(int i = 0 ; i< videoDataList.size() ;i++){
                    VideoBean videoBean = videoDataList.get(i);
                    if(videoBean.getMiaoshu().contains("集")){
                        videoBeanList.add(videoBean);
                    }
                }
                break;
            case R.id.check3:
                for(int i = 0 ; i< videoDataList.size() ;i++){
                    VideoBean videoBean = videoDataList.get(i);
                    if(videoBean.getMiaoshu().contains(":")){
                        videoBeanList.add(videoBean);
                    }
                }
                break;
        }
        videoRecyclerViewAdapter.notifyDataSetChanged();
    }


    public void requestDate(String mUrl){
        new Thread(new Runnable(){
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(mUrl).get();
                } catch (IOException e) {
                    e.printStackTrace( );
                }

                Elements elements = doc.getElementsByClass("mod_bd");
                for(Element e : elements){
                    Elements list_item = e.getElementsByClass("list_item");
                    for(Element item : list_item){
                        VideoBean videoBean = new VideoBean();
                        videoBean.setId(list_item.attr("data-id"));
                       // Log.e("id",videoBean.getId());
                        Elements content = item.getElementsByClass("figure_pic");
                        String title = content.attr("alt");
                        videoBean.setTitle(title);
                      //  Log.e("标题",title);
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
                       // Log.e("图片",src);
                        String miaoShu = item.getElementsByClass("figure_count").text();
                        videoBean.setMiaoshu(miaoShu);
                       // Log.e("观看数",miaoShu);
                        String link = item.getElementsByClass("figure").attr("href");
                        if(link== null ||link.equals("")||link.length()<4)
                            continue;
                        videoBean.setVideo_link(link);
                       // Log.e("link",link);
                        videoDataList.add(videoBean);
                    }
                }

                Elements videos = doc.getElementsByClass("poplayer_quickplay");
                for(Element e : videos){
                    //Log.e("aaa",e.html());
                }
                handler.sendEmptyMessage(1001);
            }
        }).start();
    }

    public void requestVideoUrl(){
        new Thread(new Runnable( ) {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect("https://v.qq.com/x/page/i0765onuty9.html").get();
                } catch (IOException e) {
                    e.printStackTrace( );
                }
                Elements elements = doc.getElementsByClass("site_container container_player");
                for(Element e : elements){
                    Elements item = e.getElementsByClass("mod_player");
                    for(Element content: item){//txp_video_container
//                        Log.e("a",item.html());
                        Elements player = content.getElementsByClass("tenvideo_player");
                        for(Element element: player){
                            Elements source = element.getElementsByClass("txp_video_container");
//                            Log.e("视频源",source.attr("src"));
                        }
//                        Log.e("aaa",player.html());
                    }
                }
            }
        }).start();
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        sort();
    }
}
