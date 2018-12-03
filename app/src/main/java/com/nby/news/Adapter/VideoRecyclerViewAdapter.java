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
import com.nby.news.Interface.OnItemClickListener;
import com.nby.news.R;
import com.nby.news.Bean.VideoBean;

import java.util.ArrayList;
import java.util.List;

public class VideoRecyclerViewAdapter extends RecyclerView.Adapter<VideoRecyclerViewAdapter.ViewHolder> {
    OnItemClickListener onItemClickListener;
    private List<VideoBean> datas = new ArrayList<>();
    private Context mContext;

    public VideoRecyclerViewAdapter(List<VideoBean> list, Context context, OnItemClickListener onItemClickListener) {
        datas = list;
        mContext = context;
        this.onItemClickListener = onItemClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView more,pinglun,zan,fmt;
        private TextView from,title;

        public ViewHolder(View itemView) {
            super(itemView);
            more = itemView.findViewById(R.id.video_more);
            pinglun = itemView.findViewById(R.id.video_pinglun);
            zan = itemView.findViewById(R.id.video_zan);
            fmt = itemView.findViewById(R.id.video_fmt);
            from = itemView.findViewById(R.id.video_from);
            title = itemView.findViewById(R.id.video_title);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_video, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final View view = holder.itemView;
        holder.title.setText(datas.get(position).getTitle());
        Glide.with(mContext).load(datas.get(position).getImgUrl()).into(holder.fmt);
        String describeStr =datas.get(position).getMiaoshu();

        holder.from.setText(describeStr);

        view.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(view,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}
