package com.letian.view;

import com.letian.R;
import com.letian.services.FetchNoticeService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ReceiverActivityMedimDialog extends Activity {
	@Override
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.middle);
        Button handle_now = (Button)findViewById(R.id.handle_now);
        Button handle_later = (Button)findViewById(R.id.handle_later);
        
        handle_now.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent mIntent = new Intent(context, ReceiverActivityMedimDialog.class);
				
				Intent mIntent = new Intent(ReceiverActivityMedimDialog.this.getApplicationContext(), NoticeActivity.class);
				 mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(mIntent);
			}
        	
        });
        
        handle_later.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				FetchNoticeService.set_goon(true);
				ReceiverActivityMedimDialog.this.finish();
			}
        	
        });
	
    }
}
