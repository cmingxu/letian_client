package com.letian.model;


import java.util.ArrayList;
import java.util.HashMap;

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

public class KfWeixiujilu extends Model {
	public Integer _id;
	public String xiangmu_id;
	public String weixuneirong;
	public String weixiushuoming;

	public int weixiudan_id;
	public String photo_paths;
	public int is_syned;
	public int is_photo_syned;

	public static final String LOG_TAG = "KfWeixiuanjilu_MODEL";
	public Context context;

	

	private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS kf_weixiujilus("
			+ "_id INTEGER PRIMARY KEY,"
			+ "xiangmu_id TEXT,"
			+ "weixuneirong TEXT,"
			+ "weixiushuoming TEXT,"
			+ "photo_paths TEXT,"
			+ "is_syned INTEGER,"
			+ "is_photo_syned INTEGER,"
			+ "weixiudan_id INTEGER,"
			+ "createdTime TEXT" + ");";


	
	public KfWeixiujilu(Context context) {
		this.context = context;
		this.is_syned = 0;
		this.is_photo_syned = 0;

		LocalAccessor.getInstance(this.context).create_db(
				SQL_CREATE_TABLE_MESSAGE);
	}
	
	
	
	public static boolean table_exists(Context context) {
		boolean res = true;
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		try {
			db.rawQuery("select * from kf_weixiujilus", null);
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
		if (this.weixuneirong.equals("")) {
			verifiedInfo.verifyCode = VerifiedInfo.VERIFY_ERROR;
			ar.add(new String("ά������  "));
		}

		if (this.weixiushuoming.equals("")) {
			verifiedInfo.verifyCode = VerifiedInfo.VERIFY_ERROR;
			ar.add(new String("ά��˵��  "));
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

	
	
	public boolean save_into_db() throws LTException {
		ContentValues values = new ContentValues();
		values.put("weixuneirong", this.weixuneirong);
		values.put("weixiushuoming", this.weixiushuoming);
		values.put("xiangmu_id", this.xiangmu_id);
		values.put("photo_paths", this.photo_paths);
		values.put("weixiudan_id", this.weixiudan_id);
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		db.insertOrThrow("kf_weixiujilus", null, values);
		db.close();
		return true;
	}
	
	

	
	private static int max_count(Context context) {
		int offset = 0;
		// make sure table created
		new KfWeixiudan(context);
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		Cursor c = db.query("kf_weixiujilus", null, null, null, null, null, null);
		offset = c.getCount();
		c.close();
		db.close();
		return offset;

	}
	
	public static Cursor getScrollDataCursor(long startIndex, long maxCount,
			Context context) {

		new KfWeixiudan(context);
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		String sql = "select * from kf_weixiujilus limit ?,?";
		String[] selectionArgs = { String.valueOf(startIndex),
				String.valueOf(maxCount) };
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		return cursor;
	}
	
	public static HashMap<String,String> mingcheng_bianhao_map(Context context){
		HashMap<String,String> res  = new HashMap<String,String>();
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		String sql = "select * from kf_weixiujilus order by _id DESC";
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
		sb.append("<kf_weixiujilu>");
		sb.append("<xiangmu_id>").append(this.xiangmu_id).append(
				"</xiangmu_id>");
		sb.append("<weixiushuoming>").append(this.weixiushuoming).append(
				"</weixiushuoming>");
		sb.append("<weixiuneirong>").append(this.weixuneirong).append(
				"</weixiuneirong>");
		sb.append("<weixiudan_id>").append(this.weixiudan().kf_weixiudan_id).append(
		"</weixiudan_id>");
		sb.append("</kf_weixiujilu>");
		return sb.toString();
	}

	public KfWeixiudan weixiudan(){
		KfWeixiudan weixiudan = new KfWeixiudan(this.context);
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		Cursor c = db.query("kf_weixiudans", null, "_id=" + this.weixiudan_id, null, null, null,
				null);
	
		if (c.moveToFirst()) {
			weixiudan.kf_weixiudan_id = c.getString(1);
		}
		c.close();
		db.close();
		return weixiudan;
	}
	public boolean save_to_server() throws LTException {
		String url = LocalAccessor.getInstance(context).get_server_url()
				+ "/kf_weixiujilus.xml";
		boolean res = true;
		String res_str;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("kf_weixiujilu", this.to_xml());
			res_str = BaseAuthenicationHttpClient.doRequest(url,
					User.current_user.name, User.current_user.password, map);

			if (res_str == "failed") {
				res = false;
			} else {
				if(this.photo_paths != ""){
				save_image_to_server(res_str);
				Log.e("photo_paths", "overrrrrrrrrr");
				}
			}

	
		return res;
	}

	public boolean update_syned() {
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		Cursor c = db.query("kf_weixiujilus", null, "_id=" + _id, null, null, null,
				null);

		if (c.moveToFirst()) {
			ContentValues values = new ContentValues();


			values.put("is_syned", 1);
			db.update("kf_weixiujilus", values, "_id=" + _id, null);
			Cursor cursor = db.rawQuery(
					"select * from kf_weixiujilus order by _id desc limit 1", null);
			cursor.moveToFirst();
			this.is_syned = cursor.getInt(5);

		}

		c.close();
		db.close();
		return true;
	}
	
	public boolean update_photo_syned() {
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		Cursor c = db.query("kf_weixiujilus", null, "_id=" + _id, null, null, null,
				null);

		if (c.moveToFirst()) {
			ContentValues values = new ContentValues();


			values.put("is_photo_syned", 1);
			db.update("kf_weixiujilus", values, "_id=" + _id, null);

			Cursor cursor = db.rawQuery(
					"select * from kf_weixiujilus order by _id desc limit 1", null);
			cursor.moveToFirst();
			this.is_photo_syned = cursor.getInt(6);

		}

		c.close();
		db.close();
		return true;
	}
	
	private String my_image_paths() {
		return IOUtils.data_file_name(this.photo_paths);
	}
	
	public boolean save_image_to_server(String res) {
		
		String url_path = LocalAccessor.getInstance(context)
				.get_file_upload_path("kf_weixiujilus",res);
		
		Log.e("url_path", url_path);
		Log.e("file_exists", Boolean.toString(IOUtils.file_exists(this.photo_paths)));
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
	
	public int delete() throws Exception {
		SQLiteDatabase db = LocalAccessor.getInstance(this.context).openDB();
		if((this.photo_paths != "" && this.photo_paths != null && IOUtils.file_exists(this.photo_paths))){
			IOUtils.remove_file(this.photo_paths);
        }
        int count = db.delete("kf_weixiujilus", "_id=" + this._id, null);
        db.close();
		return count;
	}

}
