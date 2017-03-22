package com.namofo.radio.base;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.namofo.radio.viewholder.RecordAlbumViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: MyRecyclerAdapterWithHF
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/3/21  17:59
 *
 * @author 郑炯
 * @version 1.0
 */
public class MyRecyclerAdapterWithHF extends RecyclerView.Adapter<BaseViewHolder>{
    public static final int TYPE_MANAGER_OTHER = 0;
    public static final int TYPE_MANAGER_LINEAR = 1;
    public static final int TYPE_MANAGER_GRID = 2;
    public static final int TYPE_MANAGER_STAGGERED_GRID = 3;

    public static final int TYPE_HEADER = 7898;
    public static final int TYPE_FOOTER = 7899;

    private List<View> mHeaders = new ArrayList<View>();
    private List<View> mFooters = new ArrayList<View>();

    private int mManagerType;

    public int getHeadSize() {
        return mHeaders.size();
    }

    public int getFootSize() {
        return mFooters.size();
    }

    public int getManagerType() {
        return mManagerType;
    }

    public void notifyDataSetChangedHF() {
        notifyDataSetChanged();
    }

    public void notifyItemChangedHF(int position) {
        notifyItemChanged(getRealPosition(position));
    }

    public void notifyItemMovedHF(int fromPosition, int toPosition) {
        notifyItemMovedHF(getRealPosition(fromPosition), getRealPosition(toPosition));
    }

    public void notifyItemRangeChangedHF(int positionStart, int itemCount) {
        notifyItemRangeChanged(getRealPosition(positionStart), itemCount);
    }

    public void notifyItemRangeRemovedHF(int positionStart, int itemCount) {
        notifyItemRangeRemoved(getRealPosition(positionStart), itemCount);
    }

    public void notifyItemRemovedHF(int position) {
        notifyItemRemoved(getRealPosition(position));
    }

    public void notifyItemInsertedHF(int position) {
        notifyItemInserted(getRealPosition(position));
    }

    public void notifyItemRangeInsertedHF(int positionStart, int itemCount) {
        notifyItemRangeInserted(getRealPosition(positionStart), itemCount);
    }

    @Override
    public final long getItemId(int position) {
        return getItemIdHF(getRealPosition(position));
    }

    public long getItemIdHF(int position) {
        return mAdapter.getItemId(position);
    }

    public BaseViewHolder onCreateViewHolderHF(ViewGroup viewGroup, int type) {
        return mAdapter.onCreateViewHolder(viewGroup, type);
    }

