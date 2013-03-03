package com.letian.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.letian.R;
import com.letian.model.Model;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-22
 * Time: 下午10:18
 * To change this template use File | Settings | File Templates.
 */
public class ListItem extends Activity {
    private String tableToDisplay;
    private ListView list_view;
    private ArrayList<String> dataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item);
        tableToDisplay = getIntent().getStringExtra("tableToDisplay");
        list_view = (ListView)findViewById(R.id.custom_list_view);


        dataList = Model.inArrayList(getApplicationContext(), tableToDisplay);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                dataList
        ) ;

        list_view.setAdapter(adapter);
    }


}
