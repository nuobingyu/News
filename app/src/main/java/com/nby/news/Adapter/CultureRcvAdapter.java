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

public class CultureRcvAdapter extends RecyclerView.Adapter<CultureRcvAdapter.ViewHolder>{

    private Context mContext;
    private List<NewsBean> dataList;

    public CultureRcvAdapter(Context context, List<NewsBean> list){
        mContext = context;
        dataList = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,text;
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_culture_title);
            text = itemView.findViewById(R.id.item_culture_text);
            imageView = itemView.findViewById(R.id.item_culture_img);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_culture_rv
                ,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsBean bean = dataList.get(position);
        holder.title.setText(bean.getTitle());
        holder.text.setText(bean.getpText());
        Glide.with(mContext).load(bean.getPic()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return 10;
    }


}