    @Override
    public final BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        // if our position is one of our items (this comes from
        // getItemViewType(int position) below)
        if (type != TYPE_HEADER && type != TYPE_FOOTER) {
            BaseViewHolder vh = onCreateViewHolderHF(viewGroup, type);
            return vh;
            // else we have a header/footer
        } else {
            // create a new framelayout, or inflate from a resource
            FrameLayout frameLayout = new FrameLayout(viewGroup.getContext());
            // make sure it fills the space
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                    .LayoutParams.WRAP_CONTENT));
            return new MyRecyclerAdapterWithHF.HeaderFooterViewHolder(frameLayout);
        }
    }

    @Override
    public final void onBindViewHolder(final BaseViewHolder vh, int position) {
        // check what type of view our position is
        if (isHeader(position)) {
            View v = mHeaders.get(position);
            // add our view to a header view and display it
            prepareHeaderFooter((HeaderFooterViewHolder) vh, v);
        } else if (isFooter(position)) {
            View v = mFooters.get(position - getItemCountHF() - mHeaders.size());
            // add our view to a footer view and display it
            prepareHeaderFooter((HeaderFooterViewHolder) vh, v);
        } else {
            vh.itemView.setOnClickListener(new MyRecyclerAdapterWithHF.MyOnClickListener(vh));
            vh.itemView.setOnLongClickListener(new MyRecyclerAdapterWithHF.MyOnLongClickListener(vh));
            // it's one of our items, display as required
            onBindViewHolderHF(vh, getRealPosition(position));
        }
    }

    public int getRealPosition(int position) {
        return position - mHeaders.size();
    }

    public void onBindViewHolderHF(BaseViewHolder vh, int position) {
        mAdapter.onBindViewHolder(vh, position);
    }

    private void prepareHeaderFooter(MyRecyclerAdapterWithHF.HeaderFooterViewHolder vh, View view) {

        // if it's a staggered grid, span the whole layout
        if (mManagerType == TYPE_MANAGER_STAGGERED_GRID) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = new StaggeredGridLayoutManager.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setFullSpan(true);
            vh.itemView.setLayoutParams(layoutParams);
        }

        // if the view already belongs to another layout, remove it
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }

        // empty out our FrameLayout and replace with our header/footer
        vh.base.removeAllViews();
        vh.base.addView(view);

    }

    private boolean isHeader(int position) {
        return (position < mHeaders.size());
    }

    private boolean isFooter(int position) {
        return (position >= mHeaders.size() + getItemCountHF());
    }

    @Override
    public final int getItemCount() {
        return mHeaders.size() + getItemCountHF() + mFooters.size();
    }

    public int getItemCountHF() {
        return mAdapter.getItemCount();
    }

    @Override
    public final int getItemViewType(int position) {
        // check what type our position is, based on the assumption that the
        // order is headers > items > footers
        if (isHeader(position)) {
            return TYPE_HEADER;
        } else if (isFooter(position)) {
            return TYPE_FOOTER;
        }
        int type = getItemViewTypeHF(getRealPosition(position));
        if (type == TYPE_HEADER || type == TYPE_FOOTER) {
            throw new IllegalArgumentException("Item type cannot equal " + TYPE_HEADER + " or " + TYPE_FOOTER);
        }
        return type;
    }

    public int getItemViewTypeHF(int position) {
        return mAdapter.getItemViewType(position);
    }

    // add a header to the adapter
    public void addHeader(View header) {
        if (!mHeaders.contains(header)) {
            mHeaders.add(header);
            // animate
            notifyItemInserted(mHeaders.size() - 1);
        }
    }

    // remove a header from the adapter
    public void removeHeader(View header) {
        if (mHeaders.contains(header)) {
            // animate
            notifyItemRemoved(mHeaders.indexOf(header));
            mHeaders.remove(header);
        }
    }

    // add a footer to the adapter
    public void addFooter(View footer) {
        if (!mFooters.contains(footer)) {
            mFooters.add(footer);
            // animate
            notifyItemInserted(mHeaders.size() + getItemCountHF() + mFooters.size() - 1);
        }
    }

    // remove a footer from the adapter
    public void removeFooter(View footer) {
        if (mFooters.contains(footer)) {
            // animate
            notifyItemRemoved(mHeaders.size() + getItemCountHF() + mFooters.indexOf(footer));
            mFooters.remove(footer);
        }
    }

    // our header/footer RecyclerView.ViewHolder is just a FrameLayout
    public static class HeaderFooterViewHolder extends BaseViewHolder {
        FrameLayout base;

        public HeaderFooterViewHolder(View itemView) {
            super(itemView);
            base = (FrameLayout) itemView;
        }
    }

    public MyRecyclerAdapterWithHF.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(MyRecyclerAdapterWithHF.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        Log.d("eeee", "setOnItemClickListener " + this.onItemClickListener);
    }

    public MyRecyclerAdapterWithHF.OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(MyRecyclerAdapterWithHF.OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    private MyRecyclerAdapterWithHF.OnItemClickListener onItemClickListener;

    private MyRecyclerAdapterWithHF.OnItemLongClickListener onItemLongClickListener;

    private class MyOnClickListener implements View.OnClickListener {
        private RecyclerView.ViewHolder vh;

        public MyOnClickListener(RecyclerView.ViewHolder vh) {
            super();
            this.vh = vh;
        }

        @Override
        public void onClick(View v) {
            int position = getRealPosition(vh.getLayoutPosition());
            if (MyRecyclerAdapterWithHF.this.onItemClickListener != null) {
                MyRecyclerAdapterWithHF.this.onItemClickListener.onItemClick(MyRecyclerAdapterWithHF.this, vh, position);
            }
            onItemClick(vh, position);
        }
    }

    private class MyOnLongClickListener implements View.OnLongClickListener {
        private RecyclerView.ViewHolder vh;

        public MyOnLongClickListener(RecyclerView.ViewHolder vh) {
            super();
            this.vh = vh;
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getRealPosition(vh.getLayoutPosition());
            if (MyRecyclerAdapterWithHF.this.onItemLongClickListener != null) {
                MyRecyclerAdapterWithHF.this.onItemLongClickListener.onItemLongClick(MyRecyclerAdapterWithHF.this, vh,
                        position);
            }
            onItemLongClick(vh, position);
            return true;
        }

    }

    protected void onItemClick(RecyclerView.ViewHolder vh, int position) {

    }

    protected void onItemLongClick(RecyclerView.ViewHolder vh, int position) {

    }

    public static interface OnItemClickListener {
        void onItemClick(MyRecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position);
    }

    public static interface OnItemLongClickListener {
        void onItemLongClick(MyRecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position);
    }

    private BaseRecyclerViewAdapter<Object, BaseViewHolder> mAdapter;

    public MyRecyclerAdapterWithHF(BaseRecyclerViewAdapter<Object, BaseViewHolder> adapter) {
        super();
        this.mAdapter = adapter;
        adapter.registerAdapterDataObserver(adapterDataObserver);
    }

    private RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            notifyItemRangeChanged(positionStart + getHeadSize(), itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            notifyItemRangeInserted(positionStart + getHeadSize(), itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            notifyItemRangeRemoved(positionStart + getHeadSize(), itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            notifyItemMoved(fromPosition + getHeadSize(), toPosition + getHeadSize());
        }
    };
}
