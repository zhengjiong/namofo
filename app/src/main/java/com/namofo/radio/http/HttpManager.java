package com.namofo.radio.http;

import com.namofo.radio.common.Constants;
import com.namofo.radio.exception.RetryWhenNetworkException;
import com.namofo.radio.retrofit.CustomGsonConvertFactor;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * CreateTime:17/1/16  17:32
 *
 * @author 郑炯
 * @version 1.0
 */
public class HttpManager {
    private static final String TAG = "HttpManager";
    private HttpService httpService;

    private HttpManager() {
        /*HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);*/
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                //.addInterceptor(new LoggingInterceptor())
                .connectTimeout(Constants.TIMEOUT, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(CustomGsonConvertFactor.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .client(builder.build())
                .build();

        httpService = retrofit.create(HttpService.class);
    }

    private static class SingletonHolder {
        private static HttpManager httpManager = new HttpManager();
    }

    public static HttpManager getInstance() {
        return SingletonHolder.httpManager;
    }

    public HttpService getHttpService() {
        return httpService;
    }

    public <T> void doHttp(Observable<T> httpObservable, Consumer<T> onNext) {
        httpObservable
                /*失败后的retry配置*/
                .retryWhen(new RetryWhenNetworkException())
                /*生命周期管理*/
                //.compose(basePostEntity.getRxAppCompatActivity().bindToLifecycle())
                /*http请求线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                   /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext);
    }

    public <T> void doHttp(Observable<T> httpObservable, Consumer<T> onNext, Consumer<Throwable> onError) {
        httpObservable
                /*失败后的retry配置*/
                .retryWhen(new RetryWhenNetworkException())
                /*生命周期管理*/
                //.compose(basePostEntity.getRxAppCompatActivity().bindToLifecycle())
                /*http请求线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                   /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError);
    }

    public <T> void doHttp(Observable<T> httpObservable, Consumer<T> onNext, Consumer<Throwable> onError, Action onComplete) {
        httpObservable
                /*结果判断*/
                //.map(basePostEntity);
                /*失败后的retry配置*/
                .retryWhen(new RetryWhenNetworkException())
                /*生命周期管理*/
                //.compose(basePostEntity.getRxAppCompatActivity().bindToLifecycle())
                /*http请求线程*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                   /*回调线程*/
                .observeOn(AndroidSchedulers.mainThread()).subscribe(onNext, onError, onComplete);
    }

    /**
     * 请求响应日志信息，方便debug
     */
    public static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            Logger.i(String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));

            Response response = chain.proceed(request);
            long t2 = System.nanoTime();
            Logger.i(String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }
}
