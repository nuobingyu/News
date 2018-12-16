package com.nby.news.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nby.news.Adapter.MilitaryRevHorizontalAdapter;
import com.nby.news.Adapter.MilitaryRevVerticalAdapter;
import com.nby.news.Adapter.MilitaryViewPagerAdapter;
import com.nby.news.Bean.NewsBean;
import com.nby.news.Interface.IUpdateNewsData;
import com.nby.news.model.MilitaryModel;
import com.nby.news.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MilitaryFragment extends Fragment{

    private ViewPager mViewPager;
    private List<View> dots;
    private int currentItem; // 当前位置
    private int oldPosition = 0; // 上次的位置
    private TextView titleTextView;
    private NestedScrollView scrollView;
    private RecyclerView recyclerView_horizontal;
    private RecyclerView recyclerView_vertical;
    private MilitaryRevHorizontalAdapter horizontalAdapter;
    private MilitaryRevVerticalAdapter verticalAdapter;
    private ScheduledExecutorService scheduledExecutorService;
    private Context mContext;
    private MilitaryModel model;
    private MilitaryViewPagerAdapter pagerAdapter;
    private List<NewsBean> pageDataList = new ArrayList<>();
    private List<NewsBean> beanList_jx = new ArrayList<>();
    private List<NewsBean> beanList_yw = new ArrayList<>();
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1001:
                    pagerAdapter = new MilitaryViewPagerAdapter(mContext,pageDataList);
                    mViewPager.setAdapter(pagerAdapter);
                    titleTextView.setText(pageDataList.get(0).getTitle());
                    break;
                case 1002:
                    horizontalAdapter = new MilitaryRevHorizontalAdapter(mContext,beanList_jx);
                    recyclerView_horizontal.setAdapter(horizontalAdapter);
                    break;
                case 1003:
                    verticalAdapter = new MilitaryRevVerticalAdapter(mContext,beanList_yw);
                    recyclerView_vertical.setAdapter(verticalAdapter);
                    break;
                case 1004:
                    mViewPager.setCurrentItem(currentItem);
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(new ViewPageTask(),
                2,
                2,
                TimeUnit.SECONDS);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_military_layout,container ,false);
        titleTextView = view.findViewById(R.id.military_vp_title);
        mViewPager = view.findViewById(R.id.military_viewpager);
        dots = new ArrayList<>();
        dots.add(view.findViewById(R.id.dot_0));
        dots.add(view.findViewById(R.id.dot_1));
        dots.add(view.findViewById(R.id.dot_2));
        model = new MilitaryModel(mContext);
        model.requestViewPagerDate(new IUpdateNewsData( ) {
            @Override
            public void update(List<NewsBean> dataList) {
                if(dataList.size()<3)
                    return;
                pageDataList = dataList;
                mHandler.sendEmptyMessage(1001);
            }
        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener( ) {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                titleTextView.setText(pageDataList.get(position).getTitle());
                dots.get(position).setBackgroundResource(R.drawable.dot_focus);
                dots.get(oldPosition).setBackgroundResource(R.drawable.dot );

                oldPosition = position;
                currentItem = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        recyclerView_horizontal = view.findViewById(R.id.military_recycler_horizontal);
        LinearLayoutManager layoutManager_ht = new LinearLayoutManager(mContext);
        layoutManager_ht.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView_horizontal.setLayoutManager(layoutManager_ht);
        recyclerView_horizontal.addItemDecoration(new RecyclerView.ItemDecoration( ) {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                //super.getItemOffsets(outRect, view, parent, state);
                outRect.left = 10;
            }
        });
        model.requestNews_JX(new IUpdateNewsData( ) {
            @Override
            public void update(List<NewsBean> dataList) {
                beanList_jx = dataList;
                mHandler.sendEmptyMessage(1002);
            }
        });
        recyclerView_vertical = view.findViewById(R.id.military_recycler_vertical);
        LinearLayoutManager layoutManager_vt = new LinearLayoutManager(mContext);
        layoutManager_vt.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_vertical.setLayoutManager(layoutManager_vt);
        model.requestNews_YW(new IUpdateNewsData( ) {
            @Override
            public void update(List<NewsBean> dataList) {
                beanList_yw = dataList;
                mHandler.sendEmptyMessage(1003);
            }
        });
        recyclerView_vertical.setNestedScrollingEnabled(false);
        recyclerView_vertical.addItemDecoration(new RecyclerView.ItemDecoration( ) {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 10;
            }
        });
        scrollView = view.findViewById(R.id.military_scrollView);
        return view;
    }

    class ViewPageTask implements Runnable{
        @Override
        public void run() {
            currentItem = (currentItem +1) % pageDataList.size();
            mHandler.sendEmptyMessage(1004);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy( );
        if(scheduledExecutorService != null){
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
