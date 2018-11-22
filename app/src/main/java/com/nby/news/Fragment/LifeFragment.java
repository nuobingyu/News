package com.nby.news.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nby.news.Adapter.LifeRecyclerViewAdapter;
import com.nby.news.Bean.NewsBean;
import com.nby.news.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.StaggeredGridLayoutManager.*;

public class LifeFragment extends Fragment{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private StaggeredGridLayoutManager manager;
    private LifeRecyclerViewAdapter adapter;
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
        recyclerView = view.findViewById(R.id.life_recycler);
        manager = new StaggeredGridLayoutManager(3, VERTICAL);
        adapter = new LifeRecyclerViewAdapter(getContext() ,newsBeanList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        refreshLayout = view.findViewById(R.id.life_refresh);

        return view;
    }

    private void requestData(){
        new Thread(new Runnable( ) {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect("").get();

                } catch (IOException e) {
                    e.printStackTrace( );
                }
            }
        }).start();
    }
}
