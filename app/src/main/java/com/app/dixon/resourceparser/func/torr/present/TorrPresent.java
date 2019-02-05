package com.app.dixon.resourceparser.func.torr.present;

import com.app.dixon.resourceparser.core.manager.ParserManager;
import com.app.dixon.resourceparser.func.torr.control.TorrSearchRequest;
import com.app.dixon.resourceparser.func.torr.view.ITorrView;
import com.app.dixon.resourceparser.model.TorrDetail;

import java.util.List;

public class TorrPresent {

    private ITorrView mView;

    public TorrPresent(ITorrView view) {
        this.mView = view;
    }

    public void search(String keyword) {
        mView.showLoading();
        TorrSearchRequest searchRequest = new TorrSearchRequest(keyword, new TorrSearchRequest.Listener() {
            @Override
            public void onSuccess(List<TorrDetail> movies) {
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
