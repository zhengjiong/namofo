package com.namofo.radio.retrofit;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.orhanobut.logger.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Title: CustomGsonResponseBodyConverter
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/3/22  13:41
 *
 * @author 郑炯
 * @version 1.0
 */
final class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override public T convert(ResponseBody value) throws IOException {
        try {
            String json = value.string();
            Logger.i(json);
            /*JSONArray results = object.getJSONArray("results");
            ByteArrayInputStream inputStream = new ByteArrayInputStream(results.toString().getBytes());;
            if (object.toString().contains("results\":[")) {//原谅我,fastJson和gson都试过了,都不行,只能写死了


            } else if (object.toString().contains("results\":{")) {

                JSONObject results = object.getJSONObject("results");
                //将字符串转换成输入流
                inputStream = new ByteArrayInputStream(results.toString().getBytes());
            } else {
                //随便抛个异常的,请自动忽略
                throw new RuntimeException("Port Exception");
            }*/
            //字节流转换成字符流
            Reader reader = new InputStreamReader(new ByteArrayInputStream(json.getBytes()));
            JsonReader jsonReader = gson.newJsonReader(reader);
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}
