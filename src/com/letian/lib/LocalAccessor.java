package com.letian.lib;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.letian.model.User;
import com.letian.lib.IOUtils;




public class LocalAccessor{

	private static final String DATABASE_NAME = "letian";	

	
	private SharedPreferences prefs = null;	
	private Context ctx;
	
	

	private LocalAccessor(Context ctx){
		this.ctx = ctx;
		prefs = ctx.getSharedPreferences(IOUtils.PREFS_FILE, Context.MODE_PRIVATE);
	}	
	


	static private LocalAccessor accessor; 
	
	public static LocalAccessor getInstance(Context context){		
		if(accessor == null){
			accessor = new LocalAccessor(context);
		}
		return accessor;
	}
	
	public SQLiteDatabase openDB(){		
		SQLiteDatabase  db = ctx.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
		return db;
	}
	
	public String get_server_url(){
		return prefs.getString("server_url", Constants.SERVER_PATH);
	}
	
	public String get_file_upload_path(String models,String id){
		return prefs.getString("server_url", Constants.SERVER_PATH) + "/" + models + "/" + id + "/upload_file";
	}
	
	public void set_server_url(String server_url){
		SharedPreferences.Editor editor = prefs.edit();
	      editor.putString("server_url",server_url);
	      editor.commit();
	}
	
	public void set_admin_password(String password){
		SharedPreferences.Editor editor = prefs.edit();
	    editor.putString("admin_password",password);
	   editor.commit();
	}
	
	public String get_admin_password(){
		String s = prefs.getString("admin_password", "letian");
		return (s == "" || null == s) ? "letian" : s ;
	}
	
	public void create_db(String db_create_statement){
		SQLiteDatabase db = openDB();
		db.execSQL(db_create_statement);
		db.close();
	}
	
	//insert or update(if exist) user in SharedPreferences
	public boolean updateUser(User user) throws Exception{
		SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", user.name);
        editor.putString("password", user.password);
        editor.putLong("id", user.id);
        editor.commit();
		return true;
	}
	
	//return null if no user saved
	public User getUser(){
		User ret = new User();
        ret.name = prefs.getString("username", null);
        ret.password = prefs.getString("password", null);
        ret.id = prefs.getLong("id", 0);
		return ret;
	}	
	
	public static String login_user_title(){
		String s = "abc";
		s += User.current_user.name;
		return s;
	}
	
//	//insert or update(if exist) Favorte Item in db
//	public boolean updateFavoriteItem(FavoriteItem item) throws Exception{
//		ContentValues values = new ContentValues();		
//		values.put("url", item.url);
//		values.put("title", item.title);
//		values.put("description", item.description);
//		values.put("isPublic", item.isPublic);
//		values.put("createdTime", item.createdTime.toLocaleString());
//		values.put("categoryNames", StringUtils.combineByToken(item.categoryNames,","));
//		
//		SQLiteDatabase db = openDB();
//		if(this.getFavoriteItem(item.id)== null){//insert
//			values.put("id", item.id);
//			db.insertOrThrow("FavoriteItem", null, values);			
//		}else{//update
//		    db.update("FavoriteItem", values, "id=" + item.id, null);			
//		}		
//		db.close();
//		return true;
//	}
	
//	public ArrayList<FavoriteItem> getFavoritesByPage(int pageNo, int size) throws Exception{
//		SQLiteDatabase db = openDB();		
//        Cursor c = db.query("FavoriteItem", null, null, null, null, null, "id");  
//        
//        ArrayList<FavoriteItem> ret = new ArrayList<FavoriteItem>();		
//        int begin = c.getCount() - (pageNo - 1) * size - 1;
//        if(c.moveToPosition(begin)){
//        	ret.add(buildFavorite(c));
//        }
//        int i = 0;
//        while(i != size - 1 && c.moveToPrevious()) {
//        	ret.add(buildFavorite(c));
//        	i++;
//        }
//        
//        c.close();
//        db.close();
//        return ret;	
//   }
//	
//	
//	private FavoriteItem buildFavorite(Cursor c) {
//		FavoriteItem ret = new FavoriteItem();            
//        ret.id = c.getLong(0);
//        ret.url = c.getString(1); 
//        ret.title = c.getString(2);
//        ret.description = c.getString(3);
//        ret.categoryNames = StringUtils.spliteByToken(c.getString(4),",");
//        ret.createdTime = new Date(c.getString(5));
//        ret.isPublic = c.getInt(6) == 1 ? true:false;
//		return ret; 
//	}
//
//
//	//return null if no item found
//	public FavoriteItem getFavoriteItem(long id) throws Exception{
//		FavoriteItem ret = null;
//		SQLiteDatabase db = openDB();
//        Cursor c = db.query("FavoriteItem", null, "id="+id, null, null, null, null);        
//        if (c.moveToFirst()) {            
//            ret = buildFavorite(c);
//        }
//        c.close();
//        db.close();
//        return ret;
//	
//	}	
	public int deleteFavoriteItem(long id) throws Exception{
		SQLiteDatabase db = openDB();
		int count = db.delete("FavoriteItem", "id="+id, null );
		db.close();
		return count;
	}
	
