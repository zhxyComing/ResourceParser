package com.app.dixon.resourceparser.func.special.present;

import com.app.dixon.resourceparser.core.manager.ParserManager;
import com.app.dixon.resourceparser.func.special.control.SpecialSearchRequest;
import com.app.dixon.resourceparser.func.special.view.ISpecialSearchResultView;
import com.app.dixon.resourceparser.model.SpecialOutline;

import java.util.List;

public class SpecialSearchResultPresent {

    private ISpecialSearchResultView mView;

    public SpecialSearchResultPresent(ISpecialSearchResultView view) {
        this.mView = view;
    }

    public void search(String text) {
        mView.showLoading();
        ParserManager.queue().add(new SpecialSearchRequest(text, new SpecialSearchRequest.Listener() {
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
}
