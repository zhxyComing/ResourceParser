package com.app.dixon.resourceparser.func.movie.recommend.present;

import com.app.dixon.resourceparser.core.manager.ParserManager;
import com.app.dixon.resourceparser.func.movie.recommend.control.DownloadListRequest;
import com.app.dixon.resourceparser.func.movie.recommend.view.IMovieDownloadView;
import com.app.dixon.resourceparser.model.MovieDownload;

import java.util.List;

/**
 * Created by dixon.xu on 2019/2/1.
 * <p>
 * 电影梗概页面p层
 */

public class DownloadListPresent {

    private IMovieDownloadView mView;

    public DownloadListPresent(IMovieDownloadView view) {
        this.mView = view;
    }

    public void loadData(String address) {

        mView.showLoading();

        ParserManager.queue().add(new DownloadListRequest(address, new DownloadListRequest.Listener() {
            @Override
            public void onSuccess(List<MovieDownload> list) {
                mView.stopLoading();
                mView.showMovieDownloadList(list);
            }

            @Override
            public void onFail(String msg) {
                mView.stopLoading();
                mView.showFail(msg);
            }
        }));
    }
}
