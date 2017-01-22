package com.namofo.radio.exception;

/**
 * Title: HttpTimeException
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/1/17  17:09
 *
 * @author 郑炯
 * @version 1.0
 */
public class HttpTimeException extends RuntimeException{
    public static final int NO_DATA = 0x2;

    public HttpTimeException(int resultCode) {
        this(getApiExceptionMessage(resultCode));
    }

    public HttpTimeException(String message) {
        super(message);
    }

    private static String getApiExceptionMessage(int code) {
        String message = "";
        switch (code) {
            case NO_DATA:
                message = "无数据";
                break;
            default:
                message = "error";
                break;
        }

        return message;
    }
}
