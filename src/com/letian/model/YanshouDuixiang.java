package com.letian.model;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-21
 * Time: 上午8:39
 * To change this template use File | Settings | File Templates.
 */
public class YanshouDuixiang extends Model{
    private int _id;
    public String dxmc;
    public String dxbh;

    public static final String LOG_TAG = "YanshouDuixiang";
    public Context context;


    private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS YanshouDuixiang("
            + "_id INTEGER PRIMARY KEY,"
            + "dxmc TEXT,"
            + "dxbh TEXT,"
            + "createdTime TEXT"
            + ");";
}