	public void clearFavorites() {
		SQLiteDatabase db = openDB();		
		db.delete("FavoriteItem", null, null);
		db.close();
		
	}
	
	public boolean isFavoritesEmpty(){
		SQLiteDatabase db = openDB();		
		Cursor c = db.query("FavoriteItem", null, null, null, null, null, null);
		boolean ret = c.getCount() == 0 ? true : false ;
		c.close();
		db.close();
		return ret;
	}
//
//	//insert or update(if exist) Favorte Item
//	public boolean updateMessage(Message message) throws Exception{
//		ContentValues values = new ContentValues();
//		values.put("body", message.body);
//		values.put("title", message.title);
//		values.put("senderName", message.sender.name);
//		values.put("senderLogo", message.sender.logo);
//		values.put("senderDomain", message.sender.domain);
//		values.put("receiverName", message.receiver.name);
//		values.put("receiverLogo", message.receiver.logo);
//		values.put("receiverDomain", message.receiver.domain);
//		values.put("replyId", message.replyId);
//		values.put("isSystem", message.isSystem);
//		values.put("isRead", message.isRead);
//		values.put("isAttached", message.isAttached);
//		values.put("createdTime", message.createdTime.toLocaleString());
//		
//		SQLiteDatabase db = openDB();
//		if(this.getMessage(message.id)== null){//insert
//			values.put("id", message.id);
//			db.insertOrThrow("Message", null, values);			
//		}else{//update
//		    db.update("Message", values, "id=" + message.id, null);			
//		}
//		db.close();
//		return true;
//	}
//	//return null if no Message found
//	public Message getMessage(long id) throws Exception{
//		Message ret = null;
//		SQLiteDatabase db = openDB();
//        Cursor c = db.query("Message", null, "id="+id, null, null, null, null);
//        
//        if (c.moveToFirst()) {            
//            ret = new Message();
//            ret.id = c.getLong(0);
//            ret.body = c.getString(1);
//    		ret.title = c.getString(2);
//    		ret.sender.name = c.getString(3);
//    		ret.sender.logo = c.getString(4);
//    		ret.sender.domain = c.getString(5);
//    		ret.receiver.name = c.getString(6);
//    		ret.receiver.logo = c.getString(7);
//    		ret.receiver.domain = c.getString(8);
//    		ret.replyId = c.getLong(9);
//    		ret.isSystem = c.getInt(10) == 1 ? true : false;
//    		ret.isRead = c.getInt(11) == 1 ? true : false;
//    		ret.isAttached = c.getInt(12) == 1 ? true : false;
//    		ret.createdTime = new Date(c.getString(13));    		
//        }
//        
//        c.close();
//        db.close();
//        return ret;	
//	}
//	
//	public int deleteMessage(long id) throws Exception{
//		SQLiteDatabase db = openDB();
//		int count = db.delete("Message", "id=" + id, null);
//		db.close();
//		return count;
//	}
	

	

//	private void testDBMessage() {
//		Message message = new Message();
//		message.id = 1;
//		message.body = "message body";
//		message.title = "my title";
//		message.sender.name= "li xuechen";
//		message.sender.logo = "li xuechen' logo";
//		message.sender.domain = "li xuechen's domain";
//		message.receiver.name = "";
//		message.receiver.logo = "";
//		message.receiver.domain = "";
//		message.replyId = -1;
//		message.isSystem = false;
//		message.isRead = false;
//		message.isAttached = false;
//		message.createdTime = new Date();		
//		
//		try {
//			updateMessage(message);			
//			Message temp0 = getMessage(1);
//			temp0.title = "i changed it";
//			temp0.isRead = true;
//			updateMessage(temp0);
//			Message item3 = this.getMessage(1);
//			int count = this.deleteMessage(1);
////			int count2 =this.deleteMessage(1);
//			Message item4 = this.getMessage(1);
//			item4 = null;
//		} catch (Exception e) {
////			Log.e(LOG_TAG, e.getMessage());
//		}
//	}
//	
//	private void testDBFavoriteItem() {
//		FavoriteItem item = new FavoriteItem();
//		item.id = 1;
//		item.isPublic = true;
//		item.title = "my title";
//		item.url = "http://www.google.com";
//		item.createdTime = new Date();
//		item.description = "";
//		item.categoryNames = new ArrayList<String>();
//		item.categoryNames.add("niceinc");
//		item.categoryNames.add("mytype");
//		
//		try {
//			this.updateFavoriteItem(item);
//			FavoriteItem item2 = this.getFavoriteItem(1);
//			item2.title = "i changed it";
//			item2.isPublic = false;
//			this.updateFavoriteItem(item2);
//			FavoriteItem item3 = this.getFavoriteItem(1);
//			int count = this.deleteFavoriteItem(1);
////			int count2 =this.deleteFavoriteItem(1);
//			FavoriteItem item4 = this.getFavoriteItem(1);
//			item4 = null;
//		} catch (Exception e) {
////			Log.e(LOG_TAG, e.getMessage());
//		}
//	}



	
}
