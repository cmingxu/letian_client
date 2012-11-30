package com.letian.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.letian.Main;
import com.letian.R;
import com.letian.model.*;

import java.util.ArrayList;

import static com.letian.R.color.red;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-23
 * Time: 下午2:23
 * To change this template use File | Settings | File Templates.
 */
public class SelectorView extends Activity {
    public static final String LOG_TAG = "SelectorViewTag";
    public YfRecord record;

    private ListView louge_list_view;
    private ListView danyuan_list_view;
    private ListView fangjianleixing_list_view;
    private ListView yanshouduixiang_list_view;
    private ListView yanshouxiangmu_list_view;

    private ArrayList<Louge> louge_datas;
    private ArrayList<Danyuan> danyuan_datas;
    private ArrayList<FangjianLeixing> fangjianleixing_datas;
    private ArrayList<YanshouDuixiang> yanshouduixiang_datas;
    private ArrayList<YanshouXiangmu>  yanshouXiangmu_datas;

    PopupWindow window ;

    Button submit;
    Button cancel;
    EditText reasonText;

    RadioButton okRadio;
    RadioButton badRadio;

    ArrayList<String> danyuans = new ArrayList<String>();
    ArrayList<String> fjlxes = new ArrayList<String>();
    ArrayList<String> ysdxes = new ArrayList<String>();
    ArrayList<String> ysxms = new ArrayList<String>();

