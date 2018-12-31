package com.nby.news.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nby.news.Adapter.HomeViewPagerAdapter;
import com.nby.news.R;

import java.util.Objects;

public class HomeFragment extends Fragment{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Context mContext;
    private HomeViewPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_home_layout,container,false);
       tabLayout = view.findViewById(R.id.tablayout_home);
       viewPager = view.findViewById(R.id.viewpager_home);
       adapter = new HomeViewPagerAdapter(Objects.requireNonNull(getActivity( )).getSupportFragmentManager()
               ,mContext);
       viewPager.setAdapter(adapter);
       tabLayout.setupWithViewPager(viewPager);
       tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

       return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
