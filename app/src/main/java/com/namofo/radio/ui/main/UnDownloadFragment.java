package com.namofo.radio.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.namofo.radio.R;
import com.namofo.radio.ui.base.RxFragment;
import com.namofo.radio.view.CustomPtrFrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Title: UnDownloadFragment
 * Description:
 * Copyright:Copyright(c)2016
 * CreateTime:17/1/25  10:07
 *
 * @author 郑炯
 * @version 1.0
 */
public class UnDownloadFragment extends RxFragment {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.ptr_layout)
    CustomPtrFrameLayout ptrLayout;

    public static UnDownloadFragment newInstance() {

        Bundle args = new Bundle();

        UnDownloadFragment fragment = new UnDownloadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ptr_recyclerview_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
