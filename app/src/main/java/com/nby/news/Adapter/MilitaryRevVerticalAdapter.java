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

public class MilitaryRevVerticalAdapter extends RecyclerView.Adapter<MilitaryRevVerticalAdapter.ViewHolder> {

    private Context mContext;
    private List<NewsBean> dataList;

    public MilitaryRevVerticalAdapter(Context context, List<NewsBean> list) {
        mContext = context;
        dataList = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,text,time;
        ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.rev_vt_item_title);
            text = itemView.findViewById(R.id.rev_vt_item_text);
            img = itemView.findViewById(R.id.rev_vt_item_img);
            time = itemView.findViewById(R.id.rev_vt_item_time);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_rev_vertical,null,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsBean bean = dataList.get(position);
        holder.title.setText(bean.getTitle());
        holder.time.setText(bean.getTime());
        holder.text.setText(bean.getpText());
        if(bean.getPic().length()>5){
            Glide.with(mContext)
                    .load(bean.getPic())
                    .into(holder.img);
        }else{
            holder.img.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return 10;//dataList.size();
    }
}
