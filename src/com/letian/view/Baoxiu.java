package com.letian.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
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
        private ListView louceng_list_view;
    private ListView zhuhu_list_view;
    private EditText content;
    private Button submit_btn;
    private Button cancel_btn;
    private Button take_pic_btn;
    private ArrayList<Louge> louge_datas;
    private ArrayList<Danyuan> louceng_datas;

    private ArrayList<Danyuan> zhuhu_datas;
    private String selected_louge_bianhao;
    private String selected_loucengmingcheng;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baoxiu_view);
        louge_list_view = (ListView)findViewById(R.id.louge_list_view);
        louceng_list_view = (ListView)findViewById(R.id.louceng_list_view);
        zhuhu_list_view   = (ListView)findViewById(R.id.zhuhu_list_view);
        submit_btn = (Button)findViewById(R.id.submit);
        take_pic_btn = (Button)findViewById(R.id.take_pic);
        cancel_btn = (Button)findViewById(R.id.cancel);

        louge_datas = Louge.findAll(getApplicationContext());
        ArrayList<String> louges = new ArrayList<String>();
        for (Louge l : louge_datas) {
            louges.add(l.lougemingcheng);
        }
        ArrayAdapter a = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_expandable_list_item_1,
                louges);

        louge_list_view.setAdapter(a);

        louge_list_view.setOnItemClickListener(new LougeItemOnClick());
        louceng_list_view.setOnItemClickListener(new LoucengItemOnClick());
        zhuhu_list_view.setOnItemClickListener(new ZhuhuItemOnClick());
        submit_btn.setOnClickListener(new SubmitItemOnClick());
        cancel_btn.setOnClickListener(new CancelItemOnClick());
        take_pic_btn.setOnClickListener(new TakePicItemOnClick());

    }

   private class LougeItemOnClick implements AdapterView.OnItemClickListener{

       @Override
       public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
           louceng_datas = Danyuan.distinct_louceng(Baoxiu.this.getApplicationContext(),louge_datas.get((int)l).lougebianhao);
           selected_louge_bianhao = louge_datas.get((int)l).lougebianhao;
           ArrayList<String> loucengmingchengs = new ArrayList<String>();
           for(Danyuan dy : louceng_datas){
               if (!loucengmingchengs.contains(dy.loucengmingcheng)){
                   loucengmingchengs.add(dy.loucengmingcheng);
                   Log.d(SelectorView.LOG_TAG, dy.loucengmingcheng);
               }
           }

           ArrayAdapter a = new ArrayAdapter(getApplicationContext(),
                   android.R.layout.simple_expandable_list_item_1,
                   loucengmingchengs);

           louceng_list_view.setAdapter(a);
           louceng_list_view.invalidate();
       }
   }



    private class LoucengItemOnClick implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            zhuhu_datas = Danyuan.findAll(Baoxiu.this.getApplicationContext(), "lougebianhao = '" + selected_louge_bianhao +
                    "' and loucengmingcheng = '" + louceng_datas.get((int)l).loucengmingcheng + "'");
            Log.d(SelectorView.LOG_TAG, "" + l);
            Log.d(SelectorView.LOG_TAG, "" + (int)l);
            Log.d(SelectorView.LOG_TAG, "" +  louceng_datas.get((int)l).loucengmingcheng);
            selected_loucengmingcheng =   louceng_datas.get((int)l).loucengmingcheng;
            ArrayList<String> zhuhus = new ArrayList<String>();
            for(Danyuan dy : zhuhu_datas){
                if (!zhuhus.contains(dy.danyuanmingcheng )){
                    zhuhus.add(dy.danyuanmingcheng );
                    Log.d(SelectorView.LOG_TAG, dy.danyuanmingcheng );
                }
            }

            ArrayAdapter a = new ArrayAdapter(getApplicationContext(),
                    android.R.layout.simple_expandable_list_item_1,
                    zhuhus);

            zhuhu_list_view.setAdapter(a);
            zhuhu_list_view.invalidate();
        }
    }

    private class ZhuhuItemOnClick implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String lougebianhao = "";
            Danyuan.findAll(Baoxiu.this.getApplicationContext(), "lougebianhao = '" + "'");
        }
    }

    private class SubmitItemOnClick implements View.OnClickListener{


        @Override
        public void onClick(View view) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
    private class TakePicItemOnClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    private class CancelItemOnClick implements View.OnClickListener{


        @Override
        public void onClick(View view) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }


}