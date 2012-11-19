package com.letian.receiver;



import com.letian.view.ReceiverActivityMedimDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



public class StartActivity extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {

		
		Intent mIntent = new Intent(context, ReceiverActivityMedimDialog.class);
		
//		Intent mIntent = new Intent(context, NoticeActivity.class);
		 mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(mIntent);
}


}