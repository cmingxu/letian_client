package com.letian.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.letian.lib.BaseAuthenicationHttpClient;
import com.letian.lib.Constants;
import com.letian.lib.LocalAccessor;
import com.letian.model.xmlhandler.YanshouduixiangHandler;


import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-21
 * Time: 上午8:39
 * To change this template use File | Settings | File Templates.
 */
public class YanshouDuixiang extends Model{
    public int _id;
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

    public YanshouDuixiang(Context context) {
        this.context = context;
        LocalAccessor.getInstance(this.context).create_db(
                SQL_CREATE_TABLE_MESSAGE);
    }

    public static void syn(Context context) {
        // get xml
        String xml;
        String url = LocalAccessor.getInstance(context).get_server_url() + "/ysdxes.xml";

        ArrayList<YanshouDuixiang> items = new ArrayList<YanshouDuixiang>();
        try {
            while (true) {
                int offset = YanshouDuixiang.max_count(context);
                String params = "?offset=" + offset + "&limit="
                        + Constants.EACH_SLICE;
                Log.e(Danyuan.LOG_TAG, url + params);
                xml = BaseAuthenicationHttpClient.doRequest(url + params,
                        User.current_user.name, User.current_user.password);

                // turn xml into object
                items = turn_xml_into_items(xml, context);
                // prepare database
                for (YanshouDuixiang d : items) {
                    d.save_into_db();
                }

                // break loop
                if (items.size() < Constants.EACH_SLICE) {
                    break;
                }

            }
        } catch (Exception e) {// "received authentication challenge is null"
            Log.e(YanshouDuixiang.LOG_TAG, e.getMessage());
        }
    }

    private static ArrayList<YanshouDuixiang> turn_xml_into_items(String xml,
                                                          Context context) {
        ArrayList<YanshouDuixiang> items = new ArrayList<YanshouDuixiang>();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            YanshouduixiangHandler handler = new YanshouduixiangHandler(context);
            InputStream is = new ByteArrayInputStream(xml.getBytes());
            parser.parse(is, handler);
            items = handler.geYanshouDuixiangs();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return items;
    }

    public boolean save_into_db() throws LTException {
        ContentValues values = new ContentValues();

        values.put("dxmc", this.dxmc);
        values.put("dxbh", this.dxbh);
        values.put("createdTime", (new Date()).toLocaleString());

        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        db.insertOrThrow("YanshouDuixiang", null, values);
        db.close();
        return true;
    }

    private static int max_count(Context context) {
        int offset = 0;
        // make sure table created
        new YanshouDuixiang(context);
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        Cursor c = db.query("YanshouDuixiang", null, null, null, null, null, null);
        offset = c.getCount();
        c.close();
        db.close();
        return offset;

    }



}
