package com.app.dixon.resourceparser.func.home.view;

import com.app.dixon.resourceparser.model.MovieDownload;

import java.util.List;

public interface IMovieDownloadView {

    void showMovieDownloadList(List<MovieDownload> list);

    void showFail(String err);

    void showLoading();

    void stopLoading();
}
