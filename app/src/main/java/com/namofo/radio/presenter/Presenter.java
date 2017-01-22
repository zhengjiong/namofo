package com.namofo.radio.presenter;

/**
 * Title: Presenter
 *
 * @author 郑炯
 * @version 1.0
 */
public interface Presenter<V> {

    void attachView(V view);
    void detachView();
}
