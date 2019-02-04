package com.app.dixon.resourceparser.model;

public class SpecialDetail {

    private String article;
    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    @Override
    public String toString() {
        return "article : " + article + "\nimg : " + img;
    }
}
