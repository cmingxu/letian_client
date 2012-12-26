package com.letian.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.letian.lib.BaseAuthenicationHttpClient;
import com.letian.lib.FileUploader;
import com.letian.lib.IOUtils;
import com.letian.lib.LocalAccessor;
import com.letian.lib.FileUploader.ReturnCode;
import com.letian.view.SelectorView;

public class Weixiudan extends Model {
    public int _id;
    public String weixiu_id;
    public String weixiudanyuan;
    public String danyuanbianhao;
    public String zuhumingcheng;
    public String lianxidianhua;
    public String wufuneirong;
    public String shoudanren;
    public String shoudanshijian;
    public String weixiuzhonglei;
    public String suoshuloupan;
    public String suoshulouge;
    public String weixiurenyuan;
    public long id;
    public int is_syned;
    public int is_photo_syned;
    public String photo_paths;

    public int model_type;
    public String weixiukaishishijian;
    public String weixiujieshushijian;
    public String wanchengqingkuang;
    public String wanchengzhuangtai;
    public String sign_path;

    public static SQLiteDatabase db;

    public static final String TABLE_NAME = "Weixiudan";

    public static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS Weixiudan("
            + "_id INTEGER PRIMARY KEY ,"
            + "weixiu_id TEXT,"
            + "weixiudanyuan TEXT,"
            + "danyuanbianhao TEXT,"
            + "zuhumingcheng TEXT,"
            + "wufuneirong TEXT,"
            + "shoudanren TEXT,"
            + "shoudanshijian TEXT,"
            + "weixiuzhonglei TEXT,"
            + "lianxidianhua TEXT,"
            + "suoshuloupan TEXT,"
            + "suoshulouge TEXT,"
            + "weixiurenyuan TEXT,"
            + "is_syned INTEGER,"
            + "is_photo_syned INTEGER,"
            + "photo_paths TEXT, "
            + "createdTime TEXT,"
            + "model_type INTEGER,"
            + "weixiukaishishijian TEXT,"
            + "weixiujieshushijian TEXT,"
            + "wanchengqingkuang TEXT,"
            + "wanchengzhuangtai TEXT,"
            + "sign_path TEXT"
            + ");";


    Context context;

    public Weixiudan(Context context) {
        this.context = context;
        LocalAccessor.getInstance(context).create_db(SQL_CREATE_TABLE_MESSAGE);
        this.is_syned = 0;
        this.is_photo_syned = 0;
        this.photo_paths = "";
    }


    public VerifiedInfo verify() {
        VerifiedInfo verifiedInfo = new VerifiedInfo();
        verifiedInfo.verifyCode = VerifiedInfo.VERIFY_SUCCESS;
        verifiedInfo.verifyMessage = "";

        ArrayList<String> ar = new ArrayList<String>();
        if (this.lianxidianhua.equals("")) {
            verifiedInfo.verifyCode = VerifiedInfo.VERIFY_ERROR;
            ar.add(new String(" "));
        }

        if (this.zuhumingcheng.equals("")) {
            verifiedInfo.verifyCode = VerifiedInfo.VERIFY_ERROR;
            ar.add(new String(""));
        }

        if (this.wufuneirong.equals("")) {
            verifiedInfo.verifyCode = VerifiedInfo.VERIFY_ERROR;
            ar.add("");
        }

        if (verifiedInfo.verifyCode == VerifiedInfo.VERIFY_ERROR) {
            StringBuilder sb = new StringBuilder("");
            for (String item : ar) {
                sb.append(item + ",");
            }
            sb.deleteCharAt(sb.length() - 1);
            verifiedInfo.verifyMessage = sb.toString() + "����Ϊ��";
        }

        return verifiedInfo;
    }


    public boolean save_into_db() {
        ContentValues values = new ContentValues();

        values.put("weixiu_id", this.weixiu_id);
        values.put("weixiudanyuan", this.weixiudanyuan);
        values.put("danyuanbianhao", this.danyuanbianhao);
        values.put("zuhumingcheng", this.zuhumingcheng);
        values.put("lianxidianhua", this.lianxidianhua);
        values.put("wufuneirong", this.wufuneirong);
        values.put("shoudanren", this.shoudanren);
        values.put("shoudanshijian", this.shoudanshijian);
        values.put("weixiuzhonglei", this.weixiuzhonglei);
        values.put("suoshuloupan", this.suoshuloupan);
        values.put("suoshulouge", this.suoshulouge);
        values.put("is_syned", this.is_syned);
        values.put("photo_paths", this.photo_paths);
        values.put("wanchengzhuangtai", "0");
        values.put("model_type", 0);


        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        if (this._id == 0) {// insert
            db.insertOrThrow("Weixiudan", null, values);
            Cursor cursor = db.rawQuery(
                    "select * from Weixiudan order by _id desc limit 1", null);
            cursor.moveToFirst();
            this._id = cursor.getInt(0);

        } else {// update
            db.update("Weixiudan", values, "_id=" + this._id, null);
        }
        db.close();
        return true;
    }


    public boolean save_jiedan_into_db() {
        ContentValues values = new ContentValues();

        values.put("weixiu_id", this.weixiu_id);
        values.put("weixiudanyuan", this.weixiudanyuan);
        values.put("danyuanbianhao", this.danyuanbianhao);
        values.put("zuhumingcheng", this.zuhumingcheng);
        values.put("lianxidianhua", this.lianxidianhua);
        values.put("wufuneirong", this.wufuneirong);
        values.put("shoudanren", this.shoudanren);
        values.put("shoudanshijian", this.shoudanshijian);
        values.put("weixiuzhonglei", this.weixiuzhonglei);
        values.put("suoshuloupan", this.suoshuloupan);
        values.put("suoshulouge", this.suoshulouge);
        values.put("is_syned", this.is_syned);
        values.put("photo_paths", this.photo_paths);
        values.put("wanchengzhuangtai", "0");
        values.put("model_type", 1);


        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();


        String sql = "select * from Weixiudan where weixiu_id='" + this.weixiu_id + "'" ;
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.getCount() == 0){
            db.insertOrThrow("Weixiudan", null, values);
        }
        db.close();
        return true;
    }

