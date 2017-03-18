package com.namofo.radio.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.namofo.radio.R;
import com.namofo.radio.base.BaseViewHolder;

/**
 * Title: RecordAlbumViewHolder
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/1/23  17:12
 *
 * @author 郑炯
 * @version 1.0
 */
public class RecordAlbumViewHolder extends BaseViewHolder {
    public ImageView imageView;
    public TextView title;
    public TextView tvAuthor;
    public TextView tvCount;
    public TextView tvDate;

    public RecordAlbumViewHolder(View itemView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.img);
        title = (TextView) itemView.findViewById(R.id.title);
        tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
        tvCount = (TextView) itemView.findViewById(R.id.tv_count);
        tvDate = (TextView) itemView.findViewById(R.id.tv_date);
    }
}
