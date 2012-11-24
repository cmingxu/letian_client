package com.letian.model.xmlhandler;

import android.content.Context;
import com.letian.model.YanshouXiangmu;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-22
 * Time: 下午8:01
 * To change this template use File | Settings | File Templates.
 */
public class YanshouXiangmuHandler extends BaseHandler {
    private ArrayList<YanshouXiangmu> yanshouxiangmus;
    private YanshouXiangmu currentYanshouXiangmu;
    private StringBuilder builder;
    private Context context;

    public YanshouXiangmuHandler(Context context){
        this.context = context;
    }

    public ArrayList<YanshouXiangmu> getItems(){
        return this.yanshouxiangmus;
    }
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);
        builder.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {
        super.endElement(uri, localName, name);

        if (this.currentYanshouXiangmu != null){
            if (localName.equalsIgnoreCase("DXID")){
                currentYanshouXiangmu.duixiang_id = builder.toString().replaceAll("\\s","");
            } else if (localName.equalsIgnoreCase("XMMC")){
                currentYanshouXiangmu.xmmc = builder.toString().replaceAll("\\s","");
            }else if (localName.equalsIgnoreCase("XMBH")){
                currentYanshouXiangmu.xmbh = builder.toString().replaceAll("\\s","");
            }
            else if (localName.equalsIgnoreCase("id")){
                currentYanshouXiangmu._id = builder.toString().replaceAll("\\s","");
            }
            else if (localName.equalsIgnoreCase("ysxm")){
                yanshouxiangmus.add(currentYanshouXiangmu);
                this.currentYanshouXiangmu = null;
            }
            builder.setLength(0);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        yanshouxiangmus = new ArrayList<YanshouXiangmu>();
        builder = new StringBuilder();
    }


    @Override
    public void startElement(String uri, String localName, String name,
                             Attributes attributes) throws SAXException {
        super.startElement(uri, localName, name, attributes);

        if (localName.equalsIgnoreCase("ysxm")){

            this.currentYanshouXiangmu = new YanshouXiangmu(context);
        }

    }
}
