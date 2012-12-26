package com.letian.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.letian.lib.Constants;
import com.letian.lib.LocalAccessor;
import com.letian.model.xmlhandler.BaseHandler;
import com.letian.model.xmlhandler.HuxingFangjianLeixingHandler;
import com.letian.view.SelectorView;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

public class Model {
    public static int max_count(Context context, String tableName) {
        SQLiteDatabase db  = null    ;
        Cursor c = null;
        try {
            int offset = 0;
            // make sure table created
             db = LocalAccessor.getInstance(context).openDB();
             c = db.query(tableName, null, null, null, null, null, null);
            offset = c.getCount();
            c.close();
            db.close();
            return offset;
        } catch (SQLiteException e) {
            if (c != null) {
                c.close();
            }
            if (null != db) {
                db.close();
            }
            e.printStackTrace();
            return 0;
        }
    }

    public static ArrayList<? extends Model> turn_xml_into_items(String xml, BaseHandler handler) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            InputStream is = new ByteArrayInputStream(xml.getBytes());
            parser.parse(is, handler);
            return handler.getItems();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<String> inArrayList(Context context, String table) {

        Log.d(Constants.GENERAL_MESSAGE, table);

        try {

            ArrayList<String> res;
            res = new ArrayList<String>();
            SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
            Cursor cursor;
            cursor = db.rawQuery("select * from " + table, null);
            Log.d(SelectorView.LOG_TAG, "select * from " + table)      ;
            cursor.moveToFirst();

            cursor.moveToFirst();
            while (cursor.isAfterLast() != true) {
                Log.d(SelectorView.LOG_TAG, "wooo"  +cursor.getString(2)) ;
                res.add(cursor.getString(1));
                cursor.moveToNext();
            }

            cursor.close();
            db.close();
            return res;

        } catch (SQLiteException e) {
            e.printStackTrace();
            return new ArrayList<String>();
        }
    }

    public boolean save_into_db(Context context, String tableName, ContentValues cv) throws LTException {
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        db.insertOrThrow(tableName, null, cv);
        db.close();
        return true;
    }

    public boolean updateDb(Context context, String tableName, ContentValues cv, String wherence) throws LTException {
        Log.d(SelectorView.LOG_TAG, "update " + wherence);
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        db.update(tableName, cv, wherence, null);
        db.close();
        return true;
    }

}
