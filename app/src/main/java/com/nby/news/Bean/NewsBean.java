package com.nby.news.Bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewsBean implements Serializable{
    public String url = "";
    public String title = "";
    public String time = "";
    public String from = "";
    public String object = "";
    public String pText = "";
    public String pic = "";
    public List<String> imgUrls = new ArrayList<>();

    public NewsBean(){}

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getpText() {
        return pText;
    }

    public void setpText(String pText) {
        this.pText = pText;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return ""+"url: "+url+"\n"
                +"title: "+title+"\n"
                +""+imgUrls.size()+"\n"
                +"from: "+from+"\n"
                +"time: "+time+"\n";
    }
}
