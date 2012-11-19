package com.letian.model;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.letian.lib.BaseAuthenicationHttpClient;
import com.letian.lib.LocalAccessor;

public class KfWeixiudan extends Model {

	public Integer _id;
	public String kf_weixiudan_id;
	public String danyuanbianhao;
	public String danyuanmingcheng;
	public String lianxiren;
	public String lianxidianhua;
	public String loupanmingcheng;
	public String jiedanren;
	public String jiedanshijian;
	public String beizhu;
	public VerifiedInfo verifiedInfo;
	public int is_syned;
	public static final String LOG_TAG = "KfWeixiuan_MODEL";
	public Context context;
	
	public ArrayList<KfWeixiujilu> jilus;


	private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS kf_weixiudans("
			+ "_id INTEGER PRIMARY KEY,"
			+ "kf_weixiudan_id TEXT,"
			+ "danyuanbianhao TEXT,"
			+ "danyuanmingcheng TEXT,"
			+ "lianxiren TEXT,"
			+ "lianxidianhua TEXT,"
			+ "loupanmingcheng TEXT,"
			+ "jiedanren TEXT,"
			+ "jiedanshijian TEXT,"
			+ "beizhu TEXT,"
			+ "is_syned INTEGER,"
			+ "createdTime TEXT" + ");";


	
	public KfWeixiudan(Context context) {
		this.is_syned = 0;
		this.context = context;

		LocalAccessor.getInstance(this.context).create_db(
				SQL_CREATE_TABLE_MESSAGE);
	}
	
	
	
	public static boolean table_exists(Context context) {
		boolean res = true;
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		try {
			db.rawQuery("select * from kf_weixiudans", null);
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


	public VerifiedInfo verify() {
		VerifiedInfo verifiedInfo = new VerifiedInfo();
		verifiedInfo.verifyCode = VerifiedInfo.VERIFY_SUCCESS;
		verifiedInfo.verifyMessage = "";
	
		ArrayList<String> ar = new ArrayList<String>();
		if (this.danyuanbianhao == null || this.danyuanbianhao.equals("")) {
			verifiedInfo.verifyCode = VerifiedInfo.VERIFY_ERROR;
			ar.add(new String("��Ԫ���  "));
		}

		if (this.danyuanmingcheng.equals("")) {
			verifiedInfo.verifyCode = VerifiedInfo.VERIFY_ERROR;
			ar.add(new String("��Ԫ���  "));
		}

		if (this.lianxiren.equals("")) {
			verifiedInfo.verifyCode = VerifiedInfo.VERIFY_ERROR;
			ar.add("�ͻ����");
		}
		
		if (this.lianxidianhua.equals("")) {
			verifiedInfo.verifyCode = VerifiedInfo.VERIFY_ERROR;
			ar.add("��ϵ�绰");
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

	

	public int save_into_db() throws LTException {
		int shoulouweixiudan_id;
		ContentValues values = new ContentValues();
		values.put("danyuanbianhao", this.danyuanbianhao);
		values.put("danyuanmingcheng", this.danyuanmingcheng);
		values.put("lianxiren", this.lianxiren);
		values.put("lianxidianhua", this.lianxidianhua);
		values.put("loupanmingcheng", this.loupanmingcheng);
		values.put("beizhu",this.beizhu);
		
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		db.insertOrThrow("kf_weixiudans", null, values);
		String sql = "select * from kf_weixiudans order by _id desc";
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		shoulouweixiudan_id = cursor.getInt(0);
		db.close();
		return shoulouweixiudan_id;
	}
	
	
	

	
	private static int max_count(Context context) {
		int offset = 0;
		// make sure table created
		new KfWeixiudan(context);
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		Cursor c = db.query("kf_weixiudans", null, null, null, null, null, null);
		offset = c.getCount();
		c.close();
		db.close();
		return offset;

	}
	
	public static Cursor getScrollDataCursor(long startIndex, long maxCount,
			Context context) {
		new KfWeixiudan(context);
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		String sql = "select * from kf_weixiudans order by _id desc limit ?,?";
		String[] selectionArgs = { String.valueOf(startIndex),
				String.valueOf(maxCount) };
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		return cursor;
	}
	
	public static HashMap<String,String> mingcheng_bianhao_map(Context context){
		HashMap<String,String> res  = new HashMap<String,String>();
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		String sql = "select * from kf_weixiudans order by _id DESC";
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
	
	
	
	
	protected String to_xml() {
		StringBuilder sb = new StringBuilder();

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<kf_weixiudan>");
		sb.append("<danyuanbianhao>").append(this.danyuanbianhao).append(
				"</danyuanbianhao>");
		sb.append("<lianxiren>").append(this.lianxiren).append(
				"</lianxiren>");
		sb.append("<danyuanmingcheng>").append(this.danyuanmingcheng).append(
				"</danyuanmingcheng>");
		sb.append("<lianxidianhua>").append(this.lianxidianhua).append(
				"</lianxidianhua>");
		sb.append("</kf_weixiudan>");
		return sb.toString();
	}

	public boolean save_to_server() throws LTException {
		String url = LocalAccessor.getInstance(context).get_server_url()
				+ "/kf_weixiudans.xml";
		boolean res = true;
		String res_str;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("kf_weixiudan", this.to_xml());
//		try {
			res_str = BaseAuthenicationHttpClient.doRequest(url,
					User.current_user.name, User.current_user.password, map);
			if (res_str == "failed") {
				res = false;
			} else {
				res = true;
				this.kf_weixiudan_id = res_str;
			}

//		} catch (LTException e) {
//			// TODO Auto-generated catch block
//			Log.e("exception", e.getMessage());
//			res = false;
//		}
		return res;
	}

	public boolean update_syned() {
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		Cursor c = db.query("kf_weixiudans", null, "_id=" + Integer.toString(_id), null, null, null,
				null);

		if (c.moveToFirst()) {
			ContentValues values = new ContentValues();
			values.put("is_syned", 1);
			values.put("kf_weixiudan_id", this.kf_weixiudan_id);
			db.update("kf_weixiudans", values, "_id=" + this._id, null);
			Cursor cursor = db.rawQuery(
					"select * from kf_weixiudans order by _id desc limit 1", null);
			cursor.moveToFirst();
			this.is_syned = cursor.getInt(9);
			cursor.close();
		}

		c.close();
		db.close();
		return true;
	}
	
	public static KfWeixiudan find_by_id(int id,Context context){
		KfWeixiudan item = new KfWeixiudan(context) ;
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		Cursor c = db.query("kf_weixiudans", null, "_id=" + id, null, null, null,
				null);
	
		if (c.moveToFirst()) {
			item._id = id;
			item.kf_weixiudan_id = c.getString(1);
			item.danyuanbianhao = c.getString(2);
			item.danyuanmingcheng = c.getString(3);
			item.lianxiren = c.getString(4);
			item.lianxidianhua = c.getString(5);
			item.loupanmingcheng = c.getString(6);
			item.beizhu = c.getString(9);
		}
		c.close();
		db.close();
		return item;
	}
	
	public static boolean delete_by_id(int id,Context context){
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		String args[] = {Integer.toString(id)};
		db.delete("kf_weixiujilus", "weixiudan_id=?",args);
		db.delete("kf_weixiudans", "_id=?",args);
	    db.close();
		return true;
	}
	
	public ArrayList<KfWeixiujilu> weixiujilus(Context context){
		ArrayList<KfWeixiujilu> list= new ArrayList<KfWeixiujilu>();
		
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();

		Cursor c;
		try{
			 c = db.rawQuery("select * from kf_weixiujilus where weixiudan_id='" + this._id + "'",
					null);
		}catch(Exception e){
			return list;
		}
		
		
	
		
		c.moveToFirst();
		

		while(!c.isAfterLast()){
			KfWeixiujilu item = new KfWeixiujilu(context);
			item._id = c.getInt(0);
			item.xiangmu_id = c.getString(1);
			item.weixuneirong = c.getString(2);
			item.weixiushuoming = c.getString(3);
			item.photo_paths = c.getString(4);
			item.is_syned = c.getInt(5);
			item.is_photo_syned = c.getInt(6);
			item.weixiudan_id = c.getInt(7);
			
			list.add(item);
			c.moveToNext();
		}

		return list;
		
	}
	
	public int delete() throws Exception {
		SQLiteDatabase db = LocalAccessor.getInstance(this.context).openDB();
		int count = db.delete("kf_weixiudans", "_id=" + this._id, null);
		db.close();
		return count;
	}

}