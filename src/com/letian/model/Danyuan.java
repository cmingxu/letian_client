package com.letian.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.letian.lib.BaseAuthenicationHttpClient;
import com.letian.lib.LocalAccessor;
import com.letian.model.xmlhandler.DanyuanHandler;
import com.letian.view.SelectorView;

public class Danyuan extends Model {
    public static final String LOG_TAG = "DANYUAN_MODEL";
    public static final String TABLE_NAME = "DANYUAN";
    private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS Danyuan("
            + "_id INTEGER PRIMARY KEY,"
            + "danyuanbianhao TEXT,"
            + "danyuanmingcheng TEXT,"
            + "lougebianhao TEXT,"
            + "zhuhubianhao TEXT,"
            + "jiange TEXT,"
            + "louceng TEXT,"
            + "loucengmingcheng TEXT,"
            + "createdTime TEXT" + ");";
    public Integer _id;
    public String danyuanbianhao;
    public String danyuanmingcheng;
    public String lougebianhao;
    public String zhuhubianhao;
    public String louceng;
    public String jiange;
    public String loucengmingcheng;
    public Context context;


    public Danyuan(Integer _id, String danyuanbianhao, String danyuanmingcheng, String lougebianhao, String zhuhubianhao, String jiange, String louceng, String loucengmingcheng) {
        this._id = _id;
        this.danyuanbianhao = danyuanbianhao;
        this.danyuanmingcheng = danyuanmingcheng;
        this.lougebianhao = lougebianhao;
        this.zhuhubianhao = zhuhubianhao;
        this.jiange = jiange;
        this.louceng = louceng;
        this.loucengmingcheng = loucengmingcheng;

    }

    public Danyuan(Context context) {
        this.context = context;

        LocalAccessor.getInstance(this.context).create_db(
                SQL_CREATE_TABLE_MESSAGE);
    }

    public static void syn(Context context) throws LTException {
        Log.d(LOG_TAG, "syn start danyuan");
        // get xml
        String xml;
        String url = LocalAccessor.getInstance(context).get_server_url() + "/danyuans.xml";

        ArrayList<Danyuan> items = new ArrayList<Danyuan>();
        while (true) {

            int offset = Model.max_count(context, "Danyuan");
            Log.d(LOG_TAG, "syn start danyuan");
            String params = "?offset=" + offset + "&limit="
                    + Constants.EACH_SLICE;
            xml = BaseAuthenicationHttpClient.doRequest(url + params,
                    User.current_user.name, User.current_user.password);

            items = (ArrayList<Danyuan>) Model.turn_xml_into_items(xml, new DanyuanHandler(context));
            for (Danyuan d : items) {
                Log.d(LOG_TAG, d.danyuanbianhao);
                d.save_into_db();
            }
            if (items.size() < Constants.EACH_SLICE) {
                break;
            }

        }

    }

    public static HashMap<String, String> mingcheng_bianhao_map(
            Context context, String loucengmingcheng, String lougebianhao) {
        HashMap<String, String> res = new HashMap<String, String>();
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        String sql = "select * from Danyuan where loucengmingcheng = '"
                + loucengmingcheng.replaceAll("\\s*", "")
                + "' and lougebianhao='" + lougebianhao.replaceAll("\\s*", "")
                + "' order by _id DESC";
        Cursor cursor;
        try {
            cursor = db.rawQuery(sql, null);
        } catch (Exception e) {
            return res;
        }
        Log.e("maobing", Integer.toString(cursor.getCount()));
        cursor.moveToFirst();
        while (cursor.isAfterLast() != true) {
            res.put(cursor.getString(2), cursor.getString(1));
            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        return res;
    }

    public static ArrayList<String> distinct_louceng_name(Context context,
                                                      String louge_bianhao) {
        ArrayList<String> res = new ArrayList<String>();
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        Cursor cursor = db.rawQuery("select * from Danyuan", null);
        cursor.moveToFirst();


        String sql = "select distinct(loucengmingcheng) from Danyuan where lougebianhao = '"
                + louge_bianhao.replaceAll("\\s*", "") + "' order by _id DESC";

        cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        while (cursor.isAfterLast() != true) {
            res.add(cursor.getString(1));

            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        return res;
    }

    public static ArrayList<Danyuan> distinct_louceng(Context context,
                                                     String louge_bianhao) {
        ArrayList<Danyuan> res = new ArrayList<Danyuan>();
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        Cursor cursor = db.rawQuery("select * from Danyuan", null);
        cursor.moveToFirst();


        String sql = "select * from Danyuan where lougebianhao = '"
                + louge_bianhao.replaceAll("\\s*", "") + "' group by loucengmingcheng order by _id ASC";

        cursor = db.rawQuery(sql, null);

        cursor.moveToFirst();
        while (cursor.isAfterLast() != true) {
            res.add(new Danyuan(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7)
            ));

            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        return res;
    }

    public static ArrayList<Danyuan> findAll(Context context) {
        return Danyuan.findAll(context, null);
    }

    public static ArrayList<Danyuan> findAll(Context context, String where) {
        ArrayList<Danyuan> danyuans = new ArrayList<Danyuan>();
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        String sql;
        if (where == null) {
            sql = "select * from " + TABLE_NAME + " order by _id ASC";
        } else {
            sql = "select * from " + TABLE_NAME + " where " + where + " order by danyuanbianhao ASC";

        }
        Cursor cursor;
        try {
            cursor = db.rawQuery(sql, null);
            Log.d(SelectorView.LOG_TAG, sql);
        } catch (Exception e) {
            return danyuans;
        }
        cursor.moveToFirst();
        Log.d(SelectorView.LOG_TAG, "" + cursor.getCount());
        while (cursor.isAfterLast() != true) {
            danyuans.add(new Danyuan(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7)



            ));

            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        return danyuans;
    }

    public boolean save_into_db() throws LTException {
        ContentValues values = new ContentValues();
        values.put("danyuanbianhao", this.danyuanbianhao);
        values.put("danyuanmingcheng", this.danyuanmingcheng);
        values.put("lougebianhao", this.lougebianhao);
        values.put("zhuhubianhao", this.zhuhubianhao);
        values.put("jiange", this.jiange);
        values.put("louceng", this.louceng);
        values.put("loucengmingcheng", this.loucengmingcheng);
        values.put("createdTime", (new Date()).toString());

        return super.save_into_db(context, Danyuan.TABLE_NAME, values);
    }

}
