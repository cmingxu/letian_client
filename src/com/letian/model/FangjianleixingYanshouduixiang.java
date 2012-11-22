package com.letian.model;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-22
 * Time: 下午8:18
 * To change this template use File | Settings | File Templates.
 */
public class FangjianleixingYanshouduixiang extends Model {
    public int _id;
    public String fjlxid;
    public String dxid;


    public static final String LOG_TAG = "FangjianleixingYanshouduixiang";
    public Context context;


    private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS FangjianleixingYanshouduixiang("
            + "_id INTEGER PRIMARY KEY,"
            + "fjlxid TEXT,"
            + "dxid TEXT,"
            + "createdTime TEXT"
            + ");";

    public FangjianleixingYanshouduixiang(Context context) {
        this.context = context;
    }
}
