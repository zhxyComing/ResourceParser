package com.app.dixon.resourceparser.func.torr.control;

import com.app.dixon.resourceparser.core.pub.parser.Action1;
import com.app.dixon.resourceparser.core.pub.parser.ParseError;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TorrParseRequest extends Action1<String> {

    private static final String TORR_DETAIL_URL = "http://cntorrentkitty.xyz"; //获取下载链接需要跳转到二级页面

    private String mAddress;
    private Listener mListener;

    public TorrParseRequest(String address, Listener listener) {
        this.mAddress = address;
        this.mListener = listener;
    }

    @Override
    public String url() {
        return TORR_DETAIL_URL + mAddress;
    }

    @Override
    public String onParse(Document doc) {
        Element p = doc.selectFirst("p.magnet");
        if (p != null) {
            Element a = p.selectFirst("a");
            if (a != null) {
                return a.attr("href");
            }
        }
        return "解析失败";
    }

    @Override
    public void onResponse(String response) {
        mListener.onSuccess(response);
    }

    @Override
    public void onErrorResponse(ParseError error) {
        mListener.onFail(error.getMsg());
    }

    public interface Listener {
        void onSuccess(String url);

        void onFail(String msg);
    }
}
