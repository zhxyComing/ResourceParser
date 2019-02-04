package com.app.dixon.resourceparser.func.home.view;

import com.app.dixon.resourceparser.model.MovieOutline;

import java.util.List;

/**
 * Created by dixon.xu on 2019/2/1.
 * <p>
 * 电影梗概View
 */

public interface IMovieOutlineView {

    void showMovieOutlines(List<MovieOutline> list);

    void showFail(String err);

    void showLoading();

    void stopLoading();
}
