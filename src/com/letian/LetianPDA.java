package com.letian;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;
import com.letian.model.User;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: xucming
 * Date: 3/2/13
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class LetianPDA extends Application implements SharedPreferences.OnSharedPreferenceChangeListener{
    public static final String TAG = "LetianPDAApplication";
    public static final String PREF_NAME = "LetianPDA_PREF";

    public static SharedPreferences app_pref;
    public static User user;


    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(
                    "com.letian", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return verCode;
    }

    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    "com.letian", 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return verName;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
    }

    @Override
    public void onCreate() {
        app_pref = getSharedPreferences(PREF_NAME, 0);
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static void login(User user){
        user = user;
        put("user_name", user.name);
        put("password", user.password);
    }

    public void logout(){
        user = null;
    }

    public static void put(String key, String value){
        SharedPreferences.Editor editor = app_pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String get(String key, String default_value){
        if (app_pref == null) {
            return default_value;
        }

        return app_pref.getString(key, default_value);
    }

    public boolean logged_in(){
        return null == user;
    }

    public static boolean is_server_reachable(String addr) throws IOException {
        return true;

    }
}
