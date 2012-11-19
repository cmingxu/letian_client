package com.letian.view;

import java.util.ArrayList;

import com.letian.R;
import com.letian.model.Weixiudan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class WeixiuJiedanList extends Activity implements OnItemClickListener {
	ListView listView;
	Cursor cursor;
	public static final int BACK = Menu.FIRST;   
	ArrayList<Weixiudan> list = new ArrayList<Weixiudan>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.baoshi_list);

		
		list  = Weixiudan.fetch_from_server(this.getApplicationContext());
		for(Weixiudan w : list){
			w.save_jiedan_into_db();
		}
		if(Weixiudan.table_exists(this.getApplicationContext())){
			cursor = Weixiudan.jiedan_getScrollDataCursor(0, 11130,getApplicationContext()); 
			list = get_list_from_cursor(cursor);
		}else{
			cursor = null;
		}
		ListAdapter adapter = new ListAdapter(this.getApplicationContext(),list);

        listView  = ((ListView) findViewById(R.id.baoshi_list));
        listView.setAdapter(adapter);  
        listView.setOnItemClickListener(WeixiuJiedanList.this);
	}
	
	public ArrayList<Weixiudan> get_list_from_cursor(Cursor c){
		ArrayList<Weixiudan> list = new ArrayList<Weixiudan>();
		
		c.moveToFirst();
		while(!c.isAfterLast()){
			Weixiudan weixiudan = new Weixiudan(WeixiuJiedanList.this.getApplicationContext());
			weixiudan._id = c.getInt(0);
			weixiudan.weixiudanyuan = c.getString(2);
			weixiudan.danyuanbianhao = c.getString(3);
			weixiudan.zuhumingcheng = c.getString(4);
			weixiudan.wufuneirong = c.getString(5);
			weixiudan.shoudanren = c.getString(6);
			weixiudan.shoudanshijian = c.getString(7);
			weixiudan.weixiuzhonglei = c.getString(8);
			
			weixiudan.lianxidianhua = c.getString(9);
			weixiudan.suoshuloupan = c.getString(10);
			weixiudan.suoshulouge = c.getString(11);
			weixiudan.weixiurenyuan = c.getString(12);
			weixiudan.is_syned = c.getInt(13);
			
			weixiudan.wanchengzhuangtai = c.getString(21);
			list.add(weixiudan);

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
    	intent.setClass(WeixiuJiedanList.this,BaoshiNew.class);
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
		Weixiudan.db.close();
		intent.putExtras(bundle);
		intent.setClass(WeixiuJiedanList.this, WeixiuJiedanNew.class);
		startActivity(intent);
		WeixiuJiedanList.this.finish();
		
	}
private class ListAdapter extends BaseAdapter {
		
		public ListAdapter(Context context, ArrayList<Weixiudan> list) {
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
	
	
			Weixiudan weixiudan = list.get(position);
			Log.e("wanchengzhuangtai", weixiudan.wanchengzhuangtai);
			Log.e("wanchengzhuangtai", weixiudan.wanchengzhuangtai);
			Log.e("wanchengzhuangtai", weixiudan.wanchengzhuangtai);
			Log.e("wanchengzhuangtai", weixiudan.wanchengzhuangtai);
			Log.e("wanchengzhuangtai", weixiudan.wanchengzhuangtai);
			Log.e("wanchengzhuangtai", weixiudan.wanchengzhuangtai);
			if (weixiudan.wanchengzhuangtai.equalsIgnoreCase("0")) {
				view = new ListItemView(WeixiuJiedanList.this.getApplicationContext(),
						weixiudan.wufuneirong,weixiudan.lianxidianhua,false);
			} 
			else if(weixiudan.wanchengzhuangtai.equalsIgnoreCase("1")){
				view = new ListItemView(WeixiuJiedanList.this.getApplicationContext(),
						weixiudan.wufuneirong,weixiudan.lianxidianhua,true);
			}
			else if(weixiudan.wanchengzhuangtai.equalsIgnoreCase("2")){
				view = new ListItemView(WeixiuJiedanList.this.getApplicationContext(),
						weixiudan.wufuneirong,weixiudan.lianxidianhua,true);
			}

			return view;
		}

		private Context mContext;

		private ArrayList<Weixiudan> list;

	}
}
