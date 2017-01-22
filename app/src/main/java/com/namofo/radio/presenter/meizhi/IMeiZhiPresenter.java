package com.namofo.radio.presenter.meizhi;

import com.namofo.radio.entity.MeiZhi;

import java.util.List;

/**
 * Title: IMeiZhiPresenter
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/1/22  12:19
 *
 * @author 郑炯
 * @version 1.0
 */
public interface IMeiZhiPresenter {
    void refresh();
    void loadMore();
    void loadSuccess(List<MeiZhi> list);
    void loadFailure();
    void loadComplete();
}
