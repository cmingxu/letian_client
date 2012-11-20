package com.letian;

import com.letian.lib.LocalAccessor;
import com.letian.model.User;
import com.letian.model.VerifiedInfo;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Login extends Activity {
	public static final String LOG_TAG = "Login_Activty";
	public static final int SHEZHI = Menu.FIRST;
	private EditText login;
	private EditText password;
	private Button login_button;
	private Button back_button;

	private CheckBox remember_me;
	private User user;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		this.setTitle(R.string.login_title);
		login = (EditText) findViewById(R.id.login);
		password = (EditText) findViewById(R.id.password);
		login_button = (Button) findViewById(R.id.login_button);
		back_button = (Button) findViewById(R.id.back_button);
		remember_me = (CheckBox) findViewById(R.id.remeber_me);

		User last_user = User.last_user(this.getApplicationContext());
		if (last_user.remember_me == 1) {
			login.setText(last_user.name);
			password.setText(last_user.password);
			remember_me.setChecked(true);
		}

		login_button.setOnClickListener(new LoginListener());
		back_button.setOnClickListener(new BackListener());

	}

	class BackListener implements Button.OnClickListener{
		@Override
		public void onClick(View arg0) {
			Login.this.finish();
		}

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, SHEZHI, 0, "设置");
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case SHEZHI:
			shezhi();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void shezhi() {
		LayoutInflater factory = (LayoutInflater) this.getApplicationContext()
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		final View textEntryView = factory.inflate(
				R.layout.setting_dialog_layout, null);
		final EditText addr = (EditText) textEntryView.findViewById(R.id.addr);
		addr.setText(LocalAccessor.getInstance(this.getApplicationContext())
				.get_server_url());

		AlertDialog dlg = new AlertDialog.Builder(Login.this).setTitle(
				"").setView(textEntryView).setPositiveButton("",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String text = "";
						String addr_str = addr.getText().toString().trim();
						if (addr_str != ""
								&& User.is_server_reachable(addr_str)) {
							text = "abc";
							LocalAccessor.getInstance(
									Login.this.getApplicationContext())
									.set_server_url(addr_str);


						} else {
							text = "woo";
						}
						new AlertDialog.Builder(Login.this).setMessage(text)
								.setPositiveButton(R.string.i_know, null)
								.show();
					}
				}).setNegativeButton("ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

						/* User clicked cancel so do some stuff */
					}
				}).create();
		dlg.show();
	}

	private class LoginListener implements Button.OnClickListener {

		@Override
		public void onClick(View arg0) {
			user = new User();
			user.name = login.getText().toString().trim();
			user.password = password.getText().toString().trim();


			user.remember_me = remember_me.isChecked() ? 1 : 0;


//			if (NetworkConnection.getInstance(
//					Login.this.getApplicationContext()).NetworkNotAvailable()) {
//				Log.e(Login.LOG_TAG, "network can not be reach");
//				new AlertDialog.Builder(Login.this).setMessage(
//						getResources().getString(
//								R.string.net_work_can_not_be_found))
//						.setPositiveButton(R.string.i_know, null).show();
//			} else if (user.name.length() == 0 || user.password.length() == 0) {
//				new AlertDialog.Builder(Login.this).setMessage(
//						getResources().getString(
//								R.string.field_should_not_blank))
//						.setPositiveButton(R.string.i_know, null).show();
//			} else {
				doLogin();
//			}

		}

		private void doLogin() {
			String login_notify = getResources().getString(
					R.string.login_notify);
			progressDialog = ProgressDialog.show(Login.this, login_notify,
					null, true);
			new Thread() {
				@Override
				public void run() {
					try {
                        Log.d(Login.LOG_TAG, "before");
						final VerifiedInfo vi = user.verify();
                        Log.d(Login.LOG_TAG, "after");
						Log.e(Login.LOG_TAG, vi.verifyMessage);

						if (vi.verifyCode == VerifiedInfo.VERIFY_SUCCESS) {

							User.set_current_user(user, Login.this.getApplicationContext());


							Intent i = new Intent(Login.this, Main.class);

							startActivity(i);
							finish();
						} else {
							handler.post(new Runnable() {
								public void run() {
									new AlertDialog.Builder(Login.this)
											.setMessage(vi.verifyMessage)
											.setPositiveButton("Okay", null)
											.show();

								}
							});
						}
					} catch (Exception e) {

                        Log.d(Login.LOG_TAG, e.getMessage());
						handler.post(new Runnable() {
							public void run() {
								new AlertDialog.Builder(Login.this).setMessage(
										"网络好像不太给力！").setPositiveButton("Okay",
										null).show();

							}
						});

					}
					progressDialog.dismiss();
				}
			}.start();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.e("onKeyDown:", " keyCode=" + keyCode + " KeyEvent=" + event);
		boolean should_capture = false;
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			should_capture = true;
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			should_capture = true;
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			should_capture = true;
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			should_capture = true;
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			should_capture = true;
			break;
		case KeyEvent.KEYCODE_0:
			should_capture = true;
			break;
		case KeyEvent.KEYCODE_BACK:
			should_capture = true;
			break;

		case KeyEvent.KEYCODE_CALL:
			should_capture = true;
			break;

		case KeyEvent.KEYCODE_ENDCALL:
			should_capture = true;
			break;

		case KeyEvent.KEYCODE_HOME:
			should_capture = true;
			break;
		// case KeyEvent.KEYCODE_MENU:
		// break;
		}
		if (should_capture) {
			return false;
		}
		return super.onKeyDown(keyCode, event);

	}

	@Override
	public void onStop() {
		super.onStop();
		this.onResume();
	}
}
