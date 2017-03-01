package com.namofo.radio.ui.fragment;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.namofo.radio.R;
import com.namofo.radio.service.AudioPlayer;
import com.namofo.radio.service.PlayerService;
import com.namofo.radio.service.RadioPlayer;
import com.namofo.radio.ui.MainActivity;
import com.namofo.radio.ui.base.BaseFragment;
import com.namofo.radio.util.ErrorDialogUtils;
import com.namofo.radio.util.LogUtils;
import com.namofo.radio.view.RotatePlayView;
import com.namofo.radio.view.playerview.MusicPlayerView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Description: 直播
 * CreateTime:17/1/14  16:21
 *
 * @author 郑炯
 * @version 1.0
 */
public class RadioFragment extends BaseFragment implements RadioPlayer.IOnLoadingListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.btn_stop)
    Button btnStop;
    @BindView(R.id.loading_view)
    View loadView;
    @BindView(R.id.txt_playing)
    TextView txtPlaying;

    @BindView(R.id.btn_test_start)
    Button btnTestStart;
    @BindView(R.id.btn_test_pause)
    Button btnTestPause;
    @BindView(R.id.btn_test_resume)
    Button btnTestResume;
    @BindView(R.id.btn_test_stop)
    Button btnTestStop;

    @BindView(R.id.music_player_view)
    MusicPlayerView mMusicPlayerView;

    @BindView(R.id.rotate_play_view)
    RotatePlayView mRotatePlayView;

    private PlayerService mPlayerService;

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

    private NotificationCompat.Action generateAction(int icon) {
        return new NotificationCompat.Action(icon, "actionTitle", PendingIntent.getBroadcast(getContext(), 0, new Intent(getContext(), MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT));
    }

    private void initView(View view) {
        toolbar.setTitle(R.string.tab_radio);
        mRotatePlayView.setImageView(R.drawable.audio);
        mMusicPlayerView.setCoverDrawable(R.drawable.ic_empty_music2);
        RxView.clicks(btnStart)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)//两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> {
                    startRadioService(RadioPlayer.RADIO_PLAY_ACTION, mConnection, null);
                }, throwable -> {
                    ErrorDialogUtils.showErrorDialog(getContext(), throwable.getMessage());
                });
       /* RxView.clicks(btnPause)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(aVoid -> {
                    if (mPLMediaPlayer != null) {
                        mPLMediaPlayer.pause();
                    }
                    txtPlaying.setText(R.string.paused);
                }, throwable -> {
                    ErrorDialogUtils.showErrorDialog(getContext(), throwable.getMessage());
                });
        RxView.clicks(btnResume)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(aVoid -> {
                    if (mPLMediaPlayer != null) {
                        mPLMediaPlayer.start();
                    }
                    txtPlaying.setText(R.string.playing);
                }, throwable -> {
                    ErrorDialogUtils.showErrorDialog(getContext(), throwable.getMessage());
                });*/
        RxView.clicks(btnStop)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(aVoid -> {
                    startRadioService(RadioPlayer.RADIO_STOP_ACTION, mConnection, null);
                }, throwable -> {
                    ErrorDialogUtils.showErrorDialog(getContext(), throwable.getMessage());
                });

        RxView.clicks(btnTestStart)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startRadioService(AudioPlayer.AUDIO_PLAY_ACTION, mConnection, "http://audio.xmcdn.com/group9/M0A/87/05/wKgDYldS66OhILGuAI7YXtdBSSk047.m4a");
                    }
                }, throwable -> {
                    ErrorDialogUtils.showErrorDialog(getContext(), throwable.getMessage());
                });
        RxView.clicks(btnTestPause)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startRadioService(AudioPlayer.AUDIO_PAUSE_ACTION, mConnection, null);
                    }
                }, throwable -> {
                    ErrorDialogUtils.showErrorDialog(getContext(), throwable.getMessage());
                });
        RxView.clicks(btnTestResume)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startRadioService(AudioPlayer.AUDIO_RESUME_ACTION, mConnection, null);
                    }
                });
        RxView.clicks(btnTestStop)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        startRadioService(AudioPlayer.AUDIO_STOP_ACTION, mConnection, null);
                    }
                }, throwable -> {
                    ErrorDialogUtils.showErrorDialog(getContext(), throwable.getMessage());
                });

        mMusicPlayerView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (mMusicPlayerView.isRotating()) {
                    mMusicPlayerView.stop();
                } else {
                    mMusicPlayerView.start();
                }
            }
        });
    }

    private void startRadioService(String action, ServiceConnection mConnection, String dataSource) {
        Intent intent = new Intent(getActivity(), PlayerService.class);
        intent.setAction(action);
        if (dataSource != null) {
            intent.putExtra(AudioPlayer.EXTRA_NAME_AUDIO_DATA_SOURCE, dataSource);
        }
        getContext().startService(intent);
        if (mPlayerService == null) {
            getContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        startRadioService(RadioPlayer.RADIO_PLAY_ACTION, mConnection, null);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            LogUtils.sout("onServiceConnected");
            mPlayerService = ((PlayerService.RadioBinder) service).getService();
            if (mPlayerService != null) {
                mPlayerService.getRadioPlayer().setLoadingListener(RadioFragment.this);
                if (mPlayerService.getRadioPlayer().isPlaying()) {
                    onRadioPlay();
                }
            }
            getContext().unbindService(this);
            //mView.onPlaybackServiceBound(mPlaybackService);
            //mView.onSongUpdated(mPlaybackService.getPlayingSong());
        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            // Because it is running in our same process, we should never
            // see this happen.
            LogUtils.sout("onServiceDisconnected");
            if (mPlayerService != null) {
                mPlayerService.getRadioPlayer().setLoadingListener(null);
                mPlayerService = null;
            }
        }
    };

    @Override
    public void onRadioLoading() {
        loadView.setVisibility(View.VISIBLE);
        txtPlaying.setVisibility(View.GONE);
    }

    @Override
    public void onRadioPlay() {
        loadView.setVisibility(View.GONE);
        txtPlaying.setVisibility(View.VISIBLE);
        txtPlaying.setText(R.string.playing);
        mRotatePlayView.start();
        mMusicPlayerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMusicPlayerView.start();
            }
        },700);
    }

    @Override
    public void onRadioStop() {
        loadView.setVisibility(View.GONE);
        txtPlaying.setVisibility(View.VISIBLE);
        txtPlaying.setText(R.string.stopped);
        mMusicPlayerView.stop();
        mRotatePlayView.stop();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mPlayerService != null) {
            mPlayerService.getRadioPlayer().setLoadingListener(null);
        }
    }
}