    private ProgressDialog progressDialog;
    private Handler handler = new Handler();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selector_view);
        louge_list_view = (ListView)findViewById(R.id.louge_list_view);
        danyuan_list_view = (ListView)findViewById(R.id.danyuan_list_view);
        fangjianleixing_list_view = (ListView)findViewById(R.id.fangjianleixing_list_view);
        yanshouduixiang_list_view = (ListView)findViewById(R.id.yanshouduixiang_list_view);
        yanshouxiangmu_list_view  = (ListView)findViewById(R.id.yanshouxiangmu_list_view);

        louge_datas = Louge.findAll(getApplicationContext());

        ArrayList<String> louges = new ArrayList<String>();
        for(Louge l : louge_datas){
            louges.add(l.lougemingcheng);
        }
        ArrayAdapter a = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_expandable_list_item_1,
                louges);

        louge_list_view.setAdapter(a);
        louge_list_view.setOnItemClickListener(new LougeOnItemClickListener());
        danyuan_list_view.setOnItemClickListener(new DanyuanOnItemClickListener());
        fangjianleixing_list_view.setOnItemClickListener(new FangjianLeixingOnItemClickListener());
        yanshouduixiang_list_view.setOnItemClickListener(new YanshouduixiangOnItemClickListener());
        yanshouxiangmu_list_view.setOnItemClickListener(new YanshouxiangmuOnItemClickListener());

        this.record = new YfRecord(getApplicationContext());
    }

    private class LougeOnItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            record.setLouge(louge_datas.get(i).lougemingcheng);
            record.setLouge_bh(louge_datas.get(i).lougebianhao);

            danyuan_datas = Danyuan.findAll(getApplicationContext(), "lougebianhao = '" + louge_datas.get(i).lougebianhao + "'");
            danyuans = new ArrayList<String>();
            for(Danyuan danyuan : danyuan_datas){
                danyuans.add(danyuan.danyuanmingcheng + "(" + danyuan.jiange + ")");
            }

            ArrayAdapter d = new ArrayAdapter(getApplicationContext(),
                    android.R.layout.simple_expandable_list_item_1,
                    danyuans);

            danyuan_list_view.setAdapter(d);
            danyuan_list_view.invalidate();


            emptyLisView(fangjianleixing_list_view);
            emptyLisView(yanshouduixiang_list_view);
            emptyLisView(yanshouxiangmu_list_view);

        }
    }

    public void emptyLisView(ListView toBeEmpty){
        ArrayAdapter d = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_expandable_list_item_1,
                new ArrayList<String>());
        toBeEmpty.setAdapter(d);
        toBeEmpty.invalidate();
    }

    private class DanyuanOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            record.setDanyuan(danyuan_datas.get(i).danyuanmingcheng);
            record.setDanyuan_id(danyuan_datas.get(i).danyuanbianhao);

            fangjianleixing_datas = FangjianLeixing.findAllByHuxing(getApplicationContext(), danyuan_datas.get(i).jiange);
            Log.d(SelectorView.LOG_TAG, "" + fangjianleixing_datas.size());
            fjlxes = new ArrayList<String>();
            for(FangjianLeixing fjlx : fangjianleixing_datas){
                fjlxes.add(fjlx.fjmc);
            }

            ArrayAdapter d = new ArrayAdapter(getApplicationContext(),
                    android.R.layout.simple_expandable_list_item_1,
                    fjlxes);

            fangjianleixing_list_view.setAdapter(d);
            fangjianleixing_list_view.invalidate();


            emptyLisView(yanshouduixiang_list_view);
            emptyLisView(yanshouxiangmu_list_view);

       }
    }

    private class FangjianLeixingOnItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            record.setFangjianleixing(fangjianleixing_datas.get(i).fjmc);
            record.setFangjianleixing_id(fangjianleixing_datas.get(i)._id);
            ysdxes = new ArrayList<String>();
            yanshouduixiang_datas = YanshouDuixiang.findAllByFjlxid(getApplicationContext(), String.valueOf(fangjianleixing_datas.get(i)._id));
            for(YanshouDuixiang ysdx : yanshouduixiang_datas){
                ysdxes.add(ysdx.dxmc);
            }

            ArrayAdapter d = new ArrayAdapter(getApplicationContext(),
                    android.R.layout.simple_expandable_list_item_1,
                    ysdxes);

            yanshouduixiang_list_view.setAdapter(d);
            yanshouduixiang_list_view.invalidate();

            emptyLisView(yanshouxiangmu_list_view);
        }

    }

    private class YanshouduixiangOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            record.setShoulouduixiang(yanshouduixiang_datas.get(i).dxmc);
            record.setShoulouduixiang_id(yanshouduixiang_datas.get(i)._id);
            ysxms = new ArrayList<String>();
            yanshouXiangmu_datas = YanshouXiangmu.findAllByYsdxid(getApplicationContext(), String.valueOf(yanshouduixiang_datas.get(i)._id));
            for(YanshouXiangmu ysxm : yanshouXiangmu_datas){
                ysxms.add(ysxm.xmmc);
            }

            ArrayAdapter yxa = new ArrayAdapter(getApplicationContext(),
                    android.R.layout.simple_expandable_list_item_1,
                    ysdxes);

            yanshouxiangmu_list_view.setAdapter(yxa);

            for(YanshouXiangmu ysxm : yanshouXiangmu_datas){
                record.shoulouxiangmu = ysxm.xmmc;
                record.shoulouxiangmu_id = ysxm._id;
                Log.d(SelectorView.LOG_TAG, " " + record.existInDb(SelectorView.this.getApplicationContext()));
                if (record.existInDb(SelectorView.this.getApplicationContext())){

                }
                ysxms.add(ysxm.xmmc);
            }
            record.shoulouxiangmu = null;
            record.shoulouxiangmu_id = null;
            yanshouxiangmu_list_view.invalidate();
        }

    }

    private class YanshouxiangmuOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            record.setShoulouxiangmu(yanshouXiangmu_datas.get(i).xmmc);
            record.setShoulouxiangmu_id(yanshouXiangmu_datas.get(i)._id);

            popAwindow(yanshouduixiang_list_view);

        }
    }


    private void popAwindow(View parent) {
        if (window == null) {
            LayoutInflater lay = LayoutInflater.from(this);
            View v = lay.inflate(R.layout.reason_form, null);
            submit = (Button) v.findViewById(R.id.submit);
            cancel = (Button) v.findViewById(R.id.cancel);

            reasonText = (EditText)v.findViewById(R.id.reason_text_view);
            submit.setOnClickListener(new SubmitListener());
            okRadio = (RadioButton)v.findViewById(R.id.radioOk);

            cancel.setOnClickListener(new Button.OnClickListener(){

                @Override
                public void onClick(View view) {
                    window.dismiss();
                }
            });
            window = new PopupWindow(v, 500,260);
        }

        window.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg));
        window.setFocusable(true);
        window.update();
        window.showAtLocation(parent, Gravity.CENTER_VERTICAL, 0, 0);
    }


    private class SubmitListener implements Button.OnClickListener{

        @Override
        public void onClick(View view) {
            progressDialog = ProgressDialog.show(SelectorView.this, "保存中， 请稍候...",
                    null, true);
            record.setResult(okRadio.isChecked());
            record.setReason(reasonText.getText().toString());
            reasonText.setText("");

            new Thread() {
                @Override
                public void run() {
                    try {

                        record.save_to_db();
                        if(record.save_to_server(SelectorView.this.getApplicationContext()))
                        {
                            handler.post(new Runnable() {
                                public void run() {
                                    new AlertDialog.Builder(SelectorView.this).setMessage(
                                            "保存成功!").setPositiveButton("Okay",
                                            null).show();

                                }
                            });

                        }
                    } catch (Exception e) {

                        e.printStackTrace();

                        handler.post(new Runnable() {
                            public void run() {
                                new AlertDialog.Builder(SelectorView.this).setMessage(
                                        "网络好像不太给力, 稍后尝试").setPositiveButton("Okay",
                                        null).show();

                            }
                        });

                    }
                    progressDialog.dismiss();
                }
            }.start();
            window.dismiss();
        }
    }

    private class YanshouXiangmuAdapter extends BaseAdapter{
        ArrayList<YanshouXiangmu> ysxms;
        private Context context;

        private YanshouXiangmuAdapter(Context ctx, ArrayList<YanshouXiangmu> ysxms) {
            this.context = ctx;
            this.ysxms = ysxms;

        }

        @Override
        public int getCount() {
            return ysxms.size();
        }

        @Override
        public Object getItem(int i) {
            return ysxms.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView tv = (TextView) SelectorView.this.findViewById(android.R.layout.simple_expandable_list_item_1);
            tv.setText(ysxms.get(i).xmmc);
            return  tv;
        }
    }

}
