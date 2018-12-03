package com.nby.news.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.nby.news.Fragment.HotSpotFragment;
import com.nby.news.Fragment.InternationalFragment;
import com.nby.news.Fragment.LifeFragment;
import com.nby.news.Fragment.MilitaryFragment;
import com.nby.news.Fragment.MusicFragment;

import java.util.List;

public class HomeViewPagerAdapter extends FragmentPagerAdapter{

    private List<View> list;
    final int PAGE_COUNT = 5;
    private Context mContext;

    private String[] tabTitles = {"热点","军事","国际","社会","生活","生活"};

    public HomeViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new HotSpotFragment();
            case 1:
                return new MilitaryFragment();
            case 2:
                return new InternationalFragment();
            case 3:
                return new MusicFragment();
            case 4:
                return new LifeFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
