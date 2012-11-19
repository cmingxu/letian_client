package com.letian.view;


import java.util.ArrayList;

import com.letian.Main;
import com.letian.R;
import com.letian.services.FetchNoticeService;

import com.letian.lib.LocalAccessor;
import com.letian.model.Syssend;
import android.app.ListActivity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class NoticeActivity extends ListActivity {

	public static final int BACK = Menu.FIRST;
	NoticeListAdapter adapter;
	Intent i;
	ArrayList<Syssend> list = new ArrayList<Syssend>();
	private NotificationManager notificationManager;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		list = Syssend.fetch_from_server(NoticeActivity.this
				.getApplicationContext());
		this.getListView().setBackgroundResource(R.drawable.bg);
		// this.setContentView(R.layout.notice_list);
		notificationManager = (NotificationManager) this
		.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.cancel(FetchNoticeService.NOTIFICATION_ID);
		this.setTitle(LocalAccessor.login_user_title());
		adapter = new NoticeListAdapter(this, list);
		setListAdapter(adapter);

	
//		button = (Button)findViewById(R.id.back_to_main_button);
//		
//		button.setOnClickListener(new OnClickListener(){
//
//			@Override
//			public void onClick(View arg0) {
//				FetchNoticeService.set_goon(true);
//				Intent intent = new Intent();
//				intent.setClass(NoticeActivity.this, Main.class);
//    			NoticeActivity.this.startActivity(intent);
//				
//			}});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, BACK, 0, "返回");
		return super.onCreateOptionsMenu(menu);

	}

	public void refresh(int location){
		list.remove(location);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		final Syssend notice = list.get(position);
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("tongzhineirong", notice.content);
		bundle.putString("tongzhishijian", notice.sendtime);
		bundle.putString("tongzhileixing", notice.style);
		bundle.putString("id", notice._id);
	
		intent.putExtras(bundle);
		intent.setClass(NoticeActivity.this, NoticeView.class);
		startActivity(intent);
		NoticeActivity.this.finish();
//		final int p = position;
//		AlertDialog dlg = new AlertDialog.Builder(NoticeActivity.this)
//		.setView(new NoticeDialogView(NoticeActivity.this.getApplication(),notice))
//				.setTitle(notice.style).setPositiveButton("ȷ�ϴ���",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int whichButton) {
//								String s ="";
//								if(notice.confirm_handle()){
//									s = "ȷ�ϳɹ�";
//									refresh(p);
//									
//								}else{
//									s = "ȷ��ʧ�ܣ�������������";
//								}
//							   	new AlertDialog.Builder(NoticeActivity.this)
//				                .setMessage(s)
//				                .setPositiveButton(R.string.i_know, null)
//				                .show(); 
//								
//							}
//						}).setNegativeButton("�ݲ�����",
//						new DialogInterface.OnClickListener() {
//							public void onClick(DialogInterface dialog,
//									int whichButton) {
//								String s ="";
//								if(notice.reject_handle()){
//									s = "ȷ�ϳɹ�";
//								}else{
//									s = "ȷ��ʧ�ܣ�������������";
//								}
//							   	new AlertDialog.Builder(NoticeActivity.this)
//				                .setMessage(s)
//				                .setPositiveButton(R.string.i_know, null)
//				                .show(); 
//							}
//						}).create();
//		dlg.show();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case BACK:
			FetchNoticeService.set_goon(true);
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
			NoticeListItemView sv;
			if (convertView == null) {
				sv = new NoticeListItemView(mContext,
						(list.get(position)).content, (list
								.get(position)).sendperson);
			} else {
				sv = (NoticeListItemView) convertView;
				sv.setTitle((list.get(position)).content);
				sv.setDialogue((list.get(position)).sendperson);
			}

			return sv;
		}

		private Context mContext;

		private ArrayList<Syssend> list;

	}

	private class NoticeListItemView extends LinearLayout {

		public NoticeListItemView(Context context, String title, String words) {
			super(context);

			this.setPadding(5, 5, 5, 5);
			this.setOrientation(VERTICAL);

			mTitle = new TextView(context);
			mTitle.setText(title);
			mTitle.setTextColor(Color.DKGRAY);
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
//	
//	private class NoticeDialogView extends LinearLayout {
//
//		public NoticeDialogView(Context context, Syssend notice) {
//			super(context);
//			
//
//			
//			this.notice = notice;
//			this.setOrientation(VERTICAL);
//
//			mTitle = new TextView(context);
//			mTitle.setText(notice.content);
//			addView(mTitle, new LinearLayout.LayoutParams(
//					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//
//			mDialogue = new TextView(context);
//			mDialogue.setText(notice.sendperson);
//			addView(mDialogue, new LinearLayout.LayoutParams(
//					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//		}
//
//		/**
//		 * Convenience method to set the title of a SpeechView
//		 */
//		public void setTitle(String title) {
//			mTitle.setText(title);
//		}
//
//		/**
//		 * Convenience method to set the dialogue of a SpeechView
//		 */
//		public void setDialogue(String words) {
//			mDialogue.setText(words);
//		}
//
//		private Syssend notice;
//
//		private TextView mTitle;
//		private TextView mDialogue;
//	}
}
