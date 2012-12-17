package com.letian.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-12-16
 * Time: 下午4:49
 * To change this template use File | Settings | File Templates.
 */
public class ItemAdapater extends BaseAdapter {
    public String[] names;
    private LayoutInflater inflater;
    int viewId;
    Context context;



    public ItemAdapater(Context c, int viewId, String[] names) {
        this.context = c;
        this.viewId = viewId;
        this.names = names;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int i) {
        return names[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
