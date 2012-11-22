package com.letian.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.letian.lib.LocalAccessor;
import com.letian.model.xmlhandler.BaseHandler;
import com.letian.model.xmlhandler.HuxingFangjianLeixingHandler;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class Model {
    private static int max_count(Context context, String tableName) {
        int offset = 0;
        // make sure table created
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        Cursor c = db.query(tableName, null, null, null, null, null, null);
        offset = c.getCount();
        c.close();
        db.close();
        return offset;

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
}
