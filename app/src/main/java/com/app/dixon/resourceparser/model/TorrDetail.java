package com.app.dixon.resourceparser.model;

public class TorrDetail {

    private String title;
    private String url;
    private boolean isHot;

    public String getUrl() {
        return url;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }

    public boolean isHot() {
        return isHot;
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

    @Override
    public String toString() {
        return "title : " + title + "\nurl : " + url + "\nisHot : " + isHot;
    }
}
