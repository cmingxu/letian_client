package com.letian.model;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-21
 * Time: 上午8:37
 * To change this template use File | Settings | File Templates.
 */
public class Huxing extends Model {

    public Integer _id;
    public String hxmc;
    public String hxbh;
    public String hxt;

    public static final String LOG_TAG = "Huxing";
    public Context context;


    private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS Huxing("
            + "_id INTEGER PRIMARY KEY,"
            + "hxmc TEXT,"
            + "hxbh TEXT,"
            + "hxt TEXT,"
            + "createdTime TEXT"
            + ");";

    public Huxing(Context context) {
        this.context = context;
    }
}

