package com.namofo.radio.presenter;

import android.view.View;

import com.namofo.radio.entity.Album;
import com.namofo.radio.entity.Audio;
import com.namofo.radio.entity.api.BaseResultEntity;
import com.namofo.radio.exception.HttpException;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;


/**
 * Title: AlbumPresenter
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/3/21  15:52
 *
 * @author 郑炯
 * @version 1.0
 */
public class AlbumPresenter extends BasePresenter<View> {
    public int page;

    public void refresh(Consumer<List<Album>> onNext, Consumer<String> onError) {
        page = 1;
        requestList(page, onNext, onError);
    }

    public void loadMore(Consumer<List<Album>> onNext, Consumer<String> onError) {
        page++;
        requestList(page, onNext, onError);
    }

    public void requestList(int album, Consumer<List<Album>> onNext, Consumer<String> onError) {
        submitRequest(getHttpService().getRecordAlbumList(), result -> {
            if (result.error) {
                throw new HttpException(result.message);
            }
            List<Album> audios = result.result;
            if (audios == null) {
                audios = new ArrayList<>();
            }
            onNext.accept(audios);
        }, throwable -> onError.accept(throwable.getMessage()));
    }
}
