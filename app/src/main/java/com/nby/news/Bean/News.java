package com.nby.news.Bean;

import com.nby.news.Bean.NewsBean;

import java.util.ArrayList;
import java.util.List;

public class News {

    public List<NewsBean> data = new ArrayList<>();

    public List<NewsBean> getData() {
        return data;
    }

    public void setData(List<NewsBean> data) {
        this.data = data;
    }
}
