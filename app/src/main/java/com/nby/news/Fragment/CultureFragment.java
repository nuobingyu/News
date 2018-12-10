package com.nby.news.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;

import com.nby.news.Adapter.CultureRcvAdapter;
import com.nby.news.Bean.NewsBean;
import com.nby.news.Interface.IUpdateNewsDate;
import com.nby.news.R;
import com.nby.news.model.CultureModel;

import java.util.List;

public class CultureFragment extends Fragment{
    private RecyclerView recyclerView;
    private CultureRcvAdapter rcvAdapter;
    private CultureModel cultureModel;
    private List<NewsBean> newsBeans;
    private Context mContext;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 2001:
                    rcvAdapter = new CultureRcvAdapter(mContext,newsBeans);
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration( ) {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = 10;
                outRect.right = 10;
                outRect.top = 10;
            }
        });
        cultureModel = new CultureModel(mContext);
        cultureModel.requestData(new IUpdateNewsDate( ) {
            @Override
            public void update(List<NewsBean> dataList) {
                newsBeans = dataList;
                mHandler.sendEmptyMessage(2001);
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
