package com.letian.test;

import com.letian.R;
import com.letian.model.Zhuhu;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ListZhuhu extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.baoshi_list);

		
        Cursor cursor = Zhuhu.getScrollDataCursor(0, 1130,getApplicationContext()); 
        Log.e("CAOAAAAAAAAAAAA",Integer.toString(cursor.getCount()));
        SimpleCursorAdapter adapter= new SimpleCursorAdapter(this,R.layout.baoshi_list_item,
        		cursor,new String[]  {"zhuhumingcheng","zhuhubianhao"},
        		new int[]{R.id.favorite_body,R.id.favorite_category}); 

        ((ListView) findViewById(R.id.baoshi_list)).setAdapter(adapter); 
		


	}
}
