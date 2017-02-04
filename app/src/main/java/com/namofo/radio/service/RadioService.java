package com.namofo.radio.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.namofo.radio.util.LogUtils;

/**
 * Title: RadioService
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/2/2  15:31
 *
 * @author 郑炯
 * @version 1.0
 */
public class RadioService extends Service {

    private Binder mBinder = new RadioBinder();
    private RadioPlayer mRadioPlayer;
    private AudioPlayer mAudioPlayer;

    public class RadioBinder extends Binder {
        public RadioService getService() {
            return RadioService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.sout("onBind");
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.sout("RadioService onCreate");
        mRadioPlayer = new RadioPlayer(this);
        mAudioPlayer = new AudioPlayer(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.sout("RadioService onStartCommand");
        if (intent != null) {
            final String action = intent.getAction();

            if (RadioPlayer.RADIO_STOP_ACTION.equals(action)) {
                //直播停止
                mRadioPlayer.stop();
                // TODO: 17/2/4 21:44 zhengjiong 添加判断
                stopForeground(false);
            } else if (RadioPlayer.RADIO_PLAY_ACTION.equals(action)) {
                //直播开始
                mRadioPlayer.play();
                startForeground(MediaNotificationUtils.NOTIFICATION_ID_RADIO, mRadioPlayer.mMediaNotificationUtils.createRadioNotification(mRadioPlayer.mIsStopped));
            } else if (AudioPlayer.AUDIO_PLAY_ACTION.equals(action)) {
                //播放录音
                String dataSource = intent.getStringExtra(AudioPlayer.EXTRA_NAME_AUDIO_DATA_SOURCE);
                mAudioPlayer.play(dataSource);
                startForeground(MediaNotificationUtils.NOTIFICATION_ID_RADIO, mRadioPlayer.mMediaNotificationUtils.createRadioNotification(mAudioPlayer.mIsStopped));

            } else if (AudioPlayer.AUDIO_PAUSE_ACTION.equals(action)) {
                //暂停录音
                mAudioPlayer.pause();
            } else if (AudioPlayer.AUDIO_RESUME_ACTION.equals(action)) {
                mAudioPlayer.resume();
            } else if (AudioPlayer.AUDIO_STOP_ACTION.equals(action)) {
                //停止录音
                mAudioPlayer.stop();
                // TODO: 17/2/4 21:44 zhengjiong 添加判断
                stopForeground(false);
            }

        }
        return Service.START_NOT_STICKY;//如果在执行完onStartCommand后，服务被异常kill掉，系统不会自动重启该服务。
    }


    @Override
    public void onDestroy() {
        mRadioPlayer.release();
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(null);
        System.out.println("RadioService onDestroy");
        super.onDestroy();
    }

    public RadioPlayer getRadioPlayer() {
        return mRadioPlayer;
    }
}
