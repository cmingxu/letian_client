package com.letian.view;

import com.letian.R;
import com.letian.lib.LocalAccessor;
import com.letian.test.ListLouge;
import com.letian.test.ListLoupan;
import com.letian.test.ListUnit;
import com.letian.test.ListZhuhu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class TestView extends Activity {
	private Button button;
	private Button drop_button;
	private Button list_unit;
	private Button list_louge;
	private Button list_loupan;
	private Button list_zhuhu;
	private Button list_slxm;
	private Button set_admin_password;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_view);
		drop_button= (Button) findViewById(R.id.drop_button);
//		button = (Button) findViewById(R.id.test_button);
		list_unit = (Button) findViewById(R.id.list_unit);
		list_louge = (Button) findViewById(R.id.list_louge);
		list_loupan = (Button) findViewById(R.id.list_loupan);
		list_zhuhu = (Button) findViewById(R.id.list_zhuhu);
		list_slxm = (Button) findViewById(R.id.list_slxm);
		set_admin_password= (Button) findViewById(R.id.set_admin_password);
		drop_button.setOnClickListener(new DropButtonListener());
//		button.setOnClickListener(new SaveButtonListener());
		list_unit.setOnClickListener(new ListUnitListener());
		list_louge.setOnClickListener(new ListlougeListener());
		list_loupan.setOnClickListener(new ListloupanListener());
		list_zhuhu.setOnClickListener(new ListZhuhuListener());

		set_admin_password.setOnClickListener(new SetAdminPasswordListener());
	}
	
	private class DropButtonListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			SQLiteDatabase db = LocalAccessor.getInstance(
					TestView.this.getApplicationContext()).openDB();
			db.execSQL("drop table  if exists Danyuan;");
			db.execSQL("drop table  if exists Louge;");
			db.execSQL("drop table  if exists Loupan;");
			db.execSQL("drop table  if exists Weixiudan;");
			db.execSQL("drop table  if exists Tousudan;");
			db.execSQL("drop table  if exists Zhuhu;");
			db.execSQL("drop table  if exists Misc;");
			db.execSQL("drop table if exists kf_weixiudans");

			db.execSQL("drop table if exists kf_weixiujilus");
			db.execSQL("drop table  if exists kf_weixiuxiangmus;");
			db.close();
		}
	}
	
	private class ListZhuhuListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent i = new Intent();
			i.setClass(TestView.this, ListZhuhu.class);
			startActivity(i);
//			TestView.this.finish();
		}
	}

	private class SetAdminPasswordListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {

			 LayoutInflater factory = (LayoutInflater)    
		        TestView.this.getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);   
			    final View textEntryView = factory.inflate(R.layout.admin_password_layout, null);
			    final EditText addr = (EditText)textEntryView.findViewById(R.id.admin_password);
			   
		      
		        AlertDialog dlg = new AlertDialog.Builder(TestView.this)
		        .setTitle("密码")
		        .setView(textEntryView)
		        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int whichButton) {
		            	String text = "";
		            	String addr_str = addr.getText().toString().trim();
		            	if(addr_str != ""){
		            		text = "Cancel";
		            		LocalAccessor.getInstance(TestView.this.getApplicationContext()).set_admin_password(addr_str);
//		            		User.set_server_addr(addr_str, Login.this.getApplicationContext());
		            	}else{
		            		text = "Cancel";
		            	}
		              	new AlertDialog.Builder(TestView.this)
		                .setMessage(text)
		                .setPositiveButton(R.string.i_know, null)
		                .show();  
		            }
		        })
		        .setNegativeButton("cancel˵", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int whichButton) {

		                /* User clicked cancel so do some stuff */
		            }
		        })
		        .create();
		        dlg.show();
		}
	}
	private class ListloupanListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent i = new Intent();
			i.setClass(TestView.this, ListLoupan.class);
			startActivity(i);
//			TestView.this.finish();
		}
	}
	private class ListlougeListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent i = new Intent();
			i.setClass(TestView.this, ListLouge.class);
			startActivity(i);
//			TestView.this.finish();
		}
	}
	
	private class ListUnitListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent i = new Intent();
			i.setClass(TestView.this, ListUnit.class);
			startActivity(i);
//			TestView.this.finish();
		}
	}

	private class SaveButtonListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			SQLiteDatabase db = LocalAccessor.getInstance(
					TestView.this.getApplicationContext()).openDB();


			db.execSQL("delete from Danyuan where _id >0;");
			db.execSQL("delete from Louge where _id >0;");
			db.execSQL("delete from Weixiudan where _id >0;");
			db.execSQL("delete from Tousudan where _id >0;");
			db.execSQL("delete from Loupan where _id >0;");
//			
			


			db.close();


		}
	}
}
