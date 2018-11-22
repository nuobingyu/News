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
import com.nby.news.I_interface.OnItemClickListener;
import com.nby.news.R;

import java.util.HashSet;
import java.util.List;

public class HotSpotListAdapter extends RecyclerView.Adapter{

    private Context mContext;
    private List<NewsBean> newsBeanList;
    private OnItemClickListener onItemClickListener;

    public HotSpotListAdapter(Context context , List<NewsBean> newsBeanList, OnItemClickListener onItemClickListener ){
        mContext = context;
        this.newsBeanList = newsBeanList;
        this.onItemClickListener = onItemClickListener;

        //去掉无效元素
        for(int i = 0 ; i< this.newsBeanList.size(); i++){
            if(newsBeanList.get(i).title== null || newsBeanList.get(i).getTitle().equals("")){
                newsBeanList.remove(i);
            }
        }
    }

    class ViewHolderNoPic extends RecyclerView.ViewHolder{
        private TextView titleText;
        private TextView timeText;
        private TextView fromText;
        public ViewHolderNoPic(View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.time_hotspot_item_nopic);
            titleText = itemView.findViewById(R.id.title_hotspot_item_nopic) ;
            fromText = itemView.findViewById(R.id.from_hotspot_item_nopic);
        }
    }

    class ViewHolderOnePic extends RecyclerView.ViewHolder{
        private TextView titleText;
        private TextView timeText;
        private TextView fromText;
        private ImageView img;

        public ViewHolderOnePic(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.title_hotspot_item1);
            timeText = itemView.findViewById(R.id.time_hotspot_item1);
            fromText = itemView.findViewById(R.id.from_hotspot_item1);
            img = itemView.findViewById(R.id.img_hotspot_item1);
        }
    }

    class ViewHolderBigPic extends RecyclerView.ViewHolder{
        private TextView titleTextView;
        private TextView fromTextView;
        private TextView timeTextView;
        private ImageView imageView;

        public ViewHolderBigPic(View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.time_hotspot_item_bigpic);
            fromTextView = itemView.findViewById(R.id.from_hotspot_item_bigpic);
            imageView = itemView.findViewById(R.id.image_hotspot_item_bigpic);
            titleTextView =itemView.findViewById(R.id.title_hotspot_item_bigpic);
        }
    }

    //暂时没写布局
    class ViewHolderTwoPic extends RecyclerView.ViewHolder{
        private TextView titleTextView,fromTextView,timeTextView;
        private ImageView img1,img2;

        public ViewHolderTwoPic(View itemView) {
            super(itemView);
        }
    }


    class ViewHolderThreePic extends RecyclerView.ViewHolder{
        private TextView titleTextView,fromTextView,timeTextView;
        private ImageView img1,img2,img3;
        public ViewHolderThreePic(View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.time_hotspot_item_threepic);
            fromTextView = itemView.findViewById(R.id.from_hotspot_item_threepic);
            titleTextView = itemView.findViewById(R.id.title_hotspot_item_threepic);
            img1 = itemView.findViewById(R.id.img_hotspot_item_threepic1);
            img2 = itemView.findViewById(R.id.img_hotspot_item_threepic2);
            img3 = itemView.findViewById(R.id.img_hotspot_item_threepic3);
        }
    }

    class ViewHolderBottomItem extends RecyclerView.ViewHolder{

        public ViewHolderBottomItem(View itemView) {
            super(itemView);

        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch(viewType){
            case 0:
                View view =  LayoutInflater.from(mContext).inflate(R.layout.item_hotspot_list_nopic,parent ,false);
                return new ViewHolderNoPic(view);
            case 1:
                View view1 =  LayoutInflater.from(mContext).inflate(R.layout.item_hotspot_list_onepic,parent ,false);;
                return new ViewHolderOnePic(view1);
            case 3:
                View view3 =  LayoutInflater.from(mContext).inflate(R.layout.item_hotspot_list_threepic ,parent ,false);
                return new ViewHolderThreePic(view3);
            case 4:
                View view4 = LayoutInflater.from(mContext).inflate(R.layout.item_load_more,parent,false);
                return new ViewHolderBottomItem(view4);
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        int tag = newsBeanList.get(position).imgUrls.size();
        switch (tag){
            case 0:
                String title = newsBeanList.get(position).getTitle();
                if(title==null || title.equals("")){
                    return 4;
                }else{
                    return 0;
                }
            case 1: return 1;
            case 2: return 2;
            case 3: return 3;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        NewsBean newsBean = newsBeanList.get(position);
        if(newsBean==null){
            return;
        }
        int imgCount = newsBean.getImgUrls().size();
        View view = holder.itemView;
        if(imgCount==0){
            ViewHolderNoPic viewHolderNoPic = new ViewHolderNoPic(view);
            if(newsBean.getTitle()==null||newsBean.getTitle().equals(""))
                return;
            viewHolderNoPic.titleText.setText(newsBean.getTitle());
            viewHolderNoPic.fromText.setText(newsBean.getFrom());
            viewHolderNoPic.timeText.setText(newsBean.getTime());
        }else if(imgCount == 1){
            ViewHolderOnePic viewHolderOnePic = new ViewHolderOnePic(view);
            viewHolderOnePic.timeText.setText(newsBean.getTime());
            viewHolderOnePic.fromText.setText(newsBean.getFrom());
            viewHolderOnePic.titleText.setText(newsBean.getTitle());
            Glide.with(mContext)
                    .load(newsBean.getImgUrls().get(0))
                    .into(viewHolderOnePic.img);
        }else if(imgCount == 2){
            ViewHolderTwoPic viewHolderTwoPic = new ViewHolderTwoPic(view);

        }else if(imgCount == 3){
            ViewHolderThreePic viewHolderThreePic = new ViewHolderThreePic(view);
            viewHolderThreePic.titleTextView.setText(newsBean.title);
            viewHolderThreePic.fromTextView.setText(newsBean.from);
            viewHolderThreePic.timeTextView.setText(newsBean.time);
            Glide.with(mContext)
                    .load(newsBean.getImgUrls().get(0))
                    .into(viewHolderThreePic.img1);
            Glide.with(mContext)
                    .load(newsBean.getImgUrls().get(1))
                    .into(viewHolderThreePic.img2);
            Glide.with(mContext)
                    .load(newsBean.getImgUrls().get(2))
                    .into(viewHolderThreePic.img3);
        }else{
            ViewHolderBottomItem viewHolderBottomItem = new ViewHolderBottomItem(view);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,position);

            }
        });


    }

    @Override
    public int getItemCount() {
        if(newsBeanList ==null||newsBeanList.size() == 0)
            return 0;
        return newsBeanList.size();
    }
}
