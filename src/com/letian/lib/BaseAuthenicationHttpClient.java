package com.letian.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;

import android.util.Log;
import com.letian.Login;
import com.letian.model.LTException;







/**
 * 
 * @author mqqqvpppm
 *
 */
public class BaseAuthenicationHttpClient {

	 static public String doRequest(String urlString, String name, String password, HashMap<String,String> params) throws LTException, IOException {
            Log.d(Login.LOG_TAG, "doRequest");
            Log.d(Login.LOG_TAG, urlString);
	    	URL url = new URL (urlString);
	        String userPassword = name+":"+password;
	
	        String encoding = Base64.encode(userPassword).trim();
	
	
	        HttpURLConnection uc = (HttpURLConnection) url.openConnection();
	        uc.setRequestProperty("Authorization", "Basic " + encoding);
	        uc.setRequestProperty("User-Agent", "Mozilla/5.0");

            uc.setConnectTimeout(5 * 1000);
	        uc.setDoInput(true);
	        uc.setDoOutput(true);
	        uc.setRequestMethod("POST");
	        
	        
	        if (params != null && !params.isEmpty()) {
	        	StringBuffer buf = new StringBuffer();
	        	for(String key : params.keySet()){
	        		 buf.append("&").append(key).append("=").append(params.get(key));
	        	}
	        	buf.deleteCharAt(0);
	            uc.getOutputStream().write(buf.toString().getBytes("UTF-8"));
	            uc.getOutputStream().close();  
	        }  
	  
	        InputStream content = uc.getInputStream();
	        BufferedReader in = new BufferedReader (new InputStreamReader (content,"UTF-8"));
	        String line = in.readLine();

	        in.close();
	        return line.trim();


	 }
	 
	 static public String doRequest(String urlString, String name, String password) throws LTException
	 {

         try{
	        URL url = new URL (urlString);

	        URLConnection uc = url.openConnection();
	        uc.setConnectTimeout(Constants.TIMEOUT);
	        String userPassword = name+":"+password;
	        String encoding = Base64.encode(userPassword).trim();
	        
	     
	        uc.setRequestProperty  ("Authorization", "Basic " + encoding);
	        uc.setRequestProperty("User-Agent", "Mozilla/5.0");   
	          
	        InputStream content = uc.getInputStream();
	       
	      
	  
	        BufferedReader in = new BufferedReader (new InputStreamReader (content,"UTF-8"));
	        String line = "";//will refactory
	        StringBuilder sb = new StringBuilder("");
	        while((line = in.readLine()) != null){
	        	sb.append(line);
	        }
	        in.close();

	        return sb.toString();
		 }catch(IOException e){
			 throw new LTException(e);
		 }
	 }
	 
}
