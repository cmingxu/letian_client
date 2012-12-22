package com.letian.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.letian.R;
import com.letian.model.Danyuan;
import com.letian.model.Louge;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-12-22
 * Time: 上午10:58
 * To change this template use File | Settings | File Templates.
 */
public class Baoxiu extends Activity {
    private ListView louge_list_view;
    private ListView danyuan_list_view;
    private ListView louceng_list_view;
    private ListView zhuhu_list_view;
    private Button submit_btn;
    private Button cancel_btn;
    private Button take_pic_btn;
    private ArrayList<Louge> louge_datas;
    private ArrayList<Danyuan> danyuan_datas;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baoxiu_view);
        louge_list_view = (ListView)findViewById(R.id.louge_list_view);
        danyuan_list_view = (ListView)findViewById(R.id.danyuan_list_view);
        louceng_list_view = (ListView)findViewById(R.id.louceng_list_view);
        zhuhu_list_view   = (ListView)findViewById(R.id.zhuhu_list_view);

        louge_datas = Louge.findAll(getApplicationContext());
        ArrayList<String> louges = new ArrayList<String>();
        for (Louge l : louge_datas) {
            louges.add(l.lougemingcheng);
        }
        ArrayAdapter a = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_expandable_list_item_1,
                louges);

        louge_list_view.setAdapter(a);

    }
}