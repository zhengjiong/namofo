package com.namofo.radio.event;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Title: RadioPlayStatusEvent
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/3/23  18:11
 *
 * @author 郑炯
 * @version 1.0
 */
public class RadioPlayStatusEvent {
    public static final String LOADING = "LOADING";
    public static final String PLAYING = "PLAYING";
    public static final String STOPED = "STOPED";

    @StringDef({LOADING, PLAYING, STOPED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RadioPlayStatus{};

    @RadioPlayStatus
    public String playStatus;

    public RadioPlayStatusEvent(@RadioPlayStatus String playStatus) {
        this.playStatus = playStatus;
    }

    @RadioPlayStatus
    public String getPlayStatu() {
        return playStatus;
    }

    public void setPlayStatu(@RadioPlayStatus String playStatus) {
        this.playStatus = playStatus;
    }
}
