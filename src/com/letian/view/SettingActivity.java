package com.letian.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.letian.R;
import com.letian.lib.NetworkConnection;
import com.letian.model.Danyuan;
import com.letian.model.Louge;
import com.letian.model.Loupan;
import com.letian.model.Zhuhu;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-20
 * Time: 下午9:04
 * To change this template use File | Settings | File Templates.
 */
public class SettingActivity extends Activity {
    public static final String LOG_TAG = "SettingActivity";

    private Button syncBtn;
    ProgressDialog progressDialog;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.setting);
        syncBtn = (Button)findViewById(R.id.sync_xml_btn);
        syncBtn.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                tongbu();
            }
        });
    }



    private void tongbu() {
        progressDialog = ProgressDialog.show(SettingActivity.this, this.getResources().getString(R.string.sync_notice), null, true);
        new Thread() {
            @Override
            public void run() {

                if (!NetworkConnection.getInstance(
                        SettingActivity.this.getApplicationContext())
                        .isNetworkAvailable()) {
                    Log.e(SettingActivity.LOG_TAG, "network can not be reach");
                    handler.post(new Runnable(){

                        @Override
                        public void run() {
                            new AlertDialog.Builder(SettingActivity.this)
                                    .setMessage(SettingActivity.this.getResources().getString(R.string.network_connection_problem)
                                    )
                                    .setPositiveButton("Okay", null)
                                    .show();

                        }

                    });



                }else{
                    get_data_from_server();

                }

                progressDialog.dismiss();

            }
        }.start();
    }


    public void get_data_from_server() {

        Danyuan.syn(SettingActivity.this.getApplicationContext());
        Louge.syn(SettingActivity.this.getApplicationContext());
        Loupan.syn(SettingActivity.this.getApplicationContext());
        Zhuhu.syn(SettingActivity.this.getApplicationContext());
    }


}
