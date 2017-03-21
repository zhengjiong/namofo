package com.namofo.radio.presenter;

import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.namofo.radio.entity.MeiZhi;
import com.namofo.radio.entity.api.BaseResultEntity;
import com.namofo.radio.http.HttpManager;
import com.namofo.radio.http.HttpService;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

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
public abstract class BasePresenter<V> implements Presenter<V>{
    HttpManager httpManager = HttpManager.getInstance();

    public HttpService getHttpService(){
        return httpManager.getHttpService();
    }

    public <T> void submitRequest(Observable<T> observable,  Action1<T> onNext){
        httpManager.doHttp(observable, onNext);
    }

    public <T> void submitRequest(Observable<T> observable,  Action1<T> onNext, Action1<Throwable> onError){
        httpManager.doHttp(observable, onNext, onError);
    }

    public <T> void submitRequest(Observable<T> observable,  Action1<T> onNext, Action1<Throwable> onError, Action0 onComplete){
        httpManager.doHttp(observable, onNext, onError, onComplete);
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
