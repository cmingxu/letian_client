package com.letian.lib;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class IOUtils {
    
  
    public static final String PREFS_FILE ="letian.prefs";
    
    public static String getUrlResponse(String url) {
        try {
            HttpGet get = new HttpGet(url);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            return convertStreamToString(entity.getContent());
        } catch (Exception e) {
        }
        return null;
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is), 8*1024);
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
       return sb.toString();
    }
    
    public static Bitmap getBitmapFromUrl(URL url) {
         Bitmap bitmap = null;
         InputStream in = null;
         OutputStream out = null;

         try {
             in = new BufferedInputStream(url.openStream(), 4 * 1024);

             final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
             out = new BufferedOutputStream(dataStream, 4 * 1024);
             copy(in, out);
             out.flush();

             final byte[] data = dataStream.toByteArray();
             bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//             Log.e(LOG_TAG, "bitmap returning something");
             return bitmap;
         } catch (IOException e) {
//             Log.e(LOG_TAG, e.getMessage());
         } finally {
             closeStream(in);
             closeStream(out);
         }
//         Log.e(LOG_TAG, "bitmap returning null");
         return null;
    }
    
    public static Drawable getDrawableFromUrl(URL url) {
        try {
            InputStream is = url.openStream();
            Drawable d = Drawable.createFromStream(is, "src");
            return d;
        } catch (MalformedURLException e) {
//            e.printStackTrace();
        } catch (IOException e) {
//            e.printStackTrace();
        }
        return null;
    }
    
    private static void copy(InputStream in, OutputStream out) throws IOException {
       byte[] b = new byte[4 * 1024];
       int read;
       while ((read = in.read(b)) != -1) {
           out.write(b, 0, read);
       }
   }
    
    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
//              Log.e(LOG_TAG, e.getMessage());
            }
        }
    }
    
  
    
    public static String constructThumbnail(String s) {
        int dotIdx = s.lastIndexOf(".");
        return "";// Constants.LT_BASE_URL() + s.substring(0, dotIdx) + "-thumb" + s.substring(dotIdx);
    }
    
  
    public static String data_file_name(String local_id){
    	return data_path() + local_id + ".jpg";
    }
    

    public static String data_path(){
    	String path = "/data/data/com.letian" + "/letian/";
    	File[] files = (new File(path)).listFiles();
//    	for(File f : files ){
//    		 if(f.lastModified() < (System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 30)){
//    			 f.delete();
//    		 }
//    	}
    	return path;
    }
    
    public static String rand_file_name(){
    	  Random r = new Random();
    	  return Long.toString(Math.abs(r.nextLong()), 36);
    }
    
    public static boolean file_exists(String temp_file_name){
    	File f = new File(data_file_name(temp_file_name));
    	return f.exists();
    }
    
    public static boolean remove_file(String temp_file_name){
    	
    	File f = new File(data_file_name(temp_file_name));
    	
    	boolean r = f.delete();
    	return r;
    }
    
}
