package com.letian.controller;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.letian.R;
import com.letian.view.*;

public class Main extends BaseActivity {
	ImageView logout_view;
    ImageView kaifashang_shoulou_view;
    ImageView setting_view;
    ImageView yezhu_shoulou_view;
    ImageView baoxiu_view;

    ImageView coming_soon_view_4;
    ImageView coming_soon_view_5;
    ImageView coming_soon_view_6;
    ImageView coming_soon_view_8;
    ImageView coming_soon_view_9;
    ImageView coming_soon_view_10;
    ImageView coming_soon_view_11;
    ImageView coming_soon_view_12;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.setTitle(getResources().getString(R.string.title));
		logout_view = (ImageView) findViewById(R.id.logout);
		logout_view.setOnClickListener(new LogoutListener());

        setting_view = (ImageView) findViewById(R.id.setting_view);
        setting_view.setOnClickListener(new SettingListener());

        baoxiu_view   = (ImageView)findViewById(R.id.baoxiu);
        kaifashang_shoulou_view = (ImageView)findViewById(R.id.kaifashang_shoulou_view);
        setting_view = (ImageView)findViewById(R.id.setting_view);
        yezhu_shoulou_view = (ImageView)findViewById(R.id.yezhu_shouloou_view);
        coming_soon_view_4 = (ImageView)findViewById(R.id.coming_soon_view_4);
        coming_soon_view_5 = (ImageView)findViewById(R.id.coming_soon_view_5);
        coming_soon_view_6 = (ImageView)findViewById(R.id.coming_soon_view_6);
        coming_soon_view_8 = (ImageView)findViewById(R.id.coming_soon_view_8);
        coming_soon_view_9 = (ImageView)findViewById(R.id.coming_soon_view_9);
        coming_soon_view_10 = (ImageView)findViewById(R.id.coming_soon_view_10);
        coming_soon_view_11 = (ImageView)findViewById(R.id.coming_soon_view_11);
        coming_soon_view_12 = (ImageView)findViewById(R.id.coming_soon_view_12);


        coming_soon_view_4.setOnClickListener(new TestListener());

        coming_soon_view_6.setOnClickListener(new ComingSoonListener());
        coming_soon_view_8.setOnClickListener(new ComingSoonListener());
        coming_soon_view_9.setOnClickListener(new ComingSoonListener());
        coming_soon_view_10.setOnClickListener(new ComingSoonListener());
        coming_soon_view_11.setOnClickListener(new ComingSoonListener());
        coming_soon_view_12.setOnClickListener(new ComingSoonListener());

        baoxiu_view.setOnClickListener(new Button.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Main.this, Baoxiu.class);
                Main.this.startActivity(intent);
            }
        });

        kaifashang_shoulou_view.setOnClickListener(new Button.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("kfs_or_yz", "kfs");
                intent.setClass(Main.this, SelectorView.class);
                Main.this.startActivity(intent);
            }
        });
        yezhu_shoulou_view.setOnClickListener(new Button.OnClickListener() {


            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("kfs_or_yz", "yz");
                intent.setClass(Main.this, SelectorView.class);
                Main.this.startActivity(intent);
            }
        });


    }

    private class TestListener implements View.OnClickListener{
        @Override
        public void onClick(View arg0) {

            Intent intent = new Intent();
            intent.setClass(Main.this, TestView.class);
            Main.this.startActivity(intent);

        }
    }


    private class ComingSoonListener implements View.OnClickListener{
        @Override
        public void onClick(View arg0) {
            Intent intent = new Intent();
            intent.setClass(Main.this, ComingSoon.class);
            Main.this.startActivity(intent);
        }
    }

    private class SettingListener implements View.OnClickListener{
        @Override
        public void onClick(View arg0) {

            Intent intent = new Intent();
            intent.setClass(Main.this, SettingActivity.class);
            Main.this.startActivity(intent);

        }
    }


    private class LogoutListener implements View.OnClickListener {

		@Override
		public void onClick(View arg0) {
            new AlertDialog.Builder(Main.this).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent();
                    intent.setClass(Main.this, Login.class);
                    Main.this.startActivity(intent);
                    Main.this.finish();
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).setTitle("确认要退出程序?").show();

			
		
		}
	}

    @Override
    protected void onPause() {
        Log.e("MAIN", "onPause");
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onStop() {

        Log.e("MAIN", "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
         super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
    }
}