    public boolean jiedan_update_into_db() {
        ContentValues values = new ContentValues();
        values.put("model_type", 1);
        values.put("weixiukaishishijian", this.weixiukaishishijian);
        values.put("weixiujieshushijian", this.weixiujieshushijian);
        values.put("wanchengqingkuang", this.wanchengqingkuang);
        values.put("wanchengzhuangtai", this.wanchengzhuangtai);
        values.put("sign_path", this.sign_path);




        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();

        int a = db.update("Weixiudan", values, "_id=" + this._id, null);
        db.close();
        return true;
    }

    public static Cursor getScrollDataCursor(long startIndex, long maxCount,
                                             Context context) {

//		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        db = LocalAccessor.getInstance(context).openDB();
        String sql = "select * from Weixiudan where model_type = 0 order by _id DESC limit ?,? ";
        String[] selectionArgs = { String.valueOf(startIndex),
                String.valueOf(maxCount) };
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        return cursor;
    }

    public static Cursor jiedan_getScrollDataCursor(long startIndex, long maxCount,
                                                    Context context) {
        //

//		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        db = LocalAccessor.getInstance(context).openDB();
        String sql = "select * from Weixiudan where model_type = 1 order by _id DESC limit ?,? ";
        String[] selectionArgs = { String.valueOf(startIndex),
                String.valueOf(maxCount) };
        Cursor cursor = db.rawQuery(sql, selectionArgs);

        return cursor;
    }

