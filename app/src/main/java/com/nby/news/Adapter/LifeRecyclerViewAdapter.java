package com.nby.news.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.nby.news.Bean.NewsBean;

import java.util.ArrayList;
import java.util.List;

public class LifeRecyclerViewAdapter extends RecyclerView.Adapter<LifeRecyclerViewAdapter.VIewHolder>{

    private Context mContext;
    private List<NewsBean> dataList = new ArrayList<>();

    public LifeRecyclerViewAdapter(Context context ,List<NewsBean> list){
        mContext = context;
        dataList = list;

    }

    @NonNull
    @Override
    public VIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull VIewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class VIewHolder extends RecyclerView.ViewHolder{

        public VIewHolder(View itemView) {
            super(itemView);
        }
    }
}
