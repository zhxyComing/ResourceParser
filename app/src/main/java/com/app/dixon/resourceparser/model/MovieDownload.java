package com.app.dixon.resourceparser.model;

public class MovieDownload {

    private String downloadUrl;

    public MovieDownload(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    @Override
    public String toString() {
        return "downloadUrl : " + downloadUrl;
    }
}
