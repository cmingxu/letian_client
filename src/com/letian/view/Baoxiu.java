package com.letian.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.*;
import com.letian.R;
import com.letian.model.Danyuan;
import com.letian.model.Louge;
import com.letian.model.User;
import com.letian.model.Weixiudan;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

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
    private EditText reasonText;
    private Button submit_btn;
    private Button cancel_btn;
    private Button take_pic_btn;
    private ArrayList<Louge> louge_datas;
    private ArrayList<Danyuan> louceng_datas;

    private ArrayList<Danyuan> zhuhu_datas;
    private String selected_louge_bianhao;
    private String selected_loucengmingcheng;
    private String selected_zhuhumingcheng;
    private String selected_danyuanbianhao;
    PopupWindow window;
    private ProgressDialog progressDialog;
    private Handler handler = new Handler();

    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baoxiu_view);
        this.setTitle(getResources().getString(R.string.title) + " -  报修");
        louge_list_view = (ListView)findViewById(R.id.louge_list_view);
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

        louge_list_view.setOnItemClickListener(new LougeItemOnClick());
        louceng_list_view.setOnItemClickListener(new LoucengItemOnClick());
        zhuhu_list_view.setOnItemClickListener(new ZhuhuItemOnClick());


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

            selected_zhuhumingcheng = zhuhu_datas.get((int)l).danyuanmingcheng;
            selected_danyuanbianhao =     zhuhu_datas.get((int)l).danyuanbianhao;
            LayoutInflater lay = LayoutInflater.from(Baoxiu.this);
            View v = lay.inflate(R.layout.reason_form, null);
            TextView navView = (TextView) v.findViewById(R.id.nav);
            navView.setText(selected_louge_bianhao + "/" + selected_loucengmingcheng + "/" + selected_zhuhumingcheng);

            submit_btn = (Button) v.findViewById(R.id.submit);
            cancel_btn = (Button) v.findViewById(R.id.cancel);
            take_pic_btn = (Button) v.findViewById(R.id.take_pic);


            reasonText = (EditText) v.findViewById(R.id.reason_text_view);
            submit_btn.setOnClickListener(new SubmitItemOnClick());


            cancel_btn.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View view) {
                    window.dismiss();
                }
            });
            take_pic_btn.setOnClickListener(new TakePicItemOnClick());
            window = new PopupWindow(v, 1280, 700);



            window.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg));
            window.setFocusable(true);
            window.update();
            window.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 30);
        }
    }

    private class SubmitItemOnClick implements View.OnClickListener{


        @Override
        public void onClick(View view) {
            if(reasonText.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(Baoxiu.this.getApplicationContext(), "请填写报修内容", Toast.LENGTH_LONG ).show();
                return;
            }



            progressDialog = ProgressDialog.show(Baoxiu.this, "保存中， 请稍候...",
                    null, true);
            new Thread() {
                @Override
                public void run() {
                    try {
                        Weixiudan w = new Weixiudan(Baoxiu.this.getApplicationContext());
                        w.shoudanren = User.current_user.name;
                        w.suoshulouge = selected_louge_bianhao;
                        w.shoudanshijian = (new Date()).toString();
                        w.danyuanbianhao = selected_danyuanbianhao;
                        w.weixiudanyuan = selected_zhuhumingcheng;
                        w.wufuneirong  = reasonText.getText().toString();
                        w.save_into_db();
                        if (w.save_to_server()) {
                            handler.post(new Runnable() {
                                public void run() {

                                    new AlertDialog.Builder(Baoxiu.this).setMessage(
                                            "保存成功!").setPositiveButton("Okay",
                                            null).show();

                                }
                            });

                        } else {
                            w.update_syned();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(Baoxiu.this.getApplicationContext(), "网络有问题， 稍后在【设置】中上传!", Toast.LENGTH_LONG).show();



                            }
                        });

                    }
                    progressDialog.dismiss();
                }
            }.start();
            window.dismiss();
        }
    }
    private class TakePicItemOnClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File imagesFolder = new File(Environment.getExternalStorageDirectory(), "letian_images/baoxiu/" + selected_louge_bianhao + "/" + selected_loucengmingcheng + "/" + selected_zhuhumingcheng);
            imagesFolder.mkdirs();
            File image = new File(imagesFolder, "image_" + (new Random()).nextInt(100) + ".jpg");
            Uri uriSavedImage = Uri.fromFile(image);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

            startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
        }
    }

}