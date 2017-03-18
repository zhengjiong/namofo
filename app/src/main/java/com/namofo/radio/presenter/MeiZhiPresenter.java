package com.namofo.radio.presenter;

import com.namofo.radio.entity.MeiZhi;
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
public class MeiZhiPresenter extends BasePresenter {
    private int page = 1;

    public void refresh(Action1<List<MeiZhi>> onNext, Action1<String> onError){
        page = 1;
        loadData(onNext, onError);
    }

    public void loadMore(Action1<List<MeiZhi>> onNext, Action1<String> onError) {
        page++;
        loadData(onNext, onError);
    }

    private void loadData(Action1<List<MeiZhi>> onNext, Action1<String> onError){
        submitRequest(httpManager.getHttpService().getMeizhi(page).delay(3000, TimeUnit.MILLISECONDS), new Action1<BaseResultEntity<List<MeiZhi>>>() {
            @Override
            public void call(BaseResultEntity<List<MeiZhi>> responseEntity) {
                if (responseEntity.error) {
                    throw new HttpException(responseEntity.message);
                }
                if (responseEntity.result == null) {
                    responseEntity.result = new ArrayList<MeiZhi>();
                }
                onNext.call(responseEntity.result);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                onError.call(throwable.getMessage());
            }
        });
    }

}
