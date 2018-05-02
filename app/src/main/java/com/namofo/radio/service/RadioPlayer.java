package com.namofo.radio.service;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.StringDef;
import android.text.TextUtils;

import com.namofo.radio.R;
import com.namofo.radio.common.IntentKey;
import com.namofo.radio.event.RadioPlayStatusEvent;
import com.namofo.radio.util.LogUtils;
import com.namofo.radio.util.ToastUtils;
import com.namofo.radio.util.Utils;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Title: RadioPlayer
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/2/3  18:27
 *
 * @author 郑炯
 * @version 1.0
 */
public class RadioPlayer {
    public static final String RTMP = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
//    public static final String RTMP = "http://26795.hlsplay.aodianyun.com/qingxinrensheng/stream.m3u8";
    //public static final String RTMP = "http://live.hkstv.hk.lxdns.com/live/hks/playlist.m3u8";
    //public static final String RTMP = "rtmp://26795.lssplay.aodianyun.com/qingxinrensheng/stream?k=ebe0c2da1df2626972ef08fd269cd6e2&t=1483501583";
    public PlayerService mService;
    private PLMediaPlayer mPLMediaPlayer;
    private AVOptions mAVOptions;
    protected boolean mIsStopped = false;
    private boolean mIsActivityPaused = true;
    private boolean mIsPrepared;

    public static final String RADIO_PLAY_ACTION = "com.namofo.radio.play";
    public static final String RADIO_STOP_ACTION = "com.namofo.radio.stop";
    public static final String RADIO_CLOSE_ACTION = "com.namofo.radio.close";

    @StringDef({RADIO_PLAY_ACTION,RADIO_STOP_ACTION,RADIO_CLOSE_ACTION})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RadioPlayAction{}

    private static final int MESSAGE_WHAT_ID_RECONNECTING = 0x1;//重新连接
    private static final int RECONNECT_DELAY_MILLIS = 3000;//重连延迟时间

    public RadioPlayer(PlayerService service) {
        mService = service;
        initAVOptions();
    }

    private void initAVOptions() {
        mAVOptions = new AVOptions();

        // 准备超时时间，包括创建资源、建立连接、请求码流等，单位是 ms
        // 默认值是：无
        mAVOptions.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);


