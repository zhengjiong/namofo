package com.namofo.radio.entity;

import java.util.List;

/**
 * Title: RetrofitEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/1/15  16:03
 *
 * @author 郑炯
 * @version 1.0
 */
public class RetrofitEntity<T> {
    public String error;
    public List<T> results;

    @Override
    public String toString() {
        return "RetrofitEntity{" +
                "error='" + error + '\'' +
                ", results=" + results +
                '}';
    }
}
