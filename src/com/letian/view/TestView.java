package com.letian.view;

import android.util.Log;
import com.letian.Main;
import com.letian.R;
import com.letian.lib.LocalAccessor;
import com.letian.model.*;
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
	private Button drop_button;
	private Button list_unit;
	private Button list_louge;
    private Button list_hx;
    private Button list_fjlx;
    private Button list_ysdx;
    private Button list_fjlx_ysdx;
    private Button list_ysxm;
	private Button set_admin_password;
    private Button back;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_view);
		drop_button= (Button) findViewById(R.id.drop_button);
		list_unit = (Button) findViewById(R.id.list_unit);
		list_louge = (Button) findViewById(R.id.list_louge);
        list_fjlx = (Button)findViewById(R.id.list_fjlx);
        list_hx   = (Button)findViewById(R.id.list_hx);
        list_ysdx = (Button) findViewById(R.id.list_ysdx);
        list_fjlx_ysdx = (Button) findViewById(R.id.list_fjlx_ysdx);
        list_ysxm = (Button) findViewById(R.id.list_ysxm);

		set_admin_password= (Button) findViewById(R.id.set_admin_password);
		drop_button.setOnClickListener(new DropButtonListener());
		list_unit.setOnClickListener(new ListItemListener());
		list_louge.setOnClickListener(new ListItemListener());
        list_fjlx.setOnClickListener(new ListItemListener());
        list_hx.setOnClickListener(new ListItemListener());
        list_ysdx.setOnClickListener(new ListItemListener());
        list_fjlx_ysdx.setOnClickListener(new ListItemListener());
        list_ysxm.setOnClickListener(new ListItemListener());

		set_admin_password.setOnClickListener(new SetAdminPasswordListener());
	    back = (Button)findViewById(R.id.back);

        back.setOnClickListener(new Button.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(TestView.this,Main.class);
                TestView.this.startActivity(i);
            }
        });
    }
	
	private class DropButtonListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			SQLiteDatabase db = LocalAccessor.getInstance(
					TestView.this.getApplicationContext()).openDB();
			db.execSQL("drop table  if exists " + Danyuan.TABLE_NAME + ";");
			db.execSQL("drop table  if exists " + Louge.TABLE_NAME + ";");
            db.execSQL("drop table  if exists " + FangjianLeixing.TABLE_NAME + ";");
            db.execSQL("drop table  if exists " + Huxing.TABLE_NAME + ";");
            db.execSQL("drop table  if exists " + FangjianleixingYanshouduixiang.TABLE_NAME + ";");
            db.execSQL("drop table  if exists " + HuxingFangjianLeixing.TABLE_NAME + ";");
            db.execSQL("drop table  if exists " + YanshouXiangmu.TABLE_NAME + ";");
            db.execSQL("drop table  if exists " + YanshouDuixiang.TABLE_NAME + ";");
			db.close();
		}
	}

    private class ListItemListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            Intent i = new Intent();
            if (view.getId() == R.id.list_unit){
                i.putExtra("tableToDisplay", Danyuan.TABLE_NAME);
            }else if(view.getId() == R.id.list_louge)      {
                i.putExtra("tableToDisplay", Louge.TABLE_NAME);
            }else if(view.getId() == R.id.list_hx){
                i.putExtra("tableToDisplay", Huxing.TABLE_NAME);
            }else  if(view.getId() == R.id.list_fjlx){
                i.putExtra("tableToDisplay", FangjianLeixing.TABLE_NAME);
            }else if(view.getId() == R.id.list_ysdx){
                i.putExtra("tableToDisplay", YanshouDuixiang.TABLE_NAME);
            } else if(view.getId() == R.id.list_fjlx_ysdx){
                i.putExtra("tableToDisplay", FangjianleixingYanshouduixiang.TABLE_NAME);
            }
            else if(view.getId() == R.id.list_ysxm){
                i.putExtra("tableToDisplay", YanshouXiangmu.TABLE_NAME);
            }

            i.setClass(TestView.this, ListItem.class);
            startActivity(i);
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

	private class SaveButtonListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			SQLiteDatabase db = LocalAccessor.getInstance(
					TestView.this.getApplicationContext()).openDB();

			db.execSQL("delete from Danyuan where _id >0;");
			db.execSQL("delete from Louge where _id >0;");


			db.close();


		}
	}
}
