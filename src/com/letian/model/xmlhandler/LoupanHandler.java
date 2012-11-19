package com.letian.model.xmlhandler;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.letian.model.Loupan;

import android.content.Context;

public class LoupanHandler extends DefaultHandler{
    private ArrayList<Loupan> loupans;
    private Loupan currentLoupan;
    private StringBuilder builder;
    private Context context;
    
    public LoupanHandler(Context context){
    	this.context = context;
    }
    
    public ArrayList<Loupan> getLoupans(){
        return this.loupans;
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
     
        if (this.currentLoupan != null){
            if (localName.equalsIgnoreCase("loupanbianhao")){
            	currentLoupan.loupanbianhao = builder.toString().replaceAll("\\s","");
            } else if (localName.equalsIgnoreCase("loupanmingcheng")){
            	currentLoupan.loupanmingcheng = builder.toString().replaceAll("\\s","");
            } 
            else if (localName.equalsIgnoreCase("loupan")){
            	loupans.add(currentLoupan);
            	this.currentLoupan = null;
            }
            builder.setLength(0);    
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
//        Log.e(Danyuan.LOG_TAG, "sssssssss document");
        loupans = new ArrayList<Loupan>();
        builder = new StringBuilder();
    }

    
    @Override
	public void startElement(String uri, String localName, String name,
            Attributes attributes) throws SAXException {
    	 super.startElement(uri, localName, name, attributes);
;
    	 if (localName.equalsIgnoreCase("loupan")){
    	
    		 this.currentLoupan = new Loupan(context);
        }
       
    }
}

