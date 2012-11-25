package com.letian.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.letian.lib.BaseAuthenicationHttpClient;
import com.letian.lib.Constants;
import com.letian.lib.LocalAccessor;

public class User extends Model {

    private static final String LOG_TAG = "Model";
    private static final String default_data = "insert into  Misc values(1,'System','123',0,'http://10.0.2.2:3000/');";
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

        // �ж��Ƿ��м�¼
        String sql = "select * from Misc;";
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() == 0) {
            db.execSQL(default_data);
        }
        // �ٴβ�ѯ
        cursor = db.rawQuery(sql, null);
        cursor.moveToLast();
        // �����û�
        user = new User(cursor.getString(1), cursor.getString(2));
        user.remember_me = cursor.getInt(3);
        cursor.close();
        db.close();
        return user;
    }

    public static boolean is_server_reachable(String addr) {
        try {

            URL url = new URL(addr);

            URLConnection uc = url.openConnection();
            uc.setConnectTimeout(Constants.TIMEOUT);
            uc.setRequestProperty("User-Agent", "Mozilla/5.0");

            InputStream content = uc.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(content, "UTF-8"));

            String line = "";//will refactory
            StringBuilder sb = new StringBuilder("");
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }

            in.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public String name() {
        return this.name;
    }

}
