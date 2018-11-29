package com.nby.news.Bean;

public class UserBean {
    private String userName;
    private String userImgeUrl;
    private String userSex;

    public UserBean(){}

    public UserBean(String userName) {
        this.userName = userName;
    }

    public UserBean(String userName, String userImgeUrl) {
        this.userName = userName;
        this.userImgeUrl = userImgeUrl;
    }

    public UserBean(String userName, String userImgeUrl, String userSex) {
        this.userName = userName;
        this.userImgeUrl = userImgeUrl;
        this.userSex = userSex;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImgeUrl() {
        return userImgeUrl;
    }

    public void setUserImgeUrl(String userImgeUrl) {
        this.userImgeUrl = userImgeUrl;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }
}
