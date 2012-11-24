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
import com.letian.view.SelectorView;


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
    public String _id;
    public String dxmc;
    public String dxbh;

    public static final String LOG_TAG = "YanshouDuixiang";
    public static final String TABLE_NAME = "YanshouDuixiang";
    public Context context;

    public YanshouDuixiang(String _id, String dxmc, String dxbh) {
        this._id = _id;
        this.dxmc = dxmc;
        this.dxbh = dxbh;
    }

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


    public static ArrayList<YanshouDuixiang> findAllByFjlxid(Context context, String fjlxid){
        ArrayList<YanshouDuixiang> yuanshouduixiangs = new ArrayList<YanshouDuixiang>();
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        StringBuffer ysdxids = new StringBuffer();

        Cursor cursor;
        try{
            String sql;
            sql = "select * from " + FangjianleixingYanshouduixiang.TABLE_NAME + " where fjlxid ='" +
                    fjlxid + "';" ;
            Log.d(SelectorView.LOG_TAG, sql);
            cursor = db.rawQuery(sql,null);
            cursor.moveToFirst();
            Log.d(SelectorView.LOG_TAG,"1111111111"  + cursor.getString(2));

            ysdxids.append(cursor.getString(2));
            cursor.moveToNext();

            while(cursor.isAfterLast() != true){
                Log.d(SelectorView.LOG_TAG, cursor.getString(3));
                ysdxids.append(",");
                ysdxids.append(cursor.getString(3));
                cursor.moveToNext();
            }
            Log.d(SelectorView.LOG_TAG, ysdxids.toString());

            sql = "select * from " + YanshouDuixiang.TABLE_NAME +
                    " where _id in (" + ysdxids.toString() + ");";
            Log.d(SelectorView.LOG_TAG, sql);
            cursor = db.rawQuery(sql,null);
            cursor.moveToFirst();
            while(cursor.isAfterLast() != true){
                yuanshouduixiangs.add(new YanshouDuixiang(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)));

                Log.d(SelectorView.LOG_TAG, "11111 hahah " + cursor.getString(2));
                cursor.moveToNext();
            }

        }catch(Exception e){
            return yuanshouduixiangs;
        }


        cursor.close();
        db.close();
        return yuanshouduixiangs;
    }


}
