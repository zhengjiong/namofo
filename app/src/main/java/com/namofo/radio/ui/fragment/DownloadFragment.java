package com.namofo.radio.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.namofo.radio.R;
import com.namofo.radio.base.BaseFragment;
import com.namofo.radio.base.FragmentAdapter;

import java.util.ArrayList;
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
public class DownloadFragment extends BaseFragment {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.tab)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    private FragmentAdapter mFragmentAdapter;
    @NonNull
    private List<String> mTitles = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();

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
        init();
        return view;
    }

    private void init() {
        mToolbar.setTitle(R.string.tab_download);

        mTitles.add(getString(R.string.has_download));
        mTitles.add(getString(R.string.undownload));

        mFragments.add(DownloadedFragment.newInstance());
        mFragments.add(UnDownloadFragment.newInstance());

        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        mFragmentAdapter = new FragmentAdapter(getChildFragmentManager(), mFragments, mTitles);
        viewpager.setAdapter(mFragmentAdapter);
        viewpager.setOffscreenPageLimit(mFragments.size());
        tabLayout.setupWithViewPager(viewpager);
    }


}
