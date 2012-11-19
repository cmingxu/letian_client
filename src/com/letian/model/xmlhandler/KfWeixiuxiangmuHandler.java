package com.letian.model.xmlhandler;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


import com.letian.model.KfWeixiuxiangmu;

import android.content.Context;


public class KfWeixiuxiangmuHandler extends DefaultHandler{
    private ArrayList<KfWeixiuxiangmu> kf_weixiuxiangmus;
    private KfWeixiuxiangmu currentKfWeixiuxiangmu;
    private StringBuilder builder;
    private Context context;
    
    public KfWeixiuxiangmuHandler(Context context){
    	this.context = context;
    }
    
    public ArrayList<KfWeixiuxiangmu> getKfWeixiuxiangmus(){
        return this.kf_weixiuxiangmus;
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
     
        if (this.currentKfWeixiuxiangmu != null){
            if (localName.equalsIgnoreCase("id")){
            	currentKfWeixiuxiangmu.xiangmu_id = builder.toString().replaceAll("\\s","");
            } else if (localName.equalsIgnoreCase("xiangmuleibie")){
            	currentKfWeixiuxiangmu.xiangmuleibie = builder.toString().replaceAll("\\s","");
            } else if (localName.equalsIgnoreCase("xiangmumingcheng")){
            	currentKfWeixiuxiangmu.xiangmumingcheng = builder.toString().replaceAll("\\s","");
            } else if (localName.equalsIgnoreCase("xiangmuneirong")){
            	currentKfWeixiuxiangmu.xiangmuneirong = builder.toString().replaceAll("\\s","");
            }
           
            else if (localName.equalsIgnoreCase("kf-weixiuxiangmu")){
            	kf_weixiuxiangmus.add(currentKfWeixiuxiangmu);
            	this.currentKfWeixiuxiangmu = null;
            }
            builder.setLength(0);    
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        kf_weixiuxiangmus = new ArrayList<KfWeixiuxiangmu>();
        builder = new StringBuilder();
    }

    
    @Override
	public void startElement(String uri, String localName, String name,
            Attributes attributes) throws SAXException {
    	 super.startElement(uri, localName, name, attributes);
;
    	 if (localName.equalsIgnoreCase("kf-weixiuxiangmu")){
    	
    		 this.currentKfWeixiuxiangmu = new KfWeixiuxiangmu(context);
        }
       
    }
}

