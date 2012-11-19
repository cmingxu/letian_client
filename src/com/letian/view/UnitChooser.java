package com.letian.view;

import com.letian.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class UnitChooser extends Activity implements OnItemClickListener {
	private Button loupan_chooser_button;
	private Button cancel_button;
	private Button done_button;
	private TextView choosed_result;
	private String loupan_mingcheng;
	private String loupan_bianhao;
	private String louge_mingcheng;
	private String louge_bianhao;
	private String loucengmingcheng;
	private String danyuan_mingcheng;
	private String danyuan_bianhao;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unit_chooser);

		find_widget_by_id();

		loupan_chooser_button.setOnClickListener(new LoupanButtonListener());
		cancel_button.setOnClickListener(new CancelButtonListener());
		done_button.setOnClickListener(new DoneButtonListener());

	}

	protected void find_widget_by_id() {
		choosed_result = (TextView) findViewById(R.id.choosed_result);
		loupan_chooser_button = (Button) findViewById(R.id.choose_loupan_button);
		cancel_button = (Button) findViewById(R.id.cancel_button);
		done_button = (Button) findViewById(R.id.done_button);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	private class LoupanButtonListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(UnitChooser.this, LoupanDialog.class);
			loupan_chooser_button.setEnabled(false);
			UnitChooser.this.startActivityForResult(intent, 1);
		}

	}

	private class CancelButtonListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			loupan_mingcheng = "";
			loupan_bianhao= "";
			louge_mingcheng= "";
			louge_bianhao= "";
			loucengmingcheng= "";
			danyuan_mingcheng= "";
			danyuan_bianhao= "";

			loupan_chooser_button.setEnabled(true);
			cancel_button.setEnabled(false);
			done_button.setEnabled(false);
		}

	}

	private class DoneButtonListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			Intent i = new Intent();  
			  Bundle b = new Bundle();  
			  b.putString("loupan_bianhao", loupan_bianhao);
			  b.putString("louge_bianhao", louge_bianhao);
			  b.putString("danyuan_bianhao", danyuan_bianhao);
			  b.putString("danyuan_quan_mingcheng", loupan_mingcheng  + "\\" + louge_mingcheng + "\\" + danyuan_mingcheng);
			
			  
			  i.putExtras(b);  
		          
		      UnitChooser.this.setResult(RESULT_OK, i);  
		      UnitChooser.this.finish();  
	      
	        
			
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			Bundle b = data.getExtras();
			int type = b.getInt("type");

			switch (type) {
			case 1:
				loupan_mingcheng = b.getString("loupan_mingcheng");
				loupan_bianhao = b.getString("loupan_bianhao");
				choosed_result.setText(loupan_mingcheng);
				open_louge_choose_dialog();
				break;
			case 2:
				louge_mingcheng = b.getString("louge_mingcheng");
				louge_bianhao = b.getString("louge_bianhao");
				choosed_result.setText(loupan_mingcheng  + "\\" + louge_mingcheng);
				open_louceng_choose_dialog();
				break;
			case 3:
				loucengmingcheng = b.getString("loucengmingcheng");
				
				open_danyuan_choose_dialog();
				break;
			case 4:
				danyuan_mingcheng = b.getString("danyuan_mingcheng");
				danyuan_bianhao = b.getString("danyuan_bianhao");
				choosed_result.setText(loupan_mingcheng  + "\\" + louge_mingcheng + "\\" + danyuan_mingcheng);
				done_button.setEnabled(true);
				break;
			}

			cancel_button.setEnabled(true);
			

		}
	}
	
	protected void open_louge_choose_dialog(){
		Intent intent = new Intent();
		Bundle b = new Bundle();
		b.putString("loupan_bianhao", loupan_bianhao);
		intent.putExtras(b);
		intent.setClass(UnitChooser.this, LougeDialog.class);
		UnitChooser.this.startActivityForResult(intent, 1);
	}
	
	protected void open_louceng_choose_dialog(){
		Intent intent = new Intent();
		Bundle b = new Bundle();
		b.putString("louge_bianhao", louge_bianhao);
		intent.putExtras(b);
		intent.setClass(UnitChooser.this, LoucengDialog.class);
		UnitChooser.this.startActivityForResult(intent, 1);
	}
	
	protected void open_danyuan_choose_dialog(){
		Intent intent = new Intent();
		Bundle b = new Bundle();
		b.putString("louge_bianhao", louge_bianhao);
		b.putString("loucengmingcheng", loucengmingcheng);
		intent.putExtras(b);
		intent.setClass(UnitChooser.this, DanyuanDialog.class);
		UnitChooser.this.startActivityForResult(intent, 1);
	}

}
