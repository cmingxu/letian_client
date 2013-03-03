package com.letian.model.xmlhandler;

import android.content.Context;
import com.letian.model.YanshouDuixiang;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-22
 * Time: 上午8:03
 * To change this template use File | Settings | File Templates.
 */
public class YanshouduixiangHandler extends BaseHandler {
    private ArrayList<YanshouDuixiang> yanshouduixiangs;
    private YanshouDuixiang currentYanshouDuixiang;
    private StringBuilder builder;
    private Context context;

    public YanshouduixiangHandler(Context context){
        this.context = context;
    }

    public ArrayList<YanshouDuixiang> getItems(){
        return this.yanshouduixiangs;
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

        if (this.currentYanshouDuixiang != null){
            if (localName.equalsIgnoreCase("DXMC")){
                currentYanshouDuixiang.dxmc = builder.toString().replaceAll("\\s","");
            } else if (localName.equalsIgnoreCase("DXBH")){
                currentYanshouDuixiang.dxbh = builder.toString().replaceAll("\\s","");
            }
            else if (localName.equalsIgnoreCase("id")){
                currentYanshouDuixiang._id = builder.toString().replaceAll("\\s","");
            }
            else if (localName.equalsIgnoreCase("ysdx")){
                yanshouduixiangs.add(currentYanshouDuixiang);
                this.currentYanshouDuixiang = null;
            }
            builder.setLength(0);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        yanshouduixiangs = new ArrayList<YanshouDuixiang>();
        builder = new StringBuilder();
    }


    @Override
    public void startElement(String uri, String localName, String name,
                             Attributes attributes) throws SAXException {
        super.startElement(uri, localName, name, attributes);

        if (localName.equalsIgnoreCase("ysdx")){
            this.currentYanshouDuixiang = new YanshouDuixiang(context);
        }

    }
}
