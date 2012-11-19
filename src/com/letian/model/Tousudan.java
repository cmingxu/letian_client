package com.letian.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.letian.lib.BaseAuthenicationHttpClient;
import com.letian.lib.FileUploader;
import com.letian.lib.IOUtils;
import com.letian.lib.LocalAccessor;
import com.letian.lib.FileUploader.ReturnCode;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Tousudan extends Model {

	public Integer _id;
	public String tousu_id;
	public String tousudanhao;
	public String danyuanbianhao;
	public String danyuanmingcheng;
	public String zuhumingcheng;
	public String tousuriqi;
	public String tousuzhuti;
	public String tousuneirong;
	public String tousuleibie;
	public String tousurenlianxifangshi;
	public String suoshuloupan;
	public String suoshulouge;
	public int is_syned;
	public VerifiedInfo verifiedInfo;
	public String photo_paths;
	public int is_photo_syned;

	public static SQLiteDatabase db;

	public static boolean table_exists(Context context) {
		boolean res = true;
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		try {
			db.rawQuery("select * from Tousudan", null);
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

	public static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS Tousudan("
			+ "_id INTEGER PRIMARY KEY ,"
			+ "tousu_id TEXT,"
			+ "tousudanhao TEXT,"
			+ "danyuanbianhao TEXT,"
			+ "danyuanmingcheng TEXT,"
			+ "zuhumingcheng TEXT,"
			+ "tousuriqi TEXT,"
			+ "tousuzhuti TEXT,"
			+ "tousuneirong TEXT,"
			+ "tousuleibie TEXT,"
			+ "tousurenlianxifangshi TEXT,"
			+ "suoshuloupan TEXT,"
			+ "suoshulouge TEXT,"
			+ "is_syned INTEGER,"
			+ "createdTime TEXT,"
			+ "is_photo_syned INTEGER,"
			+ "photo_paths TEXT" + ");";

	public Tousudan(Context context) {
		this.context = context;
		// TODO Auto-generated constructor stub
		LocalAccessor.getInstance(context).create_db(SQL_CREATE_TABLE_MESSAGE);
		this.is_syned = 0;
		this.is_photo_syned = 0;
		this.photo_paths = "";
	}



	// index��PATH
	// private static final String COLLECTION_PATH = Constants.LT_BASE_URL
	// + "tousudans.xml";

	Context context;

	public VerifiedInfo verify() {
		VerifiedInfo verifiedInfo = new VerifiedInfo();
		verifiedInfo.verifyCode = VerifiedInfo.VERIFY_SUCCESS;
		verifiedInfo.verifyMessage = "";

		ArrayList<String> ar = new ArrayList<String>();
		if (this.tousurenlianxifangshi.equals("")) {
			verifiedInfo.verifyCode = VerifiedInfo.VERIFY_ERROR;
			ar.add(new String("��ϵ�绰   "));
		}

		if (this.zuhumingcheng.equals("")) {
			verifiedInfo.verifyCode = VerifiedInfo.VERIFY_ERROR;
			ar.add(new String("�⻧���   "));
		}

		if (this.tousuneirong.equals("")) {
			verifiedInfo.verifyCode = VerifiedInfo.VERIFY_ERROR;
			ar.add("Ͷ������");
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

	public boolean save_into_db() {
		ContentValues values = new ContentValues();

		new Tousudan(context);

		values.put("tousu_id", this.tousu_id);
		values.put("tousudanhao", this.tousudanhao);
		values.put("danyuanbianhao", this.danyuanbianhao);
		values.put("danyuanmingcheng", this.danyuanmingcheng);
		values.put("zuhumingcheng", this.zuhumingcheng);
		values.put("tousuriqi", this.tousuriqi);
		values.put("tousuzhuti", this.tousuzhuti);
		values.put("tousuneirong", this.tousuneirong);
		values.put("tousuleibie", this.tousuleibie);
		values.put("tousurenlianxifangshi", this.tousurenlianxifangshi);
		values.put("suoshuloupan", this.suoshuloupan);
		values.put("suoshulouge", this.suoshulouge);
		values.put("is_syned", this.is_syned);
		values.put("photo_paths", this.photo_paths);
		values.put("createdTime", (new Date()).toLocaleString());

		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		if (this._id == null) {// insert
			db.insertOrThrow("Tousudan", null, values);
			Cursor cursor = db.rawQuery(
					"select * from Tousudan order by _id desc limit 1", null);
			cursor.moveToFirst();
			this._id = cursor.getInt(0);
			Log.e("aaaaaaa", Integer.toString(cursor.getInt(0)));
		} else {// update
			db.update("Tousudan", values, "_id=" + this._id, null);
		}
		db.close();
		return true;
	}

	public static Cursor getScrollDataCursor(long startIndex, long maxCount,
			Context context) {
		// ���� ��·��

		Tousudan.db = LocalAccessor.getInstance(context).openDB();
		String sql = "select * from Tousudan order by _id desc limit ?,?";
		String[] selectionArgs = { String.valueOf(startIndex),
				String.valueOf(maxCount) };
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		// db.close();
		return cursor;
	}

	public static Tousudan get_by_id(long id, Context ctx) {
		SQLiteDatabase db = LocalAccessor.getInstance(ctx).openDB();
		Cursor c = db.query("Tousudan", null, "_id=" + id, null, null, null,
				null);
		Tousudan tousudan = new Tousudan(ctx);
		if (c.moveToFirst()) {

			tousudan._id = c.getInt(0);
			tousudan.tousudanhao = c.getString(2);
			tousudan.danyuanbianhao = c.getString(3);
			tousudan.danyuanmingcheng = c.getString(4);
			tousudan.zuhumingcheng = c.getString(5);
			tousudan.tousuriqi = c.getString(6);
			tousudan.tousuzhuti = c.getString(7);
			tousudan.tousuneirong = c.getString(8);
			tousudan.tousuleibie = c.getString(9);
			tousudan.tousurenlianxifangshi = c.getString(10);
			tousudan.suoshuloupan = c.getString(11);
			tousudan.suoshulouge = c.getString(12);
			tousudan.is_syned = c.getInt(13);
			tousudan.is_photo_syned = c.getInt(15);
			tousudan.photo_paths = c.getString(16);
			

			Log.e("ssssssssss",tousudan.photo_paths);
		}

		c.close();
		db.close();
		return tousudan;
	}

	public int delete() throws Exception {
		SQLiteDatabase db = LocalAccessor.getInstance(this.context).openDB();
		if((this.photo_paths != "" && this.photo_paths != null && IOUtils.file_exists(this.photo_paths))){
			IOUtils.remove_file(this.photo_paths);
		}
		int count = db.delete("Tousudan", "_id=" + this._id, null);
		db.close();
		return count;
	}

	protected String to_xml() {
		StringBuilder sb = new StringBuilder();

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<tousudan>");
		sb.append("<danyuanbianhao>").append(this.danyuanbianhao).append(
				"</danyuanbianhao>");
		sb.append("<danyuanmingcheng>").append(this.danyuanmingcheng).append(
				"</danyuanmingcheng>");
		sb.append("<tousuneirong>").append(this.tousuneirong).append(
				"</tousuneirong>");
		sb.append("<tousurenlianxifangshi>").append(this.tousurenlianxifangshi)
				.append("</tousurenlianxifangshi>");
		sb.append("<zuhumingcheng>").append(this.zuhumingcheng).append(
				"</zuhumingcheng>");
		sb.append("<tousuriqi>").append(this.tousuriqi).append("</tousuriqi>");
		sb.append("<suoshulouge>").append(this.suoshulouge).append(
				"</suoshulouge>");
		sb.append("<suoshuloupan>").append(this.suoshuloupan).append(
				"</suoshuloupan>");
		sb.append("</tousudan>");
		return sb.toString();
	}

	public boolean save_to_server() {
		boolean res = true;
		String url = LocalAccessor.getInstance(context).get_server_url()
				+ "/tousudans.xml";
		String res_string;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("Tousudan", this.to_xml());
		try {
			res_string = BaseAuthenicationHttpClient.doRequest(url,
					User.current_user.name, User.current_user.password, map);
			if (res_string == "failed") {
				res = false;
			} else {
				if(this.photo_paths != ""){
					save_image_to_server(res_string);
					}
			}

		} catch (LTException e) {
			// TODO Auto-generated catch block
			Log.e("exception", e.getMessage());
			res = false;
		}
		return res;
	}

	public boolean save_image_to_server(String res) {
		String url_path = LocalAccessor.getInstance(context)
				.get_file_upload_path("tousudans", res);
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

		Cursor c = db.query("Tousudan", null, "_id=" + _id, null, null, null,
				null);
		if (c.moveToFirst()) {
			ContentValues values = new ContentValues();

			values.put("is_syned", 1);
			values.put("tousu_id", this.tousu_id);
			db.update("Tousudan", values, "_id=" + _id, null);

			Cursor cursor = db.rawQuery(
					"select * from Tousudan order by _id desc limit 1", null);
			cursor.moveToFirst();
			this.is_syned = cursor.getInt(13);

		}
		c.close();
		db.close();

		return true;
	}

	public boolean update_photo_syned() {
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();

		Cursor c = db.query("Tousudan", null, "_id=" + _id, null, null, null,
				null);
		if (c.moveToFirst()) {
			ContentValues values = new ContentValues();

			values.put("is_photo_syned", 1);
			db.update("Tousudan", values, "_id=" + _id, null);

			Cursor cursor = db.rawQuery(
					"select * from Tousudan order by _id desc limit 1", null);
			cursor.moveToFirst();
			this.is_photo_syned = cursor.getInt(13);

		}
		c.close();
		db.close();

		return true;
	}
}