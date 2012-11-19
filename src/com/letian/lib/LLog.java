package com.letian.lib;

import android.os.Debug;
import android.util.Log;

import java.util.zip.InflaterOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-18
 * Time: 上午11:02
 * To change this template use File | Settings | File Templates.
 */
public class LLog {
    public static final int DEBUG = 1;
    public static final int VERBOSE = 2;
    public static final int INFO = 3;
    public static final int ERROR = 4;

    public static int CURRENT_LEVEL = DEBUG;

    public static void v(String tag, String message){
        LLog.log(LLog.VERBOSE, tag, message);
    }
    public static void d(String tag, String message){
        LLog.log(DEBUG, tag, message);
    }
    public static void i(String tag, String message){
        LLog.log(INFO, tag, message);
    }
    public static void e(String tag, String message){
        LLog.log(ERROR, tag, message);
    }

    public static void log(int level, String tag, String message){
        if (level <= CURRENT_LEVEL) {
            return;
        }
        switch (level){
            case DEBUG:
                Log.d(tag, message);
                break;
            case  VERBOSE:
                Log.v(tag, message);
                break;
            case INFO:
                Log.i(tag, message);
                break;
            case ERROR:
                Log.e(tag, message);
                break;
            default:
                Log.d(tag, message);
        }

    }



}
