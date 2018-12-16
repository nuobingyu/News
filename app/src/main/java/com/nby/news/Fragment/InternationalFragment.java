package com.nby.news.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.nby.news.Adapter.InternationalRcvAdapter;
import com.nby.news.Bean.NewsBean;
import com.nby.news.Interface.IUpdateNewsData;
import com.nby.news.Interface.OnItemClickListener;
import com.nby.news.R;
import com.nby.news.model.InternationalModel;

import java.util.ArrayList;
import java.util.List;
import android.os.*;
import android.widget.Toast;

public class InternationalFragment extends Fragment{

    private List<NewsBean> newsBeanList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private InternationalRcvAdapter adapter;
    private InternationalModel internationalModel;
    private Context mContext;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 4001:
                    adapter = new InternationalRcvAdapter(mContext
                            ,newsBeanList ,new OnItemClickListener(){
                        @Override
                        public void onItemClick(View view, int position) {
                            Toast("跳转页面到："+newsBeanList.get(position).url);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_internation_layout,container ,false);
        internationalModel = new InternationalModel(mContext);
        recyclerView = view.findViewById(R.id.international_recyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext,2);
        recyclerView.setLayoutManager(layoutManager);
        internationalModel.requestData(new IUpdateNewsData() {
            @Override
            public void update(List<NewsBean> dataList) {
                newsBeanList = dataList;
                mHandler.sendEmptyMessage(4001);
            }
        });
        refreshLayout = view.findViewById(R.id.international_refresh);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    public void Toast(String s){
        Toast.makeText(mContext,s,Toast.LENGTH_SHORT).show();
    }
}
