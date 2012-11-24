package com.letian.model.xmlhandler;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.letian.model.Danyuan;

import android.content.Context;

public class DanyuanHandler extends BaseHandler{
    private ArrayList<Danyuan> danyuans;
    private Danyuan currentDanyuan;
    private StringBuilder builder;
    private Context context;
    
    public DanyuanHandler(Context context){
    	this.context = context;
    }
    
    public ArrayList<Danyuan> getItems(){
        return this.danyuans;
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
     
        if (this.currentDanyuan != null){
            if (localName.equalsIgnoreCase("danyuanbianhao")){
            	currentDanyuan.danyuanbianhao = builder.toString().replaceAll("\\s","");
            } else if (localName.equalsIgnoreCase("danyuanmingcheng")){
            	currentDanyuan.danyuanmingcheng = builder.toString().replaceAll("\\s","");
            } else if (localName.equalsIgnoreCase("lougebianhao")){
            	currentDanyuan.lougebianhao = builder.toString().replaceAll("\\s","");
            } else if (localName.equalsIgnoreCase("zhuhubianhao")){
            	currentDanyuan.zhuhubianhao = builder.toString().replaceAll("\\s","");
            }
            else if (localName.equalsIgnoreCase("louceng")){
            	currentDanyuan.louceng = builder.toString().replaceAll("\\s","");
            }else if (localName.equalsIgnoreCase("jiange")){
                currentDanyuan.jiange = builder.toString().replaceAll("\\s","");
            }
            else if (localName.equalsIgnoreCase("loucengmingcheng")){
            	currentDanyuan.loucengmingcheng = builder.toString().replaceAll("\\s","");
            }
            else if (localName.equalsIgnoreCase("danyuan")){
            	danyuans.add(currentDanyuan);
            	this.currentDanyuan = null;
            }
            builder.setLength(0);    
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        danyuans = new ArrayList<Danyuan>();
        builder = new StringBuilder();
    }

    
    @Override
	public void startElement(String uri, String localName, String name,
            Attributes attributes) throws SAXException {
    	 super.startElement(uri, localName, name, attributes);
;
    	 if (localName.equalsIgnoreCase("danyuan")){
    	
    		 this.currentDanyuan = new Danyuan(context);
        }
       
    }
}

