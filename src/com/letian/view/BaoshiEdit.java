package com.letian.view;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import com.letian.R;
import com.letian.lib.IOUtils;
import com.letian.lib.LocalAccessor;
import com.letian.model.Danyuan;
import com.letian.model.Tousudan;
import com.letian.model.User;
import com.letian.model.VerifiedInfo;
import com.letian.model.Weixiudan;
import com.letian.model.Zhuhu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

public class BaoshiEdit extends Activity {
	private static final String LOG_TAG = "BaoshiEditActivity";
	public static final int WEIXIU_LIST = Menu.FIRST;
	public static final int TOUSU_LIST = Menu.FIRST + 1;
	public static int current_object_id = -1;
	private Button save_to_server_button;
	private Button delete_button;
	private Button edit_button;
	private EditText unit_et;
	private EditText tel_et;
	private EditText person_et;
	private EditText desc_et;
	private RadioButton baoxiu_rb;
	private RadioButton tousu_rb;
	private Weixiudan weixiudan;
	private Tousudan tousudan;
	private boolean is_baoxiu = false;
	private VerifiedInfo ret;
	private Context ctx;
	private Handler handler = new Handler();
	private ProgressDialog progressDialog = null;

	private Button take_pic;
	private ImageView pic;

	private String loupan_bianhao;
	private String louge_bianhao;
	private String danyuan_bianhao;
	private String danyuan_quan_mingcheng;

	boolean edit = false;

