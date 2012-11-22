package com.letian.model.xmlhandler;

import android.content.Context;
import com.letian.model.FangjianleixingYanshouduixiang;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-22
 * Time: 下午8:19
 * To change this template use File | Settings | File Templates.
 */
public class FangjianleixingYanshouduixiangHandler extends DefaultHandler {
    private ArrayList<FangjianleixingYanshouduixiang> fangjianleixingYanshouduixiangs;
    private FangjianleixingYanshouduixiang currentFangjianLeixingYanshouDuixiang;
    private StringBuilder builder;
    private Context context;

    public FangjianleixingYanshouduixiangHandler(Context context){
        this.context = context;
    }

    public ArrayList<FangjianleixingYanshouduixiang> geFangjianLexings(){
        return this.fangjianleixingYanshouduixiangs;
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

        if (this.currentFangjianLeixingYanshouDuixiang != null){
            if (localName.equalsIgnoreCase("FJLXID")){
                currentFangjianLeixingYanshouDuixiang.fjlxid = builder.toString().replaceAll("\\s","");
            } else if (localName.equalsIgnoreCase("DXID")){
                currentFangjianLeixingYanshouDuixiang.dxid = builder.toString().replaceAll("\\s","");
            }
            else if (localName.equalsIgnoreCase("id")){
                currentFangjianLeixingYanshouDuixiang._id = Integer.parseInt(builder.toString().replaceAll("\\s",""));
            }
            else if (localName.equalsIgnoreCase("/fjlx-ysdx")){
                fangjianleixingYanshouduixiangs.add(currentFangjianLeixingYanshouDuixiang);
                this.currentFangjianLeixingYanshouDuixiang = null;
            }
            builder.setLength(0);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        fangjianleixingYanshouduixiangs = new ArrayList<FangjianleixingYanshouduixiang>();
        builder = new StringBuilder();
    }


    @Override
    public void startElement(String uri, String localName, String name,
                             Attributes attributes) throws SAXException {
        super.startElement(uri, localName, name, attributes);

        if (localName.equalsIgnoreCase("fjlx-ysdx")){

            this.currentFangjianLeixingYanshouDuixiang = new FangjianleixingYanshouduixiang(context);
        }

    }
}
