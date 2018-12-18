package com.nby.news.Fragment;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.nby.news.Activity.ShowNewsContentActivity;
import com.nby.news.Interface.OnItemClickListener;
import com.nby.news.R;
import com.nby.news.Bean.VideoBean;
import com.nby.news.Adapter.VideoRecyclerViewAdapter;
import com.nby.news.db.DBHelper;
import com.nby.news.model.VideoModel;

import java.util.ArrayList;
import java.util.List;

public class VideoFragment extends Fragment implements CompoundButton.OnCheckedChangeListener{

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private VideoModel videoModel;
    private Context mContext;
    private VideoRecyclerViewAdapter videoRecyclerViewAdapter;
    private List<VideoBean> videoDataList = new ArrayList<>();
    private List<VideoBean> videoBeanList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private CheckBox checkBox1,checkBox2,checkBox3;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 201:
                    videoRecyclerViewAdapter = new VideoRecyclerViewAdapter(videoBeanList
                            ,mContext,onItemClickListener);
                    recyclerView.setAdapter(videoRecyclerViewAdapter);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_layout,container,false);
        swipeRefreshLayout = view.findViewById(R.id.video_swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.video_recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(manager);
        videoModel = new VideoModel(mContext);
        videoModel.requestVideoDate(new VideoModel.IUpdateVideoModel( ) {
            @Override
            public void upDateVideoDate(List<VideoBean> dataList) {
                videoBeanList.addAll(dataList);
                videoDataList.addAll(dataList);
                mHandler.sendEmptyMessage(201);
                DBHelper dbHelper =new DBHelper(mContext,"DBHelper",null,1);
                dbHelper.queryXTable("video",new String[]{"title","url"},"title","");
            }
        });
        onItemClickListener = new OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                //点击视频播放操作
                Toast.makeText(mContext,videoBeanList.get(position).getVideo_link()
                        ,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), ShowNewsContentActivity.class);
                intent.putExtra("NewsUrl",videoBeanList.get(position).getVideo_link());
                intent.putExtra("tag","2");
                startActivity(intent);
            }
        };
        checkBox1 = view.findViewById(R.id.check1);
        checkBox2 = view.findViewById(R.id.check2);
        checkBox3 = view.findViewById(R.id.check3);
        checkBox1.setOnCheckedChangeListener(this);
        checkBox2.setOnCheckedChangeListener(this);
        checkBox3.setOnCheckedChangeListener(this);
        return view;
    }

    private void addSelectData(int id){
        Log.e("videoList",""+videoDataList.size());
        switch(id){
            case R.id.check1:
                for(int i = 0 ; i< videoDataList.size() ;i++){
                    VideoBean videoBean = videoDataList.get(i);
                    if(!videoBean.getMiaoshu().contains(":")&& !videoBean.getMiaoshu().contains("集")){
                        videoBeanList.add(videoBean);
                    }
                }
                break;
            case R.id.check2:
                for(int i = 0 ; i< videoDataList.size() ;i++){
                    VideoBean videoBean = videoDataList.get(i);
                    if(videoBean.getMiaoshu().contains("集")){
                        videoBeanList.add(videoBean);
                    }
                }
                break;
            case R.id.check3:
                for(int i = 0 ; i< videoDataList.size() ;i++){
                    VideoBean videoBean = videoDataList.get(i);
                    if(videoBean.getMiaoshu().contains(":")){
                        videoBeanList.add(videoBean);
                    }
                }
                break;
        }
        videoRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        videoBeanList.clear();
        if(checkBox1.isChecked()){
            addSelectData(R.id.check1);
        }
        if(checkBox2.isChecked()){
            addSelectData(R.id.check2);
        }
        if(checkBox3.isChecked()){
            addSelectData(R.id.check3);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
