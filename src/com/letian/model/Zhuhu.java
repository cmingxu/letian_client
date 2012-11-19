package com.letian.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

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
import com.letian.model.xmlhandler.ZhuhuHandler;

public class Zhuhu extends Model {

	public Integer _id;
	public String zhuhumingcheng;
	public String zhuhubianhao;
	public String shoujihaoma;
	public String lianxidianhua;


	public static final String LOG_TAG = "ZHUHU_MODEL";
	public Context context;

	private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS Zhuhu("
			+ "_id INTEGER PRIMARY KEY,"
			+ "zhuhumingcheng TEXT,"
			+ "zhuhubianhao TEXT,"
			+ "shoujihaoma TEXT,"
			+ "lianxidianhua TEXT,"
			+ "createdTime TEXT"
			+ ");";

//	private static final String COLLECTION_PATH = Constants.LT_BASE_URL
//			+ "zhuhus.xml";

	
	public Zhuhu(Context context) {
		this.context = context;

		LocalAccessor.getInstance(this.context).create_db(
				SQL_CREATE_TABLE_MESSAGE);
	}
	

	public static void syn(Context context) {
		// get xml
		Log.e(Zhuhu.LOG_TAG,"zhuhuuuuuu");
		String xml;
		String url = LocalAccessor.getInstance(context).get_server_url() + "/zhuhus.xml";
		ArrayList<Zhuhu> items = new ArrayList<Zhuhu>();
		try {
			while(true){
			int offset  = Zhuhu.max_count(context);
			Log.e(Zhuhu.LOG_TAG,Integer.toString(offset));
			String params =  "?offset=" + offset + "&limit=" + Constants.EACH_SLICE;
			Log.e(Zhuhu.LOG_TAG, url  + params);
			xml = BaseAuthenicationHttpClient.doRequest(url + params,
					User.current_user.name, User.current_user.password);
			// turn xml into object
			Log.e(Zhuhu.LOG_TAG, xml);
			items = turn_xml_into_items(xml,context);
			// prepare database
			for(Zhuhu d : items){
				d.save_into_db();
			}
			
//			break loop
			Log.e(Zhuhu.LOG_TAG,Integer.toString(items.size()));
			if(items.size() < Constants.EACH_SLICE){
				break;
			}
			
			}
		} catch (Exception e) {// "received authentication challenge is null"
			Log.e(Louge.LOG_TAG, e.getMessage());
		}
	}

	private static ArrayList<Zhuhu> turn_xml_into_items(String xml,
			Context context) {
		ArrayList<Zhuhu> items = new ArrayList<Zhuhu>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			ZhuhuHandler handler = new ZhuhuHandler(context);
			InputStream is = new ByteArrayInputStream(xml.getBytes());
			parser.parse(is, handler);
			items = handler.getZhuhus();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return items;
	}

	public boolean save_into_db() throws LTException {
		ContentValues values = new ContentValues();

		values.put("zhuhumingcheng", this.zhuhumingcheng);
		values.put("zhuhubianhao", this.zhuhubianhao);
		values.put("shoujihaoma", this.shoujihaoma);
		values.put("lianxidianhua", this.lianxidianhua);
		values.put("createdTime", (new Date()).toLocaleString());

		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		db.insertOrThrow("Zhuhu", null, values);
		Log.e("SAVINGGGG", "SAVINGGGG");
		db.close();
		return true;
	}

	private static int max_count(Context context) {
		int offset = 0;
		//make sure table created
		new Zhuhu(context);
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		Cursor c = db.query("Zhuhu", null, null, null, null, null, null);
		offset = c.getCount();
		c.close();
		db.close();
		return offset;
	}
	
	
	public static Cursor getScrollDataCursor(long startIndex, long maxCount,Context context) {

		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		String sql = "select * from Zhuhu limit ?,?";	
		String[] selectionArgs = { String.valueOf(startIndex),
				String.valueOf(maxCount) };
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		return cursor;
	}



}
