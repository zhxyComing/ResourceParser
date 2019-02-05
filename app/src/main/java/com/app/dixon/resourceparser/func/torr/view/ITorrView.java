package com.app.dixon.resourceparser.func.torr.view;

import com.app.dixon.resourceparser.model.TorrDetail;

import java.util.List;

public interface ITorrView {

    void showSearchResult(List<TorrDetail> list);

    void showFail(String err);

    void showLoading();

    void stopLoading();
}
