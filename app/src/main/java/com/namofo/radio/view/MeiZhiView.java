package com.namofo.radio.view;

import com.namofo.radio.entity.MeiZhi;

import java.util.List;

/**
 * Title: MeiZhiView
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/1/22  12:08
 *
 * @author 郑炯
 * @version 1.0
 */
public interface MeiZhiView {


    public void showProgress();
    public void loadFailure();
    public void loadComplete();

    public void addData(List<MeiZhi> list);
    public void loadMore(List<MeiZhi> list);
}
