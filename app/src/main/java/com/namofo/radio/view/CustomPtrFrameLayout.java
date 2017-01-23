package com.namofo.radio.view;

import android.content.Context;
import android.util.AttributeSet;

import com.chanven.lib.cptr.PtrClassicDefaultHeader;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.DefaultLoadMoreViewFooter;
import com.chanven.lib.cptr.loadmore.ILoadMoreViewFactory;

/**
 * Title: CustomPtrFrameLayout
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/1/23  17:51
 *
 * @author 郑炯
 * @version 1.0
 */
public class CustomPtrFrameLayout extends PtrFrameLayout {

    private PtrClassicDefaultHeader mPtrClassicHeader;

    public CustomPtrFrameLayout(Context context) {
        super(context);
        initViews();
    }

    public CustomPtrFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public CustomPtrFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    private void initViews() {
        mPtrClassicHeader = new PtrClassicDefaultHeader(getContext());
        setHeaderView(mPtrClassicHeader);
        addPtrUIHandler(mPtrClassicHeader);

        ILoadMoreViewFactory loadMoreViewFactory = new DefaultLoadMoreViewFooter();
        setFooterView(loadMoreViewFactory);
    }

    public PtrClassicDefaultHeader getHeader() {
        return mPtrClassicHeader;
    }

    /**
     * Specify the last update time by this key string
     *
     * @param key
     */
    public void setLastUpdateTimeKey(String key) {
        if (mPtrClassicHeader != null) {
            mPtrClassicHeader.setLastUpdateTimeKey(key);
        }
    }

    /**
     * Using an object to specify the last update time.
     *
     * @param object
     */
    public void setLastUpdateTimeRelateObject(Object object) {
        if (mPtrClassicHeader != null) {
            mPtrClassicHeader.setLastUpdateTimeRelateObject(object);
        }
    }
}
