package com.letian.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.letian.lib.Base64;
import com.letian.lib.BaseAuthenicationHttpClient;
import com.letian.lib.Constants;
import com.letian.lib.LocalAccessor;
import com.letian.view.SelectorView;

public class User extends Model {

    private static final String LOG_TAG = "Model";
    private static final String default_data = "insert into  Misc values(1,'System','123',0,' " + Constants.SERVER_PATH + "');";
    public String name;
    public int remember_me;
    public String password;
    public long id;
    public VerifiedInfo verifiedInfo;
    public static User current_user;

    public Context context;


    private static final String SQL_CREATE_TABLE_MESSAGE = "CREATE TABLE IF NOT EXISTS Misc("
            + "_id INTEGER PRIMARY KEY,"
            + "name TEXT,"
            + "password TEXT,"
            + "remember_me INTEGER," + "server_addr TEXT" + ");";

    // + "

    public VerifiedInfo verify() {

        String url = LocalAccessor.getInstance(context).get_server_url() + "/session/verify";
        String xmlString = null;

        Log.d(SelectorView.LOG_TAG, url);

        try {
            xmlString = BaseAuthenicationHttpClient.doRequest(url, this.name,
                    this.password);
        } catch (LTException e) {
            e.printStackTrace();
        }

        VerifiedInfo ret = new VerifiedInfo();
        if (null == xmlString || xmlString.contains("login.failed")) {
            ret.verifyCode = VerifiedInfo.VERIFY_ERROR;
            ret.verifyMessage = "登录失败， 检查用户名密码";
        } else {
            ret.verifyCode = VerifiedInfo.VERIFY_SUCCESS;
            ret.verifyMessage = "欢迎登录 " + this.name;
        }
        return ret;
    }

    public User() {
        this("", "");
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public boolean update() {
        return false;
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

    public static boolean is_server_reachable(String addr) throws IOException {
        boolean result = false;
        URL url = null;
        HttpURLConnection connection = null;
        InputStreamReader in = null;
        url = new URL(addr);
        connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(5 * 1000);
        result = (connection.getResponseCode() == 200);

        return result;

    }

    public String name() {
        return this.name;
    }

}
