package com.letian.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.letian.R;
import com.letian.lib.LocalAccessor;
import com.letian.lib.NetworkConnection;
import com.letian.model.*;
import com.letian.view.CustomOnClickListener;
import com.letian.view.SelectorView;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-20
 * Time: 下午9:04
 * To change this template use File | Settings | File Templates.
 */
public class SettingActivity extends BaseActivity {
    public static final String LOG_TAG = "SettingActivity";

    private Button syncBtn;
    private Button backBtn;

    private Button drop_button;
    private Button upload_kfs_yfd;
    private Button upload_yz_yfd;
    private Button upload_weixiudan;


    ProgressDialog progressDialog;
    Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        handler = new Handler();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        this.setTitle(getResources().getString(R.string.title) + " -  设置");
        syncBtn = (Button) findViewById(R.id.sync_xml_btn);
        backBtn = (Button) findViewById(R.id.setting_back);
        syncBtn.setOnClickListener(new ActionListener(new TongbuListener(), "确认要同步， 注意同步前需清空数据库!"));


        drop_button = (Button) findViewById(R.id.drop_button);

        upload_kfs_yfd = (Button) findViewById(R.id.upload_kfs_yfd);
        upload_yz_yfd = (Button) findViewById(R.id.upload_yz_yfd);
        upload_weixiudan = (Button) findViewById(R.id.upload_weixiudan);
        upload_kfs_yfd.setOnClickListener(new ActionListener(new UploadKfsYfdClickListener(), "确认要上传开发商验房单？"));

        upload_yz_yfd.setOnClickListener(new ActionListener(new UploadYzYfdClickListener(), "确认要上传业主验房单?"));
        upload_weixiudan.setOnClickListener(new ActionListener(new UploadWeixiudanClickListener(), "确认要上传维修单单?"));


        drop_button.setOnClickListener(new ActionListener(new DropButtonListener(), "确认要删除本机上的所有数据库表？"));


        backBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setClass(SettingActivity.this, Main.class);
                SettingActivity.this.startActivity(i);
                SettingActivity.this.finish();
            }
        });
    }

    private class ActionListener implements View.OnClickListener {
        private String notice;
        private CustomOnClickListener listener;

        private ActionListener(CustomOnClickListener listener, String notice) {
            this.listener = listener;
            this.notice = notice;
        }

        @Override
        public void onClick(final View view) {
            new AlertDialog.Builder(SettingActivity.this).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    listener.onClick(view);
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).setTitle(notice).show();
        }
    }


    private class TongbuListener implements CustomOnClickListener {
        public void onClick(View view) {
            progressDialog = ProgressDialog.show(SettingActivity.this, SettingActivity.this.getResources().getString(R.string.sync_notice), null, true);
            new Thread() {
                @Override
                public void run() {
                    if (!NetworkConnection.getInstance(
                            SettingActivity.this.getApplicationContext())
                            .isNetworkAvailable()) {
                        Log.e(SettingActivity.LOG_TAG, "network can not be reach");
                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                new AlertDialog.Builder(SettingActivity.this)
                                        .setMessage(SettingActivity.this.getResources().getString(R.string.network_connection_problem)
                                        )
                                        .setPositiveButton("Okay", null)
                                        .show();

                            }

                        });
                    } else {
                        get_data_from_server();

                    }

                    progressDialog.dismiss();

                }
            }.start();
        }

        public void get_data_from_server() {
            Context context = getApplication();
            try {
                User.syn(context);
                Louge.syn(context);
                Danyuan.syn(context);
                FangjianLeixing.syn(context);
                FangjianleixingYanshouduixiang.syn(context);
                Huxing.syn(context);
                HuxingFangjianLeixing.syn(context);
                YanshouDuixiang.syn(context);
                YanshouXiangmu.syn(context);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("", e.toString());
                progressDialog.dismiss();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SettingActivity.this.getApplicationContext(),
                                "同步出错， 请检查网络！", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

    }


    private class DropButtonListener implements CustomOnClickListener {

        @Override
        public void onClick(View arg0) {
            SQLiteDatabase db = LocalAccessor.getInstance(
                    SettingActivity.this.getApplicationContext()).openDB();
            db.execSQL("drop table  if exists " + User.TABLE_NAME + ";");
            db.execSQL("drop table  if exists " + Danyuan.TABLE_NAME + ";");
            db.execSQL("drop table  if exists " + Louge.TABLE_NAME + ";");
            db.execSQL("drop table  if exists " + FangjianLeixing.TABLE_NAME + ";");
            db.execSQL("drop table  if exists " + Huxing.TABLE_NAME + ";");
            db.execSQL("drop table  if exists " + FangjianleixingYanshouduixiang.TABLE_NAME + ";");
            db.execSQL("drop table  if exists " + HuxingFangjianLeixing.TABLE_NAME + ";");
            db.execSQL("drop table  if exists " + YanshouXiangmu.TABLE_NAME + ";");
            db.execSQL("drop table  if exists " + YanshouDuixiang.TABLE_NAME + ";");
            db.execSQL("drop table  if exists " + YfRecord.TABLE_NAME + ";");
            db.execSQL("drop table  if exists " + Weixiudan.TABLE_NAME + ";");
            db.close();
            Toast.makeText(SettingActivity.this.getApplicationContext(), "数据库已经清空， 请重新同步", Toast.LENGTH_LONG).show();

        }
    }

    private class UploadKfsYfdClickListener implements CustomOnClickListener {

        @Override
        public void onClick(View view) {
            progressDialog = ProgressDialog.show(SettingActivity.this, "保存中， 请稍候...",
                    null, true);


            new Thread() {
                @Override
                public void run() {
                    try {

                        for (YfRecord record : YfRecord.findAll(SettingActivity.this.getApplicationContext(), "save_to_server = 0 and kfs_or_yz = 'kfs'")) {
                            Log.d(SelectorView.LOG_TAG, record.shoulouxiangmu_id);
                            Log.d(SelectorView.LOG_TAG, Boolean.toString(record.saved));

                            if (record.save_to_server(SettingActivity.this.getApplicationContext())) {
                                record.update_save_status(true);
                            }

                        }
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(SettingActivity.this.getApplicationContext(), "保存成功!", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();

                        Log.e("aaaaaaaaaaaaaaa", e.toString());
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(SettingActivity.this.getApplicationContext(), "网络有问题， 稍后重试!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    progressDialog.dismiss();
                }
            }.start();

        }
    }

    private class UploadYzYfdClickListener implements CustomOnClickListener {

        @Override
        public void onClick(View view) {
            progressDialog = ProgressDialog.show(SettingActivity.this, "保存中， 请稍候...",
                    null, true);


            new Thread() {
                @Override
                public void run() {
                    try {

                        for (YfRecord record : YfRecord.findAll(SettingActivity.this.getApplicationContext(), "save_to_server = 0 and kfs_or_yz = 'yz'")) {
                            Log.d(SelectorView.LOG_TAG, Boolean.toString(record.saved));
                            if (record.save_to_server(SettingActivity.this.getApplicationContext())) {
                                record.update_save_status(true);

                            }
                        }

                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(SettingActivity.this.getApplicationContext(), "保存成功!", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(SettingActivity.this.getApplicationContext(), "网络有问题， 稍后重试!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    progressDialog.dismiss();
                }
            }.start();

        }
    }


    private class UploadWeixiudanClickListener implements CustomOnClickListener {

        @Override
        public void onClick(View view) {
            progressDialog = ProgressDialog.show(SettingActivity.this, "保存中， 请稍候...",
                    null, true);


            new Thread() {
                @Override
                public void run() {
                    try {

                        for (Weixiudan record : Weixiudan.findAll(SettingActivity.this.getApplicationContext(), "is_synced = 0")) {

                            if (record.save_to_server()) {
                                record.update_syned();

                            }
                        }

                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(SettingActivity.this.getApplicationContext(), "保存成功!", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(SettingActivity.this.getApplicationContext(), "网络有问题， 稍后重试!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    progressDialog.dismiss();
                }
            }.start();

        }
    }

}
