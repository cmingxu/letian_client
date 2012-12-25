package com.letian.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.letian.R;
import com.letian.model.Danyuan;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class LoucengDialog extends Activity {
    /** Called when the activity is first created. */

    private TextView unit_chooser_dialog_title;
    private String louge_bianhao;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.unit_chooser_dialog);
        unit_chooser_dialog_title = (TextView)findViewById(R.id.unit_chooser_dialog_title);
        unit_chooser_dialog_title.setText("选择单元");
        louge_bianhao = getIntent().getExtras().getString("louge_bianhao");
        ArrayList<String> items = Danyuan.distinct_louceng_name(this.getApplicationContext(),louge_bianhao);
        listItems.addAll(items);
        
		final ListView list = (ListView) findViewById(R.id.list);
		adapter = new ListItemsAdapter(listItems);
		list.setAdapter(adapter);
		
		list.setOnItemClickListener(new CustomOnItemClickListener());
    }
    
    private class CustomOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent i = new Intent();  
	          
	        Bundle b = new Bundle();  
	        b.putString("loucengmingcheng", (String)listItems.get(arg2));  
	        b.putInt("type", 3);
	        i.putExtras(b);  
	          
	        LoucengDialog.this.setResult(RESULT_OK, i);  
	        LoucengDialog.this.finish();  
			
		}}
	private class ListItemsAdapter extends ArrayAdapter<Object> {
		public ListItemsAdapter(List<Object> items) {
			super(LoucengDialog.this, android.R.layout.simple_list_item_1, items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;

			LayoutInflater inflater = getLayoutInflater();
			convertView = inflater.inflate(R.layout.unit_chooser_dialog_items, null);

			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.unit_text);		

			convertView.setTag(holder);
			holder.text.setTypeface( Typeface.defaultFromStyle (Typeface.NORMAL) );
			// Bind the data efficiently with the holder.
			holder.text.setText( "¥��: " + (String)listItems.get(position) );
			holder.text.setTextSize( 20 );
			holder.text.setTextColor( 0xFFFF0000 );
			

			
			return convertView;
		}

		private class ViewHolder {
			TextView text;
		}
	}
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent msg) {
		if (keyCode == KeyEvent.KEYCODE_CAMERA) {
			return true;
        } else if (keyCode == KeyEvent.KEYCODE_CALL) {
        	return true;
		} else if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}

		return false;
	}
	private HashMap<String,String> res = new HashMap<String,String>();
	private List<Object> listItems = new ArrayList<Object>(); 
	private ListItemsAdapter adapter = null; 
}