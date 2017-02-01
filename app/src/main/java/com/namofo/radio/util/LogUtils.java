package com.namofo.radio.util;

import com.namofo.radio.BuildConfig;

/**
 * Title: LogUtils
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/2/1  16:05
 *
 * @author 郑炯
 * @version 1.0
 */
public class LogUtils {

    public static void sout(String content){
        if (BuildConfig.DEBUG) {
            System.out.println(content);
        }
    }
}
