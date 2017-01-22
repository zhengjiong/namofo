package com.namofo.radio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.namofo.radio.R;
import com.namofo.radio.base.BaseFragment;

/**
 * Description: 直播
 * CreateTime:17/1/14  16:21
 *
 * @author 郑炯
 * @version 1.0
 */
public class RadioFragment extends BaseFragment {
    private Toolbar mToolbar;

    public static RadioFragment newInstance() {

        Bundle args = new Bundle();

        RadioFragment fragment = new RadioFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_radio_layout, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.tab_radio);
    }
}