    public static Weixiudan get_by_id(long id, Context ctx) {
        SQLiteDatabase db = LocalAccessor.getInstance(ctx).openDB();
        Cursor c = db.query("Weixiudan", null, "_id=" + id, null, null, null,
                null);

        Weixiudan weixiudan = new Weixiudan(ctx);
        if (c.moveToFirst()) {


            weixiudan._id = c.getInt(0);
            weixiudan.weixiu_id = c.getString(1);
            weixiudan.weixiudanyuan = c.getString(2);
            weixiudan.danyuanbianhao = c.getString(3);
            weixiudan.zuhumingcheng = c.getString(4);


            weixiudan.wufuneirong = c.getString(5);
            weixiudan.shoudanren = c.getString(6);
            weixiudan.shoudanshijian = c.getString(7);
            weixiudan.weixiuzhonglei = c.getString(8);
            weixiudan.lianxidianhua = c.getString(9);

            weixiudan.suoshuloupan = c.getString(10);
            weixiudan.suoshulouge = c.getString(11);
            weixiudan.weixiurenyuan = c.getString(12);
            weixiudan.is_syned = c.getInt(13);
            weixiudan.is_photo_syned = c.getInt(14);


            weixiudan.photo_paths = c.getString(15);
            weixiudan.model_type = c.getInt(17);
            weixiudan.weixiukaishishijian = c.getString(18);
            weixiudan.weixiujieshushijian = c.getString(19);
            weixiudan.wanchengqingkuang = c.getString(20);


            weixiudan.wanchengzhuangtai = c.getString(21);
            weixiudan.sign_path = c.getString(22);





        }

        c.close();
        db.close();
        return weixiudan;
    }

    public int delete() throws Exception {
        SQLiteDatabase db = LocalAccessor.getInstance(this.context).openDB();
        if((this.photo_paths != "" && this.photo_paths != null && IOUtils.file_exists(this.photo_paths))){
            IOUtils.remove_file(this.photo_paths);
        }
        int count = db.delete("Weixiudan", "_id=" + this._id, null);
        db.close();
        return count;
    }

    protected String jiedan_to_xml() {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<weixiudan>");
        sb.append("<weixiukaishishijian>").append(this.weixiukaishishijian).append(
                "</weixiukaishishijian>");
        sb.append("<weixiujieshushijian>").append(this.weixiujieshushijian).append(
                "</weixiujieshushijian>");
        sb.append("<wanchengqingkuang>").append(this.wanchengqingkuang).append(
                "</wanchengqingkuang>");
        sb.append("<wanchengzhuangtai>").append(this.wanchengzhuangtai).append(
                "</wanchengzhuangtai>");
        sb.append("<sign_path>").append(this.sign_path).append(
                "</sign_path>");
        sb.append("<shoudanshijian>").append(this.shoudanshijian).append(
                "</shoudanshijian>");



        sb.append("</weixiudan>");
        return sb.toString();
    }


    protected String to_xml() {
        StringBuilder sb = new StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("<weixiudan>");
        sb.append("<danyuanbianhao>").append(this.danyuanbianhao).append(
                "</danyuanbianhao>");
        sb.append("<danyuanmingcheng>").append(this.weixiudanyuan).append(
                "</danyuanmingcheng>");
        sb.append("<fuwuneirong>").append(this.wufuneirong).append(
                "</fuwuneirong>");

        sb.append("<lianxidianhua>").append(this.lianxidianhua).append(
                "</lianxidianhua>");
        sb.append("<shoudanren>").append(this.shoudanren).append(
                "</shoudanren>");
        sb.append("<shoudanshijian>").append(this.shoudanshijian).append(
                "</shoudanshijian>");
        sb.append("<suoshulouge>").append(this.suoshulouge).append(
                "</suoshulouge>");
        sb.append("<suoshuloupan>").append(this.suoshuloupan).append(
                "</suoshuloupan>");
        sb.append("<zuhumingcheng>").append(this.zuhumingcheng).append(
                "</zuhumingcheng>");

        sb.append("</weixiudan>");


        return sb.toString();
    }

