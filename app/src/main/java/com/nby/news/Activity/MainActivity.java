package com.nby.news.Activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.nby.news.Adapter.MainViewPagerAdapter;
import com.nby.news.R;
import com.nby.news.Service.UpdateNewsService;

import org.jsoup.Jsoup;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout_bottom;
    private ViewPager viewPager;
    private MainViewPagerAdapter mainViewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("aaa","我活了！");
        Intent intent = new Intent(MainActivity.this, UpdateNewsService.class);
        startService(intent);
        isServiceRunning();
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

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.example.MyService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void intentSearchActivity(View view){
        Intent intent = new Intent(MainActivity.this,SearchActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy( );
        Log.e("aaa","我死了！");
    }
}
