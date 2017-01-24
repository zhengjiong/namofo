package com.namofo.radio.ui.fragment;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jakewharton.rxbinding.view.RxView;
import com.namofo.radio.R;
import com.namofo.radio.base.BaseFragment;
import com.namofo.radio.util.ErrorDialogUtils;
import com.namofo.radio.util.ToastUtils;
import com.namofo.radio.util.Utils;
import com.orhanobut.logger.Logger;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description: 直播
 * CreateTime:17/1/14  16:21
 *
 * @author 郑炯
 * @version 1.0
 */
public class RadioFragment extends BaseFragment {
    private static final int MESSAGE_ID_RECONNECTING = 0x1;
    //public static final String RTMP = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
    public static final String RTMP = "http://live.hkstv.hk.lxdns.com/live/hks/playlist.m3u8";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.btn_pause)
    Button btnPause;
    @BindView(R.id.btn_resume)
    Button btnResume;
    @BindView(R.id.btn_stop)
    Button btnStop;
    @BindView(R.id.loading_view)
    View loadView;

    private PLMediaPlayer mPLMediaPlayer;
    private AVOptions mAVOptions;
    private boolean mIsStopped = false;
    private boolean mIsActivityPaused = true;

    public static RadioFragment newInstance() {

        Bundle args = new Bundle();

        RadioFragment fragment = new RadioFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_radio_layout, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        toolbar.setTitle(R.string.tab_radio);

        RxView.clicks(btnStart)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)//两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {
                    if (mIsStopped) {
                        prepare();
                    } else {
                        mPLMediaPlayer.start();
                    }
                }, throwable -> {
                    ErrorDialogUtils.showErrorDialog(getContext(), throwable.getMessage());
                });
        RxView.clicks(btnPause)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(aVoid -> {
                    if (mPLMediaPlayer != null) {
                        mPLMediaPlayer.pause();
                    }
                }, throwable -> {
                    ErrorDialogUtils.showErrorDialog(getContext(), throwable.getMessage());
                });
        RxView.clicks(btnResume)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(aVoid -> {
                    if (mPLMediaPlayer != null) {
                        mPLMediaPlayer.start();
                    }
                }, throwable -> {
                    ErrorDialogUtils.showErrorDialog(getContext(), throwable.getMessage());
                });
        RxView.clicks(btnStop)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(aVoid -> {
                    if (mPLMediaPlayer != null) {
                        mPLMediaPlayer.stop();
                        mPLMediaPlayer.reset();
                    }
                    mIsStopped = true;
                    mPLMediaPlayer = null;
                }, throwable -> {
                    ErrorDialogUtils.showErrorDialog(getContext(), throwable.getMessage());
                });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initAVOptions();
        prepare();
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
        if (mPLMediaPlayer == null) {
            mPLMediaPlayer = new PLMediaPlayer(getContext(), mAVOptions);
            mPLMediaPlayer.setOnPreparedListener(mOnPreparedListener);
            mPLMediaPlayer.setOnCompletionListener(mOnCompletionListener);
            mPLMediaPlayer.setOnErrorListener(mOnErrorListener);
            mPLMediaPlayer.setOnInfoListener(mOnInfoListener);
            //mPLMediaPlayer.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
            mPLMediaPlayer.setWakeMode(getContext(), PowerManager.PARTIAL_WAKE_LOCK);
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

    public void release() {
        if (mPLMediaPlayer != null) {
            mPLMediaPlayer.stop();
            mPLMediaPlayer.release();
            mPLMediaPlayer = null;
        }
    }

    private PLMediaPlayer.OnPreparedListener mOnPreparedListener = new PLMediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(PLMediaPlayer mp) {
            Logger.i("onPrepared isPlaying=" + mp.isPlaying());
            mPLMediaPlayer.start(); //会自动播放可以不用加
            mIsStopped = false;

            /**
             * AUDIOFOCUS_GAIN指示申请得到的Audio Focus不知道会持续多久，一般是长期占有；
             */
            AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        }
    };

    private PLMediaPlayer.OnCompletionListener mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(PLMediaPlayer mp) {
            Logger.i("播放完成");
        }
    };

    private PLMediaPlayer.OnInfoListener mOnInfoListener = new PLMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(PLMediaPlayer mp, int what, int extra) {
            Logger.i("OnInfo, what = " + what + ", extra = " + extra);
            switch (what) {
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    loadView.setVisibility(View.VISIBLE);
                    break;
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
                case PLMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                    loadView.setVisibility(View.INVISIBLE);
                    break;
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

    private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(PLMediaPlayer mp, int errorCode) {
            boolean isNeedReconnect = false;
            Logger.i("Error happened, errorCode = " + errorCode);
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
                    showToast("Empty playlist !");
                    break;
                case PLMediaPlayer.ERROR_CODE_STREAM_DISCONNECTED:
                    showToast("Stream disconnected !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                    //showToastTips("Network IO Error !");
                    showToast(R.string.error_code_io_error);
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_UNAUTHORIZED:
                    showToast("Unauthorized Error !");
                    break;
                case PLMediaPlayer.ERROR_CODE_PREPARE_TIMEOUT:
                    showToast("Prepare timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.ERROR_CODE_READ_FRAME_TIMEOUT:
                    showToast("Read frame timeout !");
                    isNeedReconnect = true;
                    break;
                case PLMediaPlayer.MEDIA_ERROR_UNKNOWN:
                    break;
                default:
                    showToast("未知错误");
                    break;
            }
            // Todo pls handle the error status here, reconnect or call finish()
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
        ToastUtils.showShort(getContext(), R.string.re_connect);
        loadView.setVisibility(View.VISIBLE);
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_ID_RECONNECTING), 500);
    }

    protected Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != MESSAGE_ID_RECONNECTING) {
                return;
            }
            /*if (mIsActivityPaused || !Utils.isLiveStreamingAvailable()) {
                return;
            }*/
            if (!Utils.isNetworkAvailable(getContext())) {
                sendReconnectMessage();
                return;
            }
            // The PLMediaPlayer has moved to the Error state, if you want to retry, must reset first !
            prepare();
        }
    };

    public void showToast(int resId){
        if (mIsActivityPaused) {
            return;
        }
        ToastUtils.showShort(getContext(), resId);
    }

    public void showToast(String message){
        if (mIsActivityPaused) {
            return;
        }
        ToastUtils.showShort(getContext(), message);
    }

    @Override
    public void onPause() {
        super.onPause();
        mIsActivityPaused = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        mIsActivityPaused = false;
    }

    @Override
    public void onDetach() {
        release();
        AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(null);
        super.onDetach();
    }
}
