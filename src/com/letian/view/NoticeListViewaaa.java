package com.letian.view;

import java.util.ArrayList;
import java.util.Date;

import com.letian.model.*;
import com.letian.Main;
import com.letian.R;
import com.letian.lib.LocalAccessor;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class NoticeListViewaaa extends ListActivity {
	public ArrayList<Syssend> list = new ArrayList<Syssend>();;
	public static final int BACK = Menu.FIRST;
	NoticeListAdapter adapter;
	@Override
	public void onCreate(Bundle savedInstanceState) {


		this.list = fake_notices();
		super.onCreate(savedInstanceState);

		this.getListView().setBackgroundResource(R.drawable.bg);
		// this.setContentView(R.layout.notice_list);
		this.setTitle(LocalAccessor.login_user_title());
		adapter = new NoticeListAdapter(this, list);
		setListAdapter(adapter);

		// cancel notification TODO
	}

	private ArrayList<Syssend> fake_notices() {
		ArrayList<Syssend> list = new ArrayList<Syssend>();

		for (int i = 100; i < 110; i++) {
			Syssend notice = new Syssend(NoticeListViewaaa.this.getApplicationContext());
			notice._id = Integer.toString(i);
			notice.content = "����֪ͨ����" + Integer.toString(i);
			notice.sendtime = (new Date()).toLocaleString();
			notice.sendperson = User.current_user.name;
			notice.style = "ά��";
			list.add(notice);
		}
		return list;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, BACK, 0, "����");
		return super.onCreateOptionsMenu(menu);

	}

	public void refresh(int location){
		list.remove(location);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		final Syssend notice = list.get(position);
		final int p =position;
		AlertDialog dlg = new AlertDialog.Builder(NoticeListViewaaa.this)
		.setView(new NoticeDialogView(NoticeListViewaaa.this.getApplication(),notice))
				.setTitle(notice.style).setPositiveButton("ȷ�ϴ���",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								String s ="";
								if(notice.confirm_handle()){
									s = "ȷ�ϳɹ�";
									refresh(p);
									
								}else{
									s = "ȷ��ʧ�ܣ�������������";
								}
							   	new AlertDialog.Builder(NoticeListViewaaa.this)
				                .setMessage(s)
				                .setPositiveButton(R.string.i_know, null)
				                .show(); 
								
							}
						}).setNegativeButton("�ݲ�����",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								String s ="";
								if(notice.reject_handle()){
									s = "ȷ�ϳɹ�";
								}else{
									s = "ȷ��ʧ�ܣ�������������";
								}
							   	new AlertDialog.Builder(NoticeListViewaaa.this)
				                .setMessage(s)
				                .setPositiveButton(R.string.i_know, null)
				                .show(); 
							}
						}).create();
		dlg.show();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case BACK:
			Intent intent = new Intent();
			intent.setClass(this, Main.class);
			startActivity(intent);
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private class NoticeListAdapter extends BaseAdapter {
		public NoticeListAdapter(Context context, ArrayList<Syssend> list) {
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
			NoticeView sv;
			if (convertView == null) {
				sv = new NoticeView(mContext,
						(list.get(position)).content, (list
								.get(position)).sendperson);
			} else {
				sv = (NoticeView) convertView;
				sv.setTitle((list.get(position)).content);
				sv.setDialogue((list.get(position)).sendperson);
			}

			return sv;
		}

		private Context mContext;

		private ArrayList<Syssend> list;

	}

	private class NoticeView extends LinearLayout {

		public NoticeView(Context context, String title, String words) {
			super(context);

			this.setOrientation(VERTICAL);

			mTitle = new TextView(context);
			mTitle.setText(title);
			addView(mTitle, new LinearLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

			mDialogue = new TextView(context);
			mDialogue.setText(words);
			addView(mDialogue, new LinearLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		}

		/**
		 * Convenience method to set the title of a SpeechView
		 */
		public void setTitle(String title) {
			mTitle.setText(title);
		}

		/**
		 * Convenience method to set the dialogue of a SpeechView
		 */
		public void setDialogue(String words) {
			mDialogue.setText(words);
		}

		private TextView mTitle;
		private TextView mDialogue;
	}
	
	private class NoticeDialogView extends LinearLayout {

		public NoticeDialogView(Context context, Syssend notice) {
			super(context);
			this.notice = notice;
			this.setOrientation(VERTICAL);

			mTitle = new TextView(context);
			mTitle.setText(notice.content);
			addView(mTitle, new LinearLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

			mDialogue = new TextView(context);
			mDialogue.setText(notice.sendperson);
			addView(mDialogue, new LinearLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		}

		/**
		 * Convenience method to set the title of a SpeechView
		 */
		public void setTitle(String title) {
			mTitle.setText(title);
		}

		/**
		 * Convenience method to set the dialogue of a SpeechView
		 */
		public void setDialogue(String words) {
			mDialogue.setText(words);
		}

		private Syssend notice;

		private TextView mTitle;
		private TextView mDialogue;
	}
}
