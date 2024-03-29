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
    private ArrayList<YanshouXiangmu> yanshouXiangmu_datas;


    PopupWindow window;

    Button submit;
    Button cancel;
    Button takePic;

    EditText reasonText;



    ArrayList<String> danyuans = new ArrayList<String>();
    ArrayList<String> fjlxes = new ArrayList<String>();
    ArrayList<String> ysdxes = new ArrayList<String>();
    ArrayList<String> ysxms = new ArrayList<String>();

    private ProgressDialog progressDialog;
    private Handler handler = new Handler();

    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;


    public void onCreate(Bundle savedInstanceState) {

        this.record = new YfRecord(getApplicationContext());
        record.kfs_or_yz = getIntent().getStringExtra("kfs_or_yz");

        Log.d(SelectorView.LOG_TAG, record.kfs_or_yz)      ;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selector_view);
        if(record.isKfs())
        this.setTitle(getResources().getString(R.string.title) + " -  开发商");
        else{
        this.setTitle(getResources().getString(R.string.title) + " -   业主");
        }
        louge_list_view = (ListView) findViewById(R.id.louge_list_view);
        danyuan_list_view = (ListView) findViewById(R.id.danyuan_list_view);
        fangjianleixing_list_view = (ListView) findViewById(R.id.fangjianleixing_list_view);
        yanshouduixiang_list_view = (ListView) findViewById(R.id.yanshouduixiang_list_view);
        yanshouxiangmu_list_view = (ListView) findViewById(R.id.yanshouxiangmu_list_view);

        louge_datas = Louge.findAll(getApplicationContext());

        ArrayList<String> louges = new ArrayList<String>();
        for (Louge l : louge_datas) {
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

    }

    private class LougeOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            record.setLouge(louge_datas.get(i).lougemingcheng);
            record.setLouge_bh(louge_datas.get(i).lougebianhao);

            danyuan_datas = Danyuan.findAll(getApplicationContext(), "lougebianhao = '" + louge_datas.get(i).lougebianhao + "'");
            danyuans = new ArrayList<String>();
            for (Danyuan danyuan : danyuan_datas) {
                danyuans.add(danyuan.danyuanmingcheng + "(" + danyuan.jiange + ")");
            }

            ArrayAdapter d = new ArrayAdapter(getApplicationContext(),
                    android.R.layout.simple_expandable_list_item_1,
                    danyuans);

            danyuan_list_view.setAdapter(d);
            emptyLisView(fangjianleixing_list_view);
            emptyLisView(yanshouduixiang_list_view);
            emptyLisView(yanshouxiangmu_list_view);

            highLightView(louge_list_view, l);

        }
    }

    public void emptyLisView(ListView toBeEmpty) {
        ArrayAdapter d = new ArrayAdapter(getApplicationContext(),
                android.R.layout.simple_expandable_list_item_1,
                new ArrayList<String>());
        toBeEmpty.setAdapter(d);
        toBeEmpty.invalidate();
    }

        private class DanyuanOnItemClickListener implements AdapterView.OnItemClickListener {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                record.setDanyuan(danyuan_datas.get(i).danyuanmingcheng);
                record.setDanyuan_id(Integer.toString(danyuan_datas.get(i)._id));
                record.setDanyuan_bh(danyuan_datas.get(i).danyuanbianhao);
                record.setHuxing(danyuan_datas.get(i).jiange);

                fangjianleixing_datas = FangjianLeixing.findAllByHuxing(getApplicationContext(), danyuan_datas.get(i).jiange);
                fjlxes = new ArrayList<String>();
                for (FangjianLeixing fjlx : fangjianleixing_datas) {
                    fjlxes.add(fjlx.fjmc);
                }

                ArrayAdapter d = new ArrayAdapter(getApplicationContext(),
                        android.R.layout.simple_expandable_list_item_1,
                        fjlxes);

                fangjianleixing_list_view.setAdapter(d);


                emptyLisView(yanshouduixiang_list_view);
                emptyLisView(yanshouxiangmu_list_view);

                highLightView(danyuan_list_view, l);
            }
    }

    private class FangjianLeixingOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            record.setFangjianleixing(fangjianleixing_datas.get(i).fjmc);
            record.setFangjianleixing_id(fangjianleixing_datas.get(i)._id);

            ysdxes = new ArrayList<String>();
            yanshouduixiang_datas = YanshouDuixiang.findAllByFjlxid(getApplicationContext(), String.valueOf(fangjianleixing_datas.get(i)._id));
            for (YanshouDuixiang ysdx : yanshouduixiang_datas) {
                ysdxes.add(ysdx.dxmc);
            }

            ArrayAdapter d = new ArrayAdapter(getApplicationContext(),
                    android.R.layout.simple_expandable_list_item_1,
                    ysdxes);

            yanshouduixiang_list_view.setAdapter(d);

            emptyLisView(yanshouxiangmu_list_view);

            fillDb(fangjianleixing_datas.get(i)._id);

            highLightView(fangjianleixing_list_view, l);
        }

    }

    private class YanshouduixiangOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            record.setShoulouduixiang(yanshouduixiang_datas.get(i).dxmc);
            record.setShoulouduixiang_id(yanshouduixiang_datas.get(i)._id);

            ysxms = new ArrayList<String>();
            yanshouXiangmu_datas = YanshouXiangmu.findAllByYsdxid(getApplicationContext(), String.valueOf(yanshouduixiang_datas.get(i)._id));
            for (YanshouXiangmu ysxm : yanshouXiangmu_datas) {
                record.shoulouxiangmu = ysxm.xmmc;
                record.shoulouxiangmu_id = ysxm._id;
                if (record.existInDb(SelectorView.this.getApplicationContext())) {
                    if (record.hasProblem) {
                        ysxms.add("[不合格]" + ysxm.xmmc);
                    } else {
                        ysxms.add("[合格]" + ysxm.xmmc);

                    }
                } else {
                    ysxms.add(ysxm.xmmc);
                }
            }

            ArrayAdapter yxa = new ArrayAdapter(getApplicationContext(),
                    android.R.layout.simple_expandable_list_item_1,
                    ysxms);


            yanshouxiangmu_list_view.setAdapter(yxa);

            highLightView(yanshouduixiang_list_view, l);

        }

    }

    private class YanshouxiangmuOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            record.setShoulouxiangmu(yanshouXiangmu_datas.get(i).xmmc);
            record.setShoulouxiangmu_id(yanshouXiangmu_datas.get(i)._id);

            highLightView(yanshouxiangmu_list_view, l);
            popAwindow(yanshouduixiang_list_view);

        }
    }


    private void popAwindow(View parent) {

        LayoutInflater lay = LayoutInflater.from(this);
        View v = lay.inflate(R.layout.reason_form, null);
        TextView navView = (TextView) v.findViewById(R.id.nav);
        navView.setText(record.danyuan + " -  " + record.fangjianleixing + " - " + record.shoulouduixiang + " - " + record.shoulouxiangmu);
        submit = (Button) v.findViewById(R.id.submit);
        cancel = (Button) v.findViewById(R.id.cancel);
        takePic = (Button) v.findViewById(R.id.take_pic);


        reasonText = (EditText) v.findViewById(R.id.reason_text_view);
        submit.setOnClickListener(new SubmitListener());


        cancel.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                window.dismiss();
            }
        });
        takePic.setOnClickListener(new TakePicClickListener());
        window = new PopupWindow(v, 1280, 700);

        if (record.existInDb(SelectorView.this.getApplicationContext())) {
            reasonText.setText(record.reason);
        }

        window.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg));
        window.setFocusable(true);
        window.update();
        window.showAtLocation(parent, Gravity.CENTER_VERTICAL, 0, 30);
    }


    private class SubmitListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            progressDialog = ProgressDialog.show(SelectorView.this, "保存中， 请稍候...",
                    null, true);
