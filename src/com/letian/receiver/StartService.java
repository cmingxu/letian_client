package com.letian.receiver;



import com.letian.Login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



public class StartService extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		 Intent mIntent = new Intent(context, Login.class);
	    context.startActivity(mIntent);
		


	}
}