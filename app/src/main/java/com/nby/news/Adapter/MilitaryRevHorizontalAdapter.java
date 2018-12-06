package com.nby.news.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nby.news.Bean.NewsBean;
import com.nby.news.R;

import java.util.List;

public class MilitaryRevHorizontalAdapter extends RecyclerView.Adapter{

    private Context mContext;
    private List<NewsBean> dataList;
    private static int DEFAULT_COUNT = 10;

    public MilitaryRevHorizontalAdapter(Context context , List<NewsBean> list){
        mContext = context;
        dataList = list;
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_rcv_ht_title);
            imageView = itemView.findViewById(R.id.item_rev_ht_img);
        }
    }

    static class ViewHolderNoPic extends RecyclerView.ViewHolder{
        public TextView title_no_pic;
        public ViewHolderNoPic(View itemView) {
            super(itemView);
            title_no_pic = itemView.findViewById(R.id.item_no_pic_rcv_ht_title);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(dataList.get(position).getPic().length()>5){
            return 1;
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==1){
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_rev_horizontal,null,false);
            return new ViewHolder(view);
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rev_horizontal_nopic,null,false);
        return new ViewHolderNoPic(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NewsBean bean = dataList.get(position);
        View view = holder.itemView;
        if(holder.getItemViewType() ==1){
            ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.title.setText(bean.getTitle());
            Glide.with(mContext)
                    .load(bean.getPic())
                    .into(viewHolder.imageView);
        }else{
            ViewHolderNoPic holderNoPic = new ViewHolderNoPic(view);
            holderNoPic.title_no_pic.setText(bean.getTitle());
        }

    }

    @Override
    public int getItemCount() {
        return DEFAULT_COUNT;//dataList.size();
    }

}
