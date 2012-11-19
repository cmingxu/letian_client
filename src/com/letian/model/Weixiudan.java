package com.letian.model;

import java.io.ByteArrayInputStream;
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
import com.letian.model.xmlhandler.WeixiudanHandler;

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
		
	public boolean confirm_accept(){
		String res;
		try {
			res = BaseAuthenicationHttpClient.doRequest(LocalAccessor.getInstance(context).get_server_url() + "/weixiudans/"  +  this.weixiu_id + "/accept",
					User.current_user.name, User.current_user.password);
			if(res == "fail"){
				return false;
			}else{
				this.update_wanchengzhuangtai("1");
				return true;
			}
		} catch (LTException e) {
			e.printStackTrace();
		}
		return false;	
	}
	
	
	
	
	public static ArrayList<Weixiudan> fetch_from_server(Context context){
		ArrayList<Weixiudan> weixiudans = new ArrayList<Weixiudan>();
		String xml;
		try {
			xml = BaseAuthenicationHttpClient.doRequest(LocalAccessor.getInstance(context).get_server_url() +
					"/weixiudans/my_weixiudans.xml",
					User.current_user.name, User.current_user.password);
			weixiudans = turn_xml_into_items(xml,context);
		} catch (LTException e) {
			Log.e(Syssend.LOG_TAG, "Fetching data error");
			e.printStackTrace();
		}
		return weixiudans;
	}
	
	private static ArrayList<Weixiudan> turn_xml_into_items(String xml,
			Context context) {
		ArrayList<Weixiudan> items = new ArrayList<Weixiudan>();
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser parser = factory.newSAXParser();
			WeixiudanHandler handler = new WeixiudanHandler(context);
			InputStream is = new ByteArrayInputStream(xml.getBytes());
			parser.parse(is, handler);
			items = handler.getweixiudans();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return items;
	}


	public VerifiedInfo verify() {
		VerifiedInfo verifiedInfo = new VerifiedInfo();
		verifiedInfo.verifyCode = VerifiedInfo.VERIFY_SUCCESS;
		verifiedInfo.verifyMessage = "";

		ArrayList<String> ar = new ArrayList<String>();
		if (this.lianxidianhua.equals("")) {
			verifiedInfo.verifyCode = VerifiedInfo.VERIFY_ERROR;
			ar.add(new String("��ϵ�绰   "));
		}

		if (this.zuhumingcheng.equals("")) {
			verifiedInfo.verifyCode = VerifiedInfo.VERIFY_ERROR;
			ar.add(new String("�⻧���   "));
		}

		if (this.wufuneirong.equals("")) {
			verifiedInfo.verifyCode = VerifiedInfo.VERIFY_ERROR;
			ar.add("ά������");
		}

		if (verifiedInfo.verifyCode == VerifiedInfo.VERIFY_ERROR) {
			StringBuilder sb = new StringBuilder("�ֶ�");
			for (String item : ar) {
				sb.append(item + ",");
			}
			sb.deleteCharAt(sb.length() - 1);
			verifiedInfo.verifyMessage = sb.toString() + "����Ϊ��";
		}

		return verifiedInfo;
	}
	
	
	public VerifiedInfo jiedan_verify() {
		VerifiedInfo verifiedInfo = new VerifiedInfo();
		verifiedInfo.verifyCode = VerifiedInfo.VERIFY_SUCCESS;
		verifiedInfo.verifyMessage = "";

		ArrayList<String> ar = new ArrayList<String>();
		if (this.weixiukaishishijian.equals("")) {
			verifiedInfo.verifyCode = VerifiedInfo.VERIFY_ERROR;
			ar.add(new String("��ɽ���ʱ��   "));
		}

		if (this.zuhumingcheng.equals("")) {
			verifiedInfo.verifyCode = VerifiedInfo.VERIFY_ERROR;
			ar.add(new String("��ɿ�ʼʱ��   "));
		}

		if (this.wanchengqingkuang.equals("")) {
			verifiedInfo.verifyCode = VerifiedInfo.VERIFY_ERROR;
			ar.add("������");
		}

		if (verifiedInfo.verifyCode == VerifiedInfo.VERIFY_ERROR) {
			StringBuilder sb = new StringBuilder("�ֶ�");
			for (String item : ar) {
				sb.append(item + ",");
			}
			sb.deleteCharAt(sb.length() - 1);
			verifiedInfo.verifyMessage = sb.toString() + "����Ϊ��";
		}

		return verifiedInfo;
	}

	public static boolean table_exists(Context context) {
		boolean res = true;
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		try {

			db.rawQuery("select * from Weixiudan", null);
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
		// ���� ��·��

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
		// ���� ��·��

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
			
//			+ "_id INTEGER PRIMARY KEY ,"
//			+ "weixiu_id TEXT,"
//			+ "weixiudanyuan TEXT,"
//			+ "danyuanbianhao TEXT,"
//			+ "zuhumingcheng TEXT,"

//			+ "wufuneirong TEXT,"
//			+ "shoudanren TEXT,"
//			+ "shoudanshijian TEXT,"
//			+ "weixiuzhonglei TEXT,"
//			+ "lianxidianhua TEXT,"
			
			
//			+ "suoshuloupan TEXT,"
//			+ "suoshulouge TEXT,"
//			+ "weixiurenyuan TEXT,"
//			+ "is_syned INTEGER,"
//			+ "is_photo_syned INTEGER,"
			
			
//			+ "photo_paths TEXT, "
//			+ "createdTime TEXT," 
//			+ "model_type INTEGER," 
//			+ "weixiukaishishijian TEXT," 
//			+ "weixiujieshushijian TEXT," 
			
			
//			+ "wanchengqingkuang TEXT," 
//			+ "wanchengzhuangtai TEXT," 
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

	public boolean save_to_server() {
		String url = LocalAccessor.getInstance(context).get_server_url()
				+ "/weixiudans.xml";
		boolean res = true;
		String res_str = "";
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("weixiudan", this.to_xml());
		try {
			res_str = BaseAuthenicationHttpClient.doRequest(url,
					User.current_user.name, User.current_user.password, map);
			if (res_str == "failed") {
				res = false;
			} else {
				if(this.photo_paths != ""){
					save_image_to_server(res_str);
				}
				
			
			}
	
		} catch (Exception e) {
			Log.e("exception", e.getMessage());
			res  = false;
		
		}
		return res;
	}
	
	
	public boolean jiedan_save_to_server() {
		String url = LocalAccessor.getInstance(context).get_server_url()
				+ "/weixiudans/"+ this.weixiu_id.toString() + "/finish.xml";
		boolean res = true;
		String res_str = "";
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("weixiudan", this.jiedan_to_xml());
		try {
			res_str = BaseAuthenicationHttpClient.doRequest(url,
					User.current_user.name, User.current_user.password, map);
			if (res_str == "failed") {
				res = false;
			} else {
//				if(this.photo_paths != ""){
//					save_image_to_server(res_str);
//				}
			}
	
		} catch (LTException e) {
			Log.e("exception", e.getMessage());
			res  = false;
		
		}
		return res;
	}

	public boolean save_image_to_server(String res) {
		
		String url_path = LocalAccessor.getInstance(context)
				.get_file_upload_path("weixiudans",res);
			if (IOUtils.file_exists(this.photo_paths)) {
			if (ReturnCode.http201 == FileUploader.uploadPicture(
					this.photo_paths, url_path)) {
				update_photo_syned();
				return true;

			} else {
				return false;
			}
		} else {
			return true;
		}
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

	public boolean update_photo_syned() {
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();

		Cursor c = db.query("Weixiudan", null, "_id=" + _id, null, null, null,
				null);
		if (c.moveToFirst()) {
			ContentValues values = new ContentValues();
			values.put("is_photo_syned", 1);
			db.update("Weixiudan", values, "_id=" + _id, null);

			Cursor cursor = db.rawQuery(
					"select * from Weixiudan order by _id desc limit 1", null);
			cursor.moveToFirst();
			this.is_photo_syned = cursor.getInt(13);

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

}
