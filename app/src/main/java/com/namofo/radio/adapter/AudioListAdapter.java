package com.namofo.radio.adapter;

import android.view.ViewGroup;

import com.namofo.radio.R;
import com.namofo.radio.base.BaseRecyclerViewAdapter;
import com.namofo.radio.common.Constants;
import com.namofo.radio.entity.Audio;
import com.namofo.radio.util.ToastUtils;
import com.namofo.radio.viewholder.AudioItemViewHolder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Title: AudioListAdapter
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/3/21  16:17
 *
 * @author 郑炯
 * @version 1.0
 */
public class AudioListAdapter<VH extends AudioItemViewHolder> extends BaseRecyclerViewAdapter<Audio, VH> {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.HHMMSS);

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return (VH) new AudioItemViewHolder(inflater(R.layout.item_audio, parent));
    }

    @Override
    public void onBindViewHolder(AudioItemViewHolder holder, int position) {
        Audio audio = getItem(position);
        if (position == 0) {
            audio.audio_url = "http://audio.xmcdn.com/group26/M09/40/E6/wKgJWFjNXojitlrpABPwktDPeiM430.m4a";
        } else if (position == 1) {
            audio.audio_url = "http://audio.xmcdn.com/group26/M04/72/06/wKgJRljSLbbyNhCsABDiLBsYrB8877.m4a";
        } else if (position == 2) {
            audio.audio_url = "http://audio.xmcdn.com/group11/M0B/2B/EB/wKgDa1WJF7uiSZCOACtJAJN7dYU376.m4a";
        }
        holder.tvNumber.setText(String.valueOf(position + 1));
        holder.title.setText(audio.audio_name);
        holder.tvDate.setText(audio.mdate);
        holder.tvTime.setText(simpleDateFormat.format(new Date(audio.audio_second)));

        holder.itemView.setOnClickListener(v -> {
            ToastUtils.showShort(holder.itemView.getContext(), "即将推出,敬请期待!");
        });
    }
}
