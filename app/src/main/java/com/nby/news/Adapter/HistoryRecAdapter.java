package com.nby.news.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nby.news.R;
import com.nby.news.db.DBDataBean;

import java.util.List;

public class HistoryRecAdapter extends RecyclerView.Adapter<HistoryRecAdapter.ViewHolder>{

    private List<DBDataBean> mHistoryList;
    private Context mContext;
    private ClickListener mClickListener;

    public HistoryRecAdapter (Context context , List<DBDataBean> historyList, ClickListener clickListener){
        mContext = context;
        mHistoryList = historyList;
        mClickListener = clickListener;
    }

    public interface ClickListener{
        void onClick(View v, int position);
        void onLongClick(View v);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_history_title);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_history_rec,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(mHistoryList.get(position).getTitle());
        holder.title.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                mClickListener.onClick(holder.title,position);
            }
        });
        holder.title.setOnLongClickListener(new View.OnLongClickListener( ) {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onLongClick(holder.title);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mHistoryList.size();
    }


}
