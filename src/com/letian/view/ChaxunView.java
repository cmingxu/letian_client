package com.letian.view;


import com.letian.R;
import com.letian.model.Danyuan;
import com.letian.model.Zhuhu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ChaxunView extends Activity {
	public static String LOG_TAG = "CHAXUNVIEW";

	private Button choose_unit_button;
	private TextView unit_address;
	private TextView zhuhumingcheng;
	private TextView shoujihaoma;
	private TextView lianxidianhua;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chaxun_view);
		choose_unit_button = (Button) findViewById(R.id.choose_unit_button);
		unit_address = (TextView) findViewById(R.id.unit_address);
		zhuhumingcheng = (TextView) findViewById(R.id.zhuhumingcheng);
		shoujihaoma = (TextView) findViewById(R.id.shoujihaoma);
		lianxidianhua = (TextView) findViewById(R.id.lianxidianhua);

		choose_unit_button.setOnClickListener(new UnitSelectListener());
	}

	class UnitSelectListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {

			Intent intent = new Intent();
			intent.setClass(ChaxunView.this, UnitChooser.class);
			int requestCode = 1;
			ChaxunView.this.startActivityForResult(intent, requestCode);

		}
	}
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {


		switch (requestCode) {
		case 1:
			switch (resultCode) {
			case RESULT_OK:
				Bundle b = data.getExtras();

				Zhuhu zhuhu = Danyuan
						.get_danyuan_with_danyuanbianhao(b.getString("danyuan_bianhao"),
								ChaxunView.this.getApplicationContext())
						.get_zhuhu();
				shoujihaoma.setText(zhuhu.shoujihaoma);
				lianxidianhua.setText(zhuhu.lianxidianhua);
				zhuhumingcheng.setText(zhuhu.zhuhumingcheng);
				unit_address.setText(b.getString("danyuan_quan_mingcheng"));
			}
		
	}

}
}