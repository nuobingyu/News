package com.nby.news.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nby.news.Bean.NewsBean;

import java.util.ArrayList;
import java.util.List;

public class MilitaryViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<NewsBean> dataList;
    private List<View> viewList = new ArrayList<>();
    private ImageView view;

    public MilitaryViewPagerAdapter(Context context , List<NewsBean> list){
        mContext = context;
        dataList = list;
        for(int i = 0 ; i< dataList.size(); i++){
            ImageView view = new ImageView(context);
            view.setId(i);
            viewList.add(i,view);
        }
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        view = (ImageView) viewList.get(position);
        Glide.with(mContext)
                .load(dataList.get(position).getPic())
                .into(view);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(viewList.get(position));
    }
}
