package com.namofo.radio.entity.api;

import com.namofo.radio.exception.HttpTimeException;
import com.namofo.radio.http.HttpService;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * CreateTime:17/1/17  16:54
 *
 * @author 郑炯
 * @version 1.0
 */
public abstract class BasePostEntity<T> implements Func1<BaseResultEntity<T>, T> {
    private RxAppCompatActivity rxAppCompatActivity;
    /**
     * 设置参数
     * @param methods
     * @return
     */
    public abstract Observable getObservable(HttpService methods);

    /**
     * 设置回调
     * @return
     */
    public abstract Subscriber<T> getSubscriber();

    public T call(BaseResultEntity<T> httpResult) {
        if (httpResult.error) {
            throw new HttpTimeException(httpResult.errorMsg);
        }
        return httpResult.results;
    }

}
