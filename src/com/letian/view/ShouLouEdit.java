package com.letian.view;

import java.util.ArrayList;


import com.letian.R;
import com.letian.model.KfWeixiudan;
import com.letian.model.KfWeixiujilu;
import com.letian.model.KfWeixiuxiangmu;

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

public class ShouLouEdit extends Activity implements OnItemClickListener {
	public static final int BAOSHI_LIST = Menu.FIRST;
	public static String TAG = "Shoulounew";
	private EditText choose_unit_ed;
	private EditText tel_et;
	private EditText person_et;

	private EditText desc_et;

	private Button delete;
	private Button save_weixiudan_button;
	ListAdapter adapter;
	ListView listView;
	Cursor cursor;
	KfWeixiudan weixiudan;
	int id;
	ArrayList<KfWeixiujilu> list = new ArrayList<KfWeixiujilu>();
	ProgressDialog progressDialog;
	Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shoulou_edit);
		id = this.getIntent().getExtras().getInt("_id");
		weixiudan = KfWeixiudan.find_by_id(id, this.getApplicationContext());

		choose_unit_ed = (EditText) findViewById(R.id.show_choose_unit_ed);

		tel_et = (EditText) findViewById(R.id.show_tel_et);

		person_et = (EditText) findViewById(R.id.show_person_et);
		desc_et = (EditText) findViewById(R.id.show_desc_et);
		delete = (Button) findViewById(R.id.delete_weixiudan_button);
		save_weixiudan_button = (Button) findViewById(R.id.save_weixiudan_button);
		listView = (ListView) findViewById(R.id.weixiujilu_list);

		list = weixiudan.weixiujilus(ShouLouEdit.this.getApplication());

		adapter = new ListAdapter(this.getApplicationContext(), list);
		listView = ((ListView) findViewById(R.id.show_weixiujilu_list));
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(ShouLouEdit.this);
		tel_et.setText(weixiudan.lianxidianhua);
		choose_unit_ed.setText(weixiudan.danyuanmingcheng);
		person_et.setText(weixiudan.lianxiren);
		desc_et.setText(weixiudan.beizhu);
		delete.setOnClickListener(new DeleteButton());
		if (weixiudan.is_syned == 0) {
			save_weixiudan_button.setEnabled(true);
		}
		save_weixiudan_button.setOnClickListener(new SaveToServerLisntener());
		handler = new Handler();
	}

	class SaveToServerLisntener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			save();
		}

		private void save() {
			progressDialog = ProgressDialog.show(ShouLouEdit.this, "请等待...",
					null, true);

			new Thread() {
				boolean res = false;

				@Override
				public void run() {
					try {
						if (weixiudan.save_to_server()) {
							weixiudan.update_syned();
							ArrayList<KfWeixiujilu> list = weixiudan
									.weixiujilus(ShouLouEdit.this
											.getApplicationContext());
							for (KfWeixiujilu jilu : list) {
								jilu.save_to_server();
							}
							res = true;
						}

					} catch (Exception e) {

						res = false;
						handler.post(new Runnable() {
							public void run() {
								new AlertDialog.Builder(ShouLouEdit.this)
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
								new AlertDialog.Builder(ShouLouEdit.this)
										.setMessage(
												R.string.weixiudan_save_error)
										.setPositiveButton(R.string.i_know,
												null).show();
							} else {
								clear_fields();

								new AlertDialog.Builder(ShouLouEdit.this)
										.setMessage(R.string.weixiudan_save_ok)
										.setPositiveButton(R.string.i_know,
												null).show();
								
								Intent intent = new Intent();
								intent.setClass(ShouLouEdit.this, ShouLouList.class);
								startActivity(intent);
							}
						}

					});

				}
			}.start();

		}
		
		private void clear_fields() {
			choose_unit_ed.setText("");
			tel_et.setText("");
			person_et.setText("");
			desc_et.setText("");
			
			list = weixiudan.weixiujilus(ShouLouEdit.this.getApplication());
			Log.e("LIST SIZE",Integer.toString(list.size()));
			list.clear();
			adapter = new ListAdapter(ShouLouEdit.this.getApplicationContext(),
					list);
			
			listView = ((ListView) findViewById(R.id.show_weixiujilu_list));
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(ShouLouEdit.this);
			adapter.notifyDataSetChanged();

		}

	}

	class DeleteButton implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			String message = "";
			if (KfWeixiudan.delete_by_id(id, ShouLouEdit.this
					.getApplicationContext())) {
				message = "ɾ��ɹ�";
				new AlertDialog.Builder(ShouLouEdit.this).setMessage(message)
						.setPositiveButton(R.string.i_know, null).show();
				Intent intent = new Intent();
				intent.setClass(ShouLouEdit.this, ShouLouList.class);
				startActivity(intent);
				ShouLouEdit.this.finish();

			} else {
				message = "ɾ��ʧ��";
				new AlertDialog.Builder(ShouLouEdit.this).setMessage(message)
						.setPositiveButton(R.string.i_know, null).show();
			}

		}

	}

	public ArrayList<KfWeixiujilu> get_list_from_cursor(Cursor c) {
		ArrayList<KfWeixiujilu> list = new ArrayList<KfWeixiujilu>();

		c.moveToFirst();
		while (!c.isAfterLast()) {
			KfWeixiujilu kf_weixiujilu = new KfWeixiujilu(ShouLouEdit.this
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
		menu.add(0, BAOSHI_LIST, 0, "�½���¥ά�޵�");
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case BAOSHI_LIST:
			jump_to_shoulou_new_view();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void jump_to_shoulou_new_view() {
		Intent intent = new Intent();
		intent.setClass(ShouLouEdit.this, ShouLouNew.class);
		startActivity(intent);
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
			String leibie = KfWeixiuxiangmu.find_leibie_by_id(ShouLouEdit.this
					.getApplicationContext(), kf_weixiujilu.xiangmu_id);
			if (kf_weixiujilu.is_syned == 1) {
				view = new ListItemView(ShouLouEdit.this
						.getApplicationContext(), leibie,
						kf_weixiujilu.weixiushuoming, true);
			} else {
				view = new ListItemView(ShouLouEdit.this
						.getApplicationContext(), leibie,
						kf_weixiujilu.weixiushuoming, true);
			}
			return view;
		}

		private Context mContext;

		private ArrayList<KfWeixiujilu> list;

	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

}
