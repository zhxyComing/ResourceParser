package com.app.dixon.resourceparser.model;

public class SpecialOutline {

    private String title;
    private String url;
    private String img;

    public SpecialOutline(String title, String url, String img) {
        this.title = title;
        this.url = url;
        this.img = img;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "title : " + title + "\nurl : " + url + "\nimg : " + img;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SpecialOutline) {
            return ((SpecialOutline) obj).url.equals(this.url);
        }
        return false;
    }
}
