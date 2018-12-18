package com.nby.news.db;

public class DBDataBean {
    private  String title;
    private String url;
    private String tag;

    public DBDataBean(String title, String tag){
        this.title = title;
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
