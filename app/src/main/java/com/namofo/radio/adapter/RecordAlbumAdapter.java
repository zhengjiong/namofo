package com.namofo.radio.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.namofo.radio.R;
import com.namofo.radio.entity.RecordAlbum;
import com.namofo.radio.util.ToastUtils;
import com.namofo.radio.viewholder.RecordAlbumViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Title: RecordAlbumAdapter
 * CreateTime:17/1/22  14:04
 *
 * @author 郑炯
 * @version 1.0
 */
public class RecordAlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<RecordAlbum> mList = new ArrayList<>();

    @Override
    public RecordAlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecordAlbumViewHolder(inflater(R.layout.item_meizhi_list_layout, parent));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        RecordAlbumViewHolder recordAlbumViewHolder = (RecordAlbumViewHolder) holder;
        RecordAlbum album = getItem(position);
        recordAlbumViewHolder.title.setText(album.bum_name);
        recordAlbumViewHolder.tvAuthor.setText("讲师："+album.author);
        recordAlbumViewHolder.tvCount.setText("专辑数量："+album.audios);
        recordAlbumViewHolder.tvDate.setText("更新时间："+album.mdate);
        Glide.with(holder.itemView.getContext())
                .load(album.bum_face_url)
                .into(recordAlbumViewHolder.imageView);

        recordAlbumViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort(recordAlbumViewHolder.itemView.getContext(), "即将推出,敬请期待!");
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

    public RecordAlbum getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    public void setData(List<RecordAlbum> list){
        if (list != null) {
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void addData(List<RecordAlbum> list){
        if (list != null) {
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }
}
