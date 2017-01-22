package com.namofo.radio.presenter.meizhi;

import com.namofo.radio.entity.MeiZhi;
import com.namofo.radio.model.MeiZhiModel;
import com.namofo.radio.presenter.BasePresenter;
import com.namofo.radio.view.MeiZhiView;

import java.util.List;

import rx.functions.Action1;

/**
 * Title: MeiZhiPresenter
 * Description: View和Model的桥梁，它从Model层检索数据后，返回给View层
 * CreateTime:17/1/21  14:21
 *
 * @author 郑炯
 * @version 1.0
 */
public class MeiZhiPresenter extends BasePresenter<MeiZhiView> implements IMeiZhiPresenter {
    private MeiZhiView mMeiZhiView;
    private MeiZhiModel mMeiZhiModel;
    private int page = 1;

    public MeiZhiPresenter(MeiZhiView meiZhiView) {
        attachView(meiZhiView);
        mMeiZhiModel = new MeiZhiModel(this);
    }

    @Override
    public void refresh(){
        page = 1;
        mMeiZhiView.showProgress();
        mMeiZhiModel.loadData(page);
    }

    @Override
    public void loadMore() {
        page++;
        mMeiZhiView.showProgress();
        mMeiZhiModel.loadData(page);
    }

    @Override
    public void attachView(MeiZhiView view) {
        mMeiZhiView = view;
    }

    @Override
    public void detachView() {
        mMeiZhiView = null;
    }

    @Override
    public void loadSuccess(List<MeiZhi> list) {
        mMeiZhiView.addData(list);
    }

    @Override
    public void loadFailure() {
        mMeiZhiView.loadFailure();
    }

    @Override
    public void loadComplete() {
        mMeiZhiView.loadComplete();
    }
}
