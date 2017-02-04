package com.namofo.radio.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.namofo.radio.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: BaseRecyclerViewAdapter
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/1/22  15:05
 *
 * @author 郑炯
 * @version 1.0
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    private ArrayList<T> mList = new ArrayList<T>();

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public View inflater(int layoutRes, ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    public T getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    public void setData(List<T> list){
        if (list != null) {
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void addData(List<T> list){
        if (list != null) {
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }
}
