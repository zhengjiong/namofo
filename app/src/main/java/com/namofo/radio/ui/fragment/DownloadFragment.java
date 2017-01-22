package com.namofo.radio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.namofo.radio.R;
import com.namofo.radio.adapter.MeiZhiAdapter;
import com.namofo.radio.base.BaseFragment;
import com.namofo.radio.entity.MeiZhi;
import com.namofo.radio.presenter.meizhi.MeiZhiPresenter;
import com.namofo.radio.view.MeiZhiView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description: 下载管理
 * CreateTime:17/1/14  16:21
 *
 * @author 郑炯
 * @version 1.0
 */
public class DownloadFragment extends BaseFragment implements MeiZhiView {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;

    private MeiZhiPresenter mPresenter;

    private MeiZhiAdapter mAdapter;

    public static DownloadFragment newInstance() {

        Bundle args = new Bundle();

        DownloadFragment fragment = new DownloadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_radio_layout, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mToolbar.setTitle(R.string.tab_download);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerview.setHasFixedSize(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new MeiZhiPresenter(this);


        mAdapter = new MeiZhiAdapter();
        mRecyclerview.setAdapter(mAdapter);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mPresenter.refresh();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void loadFailure() {

    }

    @Override
    public void loadComplete() {

    }

    @Override
    public void addData(List<MeiZhi> list) {
        mAdapter.setData(list);
    }

    @Override
    public void loadMore(List<MeiZhi> list) {
        mAdapter.addData(list);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.detachView();
    }
}
