package com.letian.lib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



public class NetworkConnection {
	private Context context;
	
	private NetworkConnection(Context context){
		this.context = context;
	}	

	static private NetworkConnection accessor; 
	
	public static NetworkConnection getInstance(Context context){		
		if(accessor == null){
			accessor = new NetworkConnection(context);
		}
		return accessor;
	}
	
	
	public boolean isNetworkAvailable() {   
		  ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
//		  if (connectivity == null) {  
//		      return false;  
//		  } else {  
//		      NetworkInfo[] info = connectivity.getAllNetworkInfo();  
//		      if (info != null) {  
//		        for (int i = 0; i < info.length; i++) {  
//		            if (info[i].getState() == NetworkInfo.State.CONNECTED) {  
//		              return true;  
//		            }  
//		        }  
//		      }  
//		  }  
//		  return false;  
		  return true;
		}
	
	public boolean NetworkNotAvailable() {   
			  return !isNetworkAvailable();
		}
	
	
	public boolean reachServer() {   
		  ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
		  if (connectivity == null) {  
		      return false;  
		  } else {  
		      NetworkInfo[] info = connectivity.getAllNetworkInfo();  
		      if (info != null) {  
		        for (int i = 0; i < info.length; i++) {  
		            if (info[i].getState() == NetworkInfo.State.CONNECTED) {  
		              return true;  
		            }  
		        }  
		      }  
		  }  
		  return false;  
		}
	

}
