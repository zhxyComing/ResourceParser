package com.app.dixon.resourceparser.model;

/**
 * Created by dixon.xu on 2019/2/1.
 */

public class MovieDetail {

    private String title;
    private String downloadUrl;
    private String coverImg;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    @Override
    public String toString() {
        return "title : " + title + "\ndownloadUrl : " + downloadUrl + "\ncoverImg : " + coverImg;
    }
}
