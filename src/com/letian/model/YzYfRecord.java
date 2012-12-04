package com.letian.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import com.letian.lib.BaseAuthenicationHttpClient;
import com.letian.lib.LocalAccessor;
import com.letian.view.SelectorView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-24
 * Time: 下午4:59
 * To change this template use File | Settings | File Templates.
 */
public class YzYfRecord extends Model {
    public int id;
    public boolean result;
    public String reason;
    public String louge_bh;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String louge;
    public String fangjianleixing;
    public String danyuan;
    public String danyuan_id;
    public boolean saved;

    public String getDanyuan_bh() {
        return danyuan_bh;
    }

    public void setDanyuan_bh(String danyuan_bh) {
        this.danyuan_bh = danyuan_bh;
    }

    public String danyuan_bh;
    public String huxing_id;
    public String fangjianleixing_id;


    public static final String LOG_TAG = "YzYfRecord";
    public static final String TABLE_NAME = "YzYfRecord";


    private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS YzYfRecord("
            + "id INTEGER PRIMARY KEY,"
            + "result INTEGER,"
            + "reason TEXT,"
            + "louge_bh  TEXT,"
            + "louge TEXT,"
            + "fangjianleixing TEXT,"
            + "danyuan TEXT,"
            + "huxing TEXT,"
            + "danyuan_id TEXT,"
            + "huxing_id TEXT,"
            + "fangjianleixing_id TEXT,"
            + "save_to_server INTEGER,"
            + "danyuan_bh TEXT"
            + ");";



    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLouge_bh() {
        return louge_bh;
    }

    public void setLouge_bh(String louge_bh) {
        this.louge_bh = louge_bh;
    }

    public Context context;

    public String getDanyuan_id() {
        return danyuan_id;
    }

    public void setDanyuan_id(String danyuan_id) {
        this.danyuan_id = danyuan_id;
    }

    public String getHuxing_id() {
        return huxing_id;
    }

    public void setHuxing_id(String huxing_id) {
        this.huxing_id = huxing_id;
    }

    public String getFangjianleixing_id() {
        return fangjianleixing_id;
    }

    public void setFangjianleixing_id(String fangjianleixing_id) {
        this.fangjianleixing_id = fangjianleixing_id;
    }



    public YzYfRecord(Context context) {
        this.context = context;
    }

    public String getDanyuan() {
        return danyuan;
    }

    public void setDanyuan(String danyuan) {
        this.danyuan = danyuan;
    }


    public String getFangjianleixing() {
        return fangjianleixing;
    }

    public void setFangjianleixing(String fangjianleixing) {
        this.fangjianleixing = fangjianleixing;
    }


    public String getLouge() {
        return louge;
    }

    public void setLouge(String louge) {
        this.louge = louge;
    }


    public boolean save_to_db(){

        LocalAccessor.getInstance(this.context).create_db(
                SQL_CREATE_TABLE_MESSAGE);
        ContentValues values = new ContentValues();
        values.put("result", this.result);
        values.put("reason", this.reason);
        values.put("louge_bh", this.louge_bh);
        values.put("louge", this.louge);
        values.put("fangjianleixing", this.fangjianleixing);
        values.put("danyuan", this.danyuan);
        values.put("danyuan_id", this.danyuan_id);
        values.put("huxing_id", this.huxing_id);
        values.put("fangjianleixing_id", this.fangjianleixing_id);
        values.put("save_to_server", 0);
        values.put("danyuan_bh", this.danyuan_bh);


        try {


            Log.d(SelectorView.LOG_TAG, "save_to_db " + this.toString());
            super.save_into_db(context, YzYfRecord.TABLE_NAME, values);

            SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
            Cursor c = db.rawQuery("select id from " + YzYfRecord.TABLE_NAME + " order by id asc limit 1;", null );
            c.moveToFirst();
            this.id = c.getInt(0);
            c.close();
            db.close();

            return true;
        } catch (LTException e) {
            e.printStackTrace();
            return false;
        }

    }

    public void update_save_status(boolean updated){
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        ContentValues values = new ContentValues();
        values.put("save_to_server", updated ? 1 : 0);
        String where = "id=" + this.id;

        Log.d(SelectorView.LOG_TAG, "update_save_status" + Boolean.toString(updated));
        Log.d(SelectorView.LOG_TAG, where);
        db.update(YzYfRecord.TABLE_NAME, values, where, null);
        db.close();
    }

    public boolean save_to_server(Context context) throws IOException, LTException {
        String url = LocalAccessor.getInstance(context).get_server_url() + "/yz_yfs";
        String xmlString = null;

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("yf[result]", this.result ? "ok" : "not_ok");
        params.put("yf[reason]", this.reason);
        params.put("yf[louge_bh]", this.louge_bh);
        params.put("yf[louge]", this.louge);
        params.put("yf[fangjianleixing]", this.fangjianleixing);
        params.put("yf[danyuan]", this.danyuan);
        params.put("yf[danyuan_id]", this.danyuan_id);
        params.put("yf[huxing_id]", this.huxing_id);
        params.put("yf[fangjianleixing_id]", this.fangjianleixing_id);
        params.put("yf[danyuan_bh]", this.danyuan_bh);

        Log.d(SelectorView.LOG_TAG, "save_to_server " + this.toString());

        BaseAuthenicationHttpClient.doRequest(url, User.current_user.name,
                User.current_user.password, params);


        return true;
    }
    public static ArrayList<YzYfRecord> findAll(Context context, String where){
        ArrayList<YzYfRecord> records = new ArrayList<YzYfRecord>();
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        String sql;
        if (where != null) {
            sql = "select * from " + TABLE_NAME + " where " + where + " order by id DESC";
        }else{
            sql = "select * from " + TABLE_NAME + " order by id DESC";
        }
        Cursor cursor;
        try{
            cursor = db.rawQuery(sql,null);
            Log.d(SelectorView.LOG_TAG, sql);
        }catch(Exception e){
            return records;
        }
        cursor.moveToFirst();
        while(cursor.isAfterLast() != true){
            Log.d(YfRecord.LOG_TAG, cursor.getString(3));
            YzYfRecord r = new YzYfRecord( context  );


            r.setId(cursor.getInt(0));
            r.setResult(cursor.getString(1) == "1" ? true : false);
            r.setReason(cursor.getString(2));
            r.setLouge_bh(cursor.getString(3));
            r.setLouge(cursor.getString(4));
            r.setFangjianleixing(cursor.getString(5));
            r.setDanyuan(cursor.getString(6));
            r.setDanyuan_id(cursor.getString(8));
            r.setHuxing_id(cursor.getString(9));
            r.setFangjianleixing_id(cursor.getString(10));
            r.setDanyuan_bh(cursor.getString(12));

            Log.d(SelectorView.LOG_TAG, "findall " + r.toString());
            records.add(r);
            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        return records;
    }

    @Override
    public String toString() {
        return "YzYfRecord{" +
                "id=" + id +
                ", result=" + result +
                ", reason='" + reason + '\'' +
                ", louge_bh='" + louge_bh + '\'' +
                ", louge='" + louge + '\'' +
                ", fangjianleixing='" + fangjianleixing + '\'' +
                ", danyuan='" + danyuan + '\'' +
                ", danyuan_id='" + danyuan_id + '\'' +
                ", saved=" + saved +
                ", danyuan_bh='" + danyuan_bh + '\'' +
                ", huxing_id='" + huxing_id + '\'' +
                ", fangjianleixing_id='" + fangjianleixing_id + '\'' +
                ", context=" + context +
                '}';
    }

    public static ArrayList<YzYfRecord> findAll(Context context){
        return findAll(context, null);

    }

    public boolean existInDb(Context context){
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        String sql;
        sql = "select * from " + TABLE_NAME + " where "
                + " louge_bh = '"              + this.louge_bh + "' and "
                + " louge = '"                 + this.louge + "' and "
                + " fangjianleixing = '"       + this.fangjianleixing + "' and "
                + " danyuan = '"               + this.danyuan + "' and "
//                + " huxing = '"                + this.huxing + "' and "
                + " danyuan_id = '"            + this.danyuan_id + "' and "
//                + " huxing_id = '"             + this.huxing_id + "' and "
                + " fangjianleixing_id = '"    + this.fangjianleixing_id + "'";

        Cursor cursor = null;
        try{
            cursor = db.rawQuery(sql,null);
            Log.d(SelectorView.LOG_TAG, sql);
        }catch(Exception e){
            Log.d(SelectorView.LOG_TAG, e.toString());


            cursor.close();
            db.close();
            return false;

        }
        cursor.moveToFirst();
        if(cursor.getCount() == 0){

            cursor.close();
            db.close();
            return false;
        }else {
            this.reason = cursor.getString(2);
            this.result = Boolean.valueOf(Integer.toString(cursor.getInt(1)));
            this.saved  = Boolean.valueOf(Integer.toString(cursor.getInt(16)));

            cursor.close();
            db.close();
            return true;
        }
    }


    public void displayAll(Context context){
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        String sql;
        sql = "select * from " + TABLE_NAME + ";";

        Cursor cursor;
        try{
            cursor = db.rawQuery(sql,null);
            Log.d(SelectorView.LOG_TAG, sql);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){

                Log.d(SelectorView.LOG_TAG, "111111111111111111111111111");
                Log.d(SelectorView.LOG_TAG, "id" + cursor.getInt(0));
                Log.d(SelectorView.LOG_TAG, "result" + cursor.getInt(1));
                Log.d(SelectorView.LOG_TAG, "reason" + cursor.getString(2));
                Log.d(SelectorView.LOG_TAG, "louge_bh" + cursor.getString(3));
                Log.d(SelectorView.LOG_TAG, "louge" + cursor.getString(4));
                Log.d(SelectorView.LOG_TAG, "fangjianleixing" + cursor.getString(5));
                Log.d(SelectorView.LOG_TAG, "shoulouduixiang" + cursor.getString(6));
                Log.d(SelectorView.LOG_TAG, "shoulouxiangmu" + cursor.getString(7));
                Log.d(SelectorView.LOG_TAG, "danyuan" + cursor.getString(8));
                Log.d(SelectorView.LOG_TAG, "huxing" + cursor.getString(9));
                Log.d(SelectorView.LOG_TAG, "danyuan_id" + cursor.getString(10));
                Log.d(SelectorView.LOG_TAG, "huxing_id" + cursor.getString(11));
                Log.d(SelectorView.LOG_TAG, "fangjianleixing_id" + cursor.getString(12));
                Log.d(SelectorView.LOG_TAG, "shoulouduixiang_id" + cursor.getString(13));
                Log.d(SelectorView.LOG_TAG, "shoulouxiangmu_id" + cursor.getString(14));


                cursor.moveToNext() ;
            }
        }catch(Exception e){
            Log.d(SelectorView.LOG_TAG, e.toString());
        }



    }


    public String pic_dir(){
        return ("letian_yzyf_images/"
                + this.danyuan + "/");
    }

}
