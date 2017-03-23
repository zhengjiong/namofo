package com.namofo.radio.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;

import com.namofo.radio.util.LogUtils;

/**
 * Title: PlayerService
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/2/2  15:31
 *
 * @author 郑炯
 * @version 1.0
 */
public class PlayerService extends Service {

    private Binder mBinder = new RadioBinder();
    private RadioPlayer mRadioPlayer;
    private AudioPlayer mAudioPlayer;

    private NotificationManagerCompat mNotificationManager;
    protected MediaNotificationUtils mMediaNotificationUtils;

    private boolean isForeGround;

    public class RadioBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
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
        LogUtils.sout("PlayerService onCreate");
        mRadioPlayer = new RadioPlayer(this);
        mAudioPlayer = new AudioPlayer(this);

        mNotificationManager = NotificationManagerCompat.from(this);
        mMediaNotificationUtils = new MediaNotificationUtils(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            final String action = intent.getAction();
            LogUtils.sout("PlayerService onStartCommand action=" + action);
            if (RadioPlayer.RADIO_PLAY_ACTION.equals(action)) {
                //直播开始
                mRadioPlayer.play();
                //notifyRadioPlay();//在OnInfoListener中已添加
            } else if (RadioPlayer.RADIO_STOP_ACTION.equals(action)) {
                //直播停止
                mRadioPlayer.stop();
                notifyRadioStop();
            } else if (AudioPlayer.AUDIO_PLAY_ACTION.equals(action)) {
                //播放录音
                String dataSource = intent.getStringExtra(AudioPlayer.EXTRA_NAME_AUDIO_DATA_SOURCE);
                mAudioPlayer.play(dataSource);
                notifyAudioPlay();
            } else if (AudioPlayer.AUDIO_PAUSE_ACTION.equals(action)) {
                //暂停录音
                mAudioPlayer.pause();
            } else if (AudioPlayer.AUDIO_RESUME_ACTION.equals(action)) {
                mAudioPlayer.resume();
            } else if (AudioPlayer.AUDIO_STOP_ACTION.equals(action)) {
                //停止录音
                mAudioPlayer.stop();
                notifyAudioStop();
            } else if (RadioPlayer.RADIO_CLOSE_ACTION.equals(action)) {
                mRadioPlayer.release();
                //if (!mAudioPlayer.isPlaying()) {
                    stopForeground(false);
                //}
                mNotificationManager.cancel(MediaNotificationUtils.NOTIFICATION_ID_RADIO);
            } else if (AudioPlayer.AUDIO_CLOSE_ACTION.equals(action)) {
                mAudioPlayer.release();
                //if (!mRadioPlayer.isPlaying()) {
                    stopForeground(false);
                //}
                mNotificationManager.cancel(MediaNotificationUtils.NOTIFICATION_ID_AUDIO);
            }

        }
        return Service.START_NOT_STICKY;//如果在执行完onStartCommand后，服务被异常kill掉，系统不会自动重启该服务。
    }

    public void notifyRadioPlay(){
        if (mAudioPlayer.isPlaying()) {
            mAudioPlayer.pause();
            mNotificationManager.notify(MediaNotificationUtils.NOTIFICATION_ID_AUDIO, mMediaNotificationUtils.createAudioNotification(true));
        }
        if (!isForeGround) {
            isForeGround = true;
            startForeground(MediaNotificationUtils.NOTIFICATION_ID_RADIO, mMediaNotificationUtils.createRadioNotification(false));
        } else {
            mNotificationManager.notify(MediaNotificationUtils.NOTIFICATION_ID_RADIO, mMediaNotificationUtils.createRadioNotification(false));
        }
    }

    public void notifyRadioStop(){
        isForeGround = false;
        stopForeground(false);
        mNotificationManager.notify(MediaNotificationUtils.NOTIFICATION_ID_RADIO, mMediaNotificationUtils.createRadioNotification(true));
    }

    public void notifyAudioPlay(){
        if (mRadioPlayer.isPlaying()) {
            mRadioPlayer.stop();
            mNotificationManager.notify(MediaNotificationUtils.NOTIFICATION_ID_RADIO, mMediaNotificationUtils.createRadioNotification(true));
        }
        if (!isForeGround) {
            isForeGround = true;
            startForeground(MediaNotificationUtils.NOTIFICATION_ID_AUDIO, mMediaNotificationUtils.createAudioNotification(false));
        } else {
            mNotificationManager.notify(MediaNotificationUtils.NOTIFICATION_ID_AUDIO, mMediaNotificationUtils.createAudioNotification(false));
        }
    }

    public void notifyAudioStop(){
        isForeGround = false;
        stopForeground(false);
        mNotificationManager.notify(MediaNotificationUtils.NOTIFICATION_ID_AUDIO, mMediaNotificationUtils.createAudioNotification(true));
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        mRadioPlayer.release();
        mAudioPlayer.release();
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(null);
        System.out.println("PlayerService onDestroy");
        super.onDestroy();
    }

    public RadioPlayer getRadioPlayer() {
        return mRadioPlayer;
    }

    public AudioPlayer getAudioPlayer() {
        return mAudioPlayer;
    }

    public NotificationManagerCompat getNotificationManager() {
        return mNotificationManager;
    }

}
