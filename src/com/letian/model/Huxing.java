package com.letian.model;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import com.letian.lib.BaseAuthenicationHttpClient;
import com.letian.lib.Constants;
import com.letian.lib.LocalAccessor;
import com.letian.model.xmlhandler.FangjianLeixingHandler;
import com.letian.model.xmlhandler.HuxingHandler;

import java.util.ArrayList;

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
    public static final String TABLE_NAME = "Huxing";
    public Context context;


    private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS Huxing("
            + "id INTEGER PRIMARY KEY,"
            + "_id INTEGER,"
            + "hxmc TEXT,"
            + "hxbh TEXT,"
            + "hxt TEXT,"
            + "createdTime TEXT"
            + ");";

    public Huxing(Context context) {
        this.context = context;

        LocalAccessor.getInstance(this.context).create_db(
                SQL_CREATE_TABLE_MESSAGE);
    }

    public static void syn(Context context) {
        // get xml
        String xml;
        String url = LocalAccessor.getInstance(context).get_server_url() + "/hxes.xml";

        ArrayList<Huxing> items;
        try {
            while (true) {
                int offset = Model.max_count(context,FangjianLeixing.TABLE_NAME);
                String params = "?offset=" + offset + "&limit="
                        + Constants.EACH_SLICE;
                xml = BaseAuthenicationHttpClient.doRequest(url + params,
                        User.current_user.name, User.current_user.password);

                items = (ArrayList<Huxing>) turn_xml_into_items(xml, new HuxingHandler(context));
                // prepare database
                for (Huxing d : items) {
                    d.save_into_db();
                }

                // break loop
                if (items.size() < Constants.EACH_SLICE) {
                    break;
                }

            }
        } catch (Exception e) {// "received authentication challenge is null"
            Log.e(Huxing.LOG_TAG, e.getMessage());
        }
    }

    public boolean save_into_db() throws LTException {
        ContentValues values = new ContentValues();
        values.put("_id", this._id);
        values.put("hxmc", this.hxmc);
        values.put("hxbh", this.hxbh);

        return super.save_into_db(context, Huxing.TABLE_NAME, values);
    }

}

