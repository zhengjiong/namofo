package com.namofo.radio.event;

import com.namofo.radio.ui.base.BaseFragment;

/**
 * Created by YoKeyword on 16/6/30.
 */
public class StartBrotherEvent {
    public BaseFragment targetFragment;

    public StartBrotherEvent(BaseFragment targetFragment) {
        this.targetFragment = targetFragment;
    }
}
