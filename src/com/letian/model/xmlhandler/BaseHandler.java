package com.letian.model.xmlhandler;

import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import com.letian.model.Model;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-22
 * Time: 下午9:14
 * To change this template use File | Settings | File Templates.
 */
public class BaseHandler extends DefaultHandler {
    public ArrayList<? extends Model> getItems(){
        return new ArrayList<Model>();
    }
}
