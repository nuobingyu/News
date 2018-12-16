package com.nby.news.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nby.news.R;

import java.util.List;

public class HintListAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> titles;

    public HintListAdapter(Context context , List<String> datas){
        mContext = context;
        titles = datas;
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_hintList_item);
        }
    }


    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int position) {
        return titles.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_hintlist,parent,false);
        TextView title = view.findViewById(R.id.title_hintList_item);
        title.setText(titles.get(position));
        return view;
    }



}
