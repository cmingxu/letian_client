package com.letian.view;

import android.app.Activity;
import android.os.Bundle;
import com.letian.R;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-22
 * Time: 下午10:18
 * To change this template use File | Settings | File Templates.
 */
public class ListItem extends Activity {
    private String tableToDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item);
        tableToDisplay = savedInstanceState.getString("tableToDisplay");

        if (tableToDisplay.equals("Danyuan")) {
        } else if (tableToDisplay.equals("Louge")) {
        } else if (tableToDisplay.equals("Louceng")) {
        } else {
        }
    }


}
