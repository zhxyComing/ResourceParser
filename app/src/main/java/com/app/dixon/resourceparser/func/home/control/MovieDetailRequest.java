package com.app.dixon.resourceparser.func.home.control;

import android.util.Log;

import com.app.dixon.resourceparser.core.pub.parser.Action;
import com.app.dixon.resourceparser.model.MovieDetail;

import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dixon.xu on 2019/2/1.
 * <p>
 * 电影下载链接、封面图片解析
 */

public class MovieDetailRequest extends Action<MovieDetail> {

    private static final Map<String, MovieDetail> cache = new HashMap<>();
    private static final String HOST = "Https://www.dy2018.com/";

    private String mTitle;
    private String mAddress;
    private Listener mListener;

    public MovieDetailRequest(String title, String address, Listener listener) {
        this.mTitle = title;
        this.mAddress = address;
        this.mListener = listener;
    }

    @Override
    public String url() {
        return HOST + mAddress;
    }

    @Override
    public void onResponse(MovieDetail response) {
        mListener.onSuccess(response);
    }

    @Override
    public MovieDetail onParse(Document doc) {
        MovieDetail detail = new MovieDetail();
        detail.setTitle(mTitle);
        if (cache.containsKey(mAddress)) {
            MovieDetail cacheDetail = cache.get(mAddress);
            detail.setDownloadUrl(cacheDetail.getDownloadUrl());
            detail.setCoverImg(cacheDetail.getCoverImg());
        } else {
            detail.setDownloadUrl(parseDownloadUrl(doc));
            detail.setCoverImg(parseCoverImg(doc));
            cache.put(mAddress, detail);
        }
        return detail;
    }

    private String parseDownloadUrl(Document doc) {
        String downloadUrl = "";
        try {
            //获取带有style属性的td元素
            downloadUrl = doc.select("td[style]").first().selectFirst("a").text();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return downloadUrl;
    }

    private String parseCoverImg(Document doc) {
        String coverImg = "";
        try {
            //获取带有style属性的td元素
            coverImg = doc.selectFirst("img").attr("src");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coverImg;
    }

    public interface Listener {
        void onSuccess(MovieDetail detail);
    }
}