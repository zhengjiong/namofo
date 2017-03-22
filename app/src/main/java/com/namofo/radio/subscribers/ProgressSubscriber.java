package com.namofo.radio.subscribers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.namofo.radio.listener.HttpOnNextListener;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import android.util.Log;


/**
 * CreateTime:17/1/19  17:49
 *
 * @author 郑炯
 * @version 1.0
 */
/*@Deprecated
public class ProgressSubscriber<T> extends Subscriber<T> {

    //    回调接口
    private HttpOnNextListener mOnNext;
    //    弱引用防止内存泄露
    private WeakReference<Context> mActivity;
    //    是否能取消请求
    private boolean cancel;
    //    加载框可自己定义
    private ProgressDialog pd;

    public ProgressSubscriber(HttpOnNextListener onNext, Context context) {
        this.mOnNext = onNext;
        this.mActivity = new WeakReference<>(context);
        this.cancel = false;
        initProgressDialog();
    }

    public ProgressSubscriber(HttpOnNextListener onNext, Context context, boolean cancel) {
        this.mOnNext = onNext;
        this.mActivity = new WeakReference<>(context);
        this.cancel = cancel;
        initProgressDialog();
    }


    *//**
     * 初始化加载框
     *//*
    private void initProgressDialog() {
        Context context = mActivity.get();
        if (pd == null && context != null) {
            pd = new ProgressDialog(context);
            pd.setCancelable(cancel);
            if (cancel) {
                pd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        onCancelProgress();
                    }
                });
            }
        }
    }


    *//**
     * 显示加载框
     *//*
    private void showProgressDialog() {
        Context context = mActivity.get();
        if (pd == null || context == null) return;
        if (!pd.isShowing()) {
            pd.show();
        }
    }


    *//**
     * 隐藏
     *//*
    private void dismissProgressDialog() {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
    }


    *//**
     * 订阅开始时调用
     * 显示ProgressDialog
     *//*
    @Override
    public void onStart() {
        showProgressDialog();
    }

    *//**
     * 完成，隐藏ProgressDialog
     *//*
    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    *//**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     *
     * @param e
     *//*
    @Override
    public void onError(Throwable e) {
        Context context = mActivity.get();
        if (context == null) return;
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else if (e instanceof ConnectException) {
            Toast.makeText(context, "网络中断，请检查您的网络状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "错误" + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i("tag", "error----------->" + e.toString());
        }
        dismissProgressDialog();
    }

    *//**
     * 将onNext方法中的返回结果交给Activity或Fragment自己处理
     *
     * @param t 创建Subscriber时的泛型类型
     *//*
    @Override
    public void onNext(T t) {
        if (mOnNext != null) {
            mOnNext.onNext(t);
        }
    }

    *//**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     *//*
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}*/
