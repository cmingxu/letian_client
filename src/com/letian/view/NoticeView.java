package com.letian.view;

import com.letian.R;
import com.letian.model.Syssend;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class NoticeView extends Activity {

	TextView tongzhineirong;
	TextView tongzhishijian;
	TextView tongzhileixing;
	Button queren;
	Button zanbuchuli;
	String id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.notice_view);
		Bundle b = this.getIntent().getExtras();
		id = b.getString("id");
		tongzhineirong = (TextView) findViewById(R.id.tongzhineirong);
		tongzhineirong.setText(b.getString("tongzhineirong"));
		tongzhishijian = (TextView) findViewById(R.id.tongzhishijian);
		tongzhishijian.setText(b.getString("tongzhishijian"));
		tongzhileixing = (TextView) findViewById(R.id.tongzhileixing);
		tongzhileixing.setText(b.getString("tongzhileixing"));
		queren = (Button) findViewById(R.id.handle_now);
		zanbuchuli = (Button) findViewById(R.id.handle_later);

		queren.setOnClickListener(new QueRen());
		zanbuchuli.setOnClickListener(new ZanBuChuLi());

	}

	class QueRen implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			String s ="";
			if(Syssend.confirm_handle_id(getApplicationContext(), id)){
				s = "bac";
				
			}else{
				s = "todo";
			}
		   	new AlertDialog.Builder(NoticeView.this)
            .setMessage(s)
            .setPositiveButton(R.string.i_know, null)
            .show(); 
			Intent intent = new Intent();
			intent.setClass(NoticeView.this, NoticeActivity.class);
			startActivity(intent);
			NoticeView.this.finish();
		}
	}

	class ZanBuChuLi implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			String s ="";
			if(Syssend.reject_handle_id(getApplicationContext(), id)){
				s = "�ݲ����?֪ͨ�ɹ�";
			}else{
				s = "�ݲ�����֪ͨ�ɹ���������������";
			}
		   	new AlertDialog.Builder(NoticeView.this)
            .setMessage(s)
            .setPositiveButton(R.string.i_know, null)
            .show(); 
			Intent intent = new Intent();
			intent.setClass(NoticeView.this, NoticeActivity.class);
			startActivity(intent);
			NoticeView.this.finish();
		}

	}
}
