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

public class BaoshiNew extends Activity {
	private static final String LOG_TAG = "BaoshiNewActivity";
	public static final int WEIXIU_LIST = Menu.FIRST;
	public static final int TOUSU_LIST = Menu.FIRST + 1;
	public static final int SHOUWEIXIU_LIST = Menu.FIRST + 2;
	public static final int SHOUTOUSU_LIST = Menu.FIRST + 3;
	public static int current_object_id = -1;
	private Button save_button;

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
		setContentView(R.layout.baoshi_new);
		this.setTitle(LocalAccessor.login_user_title());
		ctx = this.getApplicationContext();
		// init widget

		temp_image_path = IOUtils.rand_file_name();
		get_widget_from_view();
		unit_et.setFocusable(false);

		save_button.setOnClickListener(new SaveButtonListener());


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
			String myJpgPath = "";

			if (type == 1) {
				this.weixiudan = Weixiudan.get_by_id(id, this
						.getApplicationContext());

				tel_et.setText(this.weixiudan.lianxidianhua);
				unit_et.setText(this.weixiudan.weixiudanyuan);
				person_et.setText(this.weixiudan.zuhumingcheng);
				desc_et.setText(this.weixiudan.wufuneirong);
//				baoxiu_rb.setChecked(true);
//				tousu_rb.setChecked(false);
//				baoxiu_rb.setClickable(false);
//				tousu_rb.setClickable(false);

	
				myJpgPath = IOUtils.data_file_name(this.weixiudan.photo_paths);

				BaoshiNew.current_object_id = weixiudan._id;

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

				myJpgPath = IOUtils.data_file_name(this.tousudan.photo_paths);
	

				BaoshiNew.current_object_id = tousudan._id;
			}

			
			if ((new File(myJpgPath)).exists()) {
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				Bitmap bm = BitmapFactory.decodeFile(myJpgPath, options);
				pic.setImageBitmap(bm);
			}

		}

	}

	@Override
	public void onResume() {
		super.onResume();
		save_button.setOnClickListener(new SaveButtonListener());
		// pic.setImageResource(R.drawable.icon);
		// temp_image_path = "";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, WEIXIU_LIST, 0, "ά��");
		menu.add(0, TOUSU_LIST, 1, "Ͷ��");
		menu.add(0, SHOUWEIXIU_LIST, 2, "ά�޽ӵ�");
//		menu.add(0, SHOUTOUSU_LIST, 3, "Ͷ�߽ӵ�");

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
		case SHOUWEIXIU_LIST:
			jump_to_weixiu_jiedan_list_view();
			break;
		case SHOUTOUSU_LIST:
			jump_to_tousu_jiedan_list_view();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void jump_to_weixiu_list_view() {
		Intent intent = new Intent();
		intent.setClass(BaoshiNew.this, WeixiuList.class);
		startActivity(intent);
	}

	protected void jump_to_tousu_list_view() {
		Intent intent = new Intent();
		intent.setClass(BaoshiNew.this, TousuList.class);
		startActivity(intent);
	}
	
	protected void jump_to_weixiu_jiedan_list_view() {
		Intent intent = new Intent();
		intent.setClass(BaoshiNew.this, WeixiuJiedanList.class);
		startActivity(intent);
	}
	
	protected void jump_to_tousu_jiedan_list_view() {
		Intent intent = new Intent();
		intent.setClass(BaoshiNew.this, TousuJiedanList.class);
		startActivity(intent);
	}

	
	// handle save button click
	private class SaveButtonListener implements Button.OnClickListener {

		public void onClick(View arg0) {

		
			// build object
			is_baoxiu = baoxiu_rb.isChecked();
			
			if (is_baoxiu) {
				weixiudan = new Weixiudan(ctx);
				build_weixiudan(weixiudan);

			} else {
				tousudan = new Tousudan(ctx);
				build_tousudan(tousudan);
			}

			// verify
			ret = is_baoxiu ? weixiudan.verify() : tousudan.verify();
			if (ret.verifyCode == VerifiedInfo.VERIFY_ERROR) {
				new AlertDialog.Builder(BaoshiNew.this).setMessage(
						ret.verifyMessage).setPositiveButton(R.string.i_know,
						null).show();
			} else {
				// save(save to server if network connection avaliable)
				save();

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
				Log.v(BaoshiNew.LOG_TAG, e.getMessage());
			}
		}

	}

	// handle edit text click , select louge / loupan / danyuan
	class UnitSelectListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {

			Intent intent = new Intent();
			intent.setClass(BaoshiNew.this, UnitChooser.class);
			int requestCode = 1;
			BaoshiNew.this.startActivityForResult(intent, requestCode);

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			switch (resultCode) {
			case RESULT_OK:
				Bundle b = data.getExtras();
//				int type = b.getInt("type");
				loupan_bianhao = b.getString("loupan_bianhao");
				louge_bianhao = b.getString("louge_bianhao");
				danyuan_bianhao = b.getString("danyuan_bianhao");
				danyuan_quan_mingcheng = b.getString("danyuan_quan_mingcheng");

				Zhuhu zhuhu = Danyuan
						.get_danyuan_with_danyuanbianhao(danyuan_bianhao,
								BaoshiNew.this.getApplicationContext())
						.get_zhuhu();
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
		this.save_button = (Button) findViewById(R.id.save_button);

		this.unit_et = (EditText) findViewById(R.id.choose_unit_ed);
		this.tel_et = (EditText) findViewById(R.id.tel_et);
		this.person_et = (EditText) findViewById(R.id.person_et);
		this.desc_et = (EditText) findViewById(R.id.desc_et);

		this.baoxiu_rb = (RadioButton) findViewById(R.id.rb_baoxiu);
		this.tousu_rb = (RadioButton) findViewById(R.id.rb_tousu);
	}

	// save weixiudan or tousudan
	private void save() {

		// net work can not be reach
		progressDialog = ProgressDialog.show(BaoshiNew.this, "������...", null,
				true);
		new Thread() {
			boolean res = false;

			@Override
			public void run() {
		
				if (is_baoxiu) {
					if (IOUtils.file_exists(temp_image_path)) {
						weixiudan.photo_paths = temp_image_path;
					}

					res = weixiudan.save_into_db();
				} else {
					if (IOUtils.file_exists(temp_image_path)) {
						tousudan.photo_paths = temp_image_path;
					}
					res = tousudan.save_into_db();
		
				}
		
				
				progressDialog.dismiss();
				handler.post(new Runnable() {
					public void run() {
						if (!res) {
							new AlertDialog.Builder(BaoshiNew.this).setMessage(
									"���浽������ݿ�ʧ��")
									.setPositiveButton(R.string.i_know, null)
									.show();
						} else {
							clear_fields();

//							new AlertDialog.Builder(BaoshiNew.this).setMessage(
//									"���浽������ݿ�ɹ�")
									
//									.setPositiveButton(R.string.i_know, null)
//									.show();
							Intent intent = new Intent();
							if(is_baoxiu){
							intent.setClass(BaoshiNew.this, WeixiuList.class);
							}else{
								intent.setClass(BaoshiNew.this, TousuList.class);	
							}
							startActivity(intent);
							
						}
					}
				});

			}
		}.start();
	}

	// build weixiudan
	protected void build_weixiudan(Weixiudan weixiudan) {
		weixiudan.is_syned = 0;
		weixiudan.lianxidianhua = tel_et.getText().toString().trim();
		weixiudan.zuhumingcheng = person_et.getText().toString().trim();
		weixiudan.wufuneirong = desc_et.getText().toString().trim();
		weixiudan.shoudanren = User.current_user.name();
		weixiudan.shoudanshijian = new Date().toLocaleString();
		weixiudan.suoshuloupan = this.loupan_bianhao;
		weixiudan.suoshulouge = this.louge_bianhao;
		weixiudan.danyuanbianhao = this.danyuan_bianhao;
		weixiudan.weixiudanyuan = this.danyuan_quan_mingcheng;

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
		tousudan.tousuriqi = (new Date()).toLocaleString();
		tousudan.tousuneirong = desc_et.getText().toString().trim();
		tousudan.zuhumingcheng = person_et.getText().toString().trim();
		tousudan.tousuzhuti = desc_et.getText().toString().trim();
		tousudan.tousurenlianxifangshi = tel_et.getText().toString().trim();
	
		tousudan.suoshuloupan = this.loupan_bianhao;
		tousudan.suoshulouge = this.louge_bianhao;
		tousudan.danyuanbianhao = this.danyuan_bianhao;
		tousudan.danyuanmingcheng = this.danyuan_quan_mingcheng;
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
