package com.nby.news.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nby.news.Adapter.LBTViewPagerAdapter;
import com.nby.news.Adapter.MillitaryRecyclerViewAdapter;
import com.nby.news.Bean.NewsBean;
import com.nby.news.I_interface.OnItemClickListener;
import com.nby.news.R;
import com.nby.news.StringPool;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MilitaryFragment extends Fragment{

    private ViewPager mViewPager; //轮播图的viewPager
    private List<ImageView> images; //
    private List<View> dots; //
    private int currentItem; // 当前位置
    private int oldPosition = 0; // 上次的位置
    private TextView titleTextView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView.LayoutManager layoutManager;
    private MillitaryRecyclerViewAdapter millitaryRecyclerViewAdapter;
    private LBTViewPagerAdapter adapter;
    private ScheduledExecutorService scheduledExecutorService;
    private String[] titles = new String[3];
    private String[] links = new String[3];
    private String[] imgUrls = new String[3];
    private List<NewsBean> newsBeanList = new ArrayList<>();
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1001:
                    millitaryRecyclerViewAdapter.notifyDataSetChanged();
                    break;
                case 0:
                    mViewPager.setCurrentItem(currentItem);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_millitary_layout,container ,false);
        init_lunbotu(view);
        getLunBoData(StringPool.URL_MILLITARY);
        requestData();
        recyclerView = view.findViewById(R.id.millitary_recycler);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        millitaryRecyclerViewAdapter = new MillitaryRecyclerViewAdapter(getContext( ),
                newsBeanList, new OnItemClickListener( ) {
            @Override
            public void onItemClick(View view, int position) {
                Toast("您点击了："+newsBeanList.get(position).title
                        +"，它的访问链接为："+newsBeanList.get(position).url);
            }
        });
        recyclerView.setAdapter(millitaryRecyclerViewAdapter);
        return view;
    }

    public void Toast(String s){
        Toast.makeText(getContext(),s, Toast.LENGTH_SHORT).show();
    }

    private void requestData(){
        new Thread(new Runnable( ) {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect("http://mil.news.sina.com.cn/roll/index.d.html?cid=57918").get();
                } catch (IOException e) {
                    e.printStackTrace( );
                }
                Elements elements = doc.getElementsByClass("fixList");
                for(Element e :elements){
                    Elements lists = e.getElementsByClass("linkNews");
                    for(Element list: lists){
                        Elements lis = list.getElementsByTag("li");
                        for(Element li : lis){
                            NewsBean newsBean = new NewsBean();
                            String title = li.getElementsByTag("a").text();
                            String link = li.getElementsByTag("a").attr("href");
                            String time = li.getElementsByTag("span").text();
                            time = time.substring(1,time.length()-1);
                            newsBean.title = title;
                            newsBean.url = link;
                            newsBean.time = time;
                            newsBeanList.add(newsBean);
                            Log.e("title_a",title);
                            Log.e("time_a",time);
                            Log.e("link_a",link);
                        }
                    }
                }
                mHandler.sendEmptyMessage(1001);
            }
        }).start();
    }

    public void getLunBoData(String mUrl){
        new Thread(new Runnable( ) {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(mUrl).get();
                } catch (IOException e) {
                    e.printStackTrace( );
                }
                assert doc != null;
                Elements elements = doc.getElementsByClass("react-swipe-container");
                for(Element e : elements){
                    Elements items = e.getElementsByClass("focus-item");
                    int i = 0 ;
                    for(Element item : items){
                        String link = item.attr("href");
                        String imgUrl = item.getElementsByTag("img").attr("src");
                        String title = item.getElementsByTag("h2").text();
                        if(!imgUrl.contains("http:")){
                            imgUrl= "http:"+imgUrl;
                        }
                        Log.e("M_link",link);
                        Log.e("M_imgUrl",imgUrl);
                        Log.e("M_title",title);
                        links[i] = link;
                        titles[i] = title;
                        imgUrls[i] = imgUrl;
                        if(i==2){
                            break;
                        }
                        i++;
                    }
                }
            }
        }).start();
    }

    private void init_lunbotu(View v){
        titleTextView = v.findViewById(R.id.title);
        titleTextView.setText(titles[0]);
        mViewPager = v.findViewById(R.id.viewpager_lbt);

        images = new ArrayList<>();
        for(int i= 0 ; i< imgUrls.length;i++){
            ImageView imageView = new ImageView(getContext());
            images.add(imageView);
        }
        adapter = new LBTViewPagerAdapter(getContext(),images,imgUrls);
        mViewPager.setAdapter(adapter);

        dots = new ArrayList<>();
        dots.add(v.findViewById(R.id.dot_0));
        dots.add(v.findViewById(R.id.dot_1));
        dots.add(v.findViewById(R.id.dot_2));

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener( ) {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                titleTextView.setText(titles[position]);
                dots.get(position).setBackgroundResource(R.drawable.dot_focus);
                dots.get(oldPosition).setBackgroundResource(R.drawable.dot );

                oldPosition = position;
                currentItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    class ViewPageTask implements Runnable{

        @Override
        public void run() {
            currentItem = (currentItem +1) % imgUrls.length;
            mHandler.sendEmptyMessage(0);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(new ViewPageTask(),
                2,
                2,
                TimeUnit.SECONDS);
    }

    @Override
    public void onDestroy() {
        super.onDestroy( );
        if(scheduledExecutorService != null){
            scheduledExecutorService.shutdown();
            scheduledExecutorService = null;
        }
    }
}
