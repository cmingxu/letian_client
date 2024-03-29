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
    public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean hasProblem;
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
    public String kfs_or_yz;

    public boolean isKfs(){
        return this.kfs_or_yz.equalsIgnoreCase(new String("kfs"));
    }


    public static final String LOG_TAG = "YfRecordModel";
    public static final String TABLE_NAME = "YfRecord";


    private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS YfRecord("
            + "id INTEGER PRIMARY KEY,"
            + "hasProblem INTEGER,"
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
            + "danyuan_bh TEXT,"
            + "kfs_or_yz TEXT"
            + ");";


    public boolean isResult() {
        return hasProblem;
    }

    public void setResult(boolean hasProblem) {
        this.hasProblem = hasProblem;
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
        Log.d(SelectorView.LOG_TAG, this.fangjianleixing_id);
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
        LocalAccessor.getInstance(this.context).create_db(
                SQL_CREATE_TABLE_MESSAGE);
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


    public void update_save_status(boolean updated) {
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        ContentValues values = new ContentValues();
        values.put("save_to_server", updated ? 1 : 0);
        String where = "id=" + this.id;


        db.update(YfRecord.TABLE_NAME, values, where, null);
        db.close();

    }


    public boolean save_to_db() {

        LocalAccessor.getInstance(this.context).create_db(
                SQL_CREATE_TABLE_MESSAGE);
        ContentValues values = new ContentValues();
        values.put("hasProblem", this.hasProblem ? 1 : 0);
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
        values.put("save_to_server", 1);
        values.put("danyuan_bh", this.danyuan_bh);
        values.put("kfs_or_yz", this.kfs_or_yz);


        Log.d(SelectorView.LOG_TAG, "save db kfs_or_yz" + this.kfs_or_yz);
        try {
            if (this.existInDb(context)) {
                super.updateDb(context, YfRecord.TABLE_NAME, values, "id=" + this.id);
                Log.d(YfRecord.LOG_TAG, "update db " + this.id + this.toString());
            } else {
                super.save_into_db(context, YfRecord.TABLE_NAME, values);
                Log.d(YfRecord.LOG_TAG, "save db " + this.id + this.toString());

            }

            SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
            Cursor c = db.rawQuery("select id from " + YfRecord.TABLE_NAME + " order by id desc limit 1;", null);
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


    public boolean save_to_server(Context context) throws IOException, LTException {
        String url = null;
        if(this.isKfs()){
            url = LocalAccessor.getInstance(context).get_server_url() + "/kfs_yfs";
        }else
        {
            url = LocalAccessor.getInstance(context).get_server_url() + "/yz_yfs";
        }

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("yf[result]", this.hasProblem ? "not_ok" : "ok");
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

        Log.d(YfRecord.LOG_TAG, "SAve to server" + this.toString());


        BaseAuthenicationHttpClient.doRequest(url, User.current_user.name,
                User.current_user.password, params);


        return true;
    }

    @Override
    public String toString() {
        return "YfRecord{" +
                "id=" + id +
                ", hasProblem=" + hasProblem +
                ", reason='" + reason + '\'' +
                ", louge_bh='" + louge_bh + '\'' +
                ", louge='" + louge + '\'' +
                ", fangjianleixing='" + fangjianleixing + '\'' +
                ", shoulouduixiang='" + shoulouduixiang + '\'' +
                ", shoulouxiangmu='" + shoulouxiangmu + '\'' +
                ", danyuan='" + danyuan + '\'' +
                ", huxing='" + huxing + '\'' +
                ", danyuan_id='" + danyuan_id + '\'' +
                ", saved=" + saved +
                ", danyuan_bh='" + danyuan_bh + '\'' +
                ", huxing_id='" + huxing_id + '\'' +
                ", fangjianleixing_id='" + fangjianleixing_id + '\'' +
                ", shoulouduixiang_id='" + shoulouduixiang_id + '\'' +
                ", shoulouxiangmu_id='" + shoulouxiangmu_id + '\'' +
                ", kfs_or_yz='" + kfs_or_yz + '\'' +

                ", context=" + context +
                '}';
    }

    public static ArrayList<YfRecord> findAll(Context context, String where) {
        ArrayList<YfRecord> records = new ArrayList<YfRecord>();
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

        YfRecord a = new YfRecord(context);

        while (!cursor.isAfterLast()) {

            YfRecord r = new YfRecord(context);
            r.setId(cursor.getInt(0));
            r.setResult(cursor.getInt(1) == 1 ? true : false);
            r.setReason(cursor.getString(2));
            r.setLouge_bh(cursor.getString(3));
            r.setLouge(cursor.getString(4));
            r.setFangjianleixing(cursor.getString(5));
            r.setShoulouduixiang(cursor.getString(6));
            r.setShoulouxiangmu(cursor.getString(7));
            r.setDanyuan(cursor.getString(8));
            r.setHuxing(cursor.getString(9));
            r.setDanyuan_id(cursor.getString(10));
            r.setHuxing_id(cursor.getString(11));
            r.setFangjianleixing_id(cursor.getString(12));
            r.setShoulouduixiang_id(cursor.getString(13));
            r.setShoulouxiangmu_id(cursor.getString(14));
            r.setDanyuan_bh(cursor.getString(16));
            r.kfs_or_yz = cursor.getString(17);

            records.add(r);

            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        return records;

    }

    public static ArrayList<YfRecord> findAll(Context context) {
        return findAll(context, "");
    }

    public boolean existInDb(Context context) {
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        String sql;
        Log.d(SelectorView.LOG_TAG, "exist in db" + this.kfs_or_yz);
        sql = "select * from " + TABLE_NAME + " where "
                + " louge_bh = '" + this.louge_bh + "' and "
                + " louge = '" + this.louge + "' and "
                + " fangjianleixing = '" + this.fangjianleixing + "' and "
                + " shoulouduixiang = '" + this.shoulouduixiang + "' and "
                + " shoulouxiangmu = '" + this.shoulouxiangmu + "' and "
                + " danyuan = '" + this.danyuan + "' and "
                + " danyuan_id = '" + this.danyuan_id + "' and "
                + " fangjianleixing_id = '" + this.fangjianleixing_id + "' and "
                + " shoulouduixiang_id = '" + this.shoulouduixiang_id + "' and "
                + " kfs_or_yz = '" + this.kfs_or_yz + "' and "
                + " shoulouxiangmu_id = '" + this.shoulouxiangmu_id + "'; ";

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, null);
        } catch (Exception e) {


            if (cursor != null) {

                cursor.close();
            }
            if (db != null) {

                db.close();

            }

            return false;

        }
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            this.id = 0;
            cursor.close();
            db.close();
            return false;
        } else {
            this.id = cursor.getInt(0);
            this.reason = cursor.getString(2);

            this.hasProblem = cursor.getInt(1) == 1 ? true : false;
            this.saved = Boolean.valueOf(Integer.toString(cursor.getInt(16)));
            Log.d(YfRecord.LOG_TAG,Integer.toString(cursor.getInt(1)));
            Log.d(YfRecord.LOG_TAG, this.toString());
            cursor.close();
            db.close();
            return true;
        }
    }


    public void displayAll(Context context) {
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        String sql;
        sql = "select * from " + TABLE_NAME + ";";

        Cursor cursor;
        try {
            cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                Log.e(SelectorView.LOG_TAG, "111111111111111111111111111");
                Log.e(SelectorView.LOG_TAG, "id" + cursor.getInt(0));
                Log.e(SelectorView.LOG_TAG, "hasProblem" + cursor.getInt(1));
                Log.e(SelectorView.LOG_TAG, "reason" + cursor.getString(2));
                Log.e(SelectorView.LOG_TAG, "louge_bh" + cursor.getString(3));
                Log.e(SelectorView.LOG_TAG, "louge" + cursor.getString(4));
                Log.e(SelectorView.LOG_TAG, "fangjianleixing" + cursor.getString(5));
                Log.e(SelectorView.LOG_TAG, "shoulouduixiang" + cursor.getString(6));
                Log.e(SelectorView.LOG_TAG, "shoulouxiangmu" + cursor.getString(7));
                Log.e(SelectorView.LOG_TAG, "danyuan" + cursor.getString(8));
                Log.e(SelectorView.LOG_TAG, "huxing" + cursor.getString(9));
                Log.e(SelectorView.LOG_TAG, "danyuan_id" + cursor.getString(10));
                Log.e(SelectorView.LOG_TAG, "huxing_id" + cursor.getString(11));
                Log.e(SelectorView.LOG_TAG, "fangjianleixing_id" + cursor.getString(12));
                Log.e(SelectorView.LOG_TAG, "shoulouduixiang_id" + cursor.getString(13));
                Log.e(SelectorView.LOG_TAG, "shoulouxiangmu_id" + cursor.getString(14));
                Log.e(SelectorView.LOG_TAG, "saved_tO_server" + cursor.getString(15));
                Log.e(SelectorView.LOG_TAG, "danyuan_bh" + cursor.getString(16));
                Log.e(SelectorView.LOG_TAG, "kfs_or_yz" + cursor.getString(17));
                cursor.moveToNext();
            }
        } catch (Exception e) {
            Log.d(SelectorView.LOG_TAG, e.toString());
        }

    }


    public String pic_dir() {
        return ("letian_images/"
                + this.danyuan + "/"
                + this.shoulouduixiang + "/"
                + this.shoulouxiangmu);
    }

}
