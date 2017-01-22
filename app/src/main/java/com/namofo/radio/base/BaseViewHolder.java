package com.namofo.radio.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Title: BaseViewHolder
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/1/22  15:06
 *
 * @author 郑炯
 * @version 1.0
 */
public class BaseViewHolder extends RecyclerView.ViewHolder{

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int id) {
        T result = (T) itemView.findViewById(id);
        if (result == null) {
            return null;
        }
        return result;
    }
}
