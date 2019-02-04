package com.app.dixon.resourceparser.func.special.control;

import com.app.dixon.resourceparser.core.pub.parser.Action1;
import com.app.dixon.resourceparser.core.pub.parser.ParseError;
import com.app.dixon.resourceparser.model.SpecialOutline;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class SpecialRequest extends Action1<List<SpecialOutline>> {

    private static final String DEFAULT_SPECIAL_URL = "https://www.menworld.org/fanhao/";
    private static final String DEFAULT_SPECIAL_URL_PAGE = "https://www.menworld.org/fanhao/list_326_";
    private int mPage = -1;

    private Listener mListener;

    public SpecialRequest(Listener listener) {
        this.mListener = listener;
    }

    public SpecialRequest(int page, Listener listener) {
        this.mPage = page;
        this.mListener = listener;
    }

    @Override
    public String url() {
        if (mPage <= 0) {
            return DEFAULT_SPECIAL_URL;
        }
        return DEFAULT_SPECIAL_URL_PAGE + mPage + ".html";
    }

    @Override
    public List<SpecialOutline> onParse(Document doc) {
        List<SpecialOutline> specials = new ArrayList<>();
        Elements divs = doc.select("div.lml_div");
        for (int i = 0; i < divs.size(); i++) {
            Element nodeA = divs.get(i).selectFirst("a");
            String title = nodeA.attr("title");
            String url = nodeA.attr("href");
            Element nodeImg = divs.get(i).selectFirst("img");
            String img = nodeImg.attr("src");
            SpecialOutline special = new SpecialOutline(title, url, img);
            specials.add(special);
        }
        return specials;
    }

    @Override
    public void onResponse(List<SpecialOutline> response) {
        mListener.onSuccess(response);
    }

    @Override
    public void onErrorResponse(ParseError error) {
        mListener.onFail(error.getMsg());
    }

    public interface Listener {
        void onSuccess(List<SpecialOutline> list);

        void onFail(String msg);
    }
}
