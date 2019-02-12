package com.app.dixon.resourceparser.func.special.view;

import com.app.dixon.resourceparser.model.SpecialOutline;

import java.util.List;

public interface ISpecialSearchResultView {

    void showSpecialList(List<SpecialOutline> list);

    void showFail(String err);

    void showLoading();

    void stopLoading();
}
