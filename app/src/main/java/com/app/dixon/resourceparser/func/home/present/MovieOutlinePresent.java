package com.app.dixon.resourceparser.func.home.present;

import com.app.dixon.resourceparser.core.manager.ParserManager;
import com.app.dixon.resourceparser.func.home.control.MovieOutlineRequest;
import com.app.dixon.resourceparser.func.home.model.IMovieOutlineView;
import com.app.dixon.resourceparser.model.MovieOutline;

import java.util.List;

/**
 * Created by dixon.xu on 2019/2/1.
 * <p>
 * 电影梗概页面p层
 */

public class MovieOutlinePresent {

    private IMovieOutlineView mView;

    public MovieOutlinePresent(IMovieOutlineView view) {
        this.mView = view;
    }

    public void loadData() {

        mView.showLoading();

        ParserManager.queue().add(new MovieOutlineRequest(new MovieOutlineRequest.Listener() {
            @Override
            public void onSuccess(List<MovieOutline> list) {
                mView.stopLoading();
                mView.showMovieOutlines(list);
            }

            @Override
            public void onFail(String msg) {
                mView.stopLoading();
                mView.showFail(msg);
            }
        }));
    }
}
