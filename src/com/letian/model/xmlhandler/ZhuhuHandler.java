package com.letian.model.xmlhandler;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.letian.model.Zhuhu;

import android.content.Context;

public class ZhuhuHandler extends DefaultHandler{
    private ArrayList<Zhuhu> zhuhus;
    private Zhuhu currentZhuhu;
    private StringBuilder builder;
    private Context context;
    
    public ZhuhuHandler(Context context){
    	this.context = context;
    }
    
    public ArrayList<Zhuhu> getZhuhus(){
        return this.zhuhus;
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
     
        if (this.currentZhuhu != null){
            if (localName.equalsIgnoreCase("zhuhumingcheng")){
            	currentZhuhu.zhuhumingcheng = builder.toString().replaceAll("\\s","");
            } else if (localName.equalsIgnoreCase("zhuhubianhao")){
            	currentZhuhu.zhuhubianhao = builder.toString().replaceAll("\\s","");
            } else if (localName.equalsIgnoreCase("shoujihaoma")){
            	currentZhuhu.shoujihaoma = builder.toString().replaceAll("\\s","");
            } else if (localName.equalsIgnoreCase("lianxidianhua")){
            	currentZhuhu.lianxidianhua = builder.toString().replaceAll("\\s","");
            }
            else if (localName.equalsIgnoreCase("zhuhu")){
            	zhuhus.add(currentZhuhu);
            	this.currentZhuhu = null;
            }
            builder.setLength(0);    
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        zhuhus = new ArrayList<Zhuhu>();
        builder = new StringBuilder();
    }

    
    @Override
	public void startElement(String uri, String localName, String name,
            Attributes attributes) throws SAXException {
    	 super.startElement(uri, localName, name, attributes);

    	 if (localName.equalsIgnoreCase("zhuhu")){
    		 this.currentZhuhu = new Zhuhu(context);
        }
       
    }
}

