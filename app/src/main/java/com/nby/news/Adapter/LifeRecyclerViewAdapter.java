package com.nby.news.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nby.news.Bean.NewsBean;
import com.nby.news.R;

import java.util.List;

public class LifeRecyclerViewAdapter extends RecyclerView.Adapter<LifeRecyclerViewAdapter.ViewHolder>{

    private Context mContext;
    private List<NewsBean> dataList ;

    public LifeRecyclerViewAdapter(Context context ,List<NewsBean> list){
        mContext = context;
        dataList = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rcv_life,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsBean newsBean = dataList.get(position);
        holder.titleTxt.setText(newsBean.getTitle());
        holder.pTextTxt.setText(newsBean.getpText());
        Glide.with(mContext)
                .load(newsBean.getPic())
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView titleTxt, pTextTxt;
        public ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.title_life);
            pTextTxt = itemView.findViewById(R.id.content_life);
            img = itemView.findViewById(R.id.img_life);
        }
    }
}
