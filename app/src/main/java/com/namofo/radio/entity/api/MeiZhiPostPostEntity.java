package com.namofo.radio.entity.api;

import com.namofo.radio.entity.MeiZhi;
import com.namofo.radio.http.HttpService;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * CreateTime:17/1/19  17:40
 *
 * @author 郑炯
 * @version 1.0
 */
public class MeiZhiPostPostEntity extends BasePostEntity<List<MeiZhi>> {

    private Subscriber<List<MeiZhi>> mSubscriber;
    private int page;

    public MeiZhiPostPostEntity(Subscriber<List<MeiZhi>> subscriber, int page) {
        this.mSubscriber = subscriber;
        this.page = page;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.getMeizhi(page);
    }

    @Override
    public Subscriber<List<MeiZhi>> getSubscriber() {
        return mSubscriber;
    }

    /*@Override
    public Object call(Object o) {
        return null;
    }*/

    public void setPage(int page) {
        this.page = page;
    }
}
