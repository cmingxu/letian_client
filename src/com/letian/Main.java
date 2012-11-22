package com.letian;


import com.letian.lib.LocalAccessor;
import com.letian.lib.NetworkConnection;
import com.letian.model.Danyuan;
import com.letian.model.Louge;
import com.letian.model.Loupan;
import com.letian.model.Zhuhu;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import com.letian.view.ComingSoon;
import com.letian.view.SettingActivity;
import com.letian.view.TestView;

public class Main extends Activity {

	private static final String LOG_TAG = "Main_Activityaaa";

	ImageView logout_view;
    ImageView shoulou_view;
    ImageView setting_view;
    ImageView coming_soon_view_1;
    ImageView coming_soon_view_2;
    ImageView coming_soon_view_3;
    ImageView coming_soon_view_4;
    ImageView coming_soon_view_5;
    ImageView coming_soon_view_6;
    ImageView coming_soon_view_7;
    ImageView coming_soon_view_8;
    ImageView coming_soon_view_9;
    ImageView coming_soon_view_10;
    ImageView coming_soon_view_11;
    ImageView coming_soon_view_12;

    private Context ctx;
	private Handler handler = new Handler();
	private ProgressDialog progressDialog = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);


		this.setTitle(getResources().getString(R.string.app_name));
		logout_view = (ImageView) findViewById(R.id.logout);
		logout_view.setOnClickListener(new LogoutListener());

        setting_view = (ImageView) findViewById(R.id.setting_view);
        setting_view.setOnClickListener(new SettingListener());

        shoulou_view = (ImageView)findViewById(R.id.shoulou_view);
        setting_view = (ImageView)findViewById(R.id.setting_view);
        coming_soon_view_1 = (ImageView)findViewById(R.id.coming_soon_view_1);
        coming_soon_view_2 = (ImageView)findViewById(R.id.coming_soon_view_2);
        coming_soon_view_3 = (ImageView)findViewById(R.id.coming_soon_view_3);
        coming_soon_view_4 = (ImageView)findViewById(R.id.coming_soon_view_4);
        coming_soon_view_5 = (ImageView)findViewById(R.id.coming_soon_view_5);
        coming_soon_view_6 = (ImageView)findViewById(R.id.coming_soon_view_6);
        coming_soon_view_7 = (ImageView)findViewById(R.id.coming_soon_view_7);
        coming_soon_view_8 = (ImageView)findViewById(R.id.coming_soon_view_8);
        coming_soon_view_9 = (ImageView)findViewById(R.id.coming_soon_view_9);
        coming_soon_view_10 = (ImageView)findViewById(R.id.coming_soon_view_10);
        coming_soon_view_11 = (ImageView)findViewById(R.id.coming_soon_view_11);
        coming_soon_view_12 = (ImageView)findViewById(R.id.coming_soon_view_12);

//        coming_soon_view_2.setOnClickListener(new ComingSoonListener());

        coming_soon_view_2.setOnClickListener(new TestListener());
        coming_soon_view_3.setOnClickListener(new ComingSoonListener());
        coming_soon_view_4.setOnClickListener(new ComingSoonListener());
        coming_soon_view_5.setOnClickListener(new ComingSoonListener());
        coming_soon_view_6.setOnClickListener(new ComingSoonListener());
        coming_soon_view_7.setOnClickListener(new ComingSoonListener());
        coming_soon_view_8.setOnClickListener(new ComingSoonListener());
        coming_soon_view_9.setOnClickListener(new ComingSoonListener());
        coming_soon_view_10.setOnClickListener(new ComingSoonListener());
        coming_soon_view_11.setOnClickListener(new ComingSoonListener());
        coming_soon_view_12.setOnClickListener(new ComingSoonListener());


    }

    private class TestListener implements View.OnClickListener{
        @Override
        public void onClick(View arg0) {

            Intent intent = new Intent();
            intent.setClass(Main.this, TestView.class);
            Main.this.startActivity(intent);
            Main.this.finish();


        }
    }


    private class ComingSoonListener implements View.OnClickListener{
        @Override
        public void onClick(View arg0) {

            Intent intent = new Intent();
            intent.setClass(Main.this, ComingSoon.class);
            Main.this.startActivity(intent);
            Main.this.finish();


        }
    }

    private class SettingListener implements View.OnClickListener{
        @Override
        public void onClick(View arg0) {

            Intent intent = new Intent();
            intent.setClass(Main.this, SettingActivity.class);
            Main.this.startActivity(intent);
            Main.this.finish();


        }
    }



    private class LogoutListener implements View.OnClickListener {

		@Override
		public void onClick(View arg0) {

			Intent intent = new Intent();
			intent.setClass(Main.this, Login.class);
			Main.this.startActivity(intent);
			Main.this.finish();
			
		
		}
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.e("onKeyDown:", " keyCode=" + keyCode + " KeyEvent=" + event);
		boolean should_capture = false;
		switch (keyCode) {
		
		case KeyEvent.KEYCODE_BACK:
			should_capture = true;
			break;

		case KeyEvent.KEYCODE_HOME:
			should_capture = true;
			break;
				}
		if (should_capture) {
			return false;
		}
		return super.onKeyDown(keyCode, event);

	}
}