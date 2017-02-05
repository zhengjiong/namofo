package com.namofo.radio.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.namofo.radio.R;
import com.namofo.radio.common.Constants;
import com.namofo.radio.ui.MainActivity;
import com.namofo.radio.util.LogUtils;

/**
 * Title: MediaNotificationUtils
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/2/3  17:39
 *
 * @author 郑炯
 * @version 1.0
 */
public class MediaNotificationUtils {
    public static final int NOTIFICATION_ID_RADIO = 999;
    public static final int NOTIFICATION_ID_AUDIO = 998;
    private Context mContext;


    public MediaNotificationUtils(Context context) {
        this.mContext = context;
    }

    public Notification createRadioNotification(boolean isStopped) {
        PendingIntent clickIntent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        /**
         * FLAG_CANCEL_CURRENT:如果当前系统中已经存在一个相同的 PendingIntent 对象，那么就将先将已有的 PendingIntent 取消，然后重新生成一个 PendingIntent 对象。

         * FLAG_NO_CREATE:如果当前系统中不存在相同的 PendingIntent 对象，系统将不会创建该 PendingIntent 对象而是直接返回 null 。

         * FLAG_ONE_SHOT:该 PendingIntent 只作用一次。

         * FLAG_UPDATE_CURRENT:如果系统中已存在该 PendingIntent 对象，那么系统将保留该 PendingIntent 对象，但是会使用新的 Intent 来更新之前 PendingIntent 中的 Intent 对象数据，例如更新 Intent 中的 Extras 。
         */
        Intent closeIntent = new Intent(mContext, PlayerService.class);
        closeIntent.setAction(RadioPlayer.RADIO_CLOSE_ACTION);
        PendingIntent closePendingIntent = PendingIntent.getService(mContext, 0, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playIntent = new Intent(mContext, PlayerService.class);
        playIntent.setAction(RadioPlayer.RADIO_PLAY_ACTION);
        PendingIntent playPendingIntent = PendingIntent.getService(mContext, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent stopIntent = new Intent(mContext, PlayerService.class);
        stopIntent.setAction(RadioPlayer.RADIO_STOP_ACTION);
        PendingIntent stopPendingIntent = PendingIntent.getService(mContext, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //MediaSessionCompat mSession = new MediaSessionCompat(getActivity(), "namofo");
        //mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        Bitmap artwork = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_empty_music2);
        NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(artwork)
                /**
                 *
                 * VISIBILITY_PUBLIC 只有在没有锁屏时会显示通知
                 * VISIBILITY_PRIVATE 任何情况都会显示通知
                 * VISIBILITY_SECRET 在安全锁和没有锁屏的情况下显示通知
                 */
                .setVisibility(android.support.v7.app.NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(clickIntent)
                .setContentTitle("FM直播")
                .setContentText("清新人生讲堂")
                .setWhen(System.currentTimeMillis())
                .setCategory(NotificationCompat.CATEGORY_SERVICE)//制定通知的分类
                //.setCustomBigContentView()//指定展开时的视图

                .setGroupSummary(true)
                .setGroup(Constants.NOTIFICATION_GROUP_NAME);
        //.setSubText("YY直播")

        if (isStopped) {
            builder.setWhen(0);
            builder.setShowWhen(false);
            builder.setUsesChronometer(false);//停止计时
            builder.setOngoing(false);
            builder.setAutoCancel(true);
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);//不会显示到最上面
            builder.addAction(R.drawable.uamp_ic_play_arrow_white_24dp, "", playPendingIntent);
        } else {
            builder.setWhen(System.currentTimeMillis());//会影响subText的时间(setUsesChronometer)
            builder.setShowWhen(true);
            builder.setUsesChronometer(true);//不能设置setSubText,不然不会显示时间
            builder.setOngoing(true);//禁止滑动删除
            builder.setAutoCancel(false);//禁止用户点击删除按钮删除
            builder.setPriority(NotificationCompat.PRIORITY_MAX);//会排到最上面
            builder.addAction(R.drawable.uamp_ic_pause_white_24dp, "", stopPendingIntent);
        }
        builder.addAction(R.drawable.ic_close_white_24dp, "", closePendingIntent);
        builder.setVisibility(Notification.VISIBILITY_PUBLIC);


        /*MediaSessionCompat mSession = new MediaSessionCompat(mContext, "NamofoRadio");
        mSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPrepare() {
                super.onPrepare();
                LogUtils.sout("MediaSessionCompat onPrepare");
            }

            @Override
            public void onPlay() {
                super.onPlay();
                LogUtils.sout("MediaSessionCompat onRadioPlay");
            }

            @Override
            public void onStop() {
                super.onStop();
                LogUtils.sout("MediaSessionCompat onStop");
            }

            @Override
            public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
                LogUtils.sout("onMediaButtonEvent onMediaButtonEvent");
                return super.onMediaButtonEvent(mediaButtonEvent);
            }

            @Override
            public void onCommand(String command, Bundle extras, ResultReceiver cb) {
                super.onCommand(command, extras, cb);
                LogUtils.sout("onMediaButtonEvent onCommand");
            }
        });
        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mSession.setActive(true);*/
        android.support.v7.app.NotificationCompat.MediaStyle style = new android.support.v7.app.NotificationCompat.MediaStyle()
                //.setMediaSession(mSession.getSessionToken())
                .setShowActionsInCompactView(0, 1);
        builder.setStyle(style);
        //builder.setColor(Palette.from(artwork).generate().getVibrantColor(Color.parseColor("#403f4d")));
        builder.setColor(mContext.getResources().getColor(R.color.colorPrimary));
        return builder.build();


        //mNotificationManager.notify(999, n);
        //loadView.setVisibility(View.GONE);
        //txtPlaying.setVisibility(View.VISIBLE);
    }

    public Notification createAudioNotification(boolean isStopped) {
        PendingIntent clickIntent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        Intent closeIntent = new Intent(mContext, PlayerService.class);
        closeIntent.setAction(AudioPlayer.AUDIO_CLOSE_ACTION);
        PendingIntent closePendingIntent = PendingIntent.getService(mContext, 0, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playIntent = new Intent(mContext, PlayerService.class);
        playIntent.setAction(AudioPlayer.AUDIO_PLAY_ACTION);
        playIntent.putExtra(AudioPlayer.EXTRA_NAME_AUDIO_DATA_SOURCE, "http://audio.xmcdn.com/group9/M0A/87/05/wKgDYldS66OhILGuAI7YXtdBSSk047.m4a");
        PendingIntent playPendingIntent = PendingIntent.getService(mContext, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent stopIntent = new Intent(mContext, PlayerService.class);
        stopIntent.setAction(AudioPlayer.AUDIO_STOP_ACTION);
        PendingIntent stopPendingIntent = PendingIntent.getService(mContext, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //MediaSessionCompat mSession = new MediaSessionCompat(getActivity(), "namofo");
        //mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        Bitmap artwork = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_empty_music2);
        NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(artwork)
                /**
                 *
                 * VISIBILITY_PUBLIC 只有在没有锁屏时会显示通知
                 * VISIBILITY_PRIVATE 任何情况都会显示通知
                 * VISIBILITY_SECRET 在安全锁和没有锁屏的情况下显示通知
                 */
                .setVisibility(android.support.v7.app.NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(clickIntent)
                .setContentTitle("录音名")
                .setContentText("录音作者")
                //.setWhen(System.currentTimeMillis())
                //.setCategory(NotificationCompat.CATEGORY_SERVICE)//制定通知的分类
                //.setCustomBigContentView()//指定展开时的视图
                .setGroupSummary(true)
                .setGroup(Constants.NOTIFICATION_GROUP_NAME);
        //.setSubText("YY直播")

        if (isStopped) {
            builder.setWhen(0);
            builder.setShowWhen(false);
            builder.setUsesChronometer(false);//停止计时
            builder.setOngoing(false);
            builder.setAutoCancel(true);
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);//不会显示到最上面
            builder.addAction(R.drawable.uamp_ic_play_arrow_white_24dp, "", playPendingIntent);
        } else {
            builder.setWhen(System.currentTimeMillis());//会影响subText的时间(setUsesChronometer)
            builder.setShowWhen(true);
            builder.setUsesChronometer(true);//不能设置setSubText,不然不会显示时间
            builder.setOngoing(true);//禁止滑动删除
            builder.setAutoCancel(false);//禁止用户点击删除按钮删除
            builder.setPriority(NotificationCompat.PRIORITY_MAX);//会排到最上面
            builder.addAction(R.drawable.uamp_ic_pause_white_24dp, "", stopPendingIntent);
        }
        builder.addAction(R.drawable.ic_close_white_24dp, "", closePendingIntent);
        builder.setVisibility(Notification.VISIBILITY_PUBLIC);


        /*MediaSessionCompat mSession = new MediaSessionCompat(mContext, "NamofoRadio");
        mSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPrepare() {
                super.onPrepare();
                LogUtils.sout("MediaSessionCompat onPrepare");
            }

            @Override
            public void onPlay() {
                super.onPlay();
                LogUtils.sout("MediaSessionCompat onRadioPlay");
            }

            @Override
            public void onStop() {
                super.onStop();
                LogUtils.sout("MediaSessionCompat onStop");
            }

            @Override
            public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
                LogUtils.sout("onMediaButtonEvent onMediaButtonEvent");
                return super.onMediaButtonEvent(mediaButtonEvent);
            }

            @Override
            public void onCommand(String command, Bundle extras, ResultReceiver cb) {
                super.onCommand(command, extras, cb);
                LogUtils.sout("onMediaButtonEvent onCommand");
            }
        });
        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mSession.setActive(true);*/
        android.support.v7.app.NotificationCompat.MediaStyle style = new android.support.v7.app.NotificationCompat.MediaStyle()
                //.setMediaSession(mSession.getSessionToken())
                .setShowActionsInCompactView(0, 1);
        builder.setStyle(style);
        //builder.setColor(Palette.from(artwork).generate().getVibrantColor(Color.parseColor("#403f4d")));
        builder.setColor(mContext.getResources().getColor(R.color.colorPrimary));
        return builder.build();


        //mNotificationManager.notify(999, n);
        //loadView.setVisibility(View.GONE);
        //txtPlaying.setVisibility(View.VISIBLE);
    }

    public Notification test1() {
        NotificationCompat.Builder builderOne = new NotificationCompat.Builder(mContext)
                .setContentTitle("title1")
                .setContentText("text1")
                .setGroupSummary(true)
                .setGroup("nomofo");
        return builderOne.build();
    }

    public Notification test2() {
        NotificationCompat.Builder builderOne = new NotificationCompat.Builder(mContext)
                .setContentTitle("title2")
                .setContentText("text2")
                .setGroupSummary(true)
                .setGroup("nomofo");
        return builderOne.build();
    }
}
