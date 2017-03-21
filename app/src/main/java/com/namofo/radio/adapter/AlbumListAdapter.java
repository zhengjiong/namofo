package com.namofo.radio.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.namofo.radio.R;
import com.namofo.radio.base.BaseRecyclerViewAdapter;
import com.namofo.radio.entity.Album;
import com.namofo.radio.ui.base.BaseFragment;
import com.namofo.radio.ui.audio.AudioListFragment;
import com.namofo.radio.viewholder.RecordAlbumViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Title: AlbumListAdapter
 * CreateTime:17/1/22  14:04
 *
 * @author 郑炯
 * @version 1.0
 */
public class AlbumListAdapter<VH extends RecordAlbumViewHolder> extends BaseRecyclerViewAdapter<Album, VH> {
    private BaseFragment fragment;

    public AlbumListAdapter(BaseFragment fragment){
        this.fragment = fragment;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return (VH) new RecordAlbumViewHolder(inflater(R.layout.item_album, parent));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        Album album = getItem(position);
        holder.title.setText(album.bum_name);
        holder.tvAuthor.setText("讲师："+album.author);
        holder.tvCount.setText("专辑数量："+album.audios);
        holder.tvDate.setText("更新时间："+album.mdate);
        Glide.with(holder.itemView.getContext())
                .load(album.bum_face_url)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            Album item = getItem(holder.getAdapterPosition());
            fragment.start(AudioListFragment.newInstance(item.id));
        });
    }
}
