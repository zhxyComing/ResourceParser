package com.app.dixon.resourceparser.func.movie.search.view;

import com.app.dixon.resourceparser.model.MovieOutline;

import java.util.List;

public interface ISearchView {

    void showSearchResult(List<MovieOutline> list);

    void showFail(String err);

    void showLoading();

    void stopLoading();

    //通过搜索跳转到隐藏页面
    boolean tryStartHidePage(String search);
}
