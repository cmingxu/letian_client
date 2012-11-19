package com.letian.view;


import java.util.ArrayList;

import com.letian.R;
import com.letian.lib.LocalAccessor;
import com.letian.model.KfWeixiudan;
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

public class ShouLouList extends Activity implements OnItemClickListener {
	ListView listView;
	Cursor cursor;
	
	ArrayList<KfWeixiudan> list = new ArrayList<KfWeixiudan>();
	public static final int BACK = Menu.FIRST;   

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.baoshi_list);
		this.setTitle(LocalAccessor.login_user_title());

	

		if(KfWeixiudan.table_exists(this.getApplicationContext())){
			cursor = KfWeixiudan.getScrollDataCursor(0, 11130,getApplicationContext()); 
			list = get_list_from_cursor(cursor);
		}else{
			cursor = null;
		}


		ListAdapter adapter = new ListAdapter(this.getApplicationContext(),list);
        listView  = ((ListView) findViewById(R.id.baoshi_list));
        listView.setAdapter(adapter); 
        listView.setOnItemClickListener(ShouLouList.this);

	}
	
	public ArrayList<KfWeixiudan> get_list_from_cursor(Cursor c){
		ArrayList<KfWeixiudan> list = new ArrayList<KfWeixiudan>();
		
		c.moveToFirst();
		while(!c.isAfterLast()){
			KfWeixiudan kf_weixiudan = new KfWeixiudan(ShouLouList.this.getApplicationContext());
			kf_weixiudan._id = c.getInt(0);
			kf_weixiudan.danyuanmingcheng = c.getString(3);
			kf_weixiudan.lianxiren = c.getString(4);
			kf_weixiudan.lianxidianhua =c.getString(5);
			kf_weixiudan.beizhu = c.getString(9);
			kf_weixiudan.is_syned = c.getInt(10);
			list.add(kf_weixiudan);
			c.moveToNext();
		}
		
//		c.close();
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
            case BACK:jump_to_weixiu_view();break;  
        }  
        return super.onOptionsItemSelected(item);  
    }  
    
    protected void jump_to_weixiu_view(){
    	Intent intent = new Intent();
    	intent.setClass(ShouLouList.this,ShouLouNew.class);
    	startActivity(intent);
    	finish();
    }


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		cursor.moveToPosition(arg2);
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt("_id", cursor.getInt(0));
		intent.putExtras(bundle);
		intent.setClass(ShouLouList.this, ShouLouEdit.class);
		startActivity(intent);
		this.finish();

	}
	
	
	private class ListAdapter extends BaseAdapter {
		
		public ListAdapter(Context context, ArrayList<KfWeixiudan> list) {
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
	

			KfWeixiudan kf_weixiudan = list.get(position);
			if (kf_weixiudan.is_syned == 1) {
				view = new ListItemView(ShouLouList.this.getApplicationContext(),
						kf_weixiudan.lianxiren,kf_weixiudan.beizhu,true);
			} 
			else{
				view = new ListItemView(ShouLouList.this.getApplicationContext(),
						kf_weixiudan.lianxiren,kf_weixiudan.beizhu,false);
			}
			



			return view;
		}

		private Context mContext;

		private ArrayList<KfWeixiudan> list;

	}


	
	
}