    public boolean save_to_server() throws IOException, LTException {
        String url = LocalAccessor.getInstance(context).get_server_url()
                + "/weixiudans.xml";
        boolean res = true;
        String res_str = "";
        HashMap<String, String> map = new HashMap<String, String>();


        HashMap<String, String> params = new HashMap<String, String>();
        params.put("weixiudan[danyuanbianhao]", this.danyuanbianhao);
        params.put("weixiudan[danyuanmingcheng]", this.weixiudanyuan);
        params.put("weixiudan[fuwuneirong]", this.wufuneirong);
        params.put("weixiudan[lianxidianhua]", this.lianxidianhua);
        params.put("weixiudan[shoudanren]", this.shoudanren);
        params.put("weixiudan[shoudanshijian]", this.shoudanshijian);
        params.put("weixiudan[suoshulouge]", this.suoshulouge);
        params.put("weixiudan[suoshuloupan]", this.suoshuloupan);
        params.put("weixiudan[zuhumingcheng]", this.zuhumingcheng);


        BaseAuthenicationHttpClient.doRequest(url, User.current_user.name,
                User.current_user.password, params);

            if (res_str == "failed") {
                res = false;
            } else {
                res = true;
                           }

        return res;
    }




    public boolean update_syned() {
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();

        Cursor c = db.query("Weixiudan", null, "_id=" + _id, null, null, null,
                null);

        if (c.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put("is_syned", 1);
            values.put("weixiu_id", this.weixiu_id);
            db.update("Weixiudan", values, "_id=" + _id, null);

            Cursor cursor = db.rawQuery(
                    "select * from Weixiudan order by _id desc limit 1", null);
            cursor.moveToFirst();
            this.is_syned = cursor.getInt(13);

        }

        c.close();
        db.close();
        return true;
    }



    public boolean update_wanchengzhuangtai(String zhuangtai) {
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        Cursor c = db.query("Weixiudan", null, "_id=" + _id, null, null, null,
                null);

        if (c.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put("wanchengzhuangtai", zhuangtai);
            db.update("Weixiudan", values, "_id=" + _id, null);
        }

        c.close();
        db.close();
        return true;
    }

    public static ArrayList<Weixiudan> findAll(Context context, String where) {
        ArrayList<Weixiudan> records = new ArrayList<Weixiudan>();
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        String sql;
        if (where != null) {
            sql = "select * from " + TABLE_NAME + " where " + where + " order by id DESC";
        } else {
            sql = "select * from " + TABLE_NAME + " order by id DESC";
        }

        Cursor cursor;
        try {
            cursor = db.rawQuery(sql, null);
            Log.e(SelectorView.LOG_TAG, sql);
        } catch (Exception e) {
            return records;
        }
        cursor.moveToFirst();

        Weixiudan a = new Weixiudan(context);

        while (!cursor.isAfterLast()) {

            Weixiudan r = new Weixiudan(context);
            r._id = cursor.getInt(0);
            r.weixiu_id = cursor.getString(1);
            r.weixiudanyuan = cursor.getString(2);
            r.danyuanbianhao = cursor.getString(3);
            r.zuhumingcheng = cursor.getString(4);
            r.wufuneirong  = cursor.getString(5);
            r.shoudanren   = cursor.getString(6);
            r.shoudanshijian = cursor.getString(7);
            r.weixiuzhonglei = cursor.getString(8);
            r.lianxidianhua  = cursor.getString(9);
            r.suoshuloupan = cursor.getString(10);
            r.suoshulouge  = cursor.getString(11);
            r.weixiurenyuan = cursor.getString(12);
            r.is_syned = cursor.getInt(13);
            r.is_photo_syned = cursor.getInt(14);
            r.photo_paths  = cursor.getString(15);
            r.model_type  = cursor.getInt(17);
            r.weixiujieshushijian = cursor.getString(18);
            r.weixiujieshushijian = cursor.getString(19);
            r.wanchengqingkuang   = cursor.getString(20);
            r.wanchengzhuangtai   = cursor.getString(21);
            r.sign_path  = cursor.getString(22);

            records.add(r);
            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        return records;

    }

    public static ArrayList<Weixiudan> findAll(Context context) {
        return findAll(context, "");
    }

}
