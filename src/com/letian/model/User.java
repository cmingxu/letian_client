package com.letian.model;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.letian.lib.BaseAuthenicationHttpClient;
import com.letian.lib.LocalAccessor;
import com.letian.model.xmlhandler.UserHandler;
import com.letian.view.SelectorView;

public class User extends Model {

    public static final String TABLE_NAME = "User";
    private static final String LOG_TAG = "UserLogTag";
    private static final String default_data = "insert into  Misc values(1,'System','123',0,' " + Constants.SERVER_PATH + "');";
    public String name;
    public int remember_me;
    public String password;
    public String yonghuzu;
    public long id;
    public VerifiedInfo verifiedInfo;
    public static User current_user;

    public Context context;

    public User(Context context) {
        LocalAccessor.getInstance(context).create_db(SQL_CREATE_User_TABLE_MESSAGE);

        this.context = context;
    }

    private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS Misc("
            + "_id INTEGER PRIMARY KEY,"
            + "name TEXT,"
            + "password TEXT,"
            + "remember_me INTEGER,"
            + "server_addr TEXT" + ");";


    private static final String SQL_CREATE_User_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS User("
            + "yonghuzu TEXT,"
            + "name TEXT,"
            + "password TEXT);";

    // + "

    public VerifiedInfo verify() {

        String url = LocalAccessor.getInstance(context).get_server_url() + "/session/verify";
        String xmlString = "";

        Log.d(SelectorView.LOG_TAG, url);

        try {
            xmlString = BaseAuthenicationHttpClient.doRequest(url, this.name,
                    this.password);
        } catch (LTException e) {
            Log.e(User.LOG_TAG, e.toString());
        }
        VerifiedInfo ret = new VerifiedInfo();
        if (xmlString.contains("login.failed")) {
            ret.verifyCode = VerifiedInfo.VERIFY_ERROR;
            ret.verifyMessage = "登录失败， 检查用户名密码";
        }
        else if ("" == xmlString) {
            if (verifyFromDb()) {
                ret.verifyCode = VerifiedInfo.VERIFY_SUCCESS;
                ret.verifyMessage = "欢迎登录 " + this.name;
            } else {
                ret.verifyCode = VerifiedInfo.VERIFY_ERROR;
                ret.verifyMessage = "登录失败， 检查用户名密码";
            }
        } else {
            ret.verifyCode = VerifiedInfo.VERIFY_SUCCESS;
            ret.verifyMessage = "欢迎登录 " + this.name;
        }
        return ret;
    }

    public boolean verifyFromDb() {
        Log.d(User.LOG_TAG, "db verify" + this.name);
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();

        String sql = "select * from " + User.TABLE_NAME + " where name='" + this.name + "' limit 1;";
        Log.d(User.LOG_TAG, "db verify: " + sql);
        this.displayAll(context);
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, null);
            if (cursor.getCount() > 0) {
                return true;
            }


        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();
            }
            if (null != db) {
                db.close();
            }
            return false;
        }
        return false;
    }

    public static void syn(Context context) throws LTException {
        Log.d(LOG_TAG, "syn start user");
        // get xml
        String xml;
        String url = LocalAccessor.getInstance(context).get_server_url() + "/users.xml";

        ArrayList<User> items = new ArrayList<User>();
        while (true) {

            int offset = Model.max_count(context, User.TABLE_NAME);
            Log.d(LOG_TAG, "syn start user");
            String params = "?offset=" + offset + "&limit="
                    + Constants.EACH_SLICE;
            xml = BaseAuthenicationHttpClient.doRequest(url + params,
                    User.current_user.name, User.current_user.password);
            Log.d(LOG_TAG, xml);

            items = (ArrayList<User>) Model.turn_xml_into_items(xml, new UserHandler(context));
            for (User d : items) {
                Log.d(LOG_TAG, d.name);
                d.save_into_db();
            }
            if (items.size() < Constants.EACH_SLICE) {
                break;
            }

        }

    }

    public boolean save_into_db() throws LTException {
        ContentValues values = new ContentValues();
        Log.d(LOG_TAG, "save to db" + this.name);
        values.put("name", this.name);
        values.put("password", this.password);
        return super.save_into_db(context, User.TABLE_NAME, values);
    }

    public User() {

        this("", "");
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }



    public static void set_current_user(User user, Context context) {
        current_user = user;
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();

        // save to database
        String update_sql = "update  Misc set name='" + user.name
                + "', password='" + user.password + "', remember_me="
                + user.remember_me + ";";
        db.execSQL(update_sql);
        db.close();

    }

    public static User last_user(Context context) {
        Cursor cursor;
        User user;
        LocalAccessor.getInstance(context).create_db(SQL_CREATE_TABLE_MESSAGE);
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();

        String sql = "select * from Misc;";
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() == 0) {
            db.execSQL(default_data);
        }
        cursor = db.rawQuery(sql, null);
        cursor.moveToLast();
        user = new User(cursor.getString(1), cursor.getString(2));
        user.remember_me = cursor.getInt(3);
        cursor.close();
        db.close();
        return user;
    }



    public String name() {
        return this.name;
    }

    public void displayAll(Context context) {
        SQLiteDatabase db = LocalAccessor.getInstance(context).openDB();
        String sql;
        sql = "select * from " + User.TABLE_NAME + ";";

        Cursor cursor;
        try {
            cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Log.e(User.LOG_TAG, "111111111111111111111111111");
                Log.e(User.LOG_TAG, "yonghuzu" + cursor.getString(0));
                Log.e(User.LOG_TAG, "name" + cursor.getString(1));
                Log.e(User.LOG_TAG, "password" + cursor.getString(2));

                cursor.moveToNext();
            }
        } catch (Exception e) {
        }

    }


}
