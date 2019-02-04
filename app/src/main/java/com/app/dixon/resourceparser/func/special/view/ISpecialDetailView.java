package com.app.dixon.resourceparser.func.special.view;

import com.app.dixon.resourceparser.model.SpecialDetail;

import java.util.List;

public interface ISpecialDetailView {

    void showList(List<SpecialDetail> list);

    void showFail(String err);

    void showLoading();

    void stopLoading();
}
