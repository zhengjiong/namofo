package com.namofo.radio.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.namofo.radio.R;
import com.namofo.radio.base.BaseRecyclerViewAdapter;
import com.namofo.radio.entity.Album;
import com.namofo.radio.event.StartBrotherEvent;
import com.namofo.radio.ui.base.RxFragment;
import com.namofo.radio.ui.audio.AudioListFragment;
import com.namofo.radio.viewholder.RecordAlbumViewHolder;

import org.greenrobot.eventbus.EventBus;


/**
 * Title: AlbumListAdapter
 * CreateTime:17/1/22  14:04
 *
 * @author 郑炯
 * @version 1.0
 */
public class AlbumListAdapter<VH extends RecordAlbumViewHolder> extends BaseRecyclerViewAdapter<Album, VH> {
    private RxFragment fragment;

    public AlbumListAdapter(Context context, RxFragment fragment) {
        super(context);
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
        holder.tvAuthor.setText("讲师：" + album.author);
        holder.tvCount.setText("专辑数量：" + album.audios);
        holder.tvDate.setText("更新时间：" + album.mdate);
        Glide.with(holder.itemView.getContext())
                .load(album.bum_face_url)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            Album item = getItem(holder.getAdapterPosition());
            EventBus.getDefault().post(new StartBrotherEvent(
                    AudioListFragment.newInstance(item.id, holder.title.getText().toString())));
        });
    }
}
