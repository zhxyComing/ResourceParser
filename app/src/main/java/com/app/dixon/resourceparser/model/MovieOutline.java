package com.app.dixon.resourceparser.model;

/**
 * Created by dixon.xu on 2019/1/31.
 * <p>
 * 电影概况
 */

public class MovieOutline {

    private String title;
    private String url;

    public MovieOutline(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "title : " + title + "\nurl : " + url;
    }
}
