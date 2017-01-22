package com.namofo.radio.model;

import com.namofo.radio.entity.api.BasePostEntity;
import com.namofo.radio.http.HttpManager;

/**
 * Title: BaseModel
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/1/22  18:03
 *
 * @author 郑炯
 * @version 1.0
 */
public class BaseModel {

    public void doHttp(BasePostEntity postEntity) {
        HttpManager httpManager = HttpManager.getInstance();
        httpManager.doHttp(postEntity);
    }
}
