package com.letian.services;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import com.letian.R;
import com.letian.lib.Constants;
import com.letian.model.Syssend;
import com.letian.view.NoticeActivity;
import com.letian.view.WeixiuJiedanList;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class FetchNoticeService extends Service {

	private Notification notification;
	private NotificationManager notificationManager;
	private Intent intent;
	private PendingIntent pendingintent;
	private ArrayList<Syssend> notice_list;
	public static int NOTIFICATION_ID = 100;


	private static boolean goon = true;

	public static void set_goon(boolean g) {
		FetchNoticeService.goon = g;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	private Timer timer;
	public final String TAG = "FetchNoticeService";

	@Override
	public void onCreate() {
		super.onCreate();

	}

	@Override
	public void onStart(Intent startId, int arg) {
		timer = new Timer();
		timer.scheduleAtFixedRate(new mainTask(), 10, Constants.TIME_INTERVAL);
	}

	private class mainTask extends TimerTask {
		@Override
		public void run() {
			notice_list = Syssend.fetch_from_server(FetchNoticeService.this
					.getApplicationContext());
			if (notice_list.size() > 0 && goon) {
				show_notifications();
			}
		}
	}

	private void show_notifications() {
		goon = false;
		// Intent intent = new Intent("android.intent.action.start_activity");
		// Bundle b = new Bundle();
		//
		// this.sendBroadcast(intent);

		notificationManager = (NotificationManager) this
				.getSystemService(NOTIFICATION_SERVICE);//
		// ���֪ͨʱת������
//		intent = new Intent(this, NoticeActivity.class);
		intent = new Intent(this, WeixiuJiedanList.class);
		// ���õ��֪ͨʱ��ʾ���ݵ���
		pendingintent = PendingIntent.getActivity(this, 0, intent, 0);
		notification = new Notification();

		// TODO Auto-generated method stub
		notification.icon = R.drawable.notice;// ������״̬����ʾ��ͼ��
		notification.tickerText = "������֪ͨ���봦��... ...";// ������״̬����ʾ������
		notification.defaults = Notification.DEFAULT_SOUND;// Ĭ�ϵ�����
		// ����֪ͨ��ʾ�Ĳ���
		notification.setLatestEventInfo(FetchNoticeService.this, "��֪ͨ",
				"������֪ͨ�������...", pendingintent);
		notificationManager.notify(FetchNoticeService.NOTIFICATION_ID, notification);// ִ��֪ͨ.

	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
	}
}
