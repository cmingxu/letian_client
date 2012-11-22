package com.letian.model.xmlhandler;

import android.content.Context;
import com.letian.model.FangjianLeixing;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-22
 * Time: 下午7:57
 * To change this template use File | Settings | File Templates.
 */
public class FangjianLeixingHandler extends DefaultHandler {
    private ArrayList<FangjianLeixing> fangjianleixings;
    private FangjianLeixing currentFangjianLeixing;
    private StringBuilder builder;
    public Context context;

    public FangjianLeixingHandler(Context context) {
        this.context = context;
    }

    public ArrayList<FangjianLeixing> geFangjianLexings(){
        return this.fangjianleixings;
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

        if (this.currentFangjianLeixing != null){
            if (localName.equalsIgnoreCase("fjlx")){
                currentFangjianLeixing.fjlx = builder.toString().replaceAll("\\s","");
            } else if (localName.equalsIgnoreCase("fjbh")){
                currentFangjianLeixing.fjbh = builder.toString().replaceAll("\\s","");
            }
            else if (localName.equalsIgnoreCase("id")){
                currentFangjianLeixing._id = Integer.parseInt(builder.toString().replaceAll("\\s",""));
            }
            else if (localName.equalsIgnoreCase("/fjlx")){
                fangjianleixings.add(currentFangjianLeixing);
                this.currentFangjianLeixing = null;
            }
            builder.setLength(0);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        fangjianleixings = new ArrayList<FangjianLeixing>();
        builder = new StringBuilder();
    }


    @Override
    public void startElement(String uri, String localName, String name,
                             Attributes attributes) throws SAXException {
        super.startElement(uri, localName, name, attributes);

        if (localName.equalsIgnoreCase("fjlx")){

            this.currentFangjianLeixing = new FangjianLeixing(context);
        }

    }
}
