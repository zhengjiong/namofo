package com.namofo.radio.entity;

/**
 * Title: MeiZhi
 * Description:
 * Copyright:Copyright(c)2016
 * CreateTime:17/1/15  17:26
 *
 * @author 郑炯
 * @version 1.0
 */
public class MeiZhi {

    /**
     * _id : 5878471d421aa9119735ac13
     * createdAt : 2017-01-13T11:18:53.183Z
     * desc : 1-13
     * publishedAt : 2017-01-13T11:58:16.991Z
     * source : chrome
     * type : 福利
     * url : http://ww3.sinaimg.cn/large/610dc034gw1fbou2xsqpaj20u00u0q4h.jpg
     * used : true
     * who : daimajia
     */

    public String _id;
    public String createdAt;
    public String desc;
    public String publishedAt;
    public String source;
    public String type;
    public String url;
    public boolean used;
    public String who;

    @Override
    public String toString() {
        return "MeiZhi{" +
                "_id='" + _id + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", desc='" + desc + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", source='" + source + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", used=" + used +
                ", who='" + who + '\'' +
                '}';
    }
}
