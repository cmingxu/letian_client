package com.letian;


import com.letian.lib.LocalAccessor;
import com.letian.lib.NetworkConnection;
import com.letian.misc.BounceInterpolator;
import com.letian.misc.EasingType.Type;
import com.letian.model.Danyuan;
import com.letian.model.KfWeixiuxiangmu;
import com.letian.model.Louge;
import com.letian.model.Loupan;
import com.letian.model.Zhuhu;
import com.letian.services.FetchNoticeService;
import com.letian.view.BaoshiNew;
import com.letian.view.ChaxunView;
import com.letian.view.NoticeActivity;
import com.letian.view.NoticeListViewaaa;
import com.letian.view.ShouLouNew;
import com.letian.view.TestView;
import com.letian.view.WeixiuJiedanList;
import com.letian.view.ZhibiaoView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class Main extends Activity {

	public static final int TONGBU = Menu.FIRST;
	private static final String LOG_TAG = "Main_Activityaaa";
	private Animation animLeft, animRight;
	private Interpolator interpolator;
	ImageView baoshi_view;
	TextView baoshi_tv;

	ImageView renwu_view;
	TextView renwu_tv;
	
	ImageView chaobiao_view;
	TextView chaobiao_tv;
	
	ImageView chaxun_view;
	TextView chaxun_tv;
	
	ImageView zhibiao_view;
	TextView zhibiao_tv;
	
	ImageView shenpi_view;
	TextView shenpi_tv;
	
	ImageView shoulou_view;
	TextView shoulou_tv;
	
	ImageView ceshi_view;
	TextView ceshi_tv;
	
	ImageView logout_view;
	TextView logout_tv;

	private Context ctx;
	private Handler handler = new Handler();
	private ProgressDialog progressDialog = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		animLeft = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
		animRight = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
		interpolator = new BounceInterpolator(Type.IN);
		animLeft.setInterpolator(interpolator);
		animRight.setInterpolator(interpolator);
		this.setTitle(LocalAccessor.login_user_title());
		baoshi_view = (ImageView) findViewById(R.id.baoshi_view);
		baoshi_view.setAnimation(animLeft);
		baoshi_view.setOnClickListener(new BaoshiListener());
//
		renwu_view = (ImageView) findViewById(R.id.renwu_view);
		renwu_view.setAnimation(animLeft);
		renwu_view.setOnClickListener(new RenwuListener());
//		
//		chaobiao_view = (ImageView) findViewById(R.id.chaobiao_view);
//		chaobiao_view.setAnimation(animLeft);
//		chaobiao_view.setOnClickListener(new ChaobiaoListener());
		
		
		chaxun_view = (ImageView) findViewById(R.id.chaxun_view);
		chaxun_view.setAnimation(animLeft);
		chaxun_view.setOnClickListener(new ChaxunListener());
		
//		
//		zhibiao_view = (ImageView) findViewById(R.id.zhibiao_view);
//		zhibiao_view.setAnimation(animLeft);
//		zhibiao_view.setOnClickListener(new ZhibiaoListener());
//		
//		shenpi_view = (ImageView) findViewById(R.id.shenpi_view);
//		shenpi_view.setAnimation(animLeft);
//		shenpi_view.setOnClickListener(new ShenpiListener());
		
		shoulou_view = (ImageView) findViewById(R.id.shoulou_view);
		shoulou_view.setAnimation(animLeft);
		shoulou_view.setOnClickListener(new ShoulouListener());
		
		ceshi_view = (ImageView) findViewById(R.id.test);
		ceshi_view.setAnimation(animLeft);
		ceshi_view.setOnClickListener(new WeixiuListener());
		
		logout_view = (ImageView) findViewById(R.id.logout);
		logout_view.setAnimation(animLeft);
		logout_view.setOnClickListener(new LogoutListener());
		
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, TONGBU, 0, "同步");
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case TONGBU:
			tongbu();
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	private void tongbu() {
		progressDialog = ProgressDialog.show(Main.this, "ͬ����...", null, true);
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
		                         .setMessage("��������")
		                         .setPositiveButton("Okay", null)
		                         .show(); 
								
							}
							
						});
						 
                       
					
					}else{
						get_data_from_server();
						put_unsyn_data_into_server();
					
					}
			
				progressDialog.dismiss();

			}
		}.start();

	}

	public void get_data_from_server() {


		KfWeixiuxiangmu.syn(Main.this.getApplicationContext());
		Danyuan.syn(Main.this.getApplicationContext());
		Louge.syn(Main.this.getApplicationContext());
		Loupan.syn(Main.this.getApplicationContext());
		Zhuhu.syn(Main.this.getApplicationContext());
	}
	public void put_unsyn_data_into_server(){}
	private class BaoshiListener implements View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(Main.this, BaoshiNew.class);
			Main.this.startActivity(intent);
		}
	}
	
	
	private class RenwuListener implements View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
//			intent.setClass(Main.this, NoticeActivity.class);
			intent.setClass(Main.this, WeixiuJiedanList.class);
			Main.this.startActivity(intent);
		}
	}
	
	private class ZhibiaoListener implements View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(Main.this, ZhibiaoView.class);
			Main.this.startActivity(intent);
		}
	}
	
	private class ShenpiListener implements View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(Main.this, FetchNoticeService.class);
			Main.this.startService(intent);
		}
	}
	
	private class ShoulouListener implements View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(Main.this, ShouLouNew.class);
			Main.this.startActivity(intent);
		}
	}
	
	private class ChaobiaoListener implements View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(Main.this, NoticeListViewaaa.class);
			Main.this.startActivity(intent);
		}
	}
	
	private class ChaxunListener implements View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(Main.this, ChaxunView.class);
			Main.this.startActivity(intent);
		}
	}
	
	
	private class LogoutListener implements View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			FetchNoticeService.set_goon(false);
			Intent intent1 = new Intent();
			intent1.setClass(Main.this, FetchNoticeService.class);
			Main.this.stopService(intent1);
			
			Intent intent = new Intent();
			intent.setClass(Main.this, Login.class);
			Main.this.startActivity(intent);
			Main.this.finish();
			
		
		}
	}


	private class WeixiuListener implements View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			boolean ok = true;  
		     LayoutInflater factory = (LayoutInflater)    
		    Main.this.getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);   
			final View textEntryView = factory.inflate(R.layout.admin_password_layout, null);
		    final EditText addr = (EditText)textEntryView.findViewById(R.id.admin_password);
		     AlertDialog dlg = new AlertDialog.Builder(Main.this)
		        .setTitle("�������Ա����")
		        .setView(textEntryView)
		        .setPositiveButton("����", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int whichButton) {

		            	String password = addr.getText().toString().trim();

		            	if(password.equalsIgnoreCase(LocalAccessor.getInstance(Main.this.getApplicationContext())
		            			.get_admin_password())){
		            		Intent intent = new Intent();
		        			intent.setClass(Main.this, TestView.class);
		        			Main.this.startActivity(intent);

		            	}else{
		              	new AlertDialog.Builder(Main.this)
		                .setMessage("���벻��")
		                .setPositiveButton(R.string.i_know, null)
		                .show();  
		            	}
		            }
		        })
		        .create();
		        dlg.show();
			
			
		
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