package com.namofo.radio.model;

import com.namofo.radio.entity.MeiZhi;
import com.namofo.radio.entity.api.MeiZhiPostPostEntity;
import com.namofo.radio.http.HttpManager;
import com.namofo.radio.presenter.meizhi.IMeiZhiPresenter;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Title: MeiZhiModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/1/22  12:07
 *
 * @author 郑炯
 * @version 1.0
 */
public class MeiZhiModel extends BaseModel{
    IMeiZhiPresenter mPresenter;

    public MeiZhiModel(IMeiZhiPresenter presenter) {
        mPresenter = presenter;
    }

    public void loadData(int page) {
        MeiZhiPostPostEntity post = new MeiZhiPostPostEntity(new Subscriber<List<MeiZhi>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mPresenter.loadFailure();
            }

            @Override
            public void onNext(List<MeiZhi> list) {
                if (list == null) {
                    list = new ArrayList<>();
                }
                mPresenter.loadSuccess(list);
            }
        }, page);

        doHttp(post);
    }

}
