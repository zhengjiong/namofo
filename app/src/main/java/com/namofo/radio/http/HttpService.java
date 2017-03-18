package com.namofo.radio.http;

import com.namofo.radio.entity.MeiZhi;
import com.namofo.radio.entity.RecordAlbum;
import com.namofo.radio.entity.api.BaseResultEntity;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Description:
 * Copyright:Copyright(c)2016
 * CreateTime:17/1/15  16:01
 *
 * @author 郑炯
 * @version 1.0
 */
public interface HttpService {
    @GET("data/福利/10/{page}")
    Observable<BaseResultEntity<List<MeiZhi>>> getMeizhi(@Path("page") int page);

    @GET("web.do?key=getAlbumList")
    Observable<BaseResultEntity<List<RecordAlbum>>> getRecordAlbumList();
}
