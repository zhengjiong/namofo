package com.namofo.radio.event;

import com.namofo.radio.service.AudioPlayer;
import com.namofo.radio.service.RadioPlayer;


/**
 * Title: AudioPlayEvent
 * CreateTime:17/3/23  17:22
 *
 * @author 郑炯
 * @version 1.0
 */
public class RadioPlayEvent {
    @RadioPlayer.RadioPlayAction
    public String action;

    public RadioPlayEvent(@RadioPlayer.RadioPlayAction String action) {
        this.action = action;
    }
}
