package com.letian.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import com.letian.lib.BaseAuthenicationHttpClient;
import com.letian.lib.LocalAccessor;
import com.letian.model.xmlhandler.SyssendHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Syssend extends Model {

	public String _id;
	public String content;
	public String sendtime;
	public String sendperson;
	public String style;
	public Integer is_read;
	
	public static final String LOG_TAG = "Syssend_MODEL";
//	private static final String COLLECTION_PATH = Constants.LT_BASE_URL
//			+ "syssends/my_notices.xml";
	
	private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS Syssend("
			+ "_id INTEGER PRIMARY KEY,"
			+ "content TEXT,"
			+ "sendtime TEXT,"
			+ "sendperson TEXT,"
			+ "style TEXT,"
			+ "is_read INTEGER,"
			+ "createdTime TEXT" + ");";

	
	public Context context;
	
	public Syssend(Context context) {
		this.context = context;

		LocalAccessor.getInstance(this.context).create_db(
				SQL_CREATE_TABLE_MESSAGE);	
		this.is_read = 0;
	}
	
	public static ArrayList<Syssend> fetch_from_server(Context context){
		ArrayList<Syssend> notices = new ArrayList<Syssend>();
		String xml;
		try {
			xml = BaseAuthenicationHttpClient.doRequest(LocalAccessor.getInstance(context).get_server_url() +
					"/syssends/my_notices.xml",
					User.current_user.name, User.current_user.password);
			notices = turn_xml_into_items(xml,context);
		} catch (LTException e) {
			Log.e(Syssend.LOG_TAG, "Fetching data error");
			e.printStackTrace();
		}
		return notices;
	}
	
	private static ArrayList<Syssend> turn_xml_into_items(String xml,
			Context context) {
		ArrayList<Syssend> items = new ArrayList<Syssend>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			SyssendHandler handler = new SyssendHandler(context);
			InputStream is = new ByteArrayInputStream(xml.getBytes());
			parser.parse(is, handler);
			items = handler.getSyssends();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return items;
	}

	public static boolean confirm_handle_id(Context c,String id){
		String res;
		try {
			res = BaseAuthenicationHttpClient.doRequest(LocalAccessor.getInstance(c).get_server_url() + "/syssends/"  +  id + "/accept",
					User.current_user.name, User.current_user.password);
			if(res == "fail"){
				return false;
			}else{
				return true;
			}
		} catch (LTException e) {
			Log.e(Syssend.LOG_TAG, "Fetching data error");
			e.printStackTrace();
		}
		return false;	

	}
	
	public static boolean reject_handle_id(Context c,String id){
		String res;
		try {
			res = BaseAuthenicationHttpClient.doRequest(LocalAccessor.getInstance(c).get_server_url() + "/syssends/"  +  id + "/deny",
					User.current_user.name, User.current_user.password);
			if(res == "fail"){
				return false;
			}else{
				return true;
			}
		} catch (LTException e) {
			Log.e(Syssend.LOG_TAG, "Fetching data error");
			e.printStackTrace();
		}
		return false;	

	}
	
	
	public boolean confirm_handle(){
		String res;
		try {
			res = BaseAuthenicationHttpClient.doRequest(LocalAccessor.getInstance(context).get_server_url() + "/syssends/"  +  this._id + "/accept",
					User.current_user.name, User.current_user.password);
			if(res == "fail"){
				return false;
			}else{
				return true;
			}
		} catch (LTException e) {
			Log.e(Syssend.LOG_TAG, "Fetching data error");
			e.printStackTrace();
		}
		return false;	
	}
	
	public boolean reject_handle(){
		String res;
		try {
			res = BaseAuthenicationHttpClient.doRequest(LocalAccessor.getInstance(context).get_server_url() + "/syssends/"  +  this._id + "/deny",
					User.current_user.name, User.current_user.password);
			if(res == "fail"){
				return false;
			}else{
				return true;
			}

		} catch (LTException e) {
			Log.e(Syssend.LOG_TAG, "Fetching data error");
			e.printStackTrace();
	
		}
		return false;	
	}
	
	

	
	public boolean save_into_db() throws LTException {
		ContentValues values = new ContentValues();

		values.put("_id", this._id);
		values.put("content", this.content);
		values.put("sendtime", this.sendtime);
		values.put("sendperson", this.sendperson);
		values.put("style", this.style);
		values.put("is_read", this.is_read);
		values.put("createdTime", (new Date()).toLocaleString());

		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		db.insertOrThrow("Syssend", null, values);
		db.close();
		return true;
	}

	public boolean mark_as_read(){
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		
	        Cursor c = db.query("Syssend", null, "id="+_id, null, null, null, null);        
	        if (c.moveToFirst()) {            
	        	ContentValues values = new ContentValues();		
				values.put("content", c.getString(1));
				values.put("sendtime", c.getString(2));
				values.put("sendperson", c.getString(3));
				values.put("style", c.getString(4));
				values.put("is_read", 1);
				
				 db.update("Syssend", values, "id=" + _id, null);
	        }
	        c.close();
	        db.close();
	  
		
		  return true;
	}

}
