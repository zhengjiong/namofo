package com.namofo.radio.listener;

/**
 * Description: 成功回调处理
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/1/19  17:50
 *
 * @author 郑炯
 * @version 1.0
 */
public interface HttpOnNextListener<T> {
    void onNext(T t);
}
