package com.nby.news.Bean;

import android.widget.ImageView;

import java.io.Serializable;

public class VideoBean implements Serializable{
    private String form;
    private String miaoshu;
    private int pinglunCount;
    private ImageView FMimg;
    private String id;
    private String title; //还是String
    private String video_link;
    private String imgUrl;

    public VideoBean(){}

    public VideoBean(String form, String miaoshu, int pinglunCount, ImageView FMimg, String id
            , String title, String video_link, String imgUrl) {
        this.form = form;
        this.miaoshu = miaoshu;
        this.pinglunCount = pinglunCount;
        this.FMimg = FMimg;
        this.id = id;
        this.title = title;
        this.video_link = video_link;
        this.imgUrl = imgUrl;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getMiaoshu() {
        return miaoshu;
    }

    public void setMiaoshu(String miaoshu) {
        this.miaoshu = miaoshu;
    }

    public int getPinglunCount() {
        return pinglunCount;
    }

    public void setPinglunCount(int pinglunCount) {
        this.pinglunCount = pinglunCount;
    }

    public ImageView getFMimg() {
        return FMimg;
    }

    public void setFMimg(ImageView FMimg) {
        this.FMimg = FMimg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
