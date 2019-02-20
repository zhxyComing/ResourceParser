package com.app.dixon.resourceparser.func.movie.search.control;

import com.app.dixon.resourceparser.core.pub.parser.ActionPost1;
import com.app.dixon.resourceparser.core.pub.parser.ParseError;
import com.app.dixon.resourceparser.model.MovieOutline;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchRequest extends ActionPost1<List<MovieOutline>> {

    private static final String SEARCH_URL = "https://www.dy2018.com/e/search/index.php";

    private String mSearchText;
    private Listener mListener;

    public SearchRequest(String searchText, Listener listener) {
        this.mSearchText = searchText;
        this.mListener = listener;
    }

    @Override
    public Map<String, String> postData() {
        Map<String, String> map = new HashMap<>();
        map.put("show", "title,smalltext");
        map.put("keyboard", mSearchText);
        return map;
    }

    @Override
    public String url() {
        return SEARCH_URL;
    }

    @Override
    public List<MovieOutline> onParse(Document doc) {
        List<MovieOutline> movies = new ArrayList<>();
        Elements tables = doc.select("div.co_content8").select("table");
        for (int j = 0; j < tables.size(); j++) {
            Elements nodeA = tables.get(j).select("a");
            String title = nodeA.attr("title");
            String url = nodeA.attr("href");
            MovieOutline movie = new MovieOutline(title, url);
            movies.add(movie);
        }
        return movies;
    }

    @Override
    public void onResponse(List<MovieOutline> response) {
        mListener.onSuccess(response);
    }

    @Override
    public void onErrorResponse(ParseError error) {
        mListener.onFail(error.getMsg());
    }

    @Override
    public String charSet() {
        return "GBK";
    }

    public interface Listener {

        void onSuccess(List<MovieOutline> movies);

        void onFail(String msg);
    }
}