        // 读取视频流超时时间，单位是 ms
        // 默认值是：10 * 1000
        mAVOptions.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);


        // 播放前最大探测流的字节数，单位是 byte
        // 默认值是：128 * 1024
        mAVOptions.setInteger(AVOptions.KEY_PROBESIZE, 128 * 1024);


        // 当前播放的是否为在线直播，如果是，则底层会有一些播放优化
        // 默认值是：0:关闭优化, 1:开启优化
        mAVOptions.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);


        // 是否开启"延时优化"，只在在线直播流中有效
        // 默认值是：0 : 关闭
        mAVOptions.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);

        // 解码方式:
        // codec＝AVOptions.MEDIA_CODEC_HW_DECODE，硬解
        // codec=AVOptions.MEDIA_CODEC_SW_DECODE, 软解
        // codec=AVOptions.MEDIA_CODEC_AUTO, 硬解优先，失败后自动切换到软解
        // 默认值是：MEDIA_CODEC_SW_DECODE
        mAVOptions.setInteger(AVOptions.KEY_MEDIACODEC, AVOptions.MEDIA_CODEC_AUTO);

        // whether start play automatically after prepared, default value is 1
        // 是否自动启动播放，如果设置为 1，则在调用 `prepareAsync` 或者 `setVideoPath` 之后自动启动播放，无需调用 `start()`
        // 默认值是：1,
        // 0:不自动播放
        mAVOptions.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);
        // 最大的缓存大小，单位是 ms
        // 默认值是：4000
        mAVOptions.setInteger(AVOptions.KEY_MAX_CACHE_BUFFER_DURATION, 4000);
    }

    private void prepare() {
        LogUtils.sout("prepare");
        if (mPLMediaPlayer == null) {
            mPLMediaPlayer = new PLMediaPlayer(mService, mAVOptions);
            mPLMediaPlayer.setOnPreparedListener(mOnPreparedListener);
            mPLMediaPlayer.setOnCompletionListener(mOnCompletionListener);
            mPLMediaPlayer.setOnErrorListener(mOnErrorListener);
            mPLMediaPlayer.setOnInfoListener(mOnInfoListener);
            //mPLMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
            mPLMediaPlayer.setWakeMode(mService, PowerManager.PARTIAL_WAKE_LOCK);
        }
        try {
            //mPLMediaPlayer.setDataSource("rtmp://26795.lssplay.aodianyun.com/qingxinrensheng/stream?k=ebe0c2da1df2626972ef08fd269cd6e2&t=1483501583");
            mPLMediaPlayer.setDataSource(RTMP);
            mPLMediaPlayer.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PLMediaPlayer.OnPreparedListener mOnPreparedListener = new PLMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(PLMediaPlayer mp) {
            LogUtils.sout("mOnPreparedListener onPrepared isPlaying=" + mp.isPlaying());
            mIsPrepared = true;
            mPLMediaPlayer.start(); //如果是自动播放可以不用加
            mIsStopped = false;

            /**
             * AUDIOFOCUS_GAIN指示申请得到的Audio Focus不知道会持续多久，一般是长期占有；
             */
            AudioManager audioManager = (AudioManager) mService.getSystemService(Context.AUDIO_SERVICE);
            audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
    };

    private PLMediaPlayer.OnCompletionListener mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(PLMediaPlayer mp) {
            LogUtils.sout("mOnCompletionListener onCompletion(播放完成)");
            release();
            sendReconnectMessage();
        }
    };

    private PLMediaPlayer.OnInfoListener mOnInfoListener = new PLMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(PLMediaPlayer mp, int what, int extra) {
            /*
            what 定义了消息类型，extra 是附加参数

            what	value	描述
            MEDIA_INFO_UNKNOWN	1	未知消息
            MEDIA_INFO_VIDEO_RENDERING_START	3	第一帧视频已成功渲染
            MEDIA_INFO_BUFFERING_START	701	开始缓冲
            MEDIA_INFO_BUFFERING_END	702	停止缓冲
            MEDIA_INFO_VIDEO_ROTATION_CHANGED	10001	获取到视频的播放角度
            MEDIA_INFO_AUDIO_RENDERING_START	10002	第一帧音频已成功播放
            MEDIA_INFO_SWITCHING_SW_DECODE	802	硬解失败，自动切换软解
            该对象用于监听播放器的状态消息，在播放器启动后，SDK 会在播放器发生状态变化时调用该对象的 onInfo 方法，同步状态信息。
            */

//            Logger.i("OnInfo, what = " + what + ", extra = " + extra);
            LogUtils.sout("mOnInfoListener what = " + what + ", extra = " + extra);
            switch (what) {
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    EventBus.getDefault().post(new RadioPlayStatusEvent(RadioPlayStatusEvent.LOADING));
                    break;
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
                case PLMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                    EventBus.getDefault().post(new RadioPlayStatusEvent(RadioPlayStatusEvent.PLAYING));
                    mService.notifyRadioPlay();
                    break;
                case PLMediaPlayer.MEDIA_INFO_SWITCHING_SW_DECODE:
                    ToastUtils.showShort(mService, R.string.error_media_info_switching_sw_decode);
                default:
                    break;
            }
            return true;
        }
    };


    /*private PLMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new PLMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(PLMediaPlayer mp, int percent) {
            Logger.i("onBufferingUpdate: " + percent + "%");
        }
    };*/

    /*
    MEDIA_ERROR_UNKNOWN	-1	未知错误
    ERROR_CODE_INVALID_URI	-2	无效的 URL
    ERROR_CODE_IO_ERROR	-5	网络异常
    ERROR_CODE_STREAM_DISCONNECTED	-11	与服务器连接断开
    ERROR_CODE_EMPTY_PLAYLIST	-541478725	空的播放列表
    ERROR_CODE_404_NOT_FOUND	-875574520	播放资源不存在
    ERROR_CODE_CONNECTION_REFUSED	-111	服务器拒绝连接
    ERROR_CODE_CONNECTION_TIMEOUT	-110	连接超时
    ERROR_CODE_UNAUTHORIZED	-825242872	未授权，播放一个禁播的流
    ERROR_CODE_PREPARE_TIMEOUT	-2001	播放器准备超时
    ERROR_CODE_READ_FRAME_TIMEOUT	-2002	读取数据超时
    ERROR_CODE_HW_DECODE_FAILURE	-2003	硬解码失败
    */
    private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(PLMediaPlayer mp, int errorCode) {
            boolean isNeedReconnect = false;
            LogUtils.sout("mOnErrorListener onError errorCode = " + errorCode);
            switch (errorCode) {
                case PLMediaPlayer.ERROR_CODE_INVALID_URI:
                    showToast(R.string.error_invalid_url);
                    break;
                case PLMediaPlayer.ERROR_CODE_404_NOT_FOUND:
                    showToast(R.string.error_404_url);
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_REFUSED:
                    showToast(R.string.error_connect_refused);
                    //showToastTips("Connection refused !");
                    break;
                case PLMediaPlayer.ERROR_CODE_CONNECTION_TIMEOUT:
                    //showToastTips("Connection timeout !");
                    showToast(R.string.error_connection_timeout);
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_EMPTY_PLAYLIST:
                    showToast(R.string.error_empty_playlist);
                    break;
                case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
                    showToast(R.string.error_stream_disconnected);
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                    //showToastTips("Network IO Error !");
                    showToast(R.string.error_network);
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
                    showToast(R.string.error_unauthorized);
                    break;
                case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
                    showToast(R.string.error_prepare_timeout);
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
                    showToast(R.string.error_read_frame_timeout);
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
                    showToast(R.string.error_unknow);
                    break;
                default:
                    showToast(R.string.error_unknow);
                    break;
            }
            // Todo pls handle the error status here, reconnect or call finish()
            isNeedReconnect = true;//设置可以重连
            release();
            if (isNeedReconnect) {
                sendReconnectMessage();
            } else {
                //finish();
            }
            // Return true means the error has been handled
            // If return false, then `onCompletion` will be called
            return true;
        }
    };

    private void sendReconnectMessage() {
        //ToastUtils.showShort(mService, R.string.re_connect);
        EventBus.getDefault().post(new RadioPlayStatusEvent(RadioPlayStatusEvent.LOADING));
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_WHAT_ID_RECONNECTING), RECONNECT_DELAY_MILLIS);
    }

    protected Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != MESSAGE_WHAT_ID_RECONNECTING) {
                return;
            }
            /*if (mIsActivityPaused || !Utils.isLiveStreamingAvailable()) {
                return;
            }*/
            if (!Utils.isNetworkAvailable(mService)) {
                LogUtils.sout("mHandler 网络不可用 sendReconnectMessage");
                sendReconnectMessage();
                return;
            }
            // The PLMediaPlayer has moved to the Error state, if you want to retry, must reset first !
            prepare();
        }
    };

    public void showToast(int resId) {
        if (mIsActivityPaused) {
            return;
        }
        ToastUtils.showShort(mService, resId);
    }

    public void showToast(String message) {
        if (mIsActivityPaused) {
            return;
        }
        ToastUtils.showShort(mService, message);
    }

    public boolean isPlaying(){
        if (mPLMediaPlayer != null && mPLMediaPlayer.isPlaying()) {
            return true;
        }
        return false;
    }

    public void stop(){
        if (mPLMediaPlayer != null) {
            mPLMediaPlayer.stop();
            mPLMediaPlayer.reset();
        }
        mIsStopped = true;
        mPLMediaPlayer = null;
        EventBus.getDefault().post(new RadioPlayStatusEvent(RadioPlayStatusEvent.STOPED));

        mHandler.removeCallbacksAndMessages(null);
    }

    public void release() {
        mIsPrepared = false;
        if (mPLMediaPlayer != null) {
            mPLMediaPlayer.stop();
            mPLMediaPlayer.release();
            mPLMediaPlayer = null;
        }
        mHandler.removeCallbacksAndMessages(null);
    }

    public void play(){
        if (mIsStopped) {
            LogUtils.sout("mIsStopped = true");
            EventBus.getDefault().post(new RadioPlayStatusEvent(RadioPlayStatusEvent.LOADING));
            prepare();
        } else {
            if (mIsPrepared) {
                LogUtils.sout("mIsPrepared = true");
                if (mPLMediaPlayer.isPlaying()) {
                    EventBus.getDefault().post(new RadioPlayStatusEvent(RadioPlayStatusEvent.PLAYING));
                } else {
                    mPLMediaPlayer.start();
                }
            } else {
                prepare();
            }
        }
    }
}