//            assign id if exist in db
            record.hasProblem = true;
            record.setReason(reasonText.getText().toString());
            reasonText.setText("");

            new Thread() {
                @Override
                public void run() {
                    try {
                        record.save_to_db();
                        if (record.save_to_server(SelectorView.this.getApplicationContext())) {
                            handler.post(new Runnable() {
                                public void run() {

                                    new AlertDialog.Builder(SelectorView.this).setMessage(
                                            "保存成功!").setPositiveButton("Okay",
                                            null).show();

                                }
                            });

                        } else {
                            record.update_save_status(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        record.update_save_status(false);
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(SelectorView.this.getApplicationContext(), "网络有问题， 稍后在【设置】中上传!", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                    progressDialog.dismiss();
                }
            }.start();
            window.dismiss();
        }
    }

    private class YanshouXiangmuAdapter extends BaseAdapter {
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
            return tv;
        }
    }

    private class TakePicClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File imagesFolder = new File(Environment.getExternalStorageDirectory(), record.pic_dir());
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


    public void highLightView(ListView view, long l) {
//        view.getChildAt((int)l).setBackgroundColor(R.color.red);

    }


    public void fillDb(final String fjlxid) {

        progressDialog = ProgressDialog.show(SelectorView.this, "请稍候...",
                null, true);
        new Thread() {
            @Override
            public void run() {
                for (YanshouDuixiang ysdx : YanshouDuixiang.findAllByFjlxid(getApplicationContext(), String.valueOf(fjlxid))) {
                    for (YanshouXiangmu ysxm : YanshouXiangmu.findAllByYsdxid(getApplicationContext(), String.valueOf(ysdx._id))) {
                        YfRecord r = new YfRecord(SelectorView.this.getApplicationContext());
                        r.louge = record.louge;
                        r.louge_bh = record.louge_bh;
                        r.danyuan = record.danyuan;
                        r.danyuan_bh = record.danyuan_bh;
                        r.danyuan_id = record.danyuan_id;
                        r.huxing = record.huxing;
                        r.fangjianleixing = record.fangjianleixing;
                        r.fangjianleixing_id = record.fangjianleixing_id;
                        r.shoulouduixiang = ysdx.dxmc;
                        r.shoulouduixiang_id = ysdx._id;
                        r.shoulouxiangmu = ysxm.xmmc;
                        r.shoulouxiangmu_id = ysxm._id;
                        r.reason = "";
                        r.hasProblem = false;
                        r.saved = false;
                        r.kfs_or_yz = record.kfs_or_yz;
                        if (r.existInDb(SelectorView.this.getApplicationContext())) {
                        } else {
                            r.save_to_db();
                            r.update_save_status(false);
                        }
                    }
                }

                progressDialog.dismiss();
            }
        }.start();

    }

}
