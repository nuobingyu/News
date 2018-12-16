package com.nby.news.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.nby.news.Bean.NewsBean;
import com.nby.news.Adapter.HotSpotListAdapter;
import com.nby.news.Interface.IUpdateNewsData;
import com.nby.news.Interface.OnItemClickListener;
import com.nby.news.R;
import com.nby.news.Activity.ShowNewsContentActivity;
import com.nby.news.model.HotSportModel;

import java.util.ArrayList;
import java.util.List;

public class HotSpotFragment extends Fragment{

    private HotSpotListAdapter hotSpotListAdapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private List<NewsBean> tList = new ArrayList<>();
    private List<NewsBean> newsBeanList = new ArrayList<>();
    private HotSportModel hotSportModel;
    static boolean isLoading = false;
    static boolean isRefreshing = false;
    private Context mContext;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 101:
                    hotSpotListAdapter = new HotSpotListAdapter(mContext,newsBeanList ,new OnItemClickListener( ) {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent = new Intent(getActivity() , ShowNewsContentActivity.class);
                            intent.putExtra("NewsUrl",newsBeanList.get(position).getUrl() ); //跳转并传递新闻的Url
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(hotSpotListAdapter);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotspot_layout,container ,false);
        recyclerView = view.findViewById(R.id.hotspot_list);
        refreshLayout = view.findViewById(R.id.hotspot_refresh);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        hotSportModel = new HotSportModel(mContext);
        isLoading = true;
        hotSportModel.requestDate(new IUpdateNewsData( ) {
            @Override
            public void update(List<NewsBean> dataList) {
                isLoading = false;
                tList = dataList;
                for(int i = 0 ; i< 15&&tList.size()>15 ;i++){
                    newsBeanList.add(i,tList.get(i));
                }
                mHandler.sendEmptyMessage(101);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                int size = newsBeanList.size();
                if(isLoading|| isRefreshing||tList.size()< size+9)
                    return;
                isRefreshing = true;
                refreshLayout.setRefreshing(true);
                //加入10条
                for(int i = size+9 ; i>=size; i--){
                    newsBeanList.add(tList.get(i));
                }
                refreshLayout.setRefreshing(false);
                isRefreshing = false;
                hotSpotListAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}