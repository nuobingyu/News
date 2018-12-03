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
import com.nby.news.Interface.OnItemClickListener;
import com.nby.news.R;

import java.util.ArrayList;
import java.util.List;

public class InternationalRecyclerViewAdapter extends
        RecyclerView.Adapter<InternationalRecyclerViewAdapter.ViewHolder>{
    private Context mContext;
    private List<NewsBean> dataList = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public InternationalRecyclerViewAdapter(Context context ,List<NewsBean> list
            ,OnItemClickListener onItemClickListener){
        mContext = context;
        dataList = list;
        mOnItemClickListener = onItemClickListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img ;
        TextView pText,title;
        public ViewHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img_international);
            title = itemView.findViewById(R.id.title_international);
            pText = itemView.findViewById(R.id.pText_international);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_international,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(holder.itemView,position);
            }
        });
        Glide.with(mContext)
                .load(dataList.get(position).imgUrls.get(0))
                .into(holder.img);
        holder.title.setText(dataList.get(position).title);
        holder.pText.setText(dataList.get(position).pText);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
