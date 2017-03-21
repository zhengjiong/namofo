package com.namofo.radio.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.namofo.radio.R;
import com.namofo.radio.base.BaseViewHolder;

/**
 * Title: AudioItemViewHolder
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/3/21  16:18
 *
 * @author 郑炯
 * @version 1.0
 */
public class AudioItemViewHolder extends BaseViewHolder {
    public TextView tvNumber;
    public TextView title;
    public TextView tvDate;
    public TextView tvTime;

    public AudioItemViewHolder(View itemView) {
        super(itemView);
        tvNumber = (TextView) itemView.findViewById(R.id.tv_number);
        title = (TextView) itemView.findViewById(R.id.title);
        tvDate = (TextView) itemView.findViewById(R.id.tv_date);
        tvTime = (TextView) itemView.findViewById(R.id.tv_time);
    }
}
