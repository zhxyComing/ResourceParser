package com.app.dixon.resourceparser.func.movie.recommend.control;

import android.text.TextUtils;

import com.app.dixon.resourceparser.core.pub.parser.Action1;
import com.app.dixon.resourceparser.core.pub.parser.ParseError;
import com.app.dixon.resourceparser.model.MovieDownload;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dixon.xu on 2019/2/1.
 * <p>
 * 电影下载链接、封面图片解析
 */

public class DownloadListRequest extends Action1<List<MovieDownload>> {

    private static final String HOST = "https://www.dy2018.com/";

    private String mAddress;
    private Listener mListener;

    public DownloadListRequest(String address, Listener listener) {
        this.mAddress = address;
        this.mListener = listener;
    }

    @Override
    public String url() {
        return HOST + mAddress;
    }

    @Override
    public void onResponse(List<MovieDownload> response) {
        mListener.onSuccess(response);
    }

    @Override
    public List<MovieDownload> onParse(Document doc) {
        List<MovieDownload> list = new ArrayList<>();
        Elements tds = doc.select("td[style]");
        for (int i = 0; i < tds.size(); i++) {
            Element nodeA = tds.get(i).selectFirst("a");
            if (nodeA != null) {
                String downloadUrl = nodeA.attr("href");
                if (!TextUtils.isEmpty(downloadUrl)) {
                    list.add(new MovieDownload(downloadUrl));
                }
            }
        }
        return list;
    }

    @Override
    public void onErrorResponse(ParseError error) {
        mListener.onFail(error.getMsg());
    }

    public interface Listener {
        void onSuccess(List<MovieDownload> detail);

        void onFail(String msg);
    }
}
