package com.namofo.radio.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.namofo.radio.R;

/**
 * Title: ErrorDialogUtils
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/1/24  14:06
 *
 * @author 郑炯
 * @version 1.0
 */
public class ErrorDialogUtils {

    public static void showErrorDialog(Context context, String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(error);
        builder.setPositiveButton(context.getString(R.string.confirm), (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
