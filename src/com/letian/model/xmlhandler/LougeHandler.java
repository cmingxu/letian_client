package com.letian.model.xmlhandler;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.letian.model.Louge;

import android.content.Context;
import android.util.Log;

public class LougeHandler extends BaseHandler{
    private ArrayList<Louge> louges;
    private Louge currentLouge;
    private StringBuilder builder;
    private Context context;
    
    public LougeHandler(Context context){
    	this.context = context;
    }
    
    public ArrayList<Louge> getItems(){
        return this.louges;
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
     
        if (this.currentLouge != null){
            if (localName.equalsIgnoreCase("lougebianhao")){
            	currentLouge.lougebianhao = builder.toString().replaceAll("\\s","");
            } else if (localName.equalsIgnoreCase("loupanbianhao")){
            	currentLouge.loupanbianhao = builder.toString().replaceAll("\\s","");
            } else if (localName.equalsIgnoreCase("lougemingcheng")){
            	currentLouge.lougemingcheng = builder.toString().replaceAll("\\s","");
            } 
            else if (localName.equalsIgnoreCase("louge")){
            	louges.add(currentLouge);
            	this.currentLouge = null;
            }
            builder.setLength(0);    
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
//        Log.e(Danyuan.LOG_TAG, "sssssssss document");
        louges = new ArrayList<Louge>();
        builder = new StringBuilder();
    }

    
    @Override
	public void startElement(String uri, String localName, String name,
            Attributes attributes) throws SAXException {
    	 super.startElement(uri, localName, name, attributes);

    	 if (localName.equalsIgnoreCase("louge")){
    	
    		 this.currentLouge = new Louge(context);
    		 Log.e("ssssssss", "ssssssssssssssssssssssssss");
        }
       
    }
}

