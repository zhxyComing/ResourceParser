package com.app.dixon.resourceparser.func.torr.control;

import com.app.dixon.resourceparser.core.pub.parser.Action1;
import com.app.dixon.resourceparser.core.pub.parser.ParseError;
import com.app.dixon.resourceparser.model.TorrDetail;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class TorrSearchRequest extends Action1<List<TorrDetail>> {

    private static final String TORR_URL = "http://cntorrentkitty.xyz/tk/";
    private static final String TORR_URL_DEFAULT_PAGE = "/1-0-0.html";

    private String mKeyword;
    private Listener mListener;

    public TorrSearchRequest(String keyword, Listener listener) {
        this.mKeyword = keyword;
        this.mListener = listener;
    }

    @Override
    public String url() {
        return TORR_URL + mKeyword + TORR_URL_DEFAULT_PAGE;
    }

    @Override
    public List<TorrDetail> onParse(Document doc) {
        List<TorrDetail> list = new ArrayList<>();
        Elements ps = doc.select("p.dt");
        for (int i = 0; i < ps.size(); i++) {
            Element nodeP = ps.get(i);
            String title = nodeP.text();
            Element a = nodeP.selectFirst("a");
            String address = "";
            if (a != null) {
                address = a.attr("href");
            }
            boolean isHot = false;
            if (nodeP.select("sup").size() != 0) {
                isHot = true;
            }
            TorrDetail detail = new TorrDetail();
            detail.setTitle(title);
            detail.setUrl(address);
            detail.setHot(isHot);
            list.add(detail);
        }
        return list;
    }

    @Override
    public void onResponse(List<TorrDetail> response) {
        mListener.onSuccess(response);
    }

    @Override
    public void onErrorResponse(ParseError error) {
        mListener.onFail(error.getMsg());
    }

    public interface Listener {
        void onSuccess(List<TorrDetail> list);

        void onFail(String msg);
    }
}
