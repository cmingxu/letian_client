package com.letian.model;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import com.letian.lib.BaseAuthenicationHttpClient;
import com.letian.lib.Constants;
import com.letian.lib.LocalAccessor;
import com.letian.model.xmlhandler.FangjianLeixingHandler;
import com.letian.model.xmlhandler.YanshouXiangmuHandler;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-21
 * Time: 上午8:39
 * To change this template use File | Settings | File Templates.
 */
public class YanshouXiangmu extends Model{
    public YanshouXiangmu(Context context) {
        this.context = context;

        LocalAccessor.getInstance(this.context).create_db(
                SQL_CREATE_TABLE_MESSAGE);
    }

    public int _id;
    public String  duixiang_id;
    public String xmmc;
    public String xmbh;

    public static final String LOG_TAG = "YanshouXiangmu";
    public static final String TABLE_NAME = "YanshouXiangmu";
    public Context context;


    private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS YanshouXiangmu("
            + "id INTEGER PRIMARY KEY,"
            + "_id INTEGER,"
            + "duixiang_id TEXT,"
            + "xmmc TEXT,"
            + "xmbh TEXT,"
            + "createdTime TEXT"
            + ");";



    public static void syn(Context context) {
        // get xml
        String xml;
        String url = LocalAccessor.getInstance(context).get_server_url() + "/ysxm.xml";

        ArrayList<YanshouXiangmu> items;
        try {
            while (true) {
                int offset = Model.max_count(context,FangjianLeixing.TABLE_NAME);
                String params = "?offset=" + offset + "&limit="
                        + Constants.EACH_SLICE;
                xml = BaseAuthenicationHttpClient.doRequest(url + params,
                        User.current_user.name, User.current_user.password);

                items = (ArrayList<YanshouXiangmu>) turn_xml_into_items(xml, new YanshouXiangmuHandler(context));
                // prepare database
                for (YanshouXiangmu d : items) {
                    d.save_into_db();
                }

                // break loop
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
        values.put("duixiang_id", this.duixiang_id);
        values.put("xmmc", this.xmmc);
        values.put("xmbh", this.xmbh);

        return super.save_into_db(context, FangjianLeixing.TABLE_NAME, values);
    }

}
