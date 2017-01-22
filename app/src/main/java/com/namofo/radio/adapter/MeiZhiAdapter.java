package com.namofo.radio.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.namofo.radio.R;
import com.namofo.radio.base.BaseRecyclerViewAdapter;
import com.namofo.radio.base.BaseViewHolder;
import com.namofo.radio.entity.MeiZhi;



/**
 * Title: MeiZhiAdapter
 * CreateTime:17/1/22  14:04
 *
 * @author 郑炯
 * @version 1.0
 */
public class MeiZhiAdapter extends BaseRecyclerViewAdapter<MeiZhi> {

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(inflater(R.layout.item_meizhi_list_layout, parent));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        TextView textView = holder.getView(R.id.title);

        MeiZhi meiZhi = getItem(position);
        textView.setText(meiZhi.desc);
    }
}
