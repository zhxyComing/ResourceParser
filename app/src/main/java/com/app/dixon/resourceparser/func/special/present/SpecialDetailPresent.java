package com.app.dixon.resourceparser.func.special.present;

import com.app.dixon.resourceparser.core.manager.ParserManager;
import com.app.dixon.resourceparser.func.special.control.SpecialDetailRequest;
import com.app.dixon.resourceparser.func.special.view.ISpecialDetailView;
import com.app.dixon.resourceparser.model.SpecialDetail;

import java.util.List;

public class SpecialDetailPresent {

    private ISpecialDetailView mView;

    public SpecialDetailPresent(ISpecialDetailView view) {
        this.mView = view;
    }

    public void loadData(String address) {
        mView.showLoading();
        ParserManager.queue().add(new SpecialDetailRequest(address, new SpecialDetailRequest.Listener() {
            @Override
            public void onSuccess(List<SpecialDetail> list) {
                mView.stopLoading();
                mView.showList(list);
            }

            @Override
            public void onFail(String msg) {
                mView.stopLoading();
                mView.showFail(msg);
            }
        }));
    }
}
