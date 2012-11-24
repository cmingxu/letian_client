package com.letian.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.letian.lib.BaseAuthenicationHttpClient;
import com.letian.lib.Constants;
import com.letian.lib.LocalAccessor;
import com.letian.model.xmlhandler.DanyuanHandler;
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
    public static final String TABLE_NAME = "YanshouDuixiang";
    public Context context;


    private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS YanshouDuixiang("
            + "id INTEGER PRIMARY KEY,"
            + "_id INTEGER,"
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

                int offset = Model.max_count(context, TABLE_NAME);
                Log.d(LOG_TAG, "syn start danyuan");
                String params = "?offset=" + offset + "&limit="
                        + Constants.EACH_SLICE;
                xml = BaseAuthenicationHttpClient.doRequest(url + params,
                        User.current_user.name, User.current_user.password);

                items = (ArrayList<YanshouDuixiang>)Model.turn_xml_into_items(xml, new YanshouduixiangHandler(context));
                for (YanshouDuixiang d : items) {
                    d.save_into_db();
                }
                if (items.size() < Constants.EACH_SLICE) {
                    break;
                }

            }
        } catch (Exception e) {// "received authentication challenge is null"
        }
    }


    public boolean save_into_db() throws LTException {
        ContentValues values = new ContentValues();
        values.put("_id", this._id);
        values.put("dxmc", this.dxmc);
        values.put("dxbh", this.dxbh);

        return super.save_into_db(context, YanshouDuixiang.TABLE_NAME, values);
    }


}
