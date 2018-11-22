package com.nby.news.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

public class LBTViewPagerAdapter extends PagerAdapter {

    private List<ImageView> images;
    private String[] imgUrls = new String[4];

    private Context mContext;

    public LBTViewPagerAdapter(Context context, List<ImageView> mImages,String[] imgUrls){
        images =  mImages;
        mContext = context;
        this.imgUrls = imgUrls;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(images.get(position));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(images.get(position));
        Glide.with(mContext)
                .load(imgUrls[position])
                .into(images.get(position));
        return images.get(position);
    }
}
