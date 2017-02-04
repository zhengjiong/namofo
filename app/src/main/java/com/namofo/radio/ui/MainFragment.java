package com.namofo.radio.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.namofo.radio.R;
import com.namofo.radio.ui.base.BaseFragment;
import com.namofo.radio.event.StartBrotherEvent;
import com.namofo.radio.event.TabSelectedEvent;
import com.namofo.radio.ui.fragment.DownloadFragment;
import com.namofo.radio.ui.fragment.RadioFragment;
import com.namofo.radio.ui.fragment.RecordFragment;
import com.namofo.radio.ui.fragment.UserCenterFragment;
import com.namofo.radio.view.BottomBar;
import com.namofo.radio.view.BottomBarTab;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Description: 首页
 * Copyright:Copyright(c)2016
 * CreateTime:17/1/14  15:56
 *
 * @author 郑炯
 * @version 1.0
 */
public class MainFragment extends BaseFragment {
    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOUR = 3;

    private BaseFragment mFragments[] = new BaseFragment[4];

    private BottomBar mBottomBar;

    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        if (savedInstanceState == null) {
            mFragments[FIRST] = RadioFragment.newInstance();
            mFragments[SECOND] = RecordFragment.newInstance();
            mFragments[THIRD] = DownloadFragment.newInstance();
            mFragments[FOUR] = UserCenterFragment.newInstance();

            loadMultipleRootFragment(R.id.tab_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOUR]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用,也可以通过getChildFragmentManager.getFragments()自行进行判断查找(效率更高些),用下面的方法查找更方便些
            mFragments[FIRST] = findChildFragment(RadioFragment.class);
            mFragments[SECOND] = findChildFragment(RecordFragment.class);
            mFragments[THIRD] = findChildFragment(DownloadFragment.class);
            mFragments[FOUR] = findChildFragment(UserCenterFragment.class);
        }
        initView(view);
        EventBus.getDefault().register(this);
        return view;
    }

    private void initView(View view) {
        mBottomBar = (BottomBar) view.findViewById(R.id.bottomBar);
        mBottomBar
                .addItem(new BottomBarTab(_mActivity, R.mipmap.ic_home_white_24dp, getString(R.string.tab_radio)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.ic_account_circle_white_24dp, getString(R.string.tab_record)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.ic_discover_white_24dp, getString(R.string.tab_download)))
                .addItem(new BottomBarTab(_mActivity, R.mipmap.ic_message_white_24dp, getString(R.string.tab_user_center)));

        // 模拟未读消息
        //mBottomBar.getItem(FIRST).setUnreadCount(9);

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);

                /*BottomBarTab tab = mBottomBar.getItem(FIRST);
                if (position == FIRST) {
                    tab.setUnreadCount(0);
                } else {
                    tab.setUnreadCount(tab.getUnreadCount() + 1);
                }*/
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                // 这里推荐使用EventBus来实现 -> 解耦
                // 在FirstPagerFragment,FirstHomeFragment中接收, 因为是嵌套的Fragment
                // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
                EventBus.getDefault().post(new TabSelectedEvent(position));
            }
        });
    }

    @Subscribe
    public void onEventMainThread(StartBrotherEvent event) {
        start(event.targetFragment);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
