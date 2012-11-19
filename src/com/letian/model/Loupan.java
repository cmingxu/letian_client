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
import com.letian.model.xmlhandler.LoupanHandler;

public class Loupan extends Model {

	public Integer _id;
	public String loupanbianhao;
	public String loupanmingcheng;

	public static final String LOG_TAG = "LOUPAN_MODEL";
	public Context context;


	private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS Loupan("
			+ "_id INTEGER PRIMARY KEY,"
			+ "loupanbianhao TEXT,"
			+ "loupanmingcheng TEXT,"
			+ "createdTime TEXT"
			+ ");";


	
	public Loupan(Context context) {
		this.context = context;

		LocalAccessor.getInstance(this.context).create_db(
				SQL_CREATE_TABLE_MESSAGE);
	}
	

	public static void syn(Context context) {
		// get xml
		String xml;
		String url = LocalAccessor.getInstance(context).get_server_url() + "/loupans.xml";
		ArrayList<Loupan> items = new ArrayList<Loupan>();
		try {
			while(true){
			int offset  = Loupan.max_count(context);
			String params =  "?offset=" + offset + "&limit=" + Constants.EACH_SLICE;
	
			xml = BaseAuthenicationHttpClient.doRequest(url + params,
					User.current_user.name, User.current_user.password);
	// turn xml into object
			items = turn_xml_into_items(xml,context);
			// prepare database
			for(Loupan d : items){
				d.save_into_db();
			}
			
//			break loop
			if(items.size() < Constants.EACH_SLICE){
				break;
			}
			
			}
		} catch (Exception e) {// "received authentication challenge is null"
			Log.e(Loupan.LOG_TAG, e.getMessage());
		
		}
	}

	private static ArrayList<Loupan> turn_xml_into_items(String xml,
			Context context) {
		ArrayList<Loupan> items = new ArrayList<Loupan>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			LoupanHandler handler = new LoupanHandler(context);
			InputStream is = new ByteArrayInputStream(xml.getBytes());
			parser.parse(is, handler);
			items = handler.getLoupans();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return items;
	}

	public boolean save_into_db() throws LTException {
		ContentValues values = new ContentValues();

		values.put("loupanbianhao", this.loupanbianhao);
		values.put("loupanmingcheng", this.loupanmingcheng);
		values.put("createdTime", (new Date()).toLocaleString());
	
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		db.insertOrThrow("Loupan", null, values);
		db.close();
		return true;
	}

	private static int max_count(Context context) {
		int offset = 0;
		//make sure table created
		new Loupan(context);
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		Cursor c = db.query("Loupan", null, null, null, null, null, null);
		offset = c.getCount();
		c.close();
		db.close();
		return offset;
	}
	
	
	public static Cursor getScrollDataCursor(long startIndex, long maxCount,Context context) {

		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		String sql = "select * from Loupan limit ?,?";	
//		String sql = "select weixiudanyuan_in_string,zuhumingcheng, " +
//		"lianxidianhua Weixiudan limit ?,?";
		String[] selectionArgs = { String.valueOf(startIndex),
				String.valueOf(maxCount) };
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		return cursor;
	}
	
	public static HashMap<String,String> mingcheng_bianhao_map(Context context){
		HashMap<String,String> res  = new HashMap<String,String>();
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		String sql = "select * from Loupan order by _id DESC";
		Cursor cursor;
		try{
			cursor = db.rawQuery(sql,null);
		}catch(Exception e){
			return res;
		}
		cursor.moveToFirst();
		while(cursor.isAfterLast() != true){
			res.put(cursor.getString(2),cursor.getString(1));
			cursor.moveToNext();
		}
		cursor.close();
		db.close();
		return res;
	}


}
