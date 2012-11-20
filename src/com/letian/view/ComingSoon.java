package com.letian.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.letian.Main;
import com.letian.R;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-20
 * Time: 下午9:33
 * To change this template use File | Settings | File Templates.
 */
public class ComingSoon extends Activity {
    private Button backBtn;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.coming_soon);
        backBtn = (Button)findViewById(R.id.coming_soon_back_button);
        backBtn.setOnClickListener(new Button.OnClickListener(){


            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(ComingSoon.this, Main.class);
                ComingSoon.this.startActivity(intent);
                ComingSoon.this.finish();
            }
        });
    }

}