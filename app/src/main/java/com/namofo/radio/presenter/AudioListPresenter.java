package com.namofo.radio.presenter;

import android.view.View;

import com.namofo.radio.entity.Audio;
import com.namofo.radio.exception.HttpException;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;


/**
 * Title: MeiZhiPresenter
 * Description: View和Model的桥梁，它从Model层检索数据后，返回给View层
 * CreateTime:17/1/21  14:21
 *
 * @author 郑炯
 * @version 1.0
 */
public class AudioListPresenter extends BasePresenter<View> {
    private int page = 1;

    public void refresh(int album, Consumer<List<Audio>> onNext, Consumer<String> onError) {
        page = 1;
        loadData(album, onNext, onError);
    }

    public void loadMore(int album, Consumer<List<Audio>> onNext, Consumer<String> onError) {
        page++;
        loadData(album, onNext, onError);
    }

    private void loadData(int album, Consumer<List<Audio>> onNext, Consumer<String> onError) {
        submitRequest(getHttpService().getAudio(album+""), responseEntity -> {
            if (responseEntity.error) {
                throw new HttpException(responseEntity.message);
            }
            if (responseEntity.result == null) {
                responseEntity.result = new ArrayList<>();
            }
            onNext.accept(responseEntity.result);
        }, throwable -> onError.accept(throwable.getMessage()));
    }


}
