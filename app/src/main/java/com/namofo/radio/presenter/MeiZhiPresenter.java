package com.namofo.radio.presenter;

import com.namofo.radio.entity.MeiZhi;
import com.namofo.radio.entity.api.BaseResultEntity;
import com.namofo.radio.exception.HttpException;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;


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

    public MeiZhiPresenter(LifecycleProvider lifecycleProvider) {
        super(lifecycleProvider);
    }

    public void refresh(Consumer<List<MeiZhi>> onNext, Consumer<String> onError){
        page = 1;
        loadData(onNext, onError);
    }

    public void loadMore(Consumer<List<MeiZhi>> onNext, Consumer<String> onError) {
        page++;
        loadData(onNext, onError);
    }

    private void loadData(Consumer<List<MeiZhi>> onNext, Consumer<String> onError){
        submitRequest(httpManager.getHttpService().getMeizhi(page).delay(3000, TimeUnit.MILLISECONDS), new Consumer<BaseResultEntity<List<MeiZhi>>>() {
            @Override
            public void accept(BaseResultEntity<List<MeiZhi>> responseEntity) throws Exception {
                if (responseEntity.error) {
                    throw new HttpException(responseEntity.message);
                }
                if (responseEntity.result == null) {
                    responseEntity.result = new ArrayList<MeiZhi>();
                }
                onNext.accept(responseEntity.result);
            }

        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                onError.accept(throwable.getMessage());
            }


        });
    }

}
