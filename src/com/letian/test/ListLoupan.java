package com.letian.test;

import com.letian.R;
import com.letian.model.Loupan;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ListLoupan extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {


		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.list_unit_test);

		Cursor cursor = Loupan.getScrollDataCursor(0, 1130,getApplicationContext());  
     
        
        SimpleCursorAdapter adapter= new SimpleCursorAdapter(this,R.layout.baoshi_list_item,
        		cursor,new String[]  {"loupanmingcheng","loupanbianhao"},
        		new int[]{R.id.favorite_body,R.id.favorite_category}); 
    
        
        ((ListView) findViewById(R.id.baoshi_list)).setAdapter(adapter); 
      
	}
}
