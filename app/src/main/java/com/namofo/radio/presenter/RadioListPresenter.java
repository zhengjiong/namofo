package com.namofo.radio.presenter;

import android.view.View;

import com.namofo.radio.entity.MeiZhi;
import com.namofo.radio.entity.RecordAlbum;
import com.namofo.radio.entity.api.BaseResultEntity;
import com.namofo.radio.exception.HttpException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Title: MeiZhiPresenter
 * Description: View和Model的桥梁，它从Model层检索数据后，返回给View层
 * CreateTime:17/1/21  14:21
 *
 * @author 郑炯
 * @version 1.0
 */
public class RadioListPresenter extends BasePresenter<View> {
    private int page = 1;

    public void refresh(Action1<List<RecordAlbum>> onNext, Action1<String> onError){
        page = 1;
        loadData(onNext, onError);
    }

    public void loadMore(Action1<List<RecordAlbum>> onNext, Action1<String> onError) {
        page++;
        loadData(onNext, onError);
    }

    private void loadData(Action1<List<RecordAlbum>> onNext, Action1<String> onError){
        submitRequest(httpManager.getHttpService().getRecordAlbumList(), responseEntity -> {
            if (responseEntity.error) {
                throw new HttpException(responseEntity.message);
            }
            if (responseEntity.result == null) {
                responseEntity.result = new ArrayList<>();
            }
            onNext.call(responseEntity.result);
        }, throwable -> onError.call(throwable.getMessage()));
    }


}
