package com.letian.model.xmlhandler;

import android.content.Context;
import com.letian.model.User;
import com.letian.model.Zhuhu;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: xcm
 * Date: 12-12-14
 * Time: 下午10:16
 * To change this template use File | Settings | File Templates.
 */
public class UserHandler  extends BaseHandler {

    private ArrayList<User> users;
    private User currentUser;
    private StringBuilder builder;
    private Context context;

    public UserHandler(Context context){
        this.context = context;
    }

    public ArrayList<User> getItems(){
        return this.users;
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

        if (this.currentUser != null){
            if (localName.equalsIgnoreCase("yonghuming")){
                currentUser.name = builder.toString().replaceAll("\\s","");
            } else  if (localName.equalsIgnoreCase("mima")){
                currentUser.password = builder.toString().replaceAll("\\s","");
            }
            else  if (localName.equalsIgnoreCase("yonghuzu")){
                currentUser.yonghuzu = builder.toString().replaceAll("\\s","");
            }
            else if (localName.equalsIgnoreCase("user")){
                users.add(currentUser);
                this.currentUser = null;
            }
            builder.setLength(0);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        users = new ArrayList<User>();
        builder = new StringBuilder();
    }


    @Override
    public void startElement(String uri, String localName, String name,
                             Attributes attributes) throws SAXException {
        super.startElement(uri, localName, name, attributes);

        if (localName.equalsIgnoreCase("user")){
            this.currentUser = new User(context);
        }

    }
}




