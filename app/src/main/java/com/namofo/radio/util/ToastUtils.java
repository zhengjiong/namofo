package com.namofo.radio.util;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;


/**
 * CreateTime:17/1/23  10:58
 * @author 郑炯
 * @version 1.0
 */
public class ToastUtils {

    private static final int DURATION = 1000;
    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };

    private static void showToast(Context mContext, String text, int duration) {

        mHandler.removeCallbacks(r);
        if (mToast != null)
            mToast.setText(text);
        else
            mToast = Toast.makeText(mContext, text, duration);
        mHandler.postDelayed(r, DURATION);

        mToast.show();
    }

    private static void showToast(Context mContext, int resId, int duration) {

        mHandler.removeCallbacks(r);
        if (mToast != null)
            mToast.setText(resId);
        else
            mToast = Toast.makeText(mContext, resId, duration);
        mHandler.postDelayed(r, DURATION);

        mToast.show();
    }


    public static void showLong(final Context activity, final String message) {
        if (activity != null && !TextUtils.isEmpty(message))
            showToast(activity, message, LENGTH_LONG);
    }

    public static void showShort(final Context activity, final String message) {
        if (activity != null && !TextUtils.isEmpty(message))
            showToast(activity, message, LENGTH_SHORT);
    }

    public static void showShort(final Context activity, final int msgRes) {
        showToast(activity, msgRes, LENGTH_SHORT);
    }

    public static void showLong(final Context activity, final int msgRes) {
        showToast(activity, msgRes, LENGTH_LONG);
    }
}
