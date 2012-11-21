package com.letian.model;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-21
 * Time: 上午8:38
 * To change this template use File | Settings | File Templates.
 */
public class FangjianLeixing extends Model {
    public int _id;
    public String fjlx;
    public String fjbh;


    public static final String LOG_TAG = "FangjianLeixing";
    public Context context;


    private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS FangjianLeixing("
            + "_id INTEGER PRIMARY KEY,"
            + "fjlx TEXT,"
            + "fjbh TEXT,"
            + "createdTime TEXT"
            + ");";

    public FangjianLeixing(Context context) {
        this.context = context;
    }
}
