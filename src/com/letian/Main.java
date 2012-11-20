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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class Main extends Activity {

	public static final int TONGBU = Menu.FIRST;
	private static final String LOG_TAG = "Main_Activityaaa";

	ImageView logout_view;

	private Context ctx;
	private Handler handler = new Handler();
	private ProgressDialog progressDialog = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);


		this.setTitle(LocalAccessor.login_user_title());
		logout_view = (ImageView) findViewById(R.id.logout);
		logout_view.setOnClickListener(new LogoutListener());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, TONGBU, 0, "同步");
		return super.onCreateOptionsMenu(menu);

	}



	private void tongbu() {
		progressDialog = ProgressDialog.show(Main.this, this.getResources().getString(R.string.sync_notice) , null, true);
		new Thread() {
			@Override
			public void run() {

					if (!NetworkConnection.getInstance(
							Main.this.getApplicationContext())
							.isNetworkAvailable()) {
						Log.e(Main.LOG_TAG, "network can not be reach");
						handler.post(new Runnable(){

							@Override
							public void run() {
								 new AlertDialog.Builder(Main.this)
		                         .setMessage(Main.this.getResources().getString(R.string.network_connection_problem)
                                 )
		                         .setPositiveButton("Okay", null)
		                         .show(); 
								
							}
							
						});
						 
                       
					
					}else{
						get_data_from_server();

					}
			
				progressDialog.dismiss();

			}
		}.start();
    }



	public void get_data_from_server() {

		Danyuan.syn(Main.this.getApplicationContext());
		Louge.syn(Main.this.getApplicationContext());
		Loupan.syn(Main.this.getApplicationContext());
		Zhuhu.syn(Main.this.getApplicationContext());
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