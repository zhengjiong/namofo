package com.namofo.radio.event;

/**
 * Title: PlayEvent
 * CreateTime:17/3/23  17:22
 *
 * @author 郑炯
 * @version 1.0
 */
public class PlayEvent {
    public String action;//RADIO_PLAY_ACTION
    public String dataSource;//播放音频的url

    public PlayEvent(String action) {
        this.action = action;
    }

    public PlayEvent(String action, String dataSource) {
        this.action = action;
        this.dataSource = dataSource;
    }
}
