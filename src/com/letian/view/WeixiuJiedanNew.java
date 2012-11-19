package com.letian.view;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import com.letian.R;
import com.letian.datetimepicker.DateTimePicker;
import com.letian.lib.IOUtils;
import com.letian.lib.LocalAccessor;
import com.letian.model.Danyuan;
import com.letian.model.Tousudan;
import com.letian.model.VerifiedInfo;
import com.letian.model.Weixiudan;
import com.letian.model.Zhuhu;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class WeixiuJiedanNew extends Activity {
	private static final String LOG_TAG = "BaoshiEditActivity";
	public static final int WEIXIU_LIST = Menu.FIRST;
	public static final int TOUSU_LIST = Menu.FIRST + 1;
	public static int current_object_id = -1;
	private Button save_to_server_button;
	private Button confirm_accept_button;
	private Button save_button;
	private EditText unit_et;
	private EditText tel_et;
	private EditText person_et;
	private EditText desc_et;
	private EditText weixiukaishishijian;
	private EditText weixiujieshushijian;
	private EditText wanchengqingkuang;
	private Weixiudan weixiudan;

	private VerifiedInfo ret;
	private Context ctx;

	private ImageView pic;
//
//	private String loupan_bianhao;
//	private String louge_bianhao;
//	private String danyuan_bianhao;
//	private String danyuan_quan_mingcheng;

	boolean edit = false;

	private String temp_image_path;
//	private Integer syned = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weixiu_jiedan_new);
		this.setTitle(LocalAccessor.login_user_title());
		ctx = this.getApplicationContext();

		
		save_to_server_button = (Button) findViewById(R.id.save_to_server_button);
		confirm_accept_button = (Button) findViewById(R.id.confirm_accept_button);
		save_button = (Button) findViewById(R.id.save_button);
	
		unit_et = (EditText) findViewById(R.id.jiedan_choose_unit_ed);
		tel_et = (EditText) findViewById(R.id.jiedan_tel_et);
		person_et = (EditText) findViewById(R.id.jiedan_person_et);
		desc_et = (EditText) findViewById(R.id.jiedan_desc_et);



		weixiukaishishijian = (EditText) findViewById(R.id.weixiukaishishijian);
		weixiujieshushijian = (EditText) findViewById(R.id.weixiujieshushijian);
		wanchengqingkuang = (EditText) findViewById(R.id.wanchengqingkuang);

	
		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			edit = true;

			int id = bundle.getInt("_id");
			String myJpgPath = "";

			this.weixiudan = Weixiudan.get_by_id(id, this
					.getApplicationContext());
//			this.loupan_bianhao = this.weixiudan.suoshuloupan;
//			this.louge_bianhao = this.weixiudan.suoshulouge;
//			this.danyuan_bianhao = this.weixiudan.danyuanbianhao;
//			this.danyuan_quan_mingcheng = this.weixiudan.weixiudanyuan;

			if (this.weixiudan.photo_paths != "") {
				temp_image_path = this.weixiudan.photo_paths;
			}
			myJpgPath = IOUtils.data_file_name(temp_image_path);
			BaoshiEdit.current_object_id = weixiudan._id;

			//
			if ((new File(myJpgPath)).exists()) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 1;
				Bitmap bm = BitmapFactory.decodeFile(myJpgPath, options);
				pic.setImageBitmap(bm);
			}
			//
