package com.letian.model.xmlhandler;

import android.content.Context;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.letian.model.HuxingFangjianLeixing;

import java.util.ArrayList;
import org.xml.sax.Attributes;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-22
 * Time: 下午8:12
 * To change this template use File | Settings | File Templates.
 */
public class HuxingFangjianLeixingHandler extends DefaultHandler {
    private ArrayList<HuxingFangjianLeixing> huxingFangjianLeixings;
    private HuxingFangjianLeixing currentHuxingFangjianleixing;
    private StringBuilder builder;
    private Context context;

    public HuxingFangjianLeixingHandler(Context context){
        this.context = context;
    }

    public ArrayList<HuxingFangjianLeixing> geHuxingFangjianLexings(){
        return this.huxingFangjianLeixings;
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

        if (this.currentHuxingFangjianleixing != null){
            if (localName.equalsIgnoreCase("HXID")){
                currentHuxingFangjianleixing.hxid = builder.toString().replaceAll("\\s","");
            } else if (localName.equalsIgnoreCase("FJLXID")){
                currentHuxingFangjianleixing.fjlxid = builder.toString().replaceAll("\\s","");
            }
            else if (localName.equalsIgnoreCase("id")){
                currentHuxingFangjianleixing._id = Integer.parseInt(builder.toString().replaceAll("\\s",""));
            }
            else if (localName.equalsIgnoreCase("/hx-fjlx")){
                huxingFangjianLeixings.add(currentHuxingFangjianleixing);
                this.currentHuxingFangjianleixing = null;
            }
            builder.setLength(0);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        huxingFangjianLeixings = new ArrayList<HuxingFangjianLeixing>();
        builder = new StringBuilder();
    }


    @Override
    public void startElement(String uri, String localName, String name,
                             Attributes attributes) throws SAXException {
        super.startElement(uri, localName, name, attributes);

        if (localName.equalsIgnoreCase("hx-fjlx")){

            this.currentHuxingFangjianleixing = new HuxingFangjianLeixing(context);
        }

    }
}
