package com.example.navigator;

public class Item {
    public String url;
    public String title;
    public String detail;
    public void setTitle(String title){
        this.title=title;
    }
    public void setDetail(String detail){
        this.detail=detail;
    }
    public void setUrl(String url){
        this.url=url;
    }

    public String getDetail() {
        return detail;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
