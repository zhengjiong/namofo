package com.namofo.radio.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.namofo.radio.db.greendao3.DaoMaster;
import com.namofo.radio.db.greendao3.DaoSession;
import com.namofo.radio.util.LogUtils;

/**
 * Title: GreenDaoManager
 * Description:
 * Copyright:Copyright(c)2016
 * Company: 博智维讯信息技术有限公司
 * CreateTime:17/1/25  15:04
 *
 * @author 郑炯
 * @version 1.0
 */
public class GreenDaoManager {
    private volatile static GreenDaoManager mGreenDaoManager;
    public static final String DB_NAME = "db_namofo_radio";
    private DaoSession mDaoSession;
    private GreenDaoOpenHelper mOpenHelper;

    private GreenDaoManager() {
    }

    private GreenDaoManager(Context context) {
        init(context);
    }

    /**
     * 初始化数据库
     * 该方法只需要自行一次
     *
     * @param context 需要传入application
     * @return
     */
    private void init(Context context) {
        LogUtils.sout("init");
        mOpenHelper = new GreenDaoOpenHelper(context, DB_NAME, null);
        SQLiteDatabase sqLiteDatabase = mOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
        mDaoSession = daoMaster.newSession();
    }

    public static GreenDaoManager getInstance() {
        if (mGreenDaoManager == null) {
            throw new RuntimeException("需要先执行getInstance(Context context)方法");
        }
        return mGreenDaoManager;
    }

    public static GreenDaoManager getInstance(Context context) {
        if (mGreenDaoManager == null) {
            synchronized (GreenDaoManager.class) {
                if (mGreenDaoManager == null) {
                    mGreenDaoManager = new GreenDaoManager(context);
                }
            }
        }
        return mGreenDaoManager;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public void close() {
        if (mDaoSession != null) {
            mDaoSession.clear();
            mDaoSession = null;
        }
        if (mOpenHelper != null) {
            mOpenHelper.close();
            mOpenHelper = null;
        }

    }

    /*private static class SingletonHolder{
        private static final GreenDaoManager greenDaoManager = new GreenDaoManager();
    }*/

    private static class GreenDaoOpenHelper extends DaoMaster.OpenHelper {


        public GreenDaoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            switch (oldVersion) {
                case 1:
                    break;
            }
        }
    }

}
