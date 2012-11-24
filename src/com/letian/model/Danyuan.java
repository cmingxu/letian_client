package com.letian.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.letian.lib.BaseAuthenicationHttpClient;
import com.letian.lib.Constants;
import com.letian.lib.LocalAccessor;
import com.letian.model.xmlhandler.DanyuanHandler;

public class Danyuan extends Model {
	public Integer _id;

	public String danyuanbianhao;

	public String danyuanmingcheng;

	public String lougebianhao;

	public String zhuhubianhao;
	public String louceng;
	public String loucengmingcheng;

	public static final String LOG_TAG = "DANYUAN_MODEL";
    public static final String TABLE_NAME = "DANYUAN";
	public Context context;


	private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS Danyuan("
			+ "_id INTEGER PRIMARY KEY,"
			+ "danyuanbianhao TEXT,"
			+ "danyuanmingcheng TEXT,"
			+ "lougebianhao TEXT,"
			+ "zhuhubianhao TEXT,"
			+ "louceng TEXT,"
			+ "loucengmingcheng TEXT,"
			+ "createdTime TEXT" + ");";


    public Danyuan(Integer _id, String danyuanbianhao, String danyuanmingcheng, String lougebianhao, String zhuhubianhao) {
        this._id = _id;
        this.danyuanbianhao = danyuanbianhao;
        this.danyuanmingcheng = danyuanmingcheng;
        this.lougebianhao = lougebianhao;
        this.zhuhubianhao = zhuhubianhao;
    }

    public Danyuan(Context context) {
		this.context = context;

		LocalAccessor.getInstance(this.context).create_db(
				SQL_CREATE_TABLE_MESSAGE);
	}

	public static void syn(Context context) {
        Log.d(LOG_TAG, "syn start danyuan");
		// get xml
		String xml;
		String url = LocalAccessor.getInstance(context).get_server_url() + "/danyuans.xml";

		ArrayList<Danyuan> items = new ArrayList<Danyuan>();
		try {
			while (true) {

				int offset = Model.max_count(context, "Danyuan");
                Log.d(LOG_TAG, "syn start danyuan");
                String params = "?offset=" + offset + "&limit="
						+ Constants.EACH_SLICE;
				xml = BaseAuthenicationHttpClient.doRequest(url + params,
						User.current_user.name, User.current_user.password);

				items = (ArrayList<Danyuan>)Model.turn_xml_into_items(xml, new DanyuanHandler(context));
				for (Danyuan d : items) {
                        Log.d(LOG_TAG, d.danyuanbianhao);
						d.save_into_db();
				}
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
		values.put("danyuanbianhao", this.danyuanbianhao);
		values.put("danyuanmingcheng", this.danyuanmingcheng);
		values.put("lougebianhao", this.lougebianhao);
		values.put("zhuhubianhao", this.zhuhubianhao);
		values.put("louceng", this.louceng);
		values.put("loucengmingcheng", this.loucengmingcheng);
		values.put("createdTime", (new Date()).toString());

	    return super.save_into_db(context, Danyuan.TABLE_NAME, values);
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
		try{
			cursor = db.rawQuery(sql,null);
		}catch(Exception e){
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

	public static ArrayList<String> distinct_louceng(Context context,
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
			res.add(cursor.getString(0));
			cursor.moveToNext();
		}

		cursor.close();
		db.close();
		return res;
	}

	public static Danyuan get_danyuan_with_danyuanbianhao(String bianhao,Context context){
		Danyuan danyuan = new Danyuan(context);
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		String sql = "select * from Danyuan where danyuanbianhao='"
				+ bianhao + "'";
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		danyuan._id = cursor.getInt(0);
		danyuan.danyuanbianhao = cursor.getString(1);
		danyuan.zhuhubianhao = cursor.getString(4);
		cursor.close();
	    db.close();

		return danyuan;
	}
	
	public Zhuhu get_zhuhu() {
		Zhuhu zh;
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		String sql1 = "select * from Zhuhu";
    	Cursor cursor1 = db.rawQuery(sql1, null);
		String sql = "select * from Zhuhu where zhuhubianhao	='"
				+ this.zhuhubianhao + "'";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor == null) {
			return null;
		} else {
			cursor.moveToFirst();
			zh = new Zhuhu(context);
			try{
			zh.zhuhumingcheng = cursor.getString(1);
			zh.lianxidianhua = cursor.getString(4);
			zh.shoujihaoma = cursor.getString(3);
			}catch(Exception e){
				zh.zhuhumingcheng = "";
				zh.lianxidianhua = "";
				zh.shoujihaoma = "";
			}
			cursor.close();
		}

		db.close();
		return zh;
	}

    public static ArrayList<Danyuan> findAll(Context context){
        ArrayList<Danyuan> danyuans = new ArrayList<Danyuan>();
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        String sql = "select * from Danyuan order by _id DESC";
        Cursor cursor;
        try{
            cursor = db.rawQuery(sql,null);
        }catch(Exception e){
            return danyuans;
        }
        cursor.moveToFirst();
        while(cursor.isAfterLast() != true){
            danyuans.add(new Danyuan(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
            ));
            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        return danyuans;
    }

}