	private String temp_image_path;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baoshi_edit);
		this.setTitle(LocalAccessor.login_user_title());
		ctx = this.getApplicationContext();

		get_widget_from_view();
		unit_et.setFocusable(false);
		edit_button.setOnClickListener(new EditButtonListener());
		save_to_server_button.setOnClickListener(new SaveButtonListener());
		delete_button.setOnClickListener(new DeleteButtonListener());

		unit_et.setOnClickListener(new UnitSelectListener());
		take_pic = (Button) findViewById(R.id.take_pic_button);
		pic = (ImageView) findViewById(R.id.pic);

		take_pic.setOnClickListener(new TakePicClickListener());
		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			edit = true;
			is_baoxiu = true;
			int id = bundle.getInt("_id");
			int type = bundle.getInt("type");
			int syned = 0;
			String myJpgPath = "";

			if (type == 1) {
				this.weixiudan = Weixiudan.get_by_id(id, this
						.getApplicationContext());

				tel_et.setText(this.weixiudan.lianxidianhua);

				unit_et.setText(this.weixiudan.weixiudanyuan);
				person_et.setText(this.weixiudan.zuhumingcheng);
				desc_et.setText(this.weixiudan.wufuneirong);
				baoxiu_rb.setChecked(true);
				tousu_rb.setChecked(false);
				baoxiu_rb.setClickable(false);
				tousu_rb.setClickable(false);

				this.loupan_bianhao = this.weixiudan.suoshuloupan;
				this.louge_bianhao = this.weixiudan.suoshulouge;
				this.danyuan_bianhao = this.weixiudan.danyuanbianhao;
				this.danyuan_quan_mingcheng = this.weixiudan.weixiudanyuan;

				syned = this.weixiudan.is_syned;
				if (this.weixiudan.photo_paths != "") {
					temp_image_path = this.weixiudan.photo_paths;
				}
				myJpgPath = IOUtils.data_file_name(temp_image_path);
				BaoshiEdit.current_object_id = weixiudan._id;

			} else {
				is_baoxiu = false;
				this.tousudan = Tousudan.get_by_id(id, this
						.getApplicationContext());

				tel_et.setText(this.tousudan.tousurenlianxifangshi);
				unit_et.setText(this.tousudan.danyuanmingcheng);
				person_et.setText(this.tousudan.zuhumingcheng);
				desc_et.setText(this.tousudan.tousuneirong);
				baoxiu_rb.setChecked(false);
				tousu_rb.setChecked(true);
				baoxiu_rb.setClickable(false);
				tousu_rb.setClickable(false);

				this.loupan_bianhao = this.tousudan.suoshuloupan;
				this.louge_bianhao = this.tousudan.suoshulouge;
				this.danyuan_bianhao = this.tousudan.danyuanbianhao;
				this.danyuan_quan_mingcheng = this.tousudan.danyuanmingcheng;
				if (this.tousudan.photo_paths != "") {
					temp_image_path = this.tousudan.photo_paths;
				}
				myJpgPath = IOUtils.data_file_name(temp_image_path);
				syned = this.tousudan.is_syned;

				BaoshiEdit.current_object_id = tousudan._id;
			}

			if ((new File(myJpgPath)).exists()) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 1;
				Bitmap bm = BitmapFactory.decodeFile(myJpgPath, options);
				pic.setImageBitmap(bm);
			}

			if (syned == 0) {
				edit_button.setEnabled(true);
				delete_button.setEnabled(true);
				save_to_server_button.setEnabled(true);
			} else {
				edit_button.setEnabled(false);
				delete_button.setEnabled(false);
				save_to_server_button.setEnabled(false);
			}
		}

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
		intent.setClass(BaoshiEdit.this, WeixiuList.class);
		startActivity(intent);
		BaoshiEdit.this.finish();
	}
	
	
	protected void jump_to_baoshi_new() {
		Intent intent = new Intent();
		intent.setClass(BaoshiEdit.this, BaoshiNew.class);
		startActivity(intent);
		BaoshiEdit.this.finish();
	}

	protected void jump_to_tousu_list_view() {
		Intent intent = new Intent();
		intent.setClass(BaoshiEdit.this, TousuList.class);
		startActivity(intent);
		BaoshiEdit.this.finish();
	}

	// handle save button click
	class DeleteButtonListener implements Button.OnClickListener {
		public void onClick(View arg0) {
			// build object
			is_baoxiu = baoxiu_rb.isChecked();
			if (is_baoxiu) {
				Weixiudan weixiudan = new Weixiudan(ctx);
				build_weixiudan(weixiudan);
				weixiudan._id = BaoshiEdit.current_object_id;
				try {
					if (weixiudan.delete() == 1) {
						new AlertDialog.Builder(BaoshiEdit.this).setMessage(
								getResources().getString(
										R.string.weixiudan_delete_ok))
								.setPositiveButton(R.string.i_know, null)
								.show();
						clear_fields();

						jump_to_weixiu_list_view();
					}
				} catch (Exception e) {

					new AlertDialog.Builder(BaoshiEdit.this).setMessage(
							getResources().getString(
									R.string.weixiudan_delete_error))
							.setPositiveButton(R.string.i_know, null).show();
				}
			} else {
				Tousudan tousudan = new Tousudan(ctx);
				build_tousudan(tousudan);
				tousudan._id = BaoshiEdit.current_object_id;
				try {
					if (tousudan.delete() == 1) {
						new AlertDialog.Builder(BaoshiEdit.this).setMessage(
								getResources().getString(
										R.string.tousudan_delete_ok))
								.setPositiveButton(R.string.i_know, null)
								.show();
						clear_fields();
						jump_to_tousu_list_view();
					}
				} catch (Exception e) {

					Log.e(BaoshiEdit.LOG_TAG, e.getMessage());
					new AlertDialog.Builder(BaoshiEdit.this).setMessage(
							getResources().getString(
									R.string.tousudan_delete_error))
							.setPositiveButton(R.string.i_know, null).show();
				}
			}
		}
	}

	// handle save button click
	class EditButtonListener implements Button.OnClickListener {
		public void onClick(View arg0) {
			// build object
			is_baoxiu = baoxiu_rb.isChecked();
			if (is_baoxiu) {
				Weixiudan weixiudan = new Weixiudan(ctx);
				build_weixiudan(weixiudan);
				weixiudan._id = BaoshiEdit.current_object_id;
				try {
					if (weixiudan.save_into_db()) {
						new AlertDialog.Builder(BaoshiEdit.this).setMessage(
								getResources().getString(
										R.string.weixiudan_save_ok))
								.setPositiveButton(R.string.i_know, null)
								.show();
						clear_fields();
						jump_to_weixiu_list_view();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e(BaoshiEdit.LOG_TAG, e.getMessage());
					new AlertDialog.Builder(BaoshiEdit.this).setMessage(
							getResources().getString(
									R.string.weixiudan_save_error))
							.setPositiveButton(R.string.i_know, null).show();
				}
			} else {
				tousudan = new Tousudan(ctx);
				build_tousudan(tousudan);
				tousudan._id = BaoshiEdit.current_object_id;
				try {
					if (tousudan.save_into_db()) {
						new AlertDialog.Builder(BaoshiEdit.this).setMessage(
								getResources().getString(
										R.string.tousudan_save_ok))
								.setPositiveButton(R.string.i_know, null)
								.show();
						clear_fields();
						jump_to_tousu_list_view();
					}
				} catch (Exception e) {
					Log.e(BaoshiEdit.LOG_TAG, e.getMessage());
					new AlertDialog.Builder(BaoshiEdit.this).setMessage(
							getResources().getString(
									R.string.tousudan_save_error))
							.setPositiveButton(R.string.i_know, null).show();
				}
			}
		}
	}

	// handle save button click
	private class SaveButtonListener implements Button.OnClickListener {

		public void onClick(View arg0) {
			String msg = "�ϴ�ʧ��";

			if (is_baoxiu) {
				if (weixiudan.save_to_server()) {
					try {
						weixiudan.delete();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//					weixiudan.update_syned();
					msg = "�ϴ��ɹ�";
//					weixiudan.update_photo_syned();
					new AlertDialog.Builder(BaoshiEdit.this).setMessage(msg)
							.setPositiveButton(R.string.i_know, null).show();

					jump_to_baoshi_new();
				}
			} else {
				if (tousudan.save_to_server()) {
					msg = "�ϴ��ɹ�";
					try {
						tousudan.delete();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
//					tousudan.update_photo_syned();
					new AlertDialog.Builder(BaoshiEdit.this).setMessage(msg)
							.setPositiveButton(R.string.i_know, null).show();

					jump_to_baoshi_new();
				}
			}

		}
	}

	class TakePicClickListener implements Button.OnClickListener {

		@Override
		public void onClick(View arg0) {
			//			
			try {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, 10);
			} catch (Exception e) {
				Log.v(BaoshiEdit.LOG_TAG, e.getMessage());
			}
		}

	}

	// handle edit text click , select louge / loupan / danyuan
	class UnitSelectListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {

			Intent intent = new Intent();
			intent.setClass(BaoshiEdit.this, UnitChooser.class);
			int requestCode = 1;
			BaoshiEdit.this.startActivityForResult(intent, requestCode);

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			switch (resultCode) {
			case RESULT_OK:
				Bundle b = data.getExtras();
				// int type = b.getInt("type");
				loupan_bianhao = b.getString("loupan_bianhao");
				louge_bianhao = b.getString("louge_bianhao");
				danyuan_bianhao = b.getString("danyuan_bianhao");
				danyuan_quan_mingcheng = b.getString("danyuan_quan_mingcheng");

				Zhuhu zhuhu = Danyuan.get_danyuan_with_danyuanbianhao(
						danyuan_bianhao,
						BaoshiEdit.this.getApplicationContext()).get_zhuhu();
				tel_et.setText(zhuhu.lianxidianhua + "/" + zhuhu.shoujihaoma);
				person_et.setText(zhuhu.zhuhumingcheng);
				unit_et.setText(danyuan_quan_mingcheng);
			}
		case 10: // take pic

			if (requestCode != 10) {
				return;
			}
			super.onActivityResult(requestCode, resultCode, data);

			Bundle extras = data.getExtras();
			try {
				Bitmap mBitmap = (Bitmap) extras.get("data");

				// create dir if dir not exists
				if (!(new File(IOUtils.data_path()).exists())) {
					new File(IOUtils.data_path()).mkdirs();
				}

				String image_file_path = IOUtils
						.data_file_name(temp_image_path);
				File f = new File(image_file_path);
				try {
					BufferedOutputStream os = new BufferedOutputStream(
							new FileOutputStream(f));
					mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
					os.flush();
					os.close();

					pic.setImageBitmap(mBitmap);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	void get_widget_from_view() {
		this.save_to_server_button = (Button) findViewById(R.id.save_to_server_button);
		this.delete_button = (Button) findViewById(R.id.delete_button);
		this.edit_button = (Button) findViewById(R.id.edit_button);

		this.unit_et = (EditText) findViewById(R.id.choose_unit_ed);
		this.tel_et = (EditText) findViewById(R.id.tel_et);
		this.person_et = (EditText) findViewById(R.id.person_et);
		this.desc_et = (EditText) findViewById(R.id.desc_et);

		this.baoxiu_rb = (RadioButton) findViewById(R.id.rb_baoxiu);
		this.tousu_rb = (RadioButton) findViewById(R.id.rb_tousu);
	}

	// // save weixiudan or tousudan
	// private void save() {
	//
	// // net work can not be reach
	// progressDialog = ProgressDialog.show(BaoshiEdit.this, "������...", null,
	// true);
	// new Thread() {
	// boolean res = false;
	//
	// public void run() {
	//
	// if (is_baoxiu) {
	// if (IOUtils.file_exists(temp_image_path)) {
	// weixiudan.photo_paths = temp_image_path;
	// }
	//
	// res = weixiudan.save_into_db();
	// if (weixiudan.save_to_server()) {
	// weixiudan.update_syned();
	// }
	// } else {
	// if (IOUtils.file_exists(temp_image_path)) {
	// tousudan.photo_paths = temp_image_path;
	// }
	// res = tousudan.save_into_db();
	// if (tousudan.save_to_server()) {
	// tousudan.update_syned();
	//
	// }
	//
	// }
	// if (!NetworkConnection.getInstance(
	// BaoshiEdit.this.getApplicationContext())
	// .isNetworkAvailable()) {
	// Log.e(BaoshiEdit.LOG_TAG, "network can not be reach");
	// res = false;
	// }
	//
	// progressDialog.dismiss();
	// handler.post(new Runnable() {
	// public void run() {
	// if (!res) {
	// new AlertDialog.Builder(BaoshiEdit.this)
	// .setMessage(R.string.weixiudan_save_error)
	// .setPositiveButton(R.string.i_know, null)
	// .show();
	// } else {
	// clear_fields();
	//
	// new AlertDialog.Builder(BaoshiEdit.this)
	// .setMessage(R.string.weixiudan_save_ok)
	// .setPositiveButton(R.string.i_know, null)
	// .show();
	// }
	// }
	// });
	//
	// }
	// }.start();
	// }

	// build weixiudan
	protected void build_weixiudan(Weixiudan weixiudan) {
		weixiudan.is_syned = 0;
		weixiudan.weixiudanyuan = unit_et.getText().toString().trim();
		weixiudan.lianxidianhua = tel_et.getText().toString().trim();
		weixiudan.zuhumingcheng = person_et.getText().toString().trim();
		weixiudan.wufuneirong = desc_et.getText().toString().trim();
		weixiudan.shoudanren = User.current_user.name();
		weixiudan.shoudanshijian = new Date().toLocaleString();
		weixiudan.suoshuloupan = this.loupan_bianhao;
		weixiudan.suoshulouge = this.louge_bianhao;
		weixiudan.danyuanbianhao = this.danyuan_bianhao;
		weixiudan.weixiudanyuan = this.danyuan_quan_mingcheng;
		weixiudan.photo_paths = this.temp_image_path;

	}

	protected void fill_form_with_weixiudan() {
		tel_et.setText(weixiudan.lianxidianhua);
		unit_et.setText(weixiudan.weixiudanyuan);
		person_et.setText(weixiudan.zuhumingcheng);
		desc_et.setText(weixiudan.wufuneirong);
		baoxiu_rb.setChecked(true);
		tousu_rb.setChecked(false);
	}

	protected void fill_form_with_tousudan(Tousudan tousudan) {

		baoxiu_rb.setChecked(false);
		tousu_rb.setChecked(true);
	}

	protected void build_tousudan(Tousudan tousudan) {
		tousudan.is_syned = 0;
		tousudan.danyuanmingcheng = unit_et.getText().toString().trim();
		tousudan.tousuriqi = (new Date()).toLocaleString();
		tousudan.tousuneirong = desc_et.getText().toString().trim();
		tousudan.zuhumingcheng = person_et.getText().toString().trim();
		tousudan.tousuzhuti = desc_et.getText().toString().trim();
		tousudan.tousurenlianxifangshi = tel_et.getText().toString().trim();

		tousudan.suoshuloupan = this.loupan_bianhao;
		tousudan.suoshulouge = this.louge_bianhao;
		tousudan.danyuanbianhao = this.danyuan_bianhao;
		tousudan.photo_paths = this.temp_image_path;
	}

	protected void clear_fields() {
		unit_et.setText("");
		tel_et.setText("");
		person_et.setText("");
		desc_et.setText("");
		pic.setImageResource(R.drawable.icon);
		temp_image_path = "";
	}
}