//			if (syned == 0) {
//				confirm_accept_button.setEnabled(true);
//				save_button.setEnabled(true);
//				save_to_server_button.setEnabled(true);
//			} else {
//				confirm_accept_button.setEnabled(false);
//				save_button.setEnabled(false);
//				save_to_server_button.setEnabled(false);
//			}
			

		
			Log.e("sSSSSSSSSSSSSSAAAAAAAA",Integer.toString(weixiudan._id));
			Log.e("sSSSSSSSSSSSSSAAAAAAAAA",weixiudan.wanchengzhuangtai);
			if(weixiudan.wanchengzhuangtai.equalsIgnoreCase("0")){
				save_button.setEnabled(false);
				save_to_server_button.setEnabled(false);
			}else{
				confirm_accept_button.setEnabled(false);
			}
			
		}
		
	

		unit_et.setText(weixiudan.weixiudanyuan);
		person_et.setText(weixiudan.zuhumingcheng);
		tel_et.setText(weixiudan.lianxidianhua);
		desc_et.setText(weixiudan.wufuneirong);
		
	
		weixiukaishishijian.setText(weixiudan.weixiukaishishijian);
		weixiujieshushijian.setText(weixiudan.weixiujieshushijian);
		wanchengqingkuang.setText(weixiudan.wanchengqingkuang);

		confirm_accept_button
				.setOnClickListener(new ConfirmAcceptButtonListener());
		save_to_server_button
				.setOnClickListener(new SaveToServerButtonListener());
		save_button.setOnClickListener(new SaveButtonListener());
		
		 unit_et.setEnabled(false);
		 tel_et.setEnabled(false);
		 person_et.setEnabled(false);
		 desc_et.setEnabled(false);
		weixiukaishishijian.setOnClickListener(new KaishiShijianListener());
		weixiujieshushijian.setOnClickListener(new JieshuShijianListener());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, WEIXIU_LIST, 0, "ά��");
		menu.add(0, TOUSU_LIST, 1, "Ͷ��");

		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case WEIXIU_LIST:
			jump_to_weixiu_list_view();
			break;
		case TOUSU_LIST:
			jump_to_tousu_list_view();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void jump_to_weixiu_list_view() {
		Intent intent = new Intent();
		intent.setClass(WeixiuJiedanNew.this, WeixiuList.class);
		startActivity(intent);
		WeixiuJiedanNew.this.finish();
	}

	protected void jump_to_tousu_list_view() {
		Intent intent = new Intent();
		intent.setClass(WeixiuJiedanNew.this, TousuList.class);
		startActivity(intent);
		WeixiuJiedanNew.this.finish();
	}

	// handle save button click
	class SaveToServerButtonListener implements Button.OnClickListener {
		public void onClick(View arg0) {
		
			if (weixiudan.jiedan_save_to_server()) {
				new AlertDialog.Builder(WeixiuJiedanNew.this).setMessage(
						"ά�޵�����ϴ��ɹ�").setPositiveButton(R.string.i_know, null)

				.show();
				
				save_to_server_button.setEnabled(false);
		save_button.setEnabled(false);
		

			}else{
				new AlertDialog.Builder(WeixiuJiedanNew.this).setMessage(
				"ά�޵�����ϴ�ʧ��").setPositiveButton(R.string.i_know, null)

		.show();
			}
			// build object

//			Weixiudan weixiudan = new Weixiudan(ctx);
//			build_weixiudan(weixiudan);
//			weixiudan._id = WeixiuJiedanNew.current_object_id;
//			try {
//				if (weixiudan.save_into_db()) {
//					new AlertDialog.Builder(WeixiuJiedanNew.this).setMessage(
//							getResources()
//									.getString(R.string.weixiudan_save_ok))
//							.setPositiveButton(R.string.i_know, null).show();
//					clear_fields();
//					jump_to_weixiu_list_view();
//				}
//			} catch (Exception e) {
//
//				Log.e(WeixiuJiedanNew.LOG_TAG, e.getMessage());
//				new AlertDialog.Builder(WeixiuJiedanNew.this)
//						.setMessage(
//								getResources().getString(
//										R.string.weixiudan_save_error))
//						.setPositiveButton(R.string.i_know, null).show();
//			}

		}
	}

	class KaishiShijianListener implements OnClickListener {
		public void onClick(View arg0) {
			kaishi_showDateTimeDialog();
		}
	}

	class JieshuShijianListener implements OnClickListener {
		public void onClick(View arg0) {
			jieshu_showDateTimeDialog();
		}
	}

	// handle save button click
	class ConfirmAcceptButtonListener implements Button.OnClickListener {
		public void onClick(View arg0) {
			// build object

			try {
				if (weixiudan.confirm_accept()) {
					new AlertDialog.Builder(WeixiuJiedanNew.this).setMessage(
							"ȷ�Ͻӵ��ɹ�").setPositiveButton(R.string.i_know, null)

					.show();
					
					save_button.setEnabled(true);
					save_to_server_button.setEnabled(true);
					confirm_accept_button.setEnabled(false);
					

				}
			} catch (Exception e) {

				Log.e(WeixiuJiedanNew.LOG_TAG, e.getMessage());
				new AlertDialog.Builder(WeixiuJiedanNew.this).setMessage(
						"ȷ�Ͻӵ�ʧ��").setPositiveButton(R.string.i_know, null)
						.show();
			}

		}
	}

	// handle save button click
	class DeleteButtonListener implements Button.OnClickListener {
		public void onClick(View arg0) {
			// build object

			Weixiudan weixiudan = new Weixiudan(ctx);
			build_weixiudan(weixiudan);
			weixiudan._id = WeixiuJiedanNew.current_object_id;
			try {
				if (weixiudan.delete() == 1) {
					new AlertDialog.Builder(WeixiuJiedanNew.this).setMessage(
							getResources().getString(
									R.string.weixiudan_delete_ok))
							.setPositiveButton(R.string.i_know, null).show();
					clear_fields();

					jump_to_weixiu_list_view();
				}
			} catch (Exception e) {

				new AlertDialog.Builder(WeixiuJiedanNew.this).setMessage(
						getResources().getString(
								R.string.weixiudan_delete_error))
						.setPositiveButton(R.string.i_know, null).show();
			}

		}
	}

	// handle save button click
	class EditButtonListener implements Button.OnClickListener {
		public void onClick(View arg0) {
			// build object

			Weixiudan weixiudan = new Weixiudan(ctx);
			build_weixiudan(weixiudan);
			weixiudan._id = WeixiuJiedanNew.current_object_id;
			try {
				if (weixiudan.save_into_db()) {
					new AlertDialog.Builder(WeixiuJiedanNew.this).setMessage(
							getResources()
									.getString(R.string.weixiudan_save_ok))
							.setPositiveButton(R.string.i_know, null).show();
					clear_fields();
					jump_to_weixiu_list_view();
				}
			} catch (Exception e) {

				Log.e(WeixiuJiedanNew.LOG_TAG, e.getMessage());
				new AlertDialog.Builder(WeixiuJiedanNew.this)
						.setMessage(
								getResources().getString(
										R.string.weixiudan_save_error))
						.setPositiveButton(R.string.i_know, null).show();
			}

		}
	}

	// handle save button click
	private class SaveButtonListener implements Button.OnClickListener {

		public void onClick(View arg0) {
	
			build_weixiudan(weixiudan);
			weixiudan.wanchengzhuangtai = "2";
			ret = weixiudan.jiedan_verify();
			if (ret.verifyCode == VerifiedInfo.VERIFY_ERROR) {
				new AlertDialog.Builder(WeixiuJiedanNew.this).setMessage(
						ret.verifyMessage).setPositiveButton(R.string.i_know,
						null).show();
			} else {
				weixiudan.jiedan_update_into_db();
				new AlertDialog.Builder(WeixiuJiedanNew.this)
						.setMessage("����ɹ�").setPositiveButton(R.string.i_know,
								null).show();
				save_button.setEnabled(false);
			}

		}
	}

