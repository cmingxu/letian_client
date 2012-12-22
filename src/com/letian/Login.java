package com.letian;

import android.widget.Toast;
import com.letian.lib.LocalAccessor;
import com.letian.lib.NetworkConnection;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity {
    public static final String LOG_TAG = "Login_Activty";
    public static final int SHEZHI = Menu.FIRST;
    private EditText login;
    private EditText password;
    private Button login_button;
    private Button setting_button;
    private Button cancel_button;


    private User user;
    private ProgressDialog progressDialog;
    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        this.setTitle(R.string.title);
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        login_button = (Button) findViewById(R.id.login_button);
        setting_button = (Button) findViewById(R.id.setting_button);
        cancel_button = (Button) findViewById(R.id.cancel_button);


        User last_user = User.last_user(this.getApplicationContext());
        login.setText(last_user.name);
        password.setText(last_user.password);

        login_button.setOnClickListener(new LoginListener());
        setting_button.setOnClickListener(new SettingListener());

        cancel_button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login.this.finish();
            }
        });

    }

    public void showToast(final String s) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), s,
                        Toast.LENGTH_SHORT).show();

            }
        });
    }


    public class SettingListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            LayoutInflater factory = (LayoutInflater) Login.this.getApplicationContext()
                    .getSystemService(LAYOUT_INFLATER_SERVICE);
            final View textEntryView = factory.inflate(
                    R.layout.setting_dialog_layout, null);
            final EditText addr = (EditText) textEntryView.findViewById(R.id.addr);
            addr.setText(LocalAccessor.getInstance(Login.this.getApplicationContext())
                    .get_server_url());

            AlertDialog dlg = new AlertDialog.Builder(Login.this).setTitle(
                    "").setView(textEntryView).setPositiveButton(getResources().getString(R.string.save),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            progressDialog = ProgressDialog.show(Login.this, "",
                                    null, true);
                            new Thread() {
                                @Override
                                public void run() {
                                    try {
                                        String addr_str = addr.getText().toString().trim();
                                        if (User.is_server_reachable(addr_str)) {
                                            LocalAccessor.getInstance(
                                                    Login.this.getApplicationContext())
                                                    .set_server_url(addr_str);
                                            progressDialog.dismiss();

                                            showToast("设置成功");
                                        } else
                                            throw new Exception();

                                    } catch (Exception e) {
                                        Log.e("Login", e.toString());
                                        progressDialog.dismiss();
                                        showToast(
                                                "服务器地址设置出错或者网络无连接！");
                                    }
                                }
                            }.start();
                        }
                    }).setNegativeButton(getResources().getText(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            /* User clicked cancel so do some stuff */
                        }
                    }).create();
            dlg.show();
        }
    }

    private class LoginListener implements Button.OnClickListener {

        @Override
        public void onClick(View arg0) {
            user = new User();
            user.name = login.getText().toString().trim();
            user.password = password.getText().toString().trim();


            if (NetworkConnection.getInstance(
                    Login.this.getApplicationContext()).NetworkNotAvailable()) {
                Log.e(Login.LOG_TAG, "network can not be reach");
                new AlertDialog.Builder(Login.this).setMessage(
                        getResources().getString(
                                R.string.net_work_can_not_be_found))
                        .setPositiveButton(R.string.i_know, null).show();
            } else if (user.name.length() == 0) {
                new AlertDialog.Builder(Login.this).setMessage(
                        getResources().getString(
                                R.string.field_should_not_blank))
                        .setPositiveButton(R.string.i_know, null).show();
            } else {
                doLogin();
            }

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
                        final VerifiedInfo vi = user.verify();

                        if (vi.verifyCode == VerifiedInfo.VERIFY_SUCCESS) {

                            User.set_current_user(user, Login.this.getApplicationContext());

                            Intent i = new Intent(Login.this, Main.class);
                            startActivity(i);
                            finish();
                        } else {
                            handler.post(new Runnable() {
                                public void run() {
                                    showToast(     vi.verifyMessage);
                                }
                            });
                        }
                    } catch (Exception e) {

                        Log.e(Login.LOG_TAG, e.toString());
                        handler.post(new Runnable() {
                            public void run() {
                                showToast( "网络好像不太给力！");

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
