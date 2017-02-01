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
 * Title: RadioFragment
 * Description: 录音
 * Copyright:Copyright(c)2016
 * CreateTime:17/1/14  16:21
 *
 * @author 郑炯
 * @version 1.0
 */
public class RecordFragment extends BaseFragment {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.ptr_layout)
    CustomPtrFrameLayout mPtrFrameLayout;

    private MeiZhiPresenter mPresenter;

    private MeiZhiAdapter mAdapter;
    private RecyclerAdapterWithHF mPtrAdapter;

    public static RecordFragment newInstance() {

        Bundle args = new Bundle();

        RecordFragment fragment = new RecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_record_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new MeiZhiPresenter();
        mAdapter = new MeiZhiAdapter();
        mPtrAdapter = new RecyclerAdapterWithHF(mAdapter);
        mRecyclerView.setAdapter(mPtrAdapter);
        /*OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://gank.io/api/")
                .build();

        ProgressDialog progressDialog = new ProgressDialog(getContext());

        HttpService httpService = retrofit.create(HttpService.class);
        Observable<BaseResultEntity<List<MeiZhi>>> observable = httpService.getMeizhi(1);
        observable.delay(2000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResultEntity<List<MeiZhi>>>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("onFinish");
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError call " + e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResultEntity<List<MeiZhi>> response) {
                        System.out.println("onNext call " + response.results.toString());
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        progressDialog.show();
                    }

                });*/

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


    private void initView(View view) {
        mToolbar.setTitle(R.string.tab_record);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);

        mPtrFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                refresh();
            }
        });
        mPtrFrameLayout.setAutoLoadMoreEnable(true);
        mPtrFrameLayout.setOnLoadMoreListener(RecordFragment.this::loadMore);
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
}
