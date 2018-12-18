package com.nby.news.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.nby.news.Adapter.MainViewPagerAdapter;
import com.nby.news.R;
import com.nby.news.Service.UpdateService;
import com.nby.news.db.DBHelper;
import com.nby.news.unit.FileUnit;

public class MainActivity extends AppCompatActivity{

    private TabLayout tabLayout_bottom;
    private ViewPager viewPager;
    private MainViewPagerAdapter mainViewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBHelper dbHelper = new DBHelper(this,"DBHelper",null,1);
        dbHelper.deleteTable("search");
        dbHelper.deleteTable("video");
        //dbHelper.deleteTable("history");
        if(!dbHelper.IsTableExist("search",dbHelper.getWritableDatabase())){
            dbHelper.createTable("create table search(title text,url text)");
        }
        if(!dbHelper.IsTableExist("video",dbHelper.getWritableDatabase())){
            dbHelper.createTable("create table video(title text,url text)");
        }

        //清空文件内容
        new FileUnit(this).clearAndDelectedFile();
        //开启服务
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
        tabLayout_bottom.getTabAt(0).setCustomView(inflateView(R.layout.tab_menu_home));
        tabLayout_bottom.getTabAt(1).setCustomView(inflateView(R.layout.tab_menu_video));
        tabLayout_bottom.getTabAt(2).setCustomView(inflateView(R.layout.tab_menu_my));
    }

    public View inflateView(int id){
        return LayoutInflater.from(this).inflate(id,null,false);
    }

    public void intentSearchActivity(View view){
        Intent intent = new Intent(MainActivity.this,SearchActivity.class);
        if(viewPager.getCurrentItem()==0){
            intent.putExtra("TableName","search");
        }else if(viewPager.getCurrentItem()==1){
            intent.putExtra("TableName","video");
        }
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
