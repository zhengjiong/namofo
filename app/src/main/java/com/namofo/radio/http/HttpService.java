package com.namofo.radio.http;

import com.namofo.radio.entity.Album;
import com.namofo.radio.entity.Audio;
import com.namofo.radio.entity.MeiZhi;
import com.namofo.radio.entity.api.BaseResultEntity;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    /**
     * 获取所有专辑列表
     * @return
     */
    @GET("web.do?key=getAlbumList")
    Observable<BaseResultEntity<List<Album>>> getRecordAlbumList();

    /**
     * 根据专辑id获取专辑中的音频
     * @return
     */
    @GET("web.do?key=getAudioList")
    Observable<BaseResultEntity<List<Audio>>> getAudio(@Query("albumId") String id);
}