//	class TakePicClickListener implements Button.OnClickListener {
//
//		@Override
//		public void onClick(View arg0) {
//			//			
//			try {
//				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//				startActivityForResult(intent, 10);
//			} catch (Exception e) {
//				Log.v(WeixiuJiedanNew.LOG_TAG, e.getMessage());
//			}
//		}
//
//	}



	protected void build_weixiudan(Weixiudan weixiudan) {

		weixiudan.weixiukaishishijian = weixiukaishishijian.getText()
				.toString().trim();
		weixiudan.weixiujieshushijian = weixiujieshushijian.getText()
				.toString().trim();
		weixiudan.wanchengqingkuang = wanchengqingkuang.getText().toString()
				.trim();

	}

	

	protected void fill_form_with_tousudan(Tousudan tousudan) {

	}

	protected void clear_fields() {
		unit_et.setText("");
		tel_et.setText("");
		person_et.setText("");
		desc_et.setText("");
		pic.setImageResource(R.drawable.icon);
		temp_image_path = "";
	}

	private void kaishi_showDateTimeDialog() {
		// Create the dialog
		final Dialog mDateTimeDialog = new Dialog(this);
		// Inflate the root layout
		final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater()
				.inflate(R.layout.date_time_dialog, null);
		// Grab widget instance
		final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView
				.findViewById(R.id.DateTimePicker);
		// Check is system is set to use 24h time (this doesn't seem to work as
		// expected though)
		final String timeS = android.provider.Settings.System.getString(
				getContentResolver(),
				android.provider.Settings.System.TIME_12_24);
		final boolean is24h = !(timeS == null || timeS.equals("12"));

		// Update demo TextViews when the "OK" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						StringBuilder date_time = new StringBuilder();
						date_time.append(mDateTimePicker.get(Calendar.YEAR));
						date_time.append("-");
						date_time
								.append(mDateTimePicker.get(Calendar.MONTH) + 1);
						date_time.append("-");
						date_time.append(mDateTimePicker.get(Calendar.DAY_OF_MONTH) );
						date_time.append(" ");

						date_time.append(mDateTimePicker
								.get(Calendar.HOUR_OF_DAY));
						date_time.append(":");
						date_time.append(mDateTimePicker.get(Calendar.MINUTE));
						// date_time.append(":");

						// ((TextView)
						// findViewById(R.id.Date)).setText(mDateTimePicker.get(Calendar.YEAR)
						// + "/" + (mDateTimePicker.get(Calendar.MONTH)+1) + "/"
						// + mDateTimePicker.get(Calendar.DAY_OF_MONTH));
						//                         
						// if (mDateTimePicker.is24HourView()) {
						// ((TextView)
						// findViewById(R.id.Time)).setText(mDateTimePicker.get(Calendar.HOUR_OF_DAY)
						// + ":" + mDateTimePicker.get(Calendar.MINUTE));
						// } else {
						// ((TextView)
						// findViewById(R.id.Time)).setText(mDateTimePicker.get(Calendar.HOUR)
						// + ":" + mDateTimePicker.get(Calendar.MINUTE) + " "
						// + (mDateTimePicker.get(Calendar.AM_PM) == Calendar.AM
						// ? "AM" : "PM"));
						// }
						weixiukaishishijian.setText(date_time.toString());
						mDateTimeDialog.dismiss();
					}
				});

		// Cancel the dialog when the "Cancel" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.CancelDialog))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						mDateTimeDialog.cancel();
					}
				});

		// Reset Date and Time pickers when the "Reset" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.ResetDateTime))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						mDateTimePicker.reset();
					}
				});

		// Setup TimePicker
		mDateTimePicker.setIs24HourView(is24h);
		// No title on the dialog window
		// mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Set the dialog content view
		mDateTimeDialog.setContentView(mDateTimeDialogView);
		// Display the dialog
		mDateTimeDialog.show();

	}

	private void jieshu_showDateTimeDialog() {
		// Create the dialog
		final Dialog mDateTimeDialog = new Dialog(this);
		// Inflate the root layout
		final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater()
				.inflate(R.layout.date_time_dialog, null);
		// Grab widget instance
		final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView
				.findViewById(R.id.DateTimePicker);
		// Check is system is set to use 24h time (this doesn't seem to work as
		// expected though)
		final String timeS = android.provider.Settings.System.getString(
				getContentResolver(),
				android.provider.Settings.System.TIME_12_24);
		final boolean is24h = !(timeS == null || timeS.equals("12"));

		// Update demo TextViews when the "OK" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						StringBuilder date_time = new StringBuilder();
						date_time.append(mDateTimePicker.get(Calendar.YEAR));
						date_time.append("-");
						date_time
								.append(mDateTimePicker.get(Calendar.MONTH) + 1);
						date_time.append("-");
						date_time.append(mDateTimePicker.get(Calendar.DAY_OF_MONTH));
						date_time.append(" ");

						date_time.append(mDateTimePicker
								.get(Calendar.HOUR_OF_DAY));
						date_time.append(":");
						date_time.append(mDateTimePicker.get(Calendar.MINUTE));
						// date_time.append(":");

						// ((TextView)
						// findViewById(R.id.Date)).setText(mDateTimePicker.get(Calendar.YEAR)
						// + "/" + (mDateTimePicker.get(Calendar.MONTH)+1) + "/"
						// + mDateTimePicker.get(Calendar.DAY_OF_MONTH));
						//                         
						// if (mDateTimePicker.is24HourView()) {
						// ((TextView)
						// findViewById(R.id.Time)).setText(mDateTimePicker.get(Calendar.HOUR_OF_DAY)
						// + ":" + mDateTimePicker.get(Calendar.MINUTE));
						// } else {
						// ((TextView)
						// findViewById(R.id.Time)).setText(mDateTimePicker.get(Calendar.HOUR)
						// + ":" + mDateTimePicker.get(Calendar.MINUTE) + " "
						// + (mDateTimePicker.get(Calendar.AM_PM) == Calendar.AM
						// ? "AM" : "PM"));
						// }
						weixiujieshushijian.setText(date_time.toString());
						mDateTimeDialog.dismiss();
					}
				});

		// Cancel the dialog when the "Cancel" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.CancelDialog))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						mDateTimeDialog.cancel();
					}
				});

		// Reset Date and Time pickers when the "Reset" button is clicked
		((Button) mDateTimeDialogView.findViewById(R.id.ResetDateTime))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						// TODO Auto-generated method stub
						mDateTimePicker.reset();
					}
				});

		// Setup TimePicker
		mDateTimePicker.setIs24HourView(is24h);
		// No title on the dialog window
		// mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Set the dialog content view
		mDateTimeDialog.setContentView(mDateTimeDialogView);
		// Display the dialog
		mDateTimeDialog.show();

	}
}
