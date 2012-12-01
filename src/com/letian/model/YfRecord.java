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
public class YfRecord extends Model {
    public boolean result;
    public String reason;
    public String louge_bh;
    public String louge;
    public String fangjianleixing;
    public String shoulouduixiang;
    public String shoulouxiangmu;
    public String danyuan;
    public String huxing;
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
    public String shoulouduixiang_id;
    public String shoulouxiangmu_id;


    public static final String LOG_TAG = "YfRecordModel";
    public static final String TABLE_NAME = "YfRecord";


    private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS YfRecord("
            + "id INTEGER PRIMARY KEY,"
            + "result INTEGER,"
            + "reason TEXT,"
            + "louge_bh  TEXT,"
            + "louge TEXT,"
            + "fangjianleixing TEXT,"
            + "shoulouduixiang TEXT,"
            + "shoulouxiangmu TEXT,"
            + "danyuan TEXT,"
            + "huxing TEXT,"
            + "danyuan_id TEXT,"
            + "huxing_id TEXT,"
            + "fangjianleixing_id TEXT,"
            + "shoulouduixiang_id TEXT,"
            + "shoulouxiangmu_id TEXT,"
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

    public String getShoulouduixiang_id() {
        return shoulouduixiang_id;
    }

    public void setShoulouduixiang_id(String shoulouduixiang_id) {
        this.shoulouduixiang_id = shoulouduixiang_id;
    }

    public String getShoulouxiangmu_id() {
        return shoulouxiangmu_id;
    }

    public void setShoulouxiangmu_id(String shoulouxiangmu_id) {
        this.shoulouxiangmu_id = shoulouxiangmu_id;
    }


    public YfRecord(Context context) {
        this.context = context;
    }

    public String getDanyuan() {
        return danyuan;
    }

    public void setDanyuan(String danyuan) {
        this.danyuan = danyuan;
    }

    public String getHuxing() {
        return huxing;
    }

    public void setHuxing(String huxing) {
        this.huxing = huxing;
    }

    public String getFangjianleixing() {
        return fangjianleixing;
    }

    public void setFangjianleixing(String fangjianleixing) {
        this.fangjianleixing = fangjianleixing;
    }

    public String getShoulouduixiang() {
        return shoulouduixiang;
    }

    public void setShoulouduixiang(String shoulouduixiang) {
        this.shoulouduixiang = shoulouduixiang;
    }

    public String getShoulouxiangmu() {
        return shoulouxiangmu;
    }

    public void setShoulouxiangmu(String shoulouxiangmu) {
        this.shoulouxiangmu = shoulouxiangmu;
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
        values.put("shoulouduixiang", this.shoulouduixiang);
        values.put("shoulouxiangmu", this.shoulouxiangmu);
        values.put("danyuan", this.danyuan);
        values.put("huxing", this.huxing);
        values.put("danyuan_id", this.danyuan_id);
        values.put("huxing_id", this.huxing_id);
        values.put("fangjianleixing_id", this.fangjianleixing_id);
        values.put("shoulouduixiang_id", this.shoulouduixiang_id);
        values.put("shoulouxiangmu_id", this.shoulouxiangmu_id);
        values.put("save_to_server", 0);
        values.put("danyuan_bh", this.danyuan_bh);


        try {
            return super.save_into_db(context, YfRecord.TABLE_NAME, values);
        } catch (LTException e) {
            e.printStackTrace();
            return false;
        }

    }


    public boolean save_to_server(Context context) throws IOException, LTException {
        String url = LocalAccessor.getInstance(context).get_server_url() + "/kfs_yfs";
        String xmlString = null;

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("yf[result]", this.result ? "ok" : "not_ok");
        params.put("yf[reason]", this.reason);
        params.put("yf[louge_bh]", this.louge_bh);
        params.put("yf[louge]", this.louge);
        params.put("yf[fangjianleixing]", this.fangjianleixing);
        params.put("yf[shoulouduixiang]", this.shoulouduixiang);
        params.put("yf[shoulouxiangmu]", this.shoulouxiangmu);
        params.put("yf[danyuan]", this.danyuan);
        params.put("yf[huxing]", this.huxing);
        params.put("yf[danyuan_id]", this.danyuan_id);
        params.put("yf[huxing_id]", this.huxing_id);
        params.put("yf[fangjianleixing_id]", this.fangjianleixing_id);
        params.put("yf[shoulouduixiang_id]", this.shoulouduixiang_id);
        params.put("yf[shoulouxiangmu_id]", this.shoulouxiangmu_id);
        params.put("yf[danyuan_bh]", this.danyuan_bh);


        BaseAuthenicationHttpClient.doRequest(url, User.current_user.name,
                    User.current_user.password, params);


        return true;
    }

    public static ArrayList<YfRecord> findAll(Context context){
        ArrayList<YfRecord> records = new ArrayList<YfRecord>();
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        String sql;
        sql = "select * from " + TABLE_NAME + " order by _id DESC";

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
            YfRecord r = new YfRecord( context  );
            r.setResult(cursor.getString(1) == "1" ? true : false);
            r.setReason(cursor.getString(2));
            r.setLouge_bh(cursor.getString(3));
            r.setLouge(cursor.getString(4));
            r.setFangjianleixing(cursor.getString(5));
            r.setShoulouduixiang(cursor.getString(6));
            r.setShoulouxiangmu(cursor.getString(7));
            r.setDanyuan(cursor.getString(8));
            r.setHuxing(cursor.getString(9));
            r.setHuxing_id(cursor.getString(10));
            r.setFangjianleixing_id(cursor.getString(11));
            r.setShoulouduixiang_id(cursor.getString(12));
            r.setShoulouxiangmu_id(cursor.getString(13));
            r.setDanyuan_bh(cursor.getString(14));

            records.add(r);
            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        return records;

    }

    public boolean existInDb(Context context){
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        String sql;
        sql = "select * from " + TABLE_NAME + " where "
                + " louge_bh = '"              + this.louge_bh + "' and "
                + " louge = '"                 + this.louge + "' and "
                + " fangjianleixing = '"       + this.fangjianleixing + "' and "
                + " shoulouduixiang = '"       + this.shoulouduixiang + "' and "
                + " shoulouxiangmu = '"        + this.shoulouxiangmu + "' and "
                + " danyuan = '"               + this.danyuan + "' and "
//                + " huxing = '"                + this.huxing + "' and "
                + " danyuan_id = '"            + this.danyuan_id + "' and "
//                + " huxing_id = '"             + this.huxing_id + "' and "
                + " fangjianleixing_id = '"    + this.fangjianleixing_id + "' and "
                + " shoulouduixiang_id = '"    + this.shoulouduixiang_id + "' and "
                + " shoulouxiangmu_id = '"     + this.shoulouxiangmu_id + "'; ";

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
        return ("letian_images/"
                + this.danyuan + "/"
                + this.shoulouduixiang + "/"
                + this.shoulouxiangmu);
    }

}
