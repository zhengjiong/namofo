package com.namofo.radio.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.namofo.radio.R;
import com.namofo.radio.base.BaseViewHolder;

/**
 * Title: MeiZhiViewHolder
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/1/23  17:12
 *
 * @author 郑炯
 * @version 1.0
 */
public class MeiZhiViewHolder extends BaseViewHolder {
    public ImageView imageView;
    public TextView title;

    public MeiZhiViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.img);
        title = (TextView) itemView.findViewById(R.id.title);
    }
}
