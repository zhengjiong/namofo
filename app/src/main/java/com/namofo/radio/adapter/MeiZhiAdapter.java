package com.namofo.radio.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.namofo.radio.R;
import com.namofo.radio.entity.MeiZhi;
import com.namofo.radio.viewholder.MeiZhiViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Title: MeiZhiAdapter
 * CreateTime:17/1/22  14:04
 *
 * @author 郑炯
 * @version 1.0
 */
public class MeiZhiAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<MeiZhi> mList = new ArrayList<>();

    @Override
    public MeiZhiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MeiZhiViewHolder(inflater(R.layout.item_meizhi_list_layout, parent));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MeiZhiViewHolder meiZhiViewHolder = (MeiZhiViewHolder) holder;
        MeiZhi meiZhi = getItem(position);
        meiZhiViewHolder.title.setText(meiZhi.desc);

        Glide.with(holder.itemView.getContext())
                .load(meiZhi.url)
                .into(meiZhiViewHolder.imageView);

        meiZhiViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public View inflater(int layoutRes, ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public MeiZhi getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    public void setData(List<MeiZhi> list){
        if (list != null) {
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void addData(List<MeiZhi> list){
        if (list != null) {
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }
}
