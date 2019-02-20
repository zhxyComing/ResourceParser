package com.app.dixon.resourceparser.func.movie.search.present;

import com.app.dixon.resourceparser.core.manager.ParserManager;
import com.app.dixon.resourceparser.func.movie.search.control.SearchRequest;
import com.app.dixon.resourceparser.func.movie.search.view.ISearchView;
import com.app.dixon.resourceparser.model.MovieOutline;

import java.util.List;

public class SearchPresent {

    private ISearchView mView;

    public SearchPresent(ISearchView view) {
        this.mView = view;
    }

    public void search(String text) {
        if (mView.tryStartHidePage(text)) {
            //如果符合跳转逻辑 就不再执行搜索
            return;
        }
        mView.showLoading();
        SearchRequest searchRequest = new SearchRequest(text, new SearchRequest.Listener() {
            @Override
            public void onSuccess(List<MovieOutline> movies) {
                mView.stopLoading();
                mView.showSearchResult(movies);
            }

            @Override
            public void onFail(String msg) {
                mView.stopLoading();
                mView.showFail(msg);
            }
        });
        ParserManager.queue().add(searchRequest);
    }
}
