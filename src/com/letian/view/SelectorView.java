package com.letian.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.letian.R;
import com.letian.model.Danyuan;
import com.letian.model.Louge;
import com.letian.model.Model;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-23
 * Time: 下午2:23
 * To change this template use File | Settings | File Templates.
 */
public class SelectorView extends Activity {
    public static final String LOG_TAG = "SelectorViewTag";

    private LinearLayout container;
    private int width;
    private int height;

    private ListView louge_list_view;
    private ListView danyuan_list_view;
    private ListView shoulouxiangmu_list_view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selector_view);
        louge_list_view = (ListView)findViewById(R.id.louge_list_view);
        danyuan_list_view = (ListView)findViewById(R.id.danyuan_list_view);
//        container = (LinearLayout)findViewById(R.id.selector_view_container);
        width = getWindowManager().getDefaultDisplay().getWidth();
        height = getWindowManager().getDefaultDisplay().getHeight();


        ArrayList<String> datas = new ArrayList<String>();
        for(Louge l : Louge.findAll(getApplicationContext())){
            datas.add(l.lougemingcheng);
        }
        ArrayAdapter a = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_expandable_list_item_1,
                datas);

        Log.d(SelectorView.LOG_TAG, "" + datas.size());

        louge_list_view.setAdapter(a);


        ArrayList<String> danyuans = new ArrayList<String>();
        for(Danyuan l : Danyuan.findAll(getApplicationContext())){
            datas.add(l.danyuanmingcheng);
        }
        ArrayAdapter d = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_expandable_list_item_1,
                danyuans);

        Log.d(SelectorView.LOG_TAG, "" + datas.size());

        danyuan_list_view.setAdapter(d);


//        container.addView(danyuan_list_view);
    }
}