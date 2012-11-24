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
import com.letian.model.xmlhandler.YanshouXiangmuHandler;
import com.letian.view.SelectorView;

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

    public String _id;
    public String  duixiang_id;
    public String xmmc;
    public String xmbh;

    public static final String LOG_TAG = "YanshouXiangmu";
    public static final String TABLE_NAME = "YanshouXiangmu";
    public Context context;


    private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS YanshouXiangmu("
            + "id INTEGER PRIMARY KEY,"
            + "_id TEXT,"
            + "duixiang_id TEXT,"
            + "xmmc TEXT,"
            + "xmbh TEXT,"
            + "createdTime TEXT"
            + ");";

    public YanshouXiangmu(String _id, String duixiang_id, String xmmc, String xmbh) {
        this._id = _id;
        this.duixiang_id = duixiang_id;
        this.xmmc = xmmc;
        this.xmbh = xmbh;
    }

    public static void syn(Context context) {
        // get xml
        String xml;
        String url = LocalAccessor.getInstance(context).get_server_url() + "/ysxms.xml";

        ArrayList<YanshouXiangmu> items;
        try {
            while (true) {
                int offset = Model.max_count(context,FangjianLeixing.TABLE_NAME);
                String params = "?offset=" + offset + "&limit="
                        + Constants.EACH_SLICE;
                xml = BaseAuthenicationHttpClient.doRequest(url + params,
                        User.current_user.name, User.current_user.password);


                Log.d(SelectorView.LOG_TAG, xml);

                items = (ArrayList<YanshouXiangmu>) turn_xml_into_items(xml, new YanshouXiangmuHandler(context));
                // prepare database
                Log.d(SelectorView.LOG_TAG, "Yanshou xiangmu" + items.size());
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

    public static ArrayList<YanshouXiangmu> findAllByYsdxid(Context context, String ysdxid){
        ArrayList<YanshouXiangmu> yanshouxiangmus = new ArrayList<YanshouXiangmu>();
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();

        Cursor cursor;
        try{
            String sql;
            sql = "select * from " + YanshouXiangmu.TABLE_NAME + " where duixiang_id ='" +
                    ysdxid + "';" ;
            cursor = db.rawQuery(sql,null);
            cursor.moveToFirst();


            Log.d(SelectorView.LOG_TAG, sql);
            while(cursor.isAfterLast() != true){
                yanshouxiangmus.add(new YanshouXiangmu(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)));
                cursor.moveToNext();
            }

        }catch(Exception e){
            return yanshouxiangmus;
        }
        cursor.close();
        db.close();
        return yanshouxiangmus;
    }

    public boolean save_into_db() throws LTException {
        ContentValues values = new ContentValues();
        values.put("_id", this._id);
        values.put("duixiang_id", this.duixiang_id);
        values.put("xmmc", this.xmmc);
        values.put("xmbh", this.xmbh);

        return super.save_into_db(context, YanshouXiangmu.TABLE_NAME, values);
    }

}
