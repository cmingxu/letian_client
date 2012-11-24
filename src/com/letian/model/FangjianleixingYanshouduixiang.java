package com.letian.model;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import com.letian.lib.BaseAuthenicationHttpClient;
import com.letian.lib.Constants;
import com.letian.lib.LocalAccessor;
import com.letian.model.xmlhandler.FangjianLeixingHandler;
import com.letian.model.xmlhandler.FangjianleixingYanshouduixiangHandler;

import java.util.ArrayList;

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
    public static final String TABLE_NAME = "FangjianleixingYanshouduixiang";
    public Context context;


    private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS FangjianleixingYanshouduixiang("
            + "id INTEGER PRIMARY KEY,"
            + "_id INTEGER,"
            + "fjlxid TEXT,"
            + "dxid TEXT"
            + ");";

    public FangjianleixingYanshouduixiang(Context context) {
        this.context = context;

        LocalAccessor.getInstance(this.context).create_db(
                SQL_CREATE_TABLE_MESSAGE);
    }

    public static void syn(Context context) {
        // get xml
        String xml;
        String url = LocalAccessor.getInstance(context).get_server_url() + "/fjlx_ysdxes.xml";

        ArrayList<FangjianleixingYanshouduixiang> items = new ArrayList<FangjianleixingYanshouduixiang>();
        try {
            while (true) {
                int offset = Model.max_count(context,FangjianLeixing.TABLE_NAME);
                String params = "?offset=" + offset + "&limit="
                        + Constants.EACH_SLICE;
                xml = BaseAuthenicationHttpClient.doRequest(url + params,
                        User.current_user.name, User.current_user.password);

                items = (ArrayList<FangjianleixingYanshouduixiang>) turn_xml_into_items(xml, new FangjianleixingYanshouduixiangHandler(context));
                // prepare database
                for (FangjianleixingYanshouduixiang d : items) {
                    Log.d(LOG_TAG, d.fjlxid);
                    d.save_into_db();
                }

                // break loop
                if (items.size() < Constants.EACH_SLICE) {
                    break;
                }

            }
        } catch (Exception e) {// "received authentication challenge is null"
            Log.e(Danyuan.LOG_TAG, e.getMessage());
        }
    }

    public boolean save_into_db() throws LTException {
        ContentValues values = new ContentValues();
        values.put("_id", this._id);
        values.put("fjlxid", this.fjlxid);
        values.put("dxid", this.dxid);

        return super.save_into_db(context, FangjianleixingYanshouduixiang.TABLE_NAME, values);
    }
}
