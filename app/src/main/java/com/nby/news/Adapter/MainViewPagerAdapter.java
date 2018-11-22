package com.nby.news.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nby.news.Fragment.HomeFragment;
import com.nby.news.Fragment.VideoFragment;
import com.nby.news.Fragment.WodeFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter{
    private Context mContext;
    private final static int PAGE_COUNT = 3;

    public MainViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new VideoFragment();
            case 2:
                return new WodeFragment();
        }
        return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
