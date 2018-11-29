package com.nby.news.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.nby.news.Adapter.MainViewPagerAdapter;
import com.nby.news.R;
import com.nby.news.Service.UpdateService;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout_bottom;
    private ViewPager viewPager;
    private MainViewPagerAdapter mainViewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, UpdateService.class);
        startService(intent);
        //控件初始化
        tabLayout_bottom = findViewById(R.id.bottom_tabLayout);
        viewPager = findViewById(R.id.main_viewpager);
        viewPager.setOffscreenPageLimit(2);
        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(mainViewPagerAdapter);
        tabLayout_bottom.setupWithViewPager(viewPager);
        tabLayout_bottom.setTabMode(TabLayout.MODE_FIXED);
        //控件加载，安排
        tabLayout_bottom.getTabAt(0).setText("主页").setIcon(R.drawable.home_on);
        tabLayout_bottom.getTabAt(1).setText("视频").setIcon(R.drawable.video_off);
        tabLayout_bottom.getTabAt(2).setText("我的").setIcon(R.drawable.wode1_off);

        tabLayout_bottom.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener( ) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()){
                    case 0:
                        tabLayout_bottom.getTabAt(0).setIcon(R.drawable.home_on);
                        tabLayout_bottom.getTabAt(1).setIcon(R.drawable.video_off);
                        tabLayout_bottom.getTabAt(2).setIcon(R.drawable.wode1_off);
                        break;
                    case 1:
                        tabLayout_bottom.getTabAt(0).setIcon(R.drawable.home_off);
                        tabLayout_bottom.getTabAt(1).setIcon(R.drawable.video_on);
                        tabLayout_bottom.getTabAt(2).setIcon(R.drawable.wode1_off);
                        break;
                    case 2:
                        tabLayout_bottom.getTabAt(0).setIcon(R.drawable.home_off);
                        tabLayout_bottom.getTabAt(1).setIcon(R.drawable.video_off);
                        tabLayout_bottom.getTabAt(2).setIcon(R.drawable.wode1_on);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void intentSearchActivity(View view){
        Intent intent = new Intent(MainActivity.this,SearchActivity.class);
        startActivity(intent);
    }


}
