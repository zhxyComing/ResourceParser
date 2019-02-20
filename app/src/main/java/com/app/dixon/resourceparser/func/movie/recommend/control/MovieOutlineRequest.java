package com.app.dixon.resourceparser.func.movie.recommend.control;

import com.app.dixon.resourceparser.core.pub.parser.Action1;
import com.app.dixon.resourceparser.core.pub.parser.ParseError;
import com.app.dixon.resourceparser.model.MovieOutline;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dixon.xu on 2019/1/31.
 * <p>
 * 电影梗概请求 包含对梗概的请求、解析和数据回调
 */

public class MovieOutlineRequest extends Action1<List<MovieOutline>> {

    private static final String URL = "https://www.dy2018.com/";
    private Listener mListener;

    public MovieOutlineRequest(Listener listener) {
        this.mListener = listener;
    }

    public interface Listener {
        void onSuccess(List<MovieOutline> list);

        void onFail(String msg);
    }

    //访问的url
    @Override
    public String url() {
        return URL;
    }

    //成功回调 主线程
    @Override
    public void onResponse(List<MovieOutline> response) {
        if (mListener != null) {
            mListener.onSuccess(response);
        }
    }

    //失败回调 主线程
    @Override
    public void onErrorResponse(ParseError error) {
        if (mListener != null) {
            mListener.onFail(error.getMsg());
        }
    }

    //网络成功后数据的解析 这块很耗时 子线程
    @Override
    public List<MovieOutline> onParse(Document doc) {
        List<MovieOutline> movies = new ArrayList<>();
        Elements divs = doc.select("div.co_content222");//获取div的class为co_content222的元素
        for (int i = 0; i < divs.size(); i++) {
            Elements nodeAList = divs.get(i).select("a");
            for (int j = 0; j < nodeAList.size(); j++) {
                Element nodeA = nodeAList.get(j);
                String title = nodeA.attr("title");
                String url = nodeA.attr("href"); //只能解析对应标签下的属性
                movies.add(new MovieOutline(title, url));
            }
        }
        return movies;
    }
}
