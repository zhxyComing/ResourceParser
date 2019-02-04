package com.app.dixon.resourceparser.func.special.view;

import com.app.dixon.resourceparser.model.SpecialOutline;

import java.util.List;

public interface ISpecialView {

    void showSpecialList(List<SpecialOutline> list);

    void addSpecialList(List<SpecialOutline> list);

    void showFail(String err);

    void showLoading();

    void stopLoading();

    void clearRecommend();
}
