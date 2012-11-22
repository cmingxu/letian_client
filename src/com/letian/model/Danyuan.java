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

	// index��PATH

	


	public Danyuan(Context context) {
		this.context = context;

		LocalAccessor.getInstance(this.context).create_db(
				SQL_CREATE_TABLE_MESSAGE);
	}

	public static void syn(Context context) {
		// get xml
		String xml;
		String url = LocalAccessor.getInstance(context).get_server_url() + "/danyuans.xml";
		
		ArrayList<Danyuan> items = new ArrayList<Danyuan>();
		try {
			while (true) {
				int offset = Danyuan.max_count(context);
				String params = "?offset=" + offset + "&limit="
						+ Constants.EACH_SLICE;
				Log.e(Danyuan.LOG_TAG, url + params);
				xml = BaseAuthenicationHttpClient.doRequest(url + params,
						User.current_user.name, User.current_user.password);
				// Log.e(Danyuan.LOG_TAG, xml);

				// turn xml into object
				items = turn_xml_into_items(xml, context);
				// prepare database
				for (Danyuan d : items) {
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

	private static ArrayList<Danyuan> turn_xml_into_items(String xml,
			Context context) {
		ArrayList<Danyuan> items = new ArrayList<Danyuan>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			DanyuanHandler handler = new DanyuanHandler(context);
			InputStream is = new ByteArrayInputStream(xml.getBytes());
			parser.parse(is, handler);
			items = handler.getDanyuans();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return items;
	}

	public boolean save_into_db() throws LTException {
		ContentValues values = new ContentValues();

		values.put("danyuanbianhao", this.danyuanbianhao);
		values.put("danyuanmingcheng", this.danyuanmingcheng);
		values.put("lougebianhao", this.lougebianhao);
		values.put("zhuhubianhao", this.zhuhubianhao);
		values.put("louceng", this.louceng);
		values.put("loucengmingcheng", this.loucengmingcheng);
		values.put("createdTime", (new Date()).toLocaleString());

		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		db.insertOrThrow("Danyuan", null, values);
		db.close();
		return true;
	}

	private static int max_count(Context context) {
		int offset = 0;
		// make sure table created
		new Danyuan(context);
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		Cursor c = db.query("Danyuan", null, null, null, null, null, null);
		offset = c.getCount();
		c.close();
		db.close();
		return offset;

	}

	public static Cursor getScrollDataCursor(long startIndex, long maxCount,
			Context context) {
		// ���� ��·��

		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		String sql = "select * from Danyuan limit ?,?";
		String[] selectionArgs = { String.valueOf(startIndex),
				String.valueOf(maxCount) };
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		Log.e("ssssssssss cursor", Integer.toString(cursor.getColumnCount()));
		return cursor;
	}

	public static HashMap<String, String> mingcheng_bianhao_map(
			Context context, String loucengmingcheng, String lougebianhao) {
		HashMap<String, String> res = new HashMap<String, String>();
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		String sql = "select * from Danyuan where loucengmingcheng = '"
				+ loucengmingcheng.replaceAll("\\s*", "")
				+ "' and lougebianhao='" + lougebianhao.replaceAll("\\s*", "")
				+ "' order by _id DESC";
		Log.e("sssssssssssssss", sql);
		Cursor cursor;
		try{
			cursor = db.rawQuery(sql,null);
		}catch(Exception e){
			return res;
		}
		Log.e("maobing", Integer.toString(cursor.getCount()));
		cursor.moveToFirst();
		while (cursor.isAfterLast() != true) {
			Log.e("maobing", "sssssssssssssssssssssssssss");
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
//		cursor.getColumnName(arg0)

		
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
		Log.e(Danyuan.LOG_TAG,"sql");
		Log.e(Danyuan.LOG_TAG,Integer.toString(cursor.getCount()));
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
	Log.e(Danyuan.LOG_TAG,"sql");
	Log.e(Danyuan.LOG_TAG,Integer.toString(cursor1.getCount()));
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

}
