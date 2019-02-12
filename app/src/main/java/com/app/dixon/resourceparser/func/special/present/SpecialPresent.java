package com.app.dixon.resourceparser.func.special.present;

import com.app.dixon.resourceparser.core.manager.ParserManager;
import com.app.dixon.resourceparser.func.special.control.SpecialRequest;
import com.app.dixon.resourceparser.func.special.control.SpecialSearchRequest;
import com.app.dixon.resourceparser.func.special.view.ISpecialView;
import com.app.dixon.resourceparser.model.SpecialOutline;

import java.util.List;

public class SpecialPresent {

    private ISpecialView mView;

    public SpecialPresent(ISpecialView view) {
        this.mView = view;
    }

    public void loadData() {
        mView.showLoading();
        ParserManager.queue().add(new SpecialRequest(new SpecialRequest.Listener() {
            @Override
            public void onSuccess(List<SpecialOutline> list) {
                mView.stopLoading();
                mView.showSpecialList(list);
            }

            @Override
            public void onFail(String msg) {
                mView.stopLoading();
                mView.showFail(msg);
            }
        }));
    }

    public void loadMore(int page) {
        mView.showLoading();
        ParserManager.queue().add(new SpecialRequest(page, new SpecialRequest.Listener() {
            @Override
            public void onSuccess(List<SpecialOutline> list) {
                mView.stopLoading();
                mView.addSpecialList(list);
            }

            @Override
            public void onFail(String msg) {
                mView.stopLoading();
                mView.showFail(msg);
            }
        }));
    }
}
