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
import com.letian.model.xmlhandler.LougeHandler;

public class Louge extends Model {

	public Integer _id;
	public String lougebianhao;
	public String loupanbianhao;
	public String lougemingcheng;

	public static final String LOG_TAG = "LOUGE_MODEL";
    public static final String TABLE_NAME = "Louge";
	public Context context;


	private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS Louge("
			+ "_id INTEGER PRIMARY KEY,"
			+ "lougebianhao TEXT,"
			+ "loupanbianhao TEXT,"
			+ "lougemingcheng TEXT,"
			+ "createdTime TEXT"
			+ ");";

//	private static final String COLLECTION_PATH = Constants.LT_BASE_URL
//			+ "louges.xml";


    public Louge(String lougemingcheng, String lougebianhao, String loupanbianhao, Integer _id) {
        this.lougemingcheng = lougemingcheng;
        this.lougebianhao = lougebianhao;
        this.loupanbianhao = loupanbianhao;
        this._id = _id;
    }

    public Louge(Context context) {
		this.context = context;

		LocalAccessor.getInstance(this.context).create_db(
				SQL_CREATE_TABLE_MESSAGE);
	}
	

	public static void syn(Context context) {
        Log.d(LOG_TAG, "syn start louge");
        // get xml
        String xml;
        String url = LocalAccessor.getInstance(context).get_server_url() + "/louges.xml";

        ArrayList<Louge> items = new ArrayList<Louge>();
        try {
            while (true) {

                int offset = Model.max_count(context, TABLE_NAME);
                String params = "?offset=" + offset + "&limit="
                        + Constants.EACH_SLICE;
                xml = BaseAuthenicationHttpClient.doRequest(url + params,
                        User.current_user.name, User.current_user.password);

                items = (ArrayList<Louge>)Model.turn_xml_into_items(xml, new LougeHandler(context));
                for (Louge d : items) {
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

		values.put("lougebianhao", this.lougebianhao);
		values.put("loupanbianhao", this.loupanbianhao);
		values.put("lougemingcheng", this.lougemingcheng);
		values.put("createdTime", (new Date()).toString());

        return  super.save_into_db(context, Louge.TABLE_NAME, values);
	}

    public static ArrayList<Louge> findAll(Context context){
        ArrayList<Louge> louges = new ArrayList<Louge>();
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        String sql = "select * from Louge order by _id DESC";
        Cursor cursor;
        try{
            cursor = db.rawQuery(sql,null);
        }catch(Exception e){
            return louges;
        }
        cursor.moveToFirst();
        while(cursor.isAfterLast() != true){
            louges.add(new Louge(cursor.getString(3), cursor.getString(2), cursor.getString(1), cursor.getInt(0)));
            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        return louges;
    }

	public static HashMap<String,String> mingcheng_bianhao_map(Context context,String loupan_bianhao){
		HashMap<String,String> res  = new HashMap<String,String>();
		SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
		String sql = "select * from Louge where loupanbianhao = '" + loupan_bianhao.replaceAll("\\s*", "") +  "' order by _id DESC";
		Cursor cursor;
		try{
			cursor = db.rawQuery(sql,null);
		}catch(Exception e){
			return res;
		}
		cursor.moveToFirst();
		while(cursor.isAfterLast() != true){
			res.put(cursor.getString(3),cursor.getString(1));
			cursor.moveToNext();
		}
		
		String sql1 = "select * from Louge";
			Cursor cursor1 = db.rawQuery(sql1,null);
		cursor1.moveToFirst();
		while(cursor1.isAfterLast() != true){
			Log.e("maobing",cursor1.getString(2));
			
			cursor1.moveToNext();
		}
		cursor1.close();
		cursor.close();
		db.close();
		return res;
	}
}
