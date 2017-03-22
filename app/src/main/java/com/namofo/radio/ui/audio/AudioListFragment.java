package com.namofo.radio.ui.audio;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.namofo.radio.R;
import com.namofo.radio.adapter.AudioListAdapter;
import com.namofo.radio.base.BaseSwipeBackFragment;
import com.namofo.radio.common.IntentKey;
import com.namofo.radio.presenter.AudioListPresenter;
import com.namofo.radio.util.ToastUtils;
import com.namofo.radio.view.CustomPtrFrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Title: AudioListFragment
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/3/21  15:05
 *
 * @author 郑炯
 * @version 1.0
 */
public class AudioListFragment extends BaseSwipeBackFragment {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.ptr_layout)
    CustomPtrFrameLayout mPtrFrameLayout;

    private int mAlbumId;
    private String albumName;

    private AudioListPresenter mPresenter;
    private AudioListAdapter mAdapter;



    public static AudioListFragment newInstance(int albumId, String albumName) {

        Bundle args = new Bundle();

        AudioListFragment fragment = new AudioListFragment();
        fragment.setArguments(args);
        args.putInt(IntentKey.KEY_ALBUM_ID, albumId);
        args.putString(IntentKey.KEY_TITLE, albumName);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mAlbumId = savedInstanceState.getInt(IntentKey.KEY_ALBUM_ID, 0);
            albumName = savedInstanceState.getString(IntentKey.KEY_TITLE, getString(R.string.tab_record));
        } else {
            mAlbumId = getArguments().getInt(IntentKey.KEY_ALBUM_ID, 0);
            albumName = getArguments().getString(IntentKey.KEY_TITLE, getString(R.string.tab_record));
        }
        View view = inflater.inflate(R.layout.fragment_album_layout, container, false);
        ButterKnife.bind(this, view);
        initToolbar(mToolbar, albumName);
        initView();
        return attachToSwipeBack(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new AudioListAdapter();
        mPresenter = new AudioListPresenter();
        mRecyclerView.setAdapter(new RecyclerAdapterWithHF(mAdapter));


        mPtrFrameLayout.postDelayed(() -> {
            mPtrFrameLayout.autoRefresh(true);
        }, 100);
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);

        mPtrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refresh();
            }
        });
        mPtrFrameLayout.setLoadMoreEnable(false);//是否需要加载更多
        //mPtrFrameLayout.setAutoLoadMoreEnable(false);//是否自动加载 默认true
        //mPtrFrameLayout.setOnLoadMoreListener(AlbumListFragment.this::loadMore);
    }

    private void refresh() {
        mPresenter.refresh(mAlbumId, list -> {
            mAdapter.setData(list);
            mPtrFrameLayout.refreshComplete();
            //mPtrFrameLayout.setLoadMoreEnable(recordAlba.size() != 0);//是否需要加载更多
        }, s -> {
            ToastUtils.showShort(getContext(), s);
            mPtrFrameLayout.refreshComplete();
            mPtrFrameLayout.setLoadMoreEnable(false);//是否需要加载更多
        });
    }

    private void loadMore() {
        mPresenter.loadMore(mAlbumId, list -> {
            mAdapter.addData(list);
            mPtrFrameLayout.loadMoreComplete(list.size() != 0);
        }, s -> {
            ToastUtils.showShort(getContext(), s);
            mPtrFrameLayout.loadMoreComplete(false);
        });
    }
}
