package com.namofo.radio.entity.api;

import java.util.List;

/**
 * Title: BaseResultEntity
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/1/17  16:55
 *
 * @author 郑炯
 * @version 1.0
 */
public class BaseResultEntity<T> {
    public boolean error;
    public String message = "请求失败";
    public T result;
}
