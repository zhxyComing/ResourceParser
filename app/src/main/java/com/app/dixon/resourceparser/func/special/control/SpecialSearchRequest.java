package com.app.dixon.resourceparser.func.special.control;

import android.text.TextUtils;

import com.app.dixon.resourceparser.core.pub.parser.Action1;
import com.app.dixon.resourceparser.core.pub.parser.ParseError;
import com.app.dixon.resourceparser.model.SpecialOutline;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SpecialSearchRequest extends Action1<List<SpecialOutline>> {

    private static final String SEARCH_URL = "https://www.menworld.org/plus/search.php";

    private Listener mListener;
    private String mSearchText;

    public SpecialSearchRequest(String searchText, Listener listener) {
        this.mSearchText = searchText;
        this.mListener = listener;
    }

    @Override
    public String url() {
        try {
            return SEARCH_URL + "?keyword=" + URLEncoder.encode(mSearchText, "gb2312");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return SEARCH_URL + "?keyword=";
    }

    @Override
    public List<SpecialOutline> onParse(Document doc) {
        List<SpecialOutline> specials = new ArrayList<>();
        Elements divs = doc.select("div.zhb_msg_list").select("dl");
        for (int i = 0; i < divs.size(); i++) {
            Element div = divs.get(i);
            String img = null;
            String url = null;
            String title = null;
            Element nodeImg = div.selectFirst("img");
            if (nodeImg != null) {
                img = nodeImg.attr("src");
            }
            Element nodeUrl = div.selectFirst("a");
            if (nodeImg != null) {
                url = nodeUrl.attr("href");
            }
            Element nodeTitle = div.selectFirst("h1");
            if (nodeTitle != null) {
                title = nodeTitle.text();
            }
            if (!TextUtils.isEmpty(img) && !TextUtils.isEmpty(url) && !TextUtils.isEmpty(title)) {
                SpecialOutline special = new SpecialOutline(title, url, img);
                //有重复现象 暂时用equals去重 后续优化逻辑
                if (!specials.contains(special)) {
                    specials.add(special);
                }
            }

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

    //这个网址是Get请求 Get请求不会对参数进行转码 是原样传递 所以需要手动转为gb2312
    //转gb2312不能直接newString，而要通过URLEncode！
//    @Override
//    public String charSet() {
//        return "GB2312";
//    }
}
