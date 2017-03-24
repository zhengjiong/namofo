package com.namofo.radio.presenter;

import android.support.annotation.Keep;

import com.namofo.radio.http.HttpManager;
import com.namofo.radio.http.HttpService;
import com.trello.rxlifecycle2.LifecycleProvider;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Title: BasePresenter
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/1/21  14:21
 *
 * @author 郑炯
 * @version 1.0
 */
@Keep
public abstract class BasePresenter<V> implements Presenter<V> {
    HttpManager httpManager = HttpManager.getInstance();
    LifecycleProvider<?> lifecycleProvider;

    public BasePresenter(LifecycleProvider<?> lifecycleProvider) {
        this.lifecycleProvider = lifecycleProvider;
    }

    public HttpService getHttpService() {
        return httpManager.getHttpService();
    }

    public <T> void submitRequest(Observable<T> observable, Consumer<T> onNext) {
        httpManager.doHttp(lifecycleProvider, observable, onNext);
    }

    public <T> void submitRequest(Observable<T> observable, Consumer<T> onNext, Consumer<Throwable> onError) {
        httpManager.doHttp(lifecycleProvider, observable, onNext, onError);
    }

    public <T> void submitRequest(Observable<T> observable, Consumer<T> onNext, Consumer<Throwable> onError, Action onComplete) {
        httpManager.doHttp(lifecycleProvider, observable, onNext, onError, onComplete);
    }

    public void attachView(V mvpView) {
        //this.mvpView = mvpView;
        //apiStores = AppClient.retrofit().create(ApiStores.class);
    }

    /*public void onError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg);
        builder.setPositiveButton(rightResId, rightListener);
        builder.show();
    }*/

    public void detachView() {
        //this.mvpView = null;
        //onUnsubscribe();
    }

}
