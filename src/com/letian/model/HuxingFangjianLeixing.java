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
import com.letian.model.xmlhandler.HuxingFangjianLeixingHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-22
 * Time: 下午8:10
 * To change this template use File | Settings | File Templates.
 */
public class HuxingFangjianLeixing extends Model {
    public int _id;
    public String hxid;
    public String fjlxid;


    public static final String LOG_TAG = "HuxingFangjianLeixing";
    public static final String TABLE_NAME = "HuxingFangjianLeixing";
    public Context context;


    private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS HuxingFangjianLeixing("
            + "id INTEGER PRIMARY KEY,"
            + "_id INTEGER,"
            + "hxid TEXT,"
            + "fjlxid TEXT,"
            + "createdTime TEXT"
            + ");";

    public HuxingFangjianLeixing(Context context) {
        this.context= context;

        LocalAccessor.getInstance(this.context).create_db(
                SQL_CREATE_TABLE_MESSAGE);
    }


    public static void syn(Context context) {
        new HuxingFangjianLeixing(context);
        // get xml
        String xml;
        String url = LocalAccessor.getInstance(context).get_server_url() + "/hx_fjlxes.xml";

        ArrayList<HuxingFangjianLeixing> items = new ArrayList<HuxingFangjianLeixing>();
        try {
            while (true) {

                int offset = Model.max_count(context, TABLE_NAME);
                String params = "?offset=" + offset + "&limit="
                        + Constants.EACH_SLICE;
                xml = BaseAuthenicationHttpClient.doRequest(url + params,
                        User.current_user.name, User.current_user.password);

                items = (ArrayList<HuxingFangjianLeixing>)Model.turn_xml_into_items(xml, new HuxingFangjianLeixingHandler(context));
                for (HuxingFangjianLeixing d : items) {
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
        values.put("hxid", this.hxid);
        values.put("fjlxid", this.fjlxid);


        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        db.insertOrThrow("HuxingFangjianLeixing", null, values);
        db.close();
        return true;
    }



}
