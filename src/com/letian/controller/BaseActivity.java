package com.letian.controller;

import android.app.Activity;
import android.os.Bundle;
import com.letian.AppManager;

/**
 * Created with IntelliJ IDEA.
 * User: xucming
 * Date: 3/3/13
 * Time: 7:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class BaseActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getAppManager().addActivity(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        AppManager.getAppManager().finishActivity(this);
    }
}