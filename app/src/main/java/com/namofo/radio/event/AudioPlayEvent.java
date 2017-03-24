package com.namofo.radio.event;

import android.support.annotation.StringDef;

import com.namofo.radio.service.AudioPlayer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * Title: AudioPlayEvent
 * CreateTime:17/3/23  17:22
 *
 * @author 郑炯
 * @version 1.0
 */
public class AudioPlayEvent {
    @AudioPlayer.AudioPlayAction
    public String action;
    public String title;
    public String text;
    public String dataSource;//播放音频的url

    public AudioPlayEvent(@AudioPlayer.AudioPlayAction String action, String title, String text, String dataSource) {
        this.action = action;
        this.title = title;
        this.text = text;
        this.dataSource = dataSource;
    }
}
