package com.letian.view;

import java.util.ArrayList;

import com.letian.R;
import com.letian.model.Tousudan;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class TousuList extends Activity implements OnItemClickListener {
	ListView listView;
	Cursor cursor;
	public static final int BACK = Menu.FIRST;   
	ArrayList<Tousudan> list = new ArrayList<Tousudan>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.baoshi_list);

		

		if(Tousudan.table_exists(this.getApplicationContext())){
			cursor = Tousudan.getScrollDataCursor(0, 11130,getApplicationContext()); 
			list = get_list_from_cursor(cursor);
		}else{
			cursor = null;
		}


		ListAdapter adapter = new ListAdapter(this.getApplicationContext(),list);

        listView  = ((ListView) findViewById(R.id.baoshi_list));
        listView.setAdapter(adapter);  
        listView.setOnItemClickListener(TousuList.this);
	}
	
	public ArrayList<Tousudan> get_list_from_cursor(Cursor c){
		ArrayList<Tousudan> list = new ArrayList<Tousudan>();
		
		c.moveToFirst();
		while(!c.isAfterLast()){
			Tousudan tousudan = new Tousudan(TousuList.this.getApplicationContext());
			tousudan._id = c.getInt(0);
			tousudan.tousudanhao = c.getString(2);
			tousudan.danyuanbianhao = c.getString(3);
			tousudan.danyuanmingcheng = c.getString(4);
			tousudan.zuhumingcheng = c.getString(5);
			tousudan.tousuriqi = c.getString(6);
			tousudan.tousuzhuti = c.getString(7);
			tousudan.tousuneirong = c.getString(8);
			tousudan.tousuleibie = c.getString(9);
			tousudan.tousurenlianxifangshi = c.getString(10);
			tousudan.suoshuloupan = c.getString(11);
			tousudan.suoshulouge = c.getString(12);
			tousudan.is_syned = c.getInt(13);
			list.add(tousudan);
			c.moveToNext();
		}
		
		return list;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {  
        menu.add(0,BACK,0,"返回");
 
        return super.onCreateOptionsMenu(menu);  
          
    }  
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {  
      switch(item.getItemId()){  
            case BACK:jump_to_baoshi_view();break;  
        }  
        return super.onOptionsItemSelected(item);  
    }  
    
    protected void jump_to_baoshi_view(){
    	Intent intent = new Intent();
    	intent.setClass(TousuList.this,BaoshiNew.class);
    	startActivity(intent);
    	finish();
    }


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		this.setTitle("You Click Item:" + String.valueOf(arg2));  
		cursor.moveToPosition(arg2);
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("_id", cursor.getInt(0));
		
		cursor.close();
		Tousudan.db.close();
		bundle.putInt("type", 2);
		intent.putExtras(bundle);
		intent.setClass(TousuList.this, BaoshiEdit.class);
		startActivity(intent);
		
	}
private class ListAdapter extends BaseAdapter {
		
		public ListAdapter(Context context, ArrayList<Tousudan> list) {
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
	
	
			Tousudan tousudan = list.get(position);
			if (tousudan.is_syned == 1) {
				view = new ListItemView(TousuList.this.getApplicationContext(),
						tousudan.tousuneirong,tousudan.tousurenlianxifangshi,true);
			} 
			else{
				view = new ListItemView(TousuList.this.getApplicationContext(),
						tousudan.tousuneirong,tousudan.tousurenlianxifangshi,false);
			}
			



			return view;
		}

		private Context mContext;

		private ArrayList<Tousudan> list;

	}
}
