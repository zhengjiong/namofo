package com.namofo.radio.event;

import com.namofo.radio.base.BaseFragment;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by YoKeyword on 16/6/30.
 */
public class StartBrotherEvent {
    public BaseFragment targetFragment;

    public StartBrotherEvent(BaseFragment targetFragment) {
        this.targetFragment = targetFragment;
    }
}
