package com.namofo.radio.ui;

import android.os.Bundle;
import android.util.Log;

import com.namofo.radio.R;
import com.namofo.radio.base.BaseActivity;

import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.helper.FragmentLifecycleCallbacks;

/**
 * Description:
 * Copyright:Copyright(c)2016
 * CreateTime:17/1/14  16:06
 * @author 郑炯
 * @version 1.0
 */

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            loadRootFragment(R.id.frame_holder, MainFragment.newInstance());
        }

        registerFragmentLifecycleCallbacks(new FragmentLifecycleCallbacks(){

            @Override
            public void onFragmentSupportVisible(SupportFragment fragment) {
                Log.i("MainActivity", "onFragmentSupportVisible--->" + fragment.getClass().getSimpleName());
            }

            @Override
            public void onFragmentCreated(SupportFragment fragment, Bundle savedInstanceState) {
                super.onFragmentCreated(fragment, savedInstanceState);
                Log.i("MainActivity", "onFragmentCreated--->" + fragment.getClass().getSimpleName());
            }

            @Override
            public void onFragmentDetached(SupportFragment fragment) {
                super.onFragmentDetached(fragment);
                Log.i("MainActivity", "onFragmentDetached--->" + fragment.getClass().getSimpleName());
            }

        });
    }
}
