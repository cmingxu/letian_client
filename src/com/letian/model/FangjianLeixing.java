package com.letian.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.letian.lib.BaseAuthenicationHttpClient;
import com.letian.lib.Constants;
import com.letian.lib.LocalAccessor;
import com.letian.model.xmlhandler.FangjianLeixingHandler;
import com.letian.view.SelectorView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-21
 * Time: 上午8:38
 * To change this template use File | Settings | File Templates.
 */
public class FangjianLeixing extends Model {

    public String _id;
    public String fjmc;
    public String fjbh;

    public static final String TABLE_NAME = "FangjianLeixing";
    public static final String LOG_TAG = "FangjianLeixing";
    public Context context;

    public FangjianLeixing(String _id, String fjmc, String fjbh) {
        Log.d(SelectorView.LOG_TAG, _id);
        this._id = _id;
        this.fjmc = fjmc;
        this.fjbh = fjbh;
    }

    private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS FangjianLeixing("
            + "id INTEGER PRIMARY KEY,"
            + "_id INTEGER,"
            + "fjmc TEXT,"
            + "fjbh TEXT"
            + ");";

    public FangjianLeixing(Context context) {
        this.context = context;

        LocalAccessor.getInstance(this.context).create_db(
                SQL_CREATE_TABLE_MESSAGE);
    }

    public static ArrayList<FangjianLeixing> findAllByHuxing(Context context, String huxing){
        ArrayList<FangjianLeixing> fjlxes = new ArrayList<FangjianLeixing>();
        int huxing_id;
        StringBuilder fjlxids = new StringBuilder();
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();

        Cursor cursor;
        try{
            String sql;
            sql = "select * from " + Huxing.TABLE_NAME + " where hxmc = '" + huxing + "';";
            Log.d(SelectorView.LOG_TAG,sql);
            cursor = db.rawQuery(sql,null);
            cursor.moveToFirst();
            huxing_id = cursor.getInt(1);
            Log.d(SelectorView.LOG_TAG, "dddddddddd" + cursor.getString(2));
            Log.d(SelectorView.LOG_TAG, cursor.getString(3));

            sql = "select * from " + HuxingFangjianLeixing.TABLE_NAME +
                    " where hxid='" + huxing_id + "';";
            Log.d(SelectorView.LOG_TAG,sql);
            cursor = db.rawQuery(sql,null);
            cursor.moveToFirst();
            fjlxids.append(cursor.getString(3));
            while(cursor.isAfterLast() != true){
                fjlxids.append(",");
                fjlxids.append(cursor.getString(3));
                cursor.moveToNext();
            }
            Log.d(SelectorView.LOG_TAG, fjlxids.toString());

            sql = "select * from " + FangjianLeixing.TABLE_NAME +
                    " where _id in (" + fjlxids.toString() + ");";

            cursor = db.rawQuery(sql,null);
            cursor.moveToFirst();
            while(cursor.isAfterLast() != true){
                fjlxes.add(new FangjianLeixing(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)));
                cursor.moveToNext();
                }

        }catch(Exception e){
            return fjlxes;
        }


        cursor.close();
        db.close();

        return fjlxes;
    }

    public static void syn(Context context) {
       new FangjianLeixing(context);
        // get xml
        String xml;
        String url = LocalAccessor.getInstance(context).get_server_url() + "/fjlxes.xml";

        ArrayList<FangjianLeixing> items;
        try {
            while (true) {
                int offset = Model.max_count(context,TABLE_NAME);
                String params = "?offset=" + offset + "&limit="
                        + Constants.EACH_SLICE;
                xml = BaseAuthenicationHttpClient.doRequest(url + params,
                        User.current_user.name, User.current_user.password);

                items = (ArrayList<FangjianLeixing>) turn_xml_into_items(xml, new FangjianLeixingHandler(context));
                // prepare database
                for (FangjianLeixing d : items) {
                    Log.d(LOG_TAG, d.fjmc);
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
        values.put("fjmc", this.fjmc);
        values.put("fjbh", this.fjbh);

        return super.save_into_db(context, FangjianLeixing.TABLE_NAME, values);
    }

}
