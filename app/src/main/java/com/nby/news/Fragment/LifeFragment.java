package com.nby.news.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nby.news.Adapter.LifeRecyclerViewAdapter;
import com.nby.news.Bean.NewsBean;
import com.nby.news.Interface.IUpdateNewsData;
import com.nby.news.R;
import com.nby.news.model.LifeModel;

import java.util.ArrayList;
import java.util.List;

public class LifeFragment extends Fragment{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private LifeRecyclerViewAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private Context mContext;
    private LifeModel lifeModel;
    private List<NewsBean> newsBeanList = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 5001:
                    adapter = new LifeRecyclerViewAdapter(mContext ,newsBeanList);
                    recyclerView.setAdapter(adapter);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_life_layout,container ,false);
        String mUrl = "";
        for(int i = 1 ; i<= 10; i++){
            mUrl = "http://www.360changshi.com/ys/shipu/";
            if(i!=1){
                mUrl+="list_"+i+".html";
            }
        }
        recyclerView = view.findViewById(R.id.life_recycler);
        manager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration( ) {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 10;
            }
        });
        refreshLayout = view.findViewById(R.id.life_refresh);
        lifeModel = new LifeModel(mContext);
        lifeModel.requestLifeDate(new IUpdateNewsData( ) {
            @Override
            public void update(List<NewsBean> dataList) {
                newsBeanList = dataList;
                mHandler.sendEmptyMessage(5001);
            }
        }, mUrl);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
