package com.letian.view;


import java.util.ArrayList;
import java.util.Date;

import com.letian.R;
import com.letian.model.Danyuan;
import com.letian.model.KfWeixiudan;
import com.letian.model.KfWeixiujilu;
import com.letian.model.KfWeixiuxiangmu;
import com.letian.model.LTException;
import com.letian.model.User;
import com.letian.model.VerifiedInfo;
import com.letian.model.Zhuhu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ShouLouNew extends Activity implements OnItemClickListener {
	public static final int BAOSHI_LIST = Menu.FIRST;
	public static String TAG = "Shoulounew";
	private EditText choose_unit_ed;
	private EditText tel_et;
	private EditText person_et;

	private EditText desc_et;

	private Button new_weixiujilu_button;
	private Button update_to_server_button;
	private String danyuan_quan_mingcheng;
	private String danyuan_bianhao;

	private String loupanmingcheng;
	private int shoulouweixiudan_id;
	private Context ctx;
	ProgressDialog progressDialog;
	Handler handler;
	ListAdapter adapter;
	ListView listView;
	Cursor cursor;
	ArrayList<KfWeixiujilu> list = new ArrayList<KfWeixiujilu>();

	private KfWeixiudan kf_weixiudan;
	private VerifiedInfo ret;
	
	private boolean before_weixiujilu = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shoulou);

		choose_unit_ed = (EditText) findViewById(R.id.choose_unit_ed);
		tel_et = (EditText) findViewById(R.id.tel_et);
		person_et = (EditText) findViewById(R.id.person_et);
		desc_et = (EditText) findViewById(R.id.desc_et);
		new_weixiujilu_button = (Button) findViewById(R.id.new_weixiujilu_button);
		update_to_server_button = (Button) findViewById(R.id.update_to_server_button);
		choose_unit_ed.setFocusable(false);

		choose_unit_ed.setOnClickListener(new UnitSelectListener());
		update_to_server_button.setOnClickListener(new UpdateToServerButton());
		new_weixiujilu_button.setOnClickListener(new NewWeixiujiluButton());

		ctx = this.getApplicationContext();
		
		update_to_server_button.setEnabled(false);

		handler = new Handler();
	}

	public ArrayList<KfWeixiujilu> get_list_from_cursor(Cursor c) {
		ArrayList<KfWeixiujilu> list = new ArrayList<KfWeixiujilu>();

		c.moveToFirst();
		while (!c.isAfterLast()) {
			KfWeixiujilu kf_weixiujilu = new KfWeixiujilu(ShouLouNew.this
					.getApplicationContext());
			kf_weixiujilu._id = c.getInt(0);
			kf_weixiujilu.xiangmu_id = c.getString(1);
			kf_weixiujilu.weixuneirong = c.getString(2);
			kf_weixiujilu.weixiushuoming = c.getString(3);
			list.add(kf_weixiujilu);
			c.moveToNext();
		}

		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, BAOSHI_LIST, 0, "报事");
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case BAOSHI_LIST:
			jump_to_shoulou_list_view();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void jump_to_shoulou_list_view() {
		Intent intent = new Intent();
		intent.setClass(ShouLouNew.this, ShouLouList.class);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch (requestCode) {
		case 1:
			switch (resultCode) {
			case RESULT_OK:
				Bundle b = data.getExtras();
				danyuan_bianhao = b.getString("danyuan_bianhao");
				danyuan_quan_mingcheng = b.getString("danyuan_quan_mingcheng");
				loupanmingcheng = b.getString("loupanmingcheng");

				Zhuhu zhuhu = Danyuan.get_danyuan_with_danyuanbianhao(
						danyuan_bianhao,
						ShouLouNew.this.getApplicationContext()).get_zhuhu();
				tel_et.setText(zhuhu.lianxidianhua + "/" + zhuhu.shoujihaoma);
				person_et.setText(zhuhu.zhuhumingcheng);
				choose_unit_ed.setText(danyuan_quan_mingcheng);
				
			}
			break;
		case 2:
			if(data == null){
				break;
			}
			kf_weixiudan = KfWeixiudan.find_by_id(data.getIntExtra("shoulouweixiudan_id", 0),ShouLouNew.this.getApplication());

			if(kf_weixiudan.weixiujilus(ShouLouNew.this.getApplicationContext()).size() > 0){
				update_to_server_button.setEnabled(true);
			}

			choose_unit_ed.setText(kf_weixiudan.danyuanmingcheng);
			tel_et.setText(kf_weixiudan.lianxidianhua);
			person_et.setText(kf_weixiudan.lianxiren);
//			desc_et.setText(kf_weixiudan)
			fill_list();
			before_weixiujilu = false;
			break;
			
		}
	}

	class UnitSelectListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(ShouLouNew.this, UnitChooser.class);
			int requestCode = 1;
			ShouLouNew.this.startActivityForResult(intent, requestCode);

		}
	}
	
	public void fill_list(){
		list = kf_weixiudan.weixiujilus(ShouLouNew.this.getApplication());
		Log.e("LIST SIZE",Integer.toString(list.size()));
		
		adapter = new ListAdapter(this.getApplicationContext(),
				list);
		listView = ((ListView) findViewById(R.id.weixiujilu_list));
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(ShouLouNew.this);
		adapter.notifyDataSetChanged();
	
		
	}

	class NewWeixiujiluButton implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			
			kf_weixiudan = new KfWeixiudan(ctx);
			build_kf_weixiudan(kf_weixiudan);
			ret = kf_weixiudan.verify();
			if (ret.verifyCode == VerifiedInfo.VERIFY_ERROR) {
				new AlertDialog.Builder(ShouLouNew.this).setMessage(
						ret.verifyMessage).setPositiveButton(R.string.i_know,
						null).show();
				return;
			}
			
			try {
				if(before_weixiujilu){
				shoulouweixiudan_id = kf_weixiudan.save_into_db();
				}
			} catch (LTException e) {
				e.printStackTrace();
			}
			
			Intent intent = new Intent();
			Bundle b = new Bundle();
			b.putInt("shoulouweixiudan_id", shoulouweixiudan_id);
			intent.putExtras(b);
			intent.setClass(ShouLouNew.this, KfWeixiujiluNew.class);
			int requestCode = 2;
			ShouLouNew.this.startActivityForResult(intent, requestCode);

		}
	}



	private class UpdateToServerButton implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			if(kf_weixiudan == null){
			kf_weixiudan = new KfWeixiudan(ctx);
			build_kf_weixiudan(kf_weixiudan);
			}
			ret = kf_weixiudan.verify();
			if (ret.verifyCode == VerifiedInfo.VERIFY_ERROR) {
				new AlertDialog.Builder(ShouLouNew.this).setMessage(
						ret.verifyMessage).setPositiveButton(R.string.i_know,
						null).show();
				return;
			}
			
			save();
	

	}
	
	private void save() {
		progressDialog = ProgressDialog.show(ShouLouNew.this, "������...",
				null, true);
	
		new Thread() {
			boolean res = false;
			@Override
			public void run() {
				try {
					if (kf_weixiudan.save_to_server()) {
						kf_weixiudan.update_syned();
						ArrayList<KfWeixiujilu> list = kf_weixiudan.weixiujilus(ShouLouNew.this.getApplicationContext());
						for(KfWeixiujilu jilu : list){
							jilu.save_to_server();
							jilu.delete();
						}
						
						kf_weixiudan.delete();
						
						res = true;
					}

			
		
				} catch (Exception e) {
					
					res = false;
					handler.post(new Runnable() {
						public void run() {
							new AlertDialog.Builder(ShouLouNew.this)
									.setMessage(
											R.string.weixiudan_save_error)
									.setPositiveButton(R.string.i_know,
											null).show();
						}
					});
				}
				progressDialog.dismiss();
				
				
				
				handler.post(new Runnable() {
					public void run() {
						if (!res) {
							new AlertDialog.Builder(ShouLouNew.this)
									.setMessage(
											R.string.weixiudan_save_error)
									.setPositiveButton(R.string.i_know,
											null).show();
						} else {
							clear_fields();

							new AlertDialog.Builder(ShouLouNew.this)
									.setMessage(R.string.weixiudan_save_ok)
									.setPositiveButton(R.string.i_know,
											null).show();
						}
					}

				});

			}
		}.start();

	}
	}

	private void clear_fields() {
		choose_unit_ed.setText("");
		tel_et.setText("");
		person_et.setText("");
		desc_et.setText("");
		
		list = kf_weixiudan.weixiujilus(ShouLouNew.this.getApplication());
		Log.e("LIST SIZE",Integer.toString(list.size()));
		list.clear();
		adapter = new ListAdapter(this.getApplicationContext(),
				list);
		listView = ((ListView) findViewById(R.id.weixiujilu_list));
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(ShouLouNew.this);
		adapter.notifyDataSetChanged();

	}

	protected void build_kf_weixiudan(KfWeixiudan kf_weixiudan) {
		
		kf_weixiudan.is_syned = 0;
		kf_weixiudan.lianxidianhua = tel_et.getText().toString().trim();
		kf_weixiudan.lianxiren = person_et.getText().toString().trim();
		kf_weixiudan.danyuanmingcheng = choose_unit_ed.getText().toString().trim();
		kf_weixiudan.danyuanbianhao = this.danyuan_bianhao;
		kf_weixiudan.loupanmingcheng = this.loupanmingcheng;
		kf_weixiudan.beizhu = desc_et.getText().toString().trim();;
		kf_weixiudan.jiedanren = User.current_user.name();
		kf_weixiudan.jiedanshijian = new Date().toLocaleString();
	

	}


	private class ListAdapter extends BaseAdapter {

		public ListAdapter(Context context, ArrayList<KfWeixiujilu> list) {
			mContext = context;
			this.list = list;
		}

		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			


			View view = null;

			KfWeixiujilu kf_weixiujilu = list.get(position);
			String leibie = KfWeixiuxiangmu.find_leibie_by_id(ShouLouNew.this.getApplicationContext(), kf_weixiujilu.xiangmu_id);
			if (kf_weixiujilu.is_syned == 1) {
				view = new ListItemView(
						ShouLouNew.this.getApplicationContext(),
						leibie,kf_weixiujilu.weixiushuoming,
						true);
			} else {
				view = new ListItemView(
						ShouLouNew.this.getApplicationContext(),
						leibie,kf_weixiujilu.weixiushuoming,
						true);
			}

			return view;
		}

		private Context mContext;

		private ArrayList<KfWeixiujilu> list;

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

}