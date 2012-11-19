package com.letian.view;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.letian.R;
import com.letian.lib.IOUtils;
import com.letian.model.KfWeixiujilu;
import com.letian.model.LTException;
import com.letian.model.VerifiedInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class KfWeixiujiluNew extends Activity {
	
	private EditText weixiuxiangmu;
	private EditText weixiuneirong;
	private EditText weixiushuoming;
	private Button take_pic_button;
	private Button return_button;
	private Button save_button;
	private ImageView pic;
	
	private String temp_image_path;
	private String xiangmu_id;
	private int kf_weixiudan_id;
	
	private KfWeixiujilu weixiujilu;
	private VerifiedInfo ret;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.weixiujilu_new);

	weixiuxiangmu = (EditText) findViewById(R.id.weixiuxiangmu);
	weixiuneirong = (EditText) findViewById(R.id.weixiuneirong);
	weixiushuoming = (EditText) findViewById(R.id.weixiushuoming);
	take_pic_button = (Button) findViewById(R.id.take_pic_button);
	pic = (ImageView) findViewById(R.id.pic);
	return_button = (Button) findViewById(R.id.return_button);
	save_button = (Button) findViewById(R.id.save_button);


	weixiuxiangmu.setFocusable(false);
	
	weixiuxiangmu.setOnClickListener(new ChooseWeixiuxiangmu());
	take_pic_button.setOnClickListener(new TakePicButton());
	return_button.setOnClickListener(new ReturnButton());

	save_button.setOnClickListener(new SaveButton());
	
	temp_image_path = IOUtils.rand_file_name();
	
	Bundle b = this.getIntent().getExtras();
	kf_weixiudan_id = b.getInt("shoulouweixiudan_id");
}
	
	private class ReturnButton implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.putExtra("shoulouweixiudan_id", kf_weixiudan_id);
			KfWeixiujiluNew.this.setResult(RESULT_OK, intent);  
			KfWeixiujiluNew.this.finish();  
		}}
	
	private class SaveButton implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.putExtra("shoulouweixiudan_id", kf_weixiudan_id);
			weixiujilu = new KfWeixiujilu(KfWeixiujiluNew.this.getApplicationContext());
			build_weixiujilu(weixiujilu);
			
			ret = weixiujilu.verify();
			if (ret.verifyCode == VerifiedInfo.VERIFY_ERROR) {
				new AlertDialog.Builder(KfWeixiujiluNew.this).setMessage(
						ret.verifyMessage).setPositiveButton(R.string.i_know,
						null).show();
				
			}
			else{
				save_weixiujilu();
				KfWeixiujiluNew.this.setResult(RESULT_OK, intent);  
				KfWeixiujiluNew.this.finish();  
			}
			
	
		}}
	
	private void build_weixiujilu(KfWeixiujilu weixiujilu){
		weixiujilu.weixiushuoming = weixiushuoming.getText().toString().trim();
		weixiujilu.weixuneirong = weixiushuoming.getText().toString().trim();
		weixiujilu.xiangmu_id = xiangmu_id;
		weixiujilu.weixiudan_id = kf_weixiudan_id;
		
		if((new File(IOUtils
		.data_file_name(temp_image_path))).exists()){
		weixiujilu.photo_paths = temp_image_path;
		}
		
	}
	
	private void save_weixiujilu(){
		try {
	
			weixiujilu.save_into_db();
		} catch (LTException e) {
			// TODO Auto-generated catch block
			Log.e("sssssssss", "SSSSSSSSSSSSSSSSSSSS");
			e.printStackTrace();
		}
	}
	
	private class ChooseWeixiuxiangmu implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(KfWeixiujiluNew.this, WeixiuxiangmuChooser.class);
			int requestCode = 2;
			KfWeixiujiluNew.this.startActivityForResult(intent, requestCode);
			
		}}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 2:
			switch (resultCode) {
			case RESULT_OK:
				Bundle b = data.getExtras();
				String xiangmuleibie = b.getString("xiangmuleibie");
				weixiuxiangmu.setText(xiangmuleibie);
				xiangmu_id = b.getString("xiangmu_id");
			}
		case 10: // take pic

			if(data == null){
				break;
			}
			
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}


	private class TakePicButton implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			try {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, 10);
			} catch (Exception e) {
				Log.v(ShouLouNew.TAG, e.getMessage());
			}
		}

	}
}
