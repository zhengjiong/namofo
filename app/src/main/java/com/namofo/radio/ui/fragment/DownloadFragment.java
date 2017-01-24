package com.namofo.radio.ui.fragment;

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
import com.namofo.radio.adapter.MeiZhiAdapter;
import com.namofo.radio.base.BaseFragment;
import com.namofo.radio.presenter.MeiZhiPresenter;
import com.namofo.radio.util.ToastUtils;
import com.namofo.radio.view.CustomPtrFrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description: 下载管理
 * CreateTime:17/1/14  16:21
 *
 * @author 郑炯
 * @version 1.0
 */
public class DownloadFragment extends BaseFragment {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.ptr_layout)
    CustomPtrFrameLayout mPtrFrameLayout;

    private MeiZhiPresenter mPresenter;

    private MeiZhiAdapter mAdapter;
    private RecyclerAdapterWithHF mPtrAdapter;

    public static DownloadFragment newInstance() {

        Bundle args = new Bundle();

        DownloadFragment fragment = new DownloadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_download_layout, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mToolbar.setTitle(R.string.tab_download);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);

        mPtrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refresh();
            }
        });
        mPtrFrameLayout.setAutoLoadMoreEnable(true);
        mPtrFrameLayout.setOnLoadMoreListener(DownloadFragment.this::loadMore);
    }

    private void loadMore() {
        mPresenter.loadMore(meiZhis -> {
            mAdapter.addData(meiZhis);
            mPtrFrameLayout.loadMoreComplete(meiZhis.size() != 0);
        }, s -> {
            ToastUtils.showShort(getContext(), s);
            mPtrFrameLayout.loadMoreComplete(false);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPresenter = new MeiZhiPresenter();
        mAdapter = new MeiZhiAdapter();
        mPtrAdapter = new RecyclerAdapterWithHF(mAdapter);
        mRecyclerView.setAdapter(mPtrAdapter);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mPtrFrameLayout.postDelayed(() -> {
            mPtrFrameLayout.autoRefresh(true);
        }, 100);
    }

    private void refresh() {
        mPresenter.refresh(meiZhis -> {
            mAdapter.setData(meiZhis);
            mPtrFrameLayout.refreshComplete();
            mPtrFrameLayout.setLoadMoreEnable(meiZhis.size() != 0);
        }, s -> {
            ToastUtils.showShort(getContext(), s);
            mPtrFrameLayout.refreshComplete();
            mPtrFrameLayout.setLoadMoreEnable(false);
        });
    }

}
