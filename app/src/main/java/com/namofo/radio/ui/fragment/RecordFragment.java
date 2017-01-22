package com.namofo.radio.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.namofo.radio.R;
import com.namofo.radio.base.BaseFragment;
import com.namofo.radio.entity.MeiZhi;
import com.namofo.radio.entity.api.BaseResultEntity;
import com.namofo.radio.http.HttpService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Title: RadioFragment
 * Description: 直播
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
    RecyclerView mRecyclerview;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

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

    private void initView(View view) {
        mToolbar.setTitle(R.string.tab_record);
    }
}
