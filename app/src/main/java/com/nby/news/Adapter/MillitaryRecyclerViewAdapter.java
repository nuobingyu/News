package com.nby.news.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nby.news.Bean.NewsBean;
import com.nby.news.I_interface.OnItemClickListener;
import com.nby.news.R;

import java.util.ArrayList;
import java.util.List;

public class MillitaryRecyclerViewAdapter extends
        RecyclerView.Adapter<MillitaryRecyclerViewAdapter.ViewHolder>{

    private List<NewsBean> dataList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public MillitaryRecyclerViewAdapter(Context context, List<NewsBean> list
            ,OnItemClickListener onItemClickListener){
        mContext = context;
        dataList = list;
        mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_millitary,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(holder.itemView, position);
            }
        });
        holder.titleTextView.setText(dataList.get(position).title);
        holder.timeTextView.setText(dataList.get(position).time);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titleTextView;
        private TextView timeTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_millitary);
            timeTextView = itemView.findViewById(R.id.time_millitary);
        }
    }
}
