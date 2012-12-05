package com.letian.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.letian.R;
import com.letian.model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-12-1
 * Time: 下午1:16
 * To change this template use File | Settings | File Templates.
 */
public class YezhuSelectorView extends Activity {

    public static final String LOG_TAG = "YezhuSelectorView";
    public YzYfRecord record;

    private ListView yezhu_louge_list_view;
    private ListView yezhu_danyuan_list_view;
    private ListView yezhu_fangjianleixing_list_view;

    private ArrayList<Louge> louge_datas;
    private ArrayList<Danyuan> danyuan_datas;
    private ArrayList<FangjianLeixing> fangjianleixing_datas;


    PopupWindow window ;

    Button submit;
    Button cancel;
    Button takePic;

    EditText reasonText;

    RadioButton okRadio;

    ArrayList<String> danyuans = new ArrayList<String>();
    ArrayList<String> fjlxes = new ArrayList<String>();

    private ProgressDialog progressDialog;
    private Handler handler = new Handler();

    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;


    public void onCreate(Bundle savedInstanceState) {

        this.record = new YzYfRecord(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yezhu_list_view);
        yezhu_louge_list_view = (ListView)findViewById(R.id.yezhu_louge_list_view);
        yezhu_danyuan_list_view = (ListView)findViewById(R.id.yezhu_danyuan_list_view);
        yezhu_fangjianleixing_list_view = (ListView)findViewById(R.id.yezhu_fangjianleixing_list_view);
        louge_datas = Louge.findAll(getApplicationContext());

        ArrayList<String> louges = new ArrayList<String>();
        for(Louge l : louge_datas){
            louges.add(l.lougemingcheng);
        }
        ArrayAdapter a = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_expandable_list_item_1,
                louges);

        yezhu_louge_list_view.setAdapter(a);
        yezhu_louge_list_view.setOnItemClickListener(new LougeOnItemClickListener());
        yezhu_danyuan_list_view.setOnItemClickListener(new DanyuanOnItemClickListener());
        yezhu_fangjianleixing_list_view.setOnItemClickListener(new FangjianLeixingOnItemClickListener());

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

            yezhu_danyuan_list_view.setAdapter(d);
            yezhu_danyuan_list_view.invalidate();

            emptyLisView(yezhu_fangjianleixing_list_view);

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
                record.setFangjianleixing(fjlx.fjmc);
                record.setFangjianleixing_id(fjlx._id);
                if (record.existInDb(YezhuSelectorView.this.getApplicationContext())){
                    fjlxes.add("[验完]" + fjlx.fjmc);
                }
                else{
                    fjlxes.add(fjlx.fjmc);
                }
            }

            ArrayAdapter d = new ArrayAdapter(getApplicationContext(),
                    android.R.layout.simple_expandable_list_item_1,
                    fjlxes);

            yezhu_fangjianleixing_list_view.setAdapter(d);
            yezhu_fangjianleixing_list_view.invalidate();



        }
    }

    private class FangjianLeixingOnItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            record.setFangjianleixing(fangjianleixing_datas.get(i).fjmc);
            record.setFangjianleixing_id(fangjianleixing_datas.get(i)._id);

            popAwindow(yezhu_fangjianleixing_list_view);

        }

    }

    private void popAwindow(View parent) {
            LayoutInflater lay = LayoutInflater.from(this);
            View v = lay.inflate(R.layout.reason_form, null);
            submit = (Button) v.findViewById(R.id.submit);
            cancel = (Button) v.findViewById(R.id.cancel);
            takePic = (Button) v.findViewById(R.id.take_pic);
            TextView navView = (TextView) v.findViewById(R.id.nav);
            navView.setText(record.danyuan + " -  " + record.fangjianleixing);


            reasonText = (EditText)v.findViewById(R.id.reason_text_view);
            submit.setOnClickListener(new SubmitListener());
            okRadio = (RadioButton)v.findViewById(R.id.radioOk);

            cancel.setOnClickListener(new Button.OnClickListener(){

                @Override
                public void onClick(View view) {
                    window.dismiss();
                }
            });
            takePic.setOnClickListener(new TakePicClickListener());
            window = new PopupWindow(v, 500,260);

        window.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg));
        window.setFocusable(true);
        window.update();
        window.showAtLocation(parent, Gravity.CENTER_VERTICAL, 0, 0);
    }


    private class SubmitListener implements Button.OnClickListener{

        @Override
        public void onClick(View view) {
            progressDialog = ProgressDialog.show(YezhuSelectorView.this, "保存中， 请稍候...",
                    null, true);
            record.setResult(okRadio.isChecked());
            record.setReason(reasonText.getText().toString());
            reasonText.setText("");

            new Thread() {
                @Override
                public void run() {
                    try {

                        record.save_to_db();
                        if(record.save_to_server(YezhuSelectorView.this.getApplicationContext()))
                        {
                            handler.post(new Runnable() {
                                public void run() {
                                    new AlertDialog.Builder(YezhuSelectorView.this).setMessage(
                                            "保存成功!").setPositiveButton("Okay",
                                            null).show();

                                }
                            });

                        }
                    } catch (Exception e) {

                        e.printStackTrace();

                        handler.post(new Runnable() {
                            public void run() {
                                new AlertDialog.Builder(YezhuSelectorView.this).setMessage(
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
    private class TakePicClickListener implements Button.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Log.d(SelectorView.LOG_TAG, record.pic_dir());
            File imagesFolder = new File(Environment.getExternalStorageDirectory(),  record.pic_dir());
            imagesFolder.mkdirs();
            File image = new File(imagesFolder, "image_" + (new Random()).nextInt(100) + ".jpg");
            Uri uriSavedImage = Uri.fromFile(image);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

            startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);

        }
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

            } else if (resultCode == RESULT_CANCELED) {

            } else {


            }

        }

    }

}