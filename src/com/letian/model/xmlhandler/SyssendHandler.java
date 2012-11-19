package com.letian.model.xmlhandler;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.letian.model.Syssend;

import android.content.Context;

public class SyssendHandler extends DefaultHandler {
	private ArrayList<Syssend> syssends;
	private Syssend currentSyssend;
	private StringBuilder builder;
	private Context context;

	public SyssendHandler(Context context) {
		this.context = context;
	}

	public ArrayList<Syssend> getSyssends() {
		return this.syssends;
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

		if (this.currentSyssend != null) {
			if (localName.equalsIgnoreCase("id")) {
				
				currentSyssend._id = builder.toString().replaceAll("\\s", "");
	
			} else if (localName.equalsIgnoreCase("content")) {
				currentSyssend.content = builder.toString().replaceAll("\\s",
						"");
			} else if (localName.equalsIgnoreCase("sendtime")) {
			
				currentSyssend.sendtime = builder.toString().replaceAll("\\s",
						"").replaceAll("[a-zA-Z]+",
						" ");
			}
			else if (localName.equalsIgnoreCase("sendperson")) {
				currentSyssend.sendperson = builder.toString().replaceAll("\\s",
						"");
			} 
			else if (localName.equalsIgnoreCase("style")) {
				currentSyssend.style = builder.toString().replaceAll("\\s",
						"");
			} else if (localName.equalsIgnoreCase("syssend")) {
				syssends.add(currentSyssend);
				this.currentSyssend = null;
			}
			builder.setLength(0);
		}
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		// Log.e(Danyuan.LOG_TAG, "sssssssss document");
		syssends = new ArrayList<Syssend>();
		builder = new StringBuilder();
	}

	@Override
	public void startElement(String uri, String localName, String name,
			Attributes attributes) throws SAXException {
		super.startElement(uri, localName, name, attributes);

		if (localName.equalsIgnoreCase("Syssend")) {

			this.currentSyssend = new Syssend(context);
		}

	}
}
