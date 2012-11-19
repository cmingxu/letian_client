package com.letian.model.xmlhandler;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.letian.model.Weixiudan;

import android.content.Context;
import android.util.Log;

public class WeixiudanHandler extends DefaultHandler{
    private ArrayList<Weixiudan> weixiudans;
    private Weixiudan currentWeixiudan;
    private StringBuilder builder;
    private Context context;
    
    public WeixiudanHandler(Context context){
    	this.context = context;
    }
    
    public ArrayList<Weixiudan> getweixiudans(){
        return this.weixiudans;
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
     
        if (this.currentWeixiudan != null){
            if (localName.equalsIgnoreCase("weixiu-id")){
            	currentWeixiudan.weixiu_id = builder.toString().replaceAll("\\s","");
            } else if (localName.equalsIgnoreCase("danyuanbianhao")){
            	currentWeixiudan.danyuanbianhao = builder.toString().replaceAll("\\s","");
            } else if (localName.equalsIgnoreCase("danyuanmingcheng")){
            	currentWeixiudan.weixiudanyuan = builder.toString().replaceAll("\\s","");
            } else if (localName.equalsIgnoreCase("zuhumingcheng")){
            	currentWeixiudan.zuhumingcheng = builder.toString().replaceAll("\\s","");
            
            }
            else if (localName.equalsIgnoreCase("lianxidianhua")){
            	currentWeixiudan.lianxidianhua = builder.toString().replaceAll("\\s","");
            	
            }
            else if (localName.equalsIgnoreCase("fuwuneirong")){
            	currentWeixiudan.wufuneirong = builder.toString().replaceAll("\\s","");
            	Log.e("fuwuneirong", currentWeixiudan.wufuneirong);
            	Log.e("fuwuneirong", currentWeixiudan.wufuneirong);
            	Log.e("fuwuneirong", currentWeixiudan.wufuneirong);
            	Log.e("fuwuneirong", currentWeixiudan.wufuneirong);
            	Log.e("fuwuneirong", currentWeixiudan.wufuneirong);
            	Log.e("fuwuneirong", currentWeixiudan.wufuneirong);
            }
            else if (localName.equalsIgnoreCase("zhuhumingcheng")){
            	currentWeixiudan.zuhumingcheng = builder.toString().replaceAll("\\s","");
            }
            else if (localName.equalsIgnoreCase("weixiudan")){
            	weixiudans.add(currentWeixiudan);
            	this.currentWeixiudan = null;
            }
            builder.setLength(0);    
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
//        Log.e(Weixiudan.LOG_TAG, "sssssssss document");
        weixiudans = new ArrayList<Weixiudan>();
        builder = new StringBuilder();
    }

    
    @Override
	public void startElement(String uri, String localName, String name,
            Attributes attributes) throws SAXException {
    	 super.startElement(uri, localName, name, attributes);
;
    	 if (localName.equalsIgnoreCase("Weixiudan")){
    	
    		 this.currentWeixiudan = new Weixiudan(context);
        }
       
    }
}

