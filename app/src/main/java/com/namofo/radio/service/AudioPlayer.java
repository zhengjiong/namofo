package com.namofo.radio.service;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationManagerCompat;

import com.namofo.radio.R;
import com.namofo.radio.util.LogUtils;
import com.namofo.radio.util.ToastUtils;

import java.io.IOException;

/**
 * Title: AudioPlayer
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/2/3  23:47
 *
 * @author 郑炯
 * @version 1.0
 */
public class AudioPlayer {
    //IjkMediaPlayer
    //private PLMediaPlayer mMediaPlayer;
    public static final String AUDIO_PLAY_ACTION = "com.namofo.audio.play";
    public static final String AUDIO_PAUSE_ACTION = "com.namofo.audio.pause";
    public static final String AUDIO_RESUME_ACTION = "com.namofo.audio.resume";
    public static final String AUDIO_STOP_ACTION = "com.namofo.audio.stop";
    public static final String AUDIO_CLOSE_ACTION = "com.namofo.audio.close";

    public static final String EXTRA_NAME_AUDIO_DATA_SOURCE = "extra_audio_data_source";
    //mMediaPlayer.setDataSource("http://audio.xmcdn.com/group9/M0A/87/05/wKgDYldS66OhILGuAI7YXtdBSSk047.m4a");
    //mMediaPlayer.setDataSource("rtmp://26795.lssplay.aodianyun.com/qingxinrensheng/stream?k=ebe0c2da1df2626972ef08fd269cd6e2&t=1483501583");


    private MediaPlayer mMediaPlayer;
    protected boolean mIsStopped = false;

    public static final int AUDIO_SESSION_ID = 999;
    public PlayerService mService;


    private IOnLoadingListener mLoadingListener;

    public interface IOnLoadingListener {
        void onAudioLoading();

        void onAudioPlay();

        void onAudioPause();

        void onAudioResume();

        void onAudioStop();
    }

    public AudioPlayer(PlayerService context) {
        mService = context;
    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.reset();
        //mMediaPlayer.setAudioSessionId(AUDIO_SESSION_ID);//加了不能播放
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mIsStopped = false;
                LogUtils.sout("mMediaPlayer onPrepared");
                mp.start();
                mService.notifyAudioPlay();
            }
        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                LogUtils.sout("mMediaPlayer onError what=" + what);
                return true;
            }
        });
        /*mMediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                LogUtils.sout("setOnInfoListener what = " + what + ", extra = " + extra);
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                        if (mLoadingListener != null) {
                            mLoadingListener.onAudioLoading();
                        }
                        break;
                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    //case MediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                        if (mLoadingListener != null) {
                            mLoadingListener.onAudioPlay();
                        }
                        ((PlayerService)mContext).startForeground(MediaNotificationUtils.NOTIFICATION_ID_RADIO, mMediaNotificationUtils.createRadioNotification(mIsStopped));
                        break;
                    default:
                        break;
                }
                return true;
            }
        });*/
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                LogUtils.sout("onCompletion");
            }
        });
        mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                LogUtils.sout("onBufferingUpdate: " + percent + "%");
            }
        });
    }

    public void play(String dataSource) {
        if (mMediaPlayer == null) {
            initMediaPlayer();
        }
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(dataSource);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public void resume() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    public void stop() {
        mIsStopped = true;

        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
        }
        if (mLoadingListener != null) {
            mLoadingListener.onAudioStop();
        }
        mService.notifyAudioStop();
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.setOnCompletionListener(null);
            mMediaPlayer.setOnErrorListener(null);
            mMediaPlayer.setOnPreparedListener(null);
            mMediaPlayer.setOnInfoListener(null);
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public boolean isPlaying(){
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            return true;
        }
        return false;
    }
}
