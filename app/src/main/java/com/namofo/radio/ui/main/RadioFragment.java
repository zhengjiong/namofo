package com.namofo.radio.ui.main;

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

import com.jakewharton.rxbinding2.view.RxView;
import com.namofo.radio.R;
import com.namofo.radio.event.PlayEvent;
import com.namofo.radio.service.AudioPlayer;
import com.namofo.radio.service.PlayerService;
import com.namofo.radio.service.RadioPlayer;
import com.namofo.radio.ui.base.RxFragment;
import com.namofo.radio.util.ErrorDialogUtils;
import com.namofo.radio.util.LogUtils;
import com.namofo.radio.view.RotatePlayView;
import com.namofo.radio.view.playerview.MusicPlayerView;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * Description: 直播
 * CreateTime:17/1/14  16:21
 *
 * @author 郑炯
 * @version 1.0
 */
public class RadioFragment extends RxFragment implements RadioPlayer.IOnLoadingListener {

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
                    EventBus.getDefault().post(new PlayEvent(RadioPlayer.RADIO_PLAY_ACTION));
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
                    EventBus.getDefault().post(new PlayEvent(RadioPlayer.RADIO_STOP_ACTION));
                }, throwable -> {
                    ErrorDialogUtils.showErrorDialog(getContext(), throwable.getMessage());
                });

        mMusicPlayerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMusicPlayerView.isRotating()) {
                    mMusicPlayerView.stop();
                } else {
                    mMusicPlayerView.start();
                }
            }
        });
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().post(new PlayEvent(RadioPlayer.RADIO_PLAY_ACTION, null));
    }


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
        }, 700);
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
        // TODO: 17/3/23 17:52 zhengjiong
        /*if (mPlayerService != null) {
            mPlayerService.getRadioPlayer().setLoadingListener(null);
        }*/
    }
}
