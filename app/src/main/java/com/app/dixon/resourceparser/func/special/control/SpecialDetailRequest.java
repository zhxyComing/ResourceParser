package com.app.dixon.resourceparser.func.special.control;

import android.text.TextUtils;

import com.app.dixon.resourceparser.core.pub.parser.Action1;
import com.app.dixon.resourceparser.core.pub.parser.ParseError;
import com.app.dixon.resourceparser.model.SpecialDetail;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpecialDetailRequest extends Action1<List<SpecialDetail>> {

    private static final String HOST = "https://www.menworld.org";

    private String mAddress;
    private Listener mListener;
    private List<SpecialDetail> mList;

    public SpecialDetailRequest(String address, Listener listener) {
        this.mAddress = address;
        this.mListener = listener;
        mList = new ArrayList<>();
    }

    @Override
    public String url() {
        return HOST + mAddress;
    }

    @Override
    public List<SpecialDetail> onParse(Document doc) {
        Elements article = doc.select("div.article");
        SpecialDetail specialDetail = parseArticle(article);
        if (specialDetail != null) {
            mList.add(specialDetail);
        }
        tryReadMore(1);
        return mList;
    }

    public void tryReadMore(int index) {
        Elements articleNext = null;
        try {
            Document document = Jsoup.connect(url().replace(".html", "") + "_" + ++index + ".html").get();
            articleNext = document.select("div.article");
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (articleNext != null && !TextUtils.isEmpty(articleNext.toString()) && !TextUtils.isEmpty(articleNext.text())) {
            SpecialDetail specialDetail = parseArticle(articleNext);
            if (specialDetail != null) {
                mList.add(specialDetail);
            }
            tryReadMore(index);
        }
    }

    private SpecialDetail parseArticle(Elements article) {
        String img = article.select("img").attr("src");
        String text = article.text();
        if (!TextUtils.isEmpty(img) && !TextUtils.isEmpty(text)) {
            SpecialDetail detail = new SpecialDetail();
            detail.setArticle(text);
            detail.setImg(img);
            return detail;
        }
        return null;
    }

    @Override
    public void onResponse(List<SpecialDetail> response) {
        mListener.onSuccess(response);
    }

    @Override
    public void onErrorResponse(ParseError error) {
        mListener.onFail(error.getMsg());
    }

    public interface Listener {
        void onSuccess(List<SpecialDetail> list);

        void onFail(String msg);
    }
}
