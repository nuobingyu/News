package com.nby.news.Bean;

public class NewsContent {

    private int tag;
    private String text = "";
    private String picUrl = "";
    private String picText = "";
    private String videoUrl = "";
    private String zhaoYao = "";

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPicText() {
        return picText;
    }

    public void setPicText(String picText) {
        this.picText = picText;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getZhaoYao() {
        return zhaoYao;
    }

    public void setZhaoYao(String zhaoYao) {
        this.zhaoYao = zhaoYao;
    }

    public NewsContent(){}
}
