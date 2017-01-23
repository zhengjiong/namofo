package com.namofo.radio.exception;

/**
 * Title: HttpException
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/1/17  17:09
 *
 * @author 郑炯
 * @version 1.0
 */
public class HttpException extends RuntimeException{
    public static final int NO_DATA = 0x2;

    public HttpException(int resultCode) {
        this(getApiExceptionMessage(resultCode));
    }

    public HttpException(String message) {
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
