package com.namofo.radio;

import android.app.Application;

import com.namofo.radio.db.GreenDaoManager;
import com.namofo.radio.util.LogUtils;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Title: NamofoApplication
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/2/1  15:51
 *
 * @author 郑炯
 * @version 1.0
 */
public class NamofoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        GreenDaoManager.getInstance(this);
        CrashReport.initCrashReport(getApplicationContext(), "6743ee4a27", false);
    }

}
