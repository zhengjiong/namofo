package com.namofo.radio.db.greendao3.generator;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Description: 用于生成radio表
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/1/25  14:43
 *
 * @author 郑炯
 * @version 1.0
 */
@Entity
public class Radio {
    @Id
    private long id;

    private String downloadUrl;

    @Generated(hash = 1050392752)
    public Radio(long id, String downloadUrl) {
        this.id = id;
        this.downloadUrl = downloadUrl;
    }

    @Generated(hash = 29091545)
    public Radio() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDownloadUrl() {
        return this.downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

}
