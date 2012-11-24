package com.letian.model.xmlhandler;

import android.content.Context;
import com.letian.model.FangjianLeixing;
import com.letian.model.Huxing;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-11-22
 * Time: 下午7:57
 * To change this template use File | Settings | File Templates.
 */
public class HuxingHandler extends BaseHandler {
    private ArrayList<Huxing> huxings ;
    private Huxing currentHuxing;
    private StringBuilder builder;
    public Context context;

    public HuxingHandler(Context context) {
        this.context = context;
    }

    public ArrayList<Huxing> getItems(){
        return this.huxings;
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

        if (this.currentHuxing != null){
            if (localName.equalsIgnoreCase("HXMC")){
                currentHuxing.hxmc = builder.toString().replaceAll("\\s","");
            } else if (localName.equalsIgnoreCase("HXBH")){
                currentHuxing.hxbh = builder.toString().replaceAll("\\s","");
            }
            else if (localName.equalsIgnoreCase("id")){
                currentHuxing._id = Integer.parseInt(builder.toString().replaceAll("\\s",""));
            }
            else if (localName.equalsIgnoreCase("hx")){
                huxings.add(currentHuxing);
                this.currentHuxing = null;
            }
            builder.setLength(0);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        huxings = new ArrayList<Huxing>();
        builder = new StringBuilder();
    }


    @Override
    public void startElement(String uri, String localName, String name,
                             Attributes attributes) throws SAXException {
        super.startElement(uri, localName, name, attributes);

        if (localName.equalsIgnoreCase("hx")){

            this.currentHuxing = new Huxing(context);
        }

    }
}
