package com.letian.model;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-21
 * Time: 上午8:39
 * To change this template use File | Settings | File Templates.
 */
public class YanshouXiangmu extends Model{
    private int _id;
    String  duixiang_id;
    public String xmmc;
    public String xmbh;

    public static final String LOG_TAG = "YanshouXiangmu";
    public Context context;


    private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS YanshouXiangmu("
            + "_id INTEGER PRIMARY KEY,"
            + "duixiang_id TEXT,"
            + "xmmc TEXT,"
            + "xmbh TEXT,"
            + "createdTime TEXT"
            + ");";
}
