package com.letian.test;

import com.letian.R;
import com.letian.model.KfWeixiuxiangmu;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ListKfWeixiuxiangmu extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.baoshi_list);

		
        Cursor cursor = KfWeixiuxiangmu.getScrollDataCursor(0, 130,getApplicationContext()); 
        Log.e("CAOAAAAAAAAAAAA",Integer.toString(cursor.getCount()));
        SimpleCursorAdapter adapter= new SimpleCursorAdapter(this,R.layout.baoshi_list_item,
        		cursor,new String[]  {"xiangmuleibie","xiangmumingcheng"},new int[]{R.id.favorite_body,R.id.favorite_category}); 

        ((ListView) findViewById(R.id.baoshi_list)).setAdapter(adapter); 
		

      

	}
}
