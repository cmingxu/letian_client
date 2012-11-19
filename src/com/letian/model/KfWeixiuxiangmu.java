package com.letian.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.letian.lib.BaseAuthenicationHttpClient;
import com.letian.lib.Constants;
import com.letian.lib.LocalAccessor;
import com.letian.model.xmlhandler.KfWeixiuxiangmuHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class KfWeixiuxiangmu extends Model {

	public Integer _id;
	public String xiangmu_id;
	public String xiangmuleibie;
	public String xiangmumingcheng;
	public String xiangmuneirong;
	public int is_syned;

	public static final String LOG_TAG = "KfWeixiuxiangmu_MODEL";
	public Context context;

	private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS kf_weixiuxiangmus("
			+ "_id INTEGER PRIMARY KEY,"
			+ "xiangmu_id TEXT,"
			+ "xiangmuleibie TEXT,"
			+ "xiangmumingcheng TEXT,"
			+ "xiangmuneirong TEXT,"
			+ "is_syned INTEGER,"
			+ "createdTime TEXT" + ");";


	
	public KfWeixiuxiangmu(Context context) {
		this.context = context;

		LocalAccessor.getInstance(this.context).create_db(
				SQL_CREATE_TABLE_MESSAGE);
	}
	
	
	
	public static boolean table_exists(Context context) {
		boolean res = true;
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		try {
			db.rawQuery("select * from kf_weixiuxiangmus", null);
			db.close();
		} catch (Exception e) {
			res = false;
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return res;
	}

	// �ͷ�����ͬ����USBֱ��ʱ����Ч��
	public static void syn(Context context) {
		// get xml
		String xml;
		String url = LocalAccessor.getInstance(context).get_server_url() + "/kf_weixiuxiangmus.xml";
		
		ArrayList<KfWeixiuxiangmu> items = new ArrayList<KfWeixiuxiangmu>();
		try {
			while (true) {
				int offset = KfWeixiuxiangmu.max_count(context);
				Log.e(KfWeixiuxiangmu.LOG_TAG, Integer.toString(offset));
				String params = "?offset=" + offset + "&limit="
						+ Constants.EACH_SLICE;
				Log.e(KfWeixiuxiangmu.LOG_TAG, url + params);
				xml = BaseAuthenicationHttpClient.doRequest(url + params,
						User.current_user.name, User.current_user.password);
				 Log.e(KfWeixiuxiangmu.LOG_TAG, xml);

				// turn xml into object
				items = turn_xml_into_items(xml, context);
				// prepare database
				for (KfWeixiuxiangmu d : items) {
					d.save_into_db();
				}

				// break loop
				if (items.size() < Constants.EACH_SLICE) {
					break;
				}

			}
		} catch (Exception e) {// "received authentication challenge is null"
			Log.e(KfWeixiuxiangmu.LOG_TAG, e.getMessage());
		}
	}

	public boolean save_into_db() throws LTException {
		ContentValues values = new ContentValues();
		values.put("xiangmu_id", this.xiangmu_id);
		values.put("xiangmuleibie", this.xiangmuleibie);
		values.put("xiangmumingcheng", this.xiangmumingcheng);
		values.put("xiangmuneirong", this.xiangmuneirong);

		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		db.insertOrThrow("kf_weixiuxiangmus", null, values);
		db.close();
		return true;
	}
	
	
	private static ArrayList<KfWeixiuxiangmu> turn_xml_into_items(String xml,
			Context context) {
		ArrayList<KfWeixiuxiangmu> items = new ArrayList<KfWeixiuxiangmu>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			KfWeixiuxiangmuHandler handler = new KfWeixiuxiangmuHandler(context);
			InputStream is = new ByteArrayInputStream(xml.getBytes());
			parser.parse(is, handler);
			items = handler.getKfWeixiuxiangmus();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return items;
	}
	
	private static int max_count(Context context) {
		int offset = 0;
		// make sure table created
		new KfWeixiuxiangmu(context);
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		Cursor c = db.query("kf_weixiuxiangmus", null, null, null, null, null, null);
		offset = c.getCount();
		c.close();
		db.close();
		return offset;

	}
	
	public static Cursor getScrollDataCursor(long startIndex, long maxCount,
			Context context) {
		// ���� ��·��

		new KfWeixiuxiangmu(context);
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		String sql = "select * from kf_weixiuxiangmus limit ?,?";
		String[] selectionArgs = { String.valueOf(startIndex),
				String.valueOf(maxCount) };
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		return cursor;
	}
	
	public static HashMap<String,String> mingcheng_bianhao_map(Context context){
		HashMap<String,String> res  = new HashMap<String,String>();
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		String sql = "select * from kf_weixiuxiangmus order by _id DESC";
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		while(cursor.isAfterLast() != true){
			res.put(cursor.getString(2),cursor.getString(1));
			cursor.moveToNext();
		}
		cursor.close();
		db.close();
		return res;
	}
	
	
	public static String find_leibie_by_id(Context context,String xiangmu_id){
		String res = "";
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		String sql = "select * from kf_weixiuxiangmus where xiangmu_id = " + xiangmu_id;
		Cursor cursor = db.rawQuery(sql,null);
		cursor.moveToFirst();
		res = cursor.getString(2);
		cursor.close();
		db.close();
		return res;
	}

}
