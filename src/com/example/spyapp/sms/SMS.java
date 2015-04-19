package com.example.spyapp.sms;

import com.example.spyapp.sqlite.SqlDb;

import android.content.ContentValues;
import android.database.Cursor;



public class SMS extends Msg {
			
	private String body;
	
	
	public SMS(String address, String message) {
		setAddress(address);
		setMessage(message);
	}
	
	public String getMessage() {
		return body;
	}
	
	public void setMessage(String body) {
		this.body = body;
	}
	
	public SMS(Cursor c) {
		getSMSFromCursor(c);
	}
	
	public long insert(SqlDb db) {
		ContentValues v = new ContentValues();
		v.put(SMSUtil.ADDRESS, getAddress());
		v.put(SMSUtil.BODY, getMessage());
		v.put(SMSUtil.DATE, getDateSent());
		v.put(SMSUtil.TYPE, getType());
		v.put(SMSUtil.MESSAGE_ID, getId());
		return db.insert(SMSUtil.TBL_SENT_SMS, v);		
	}
	
	
	public long insertWEb(SqlDb db) {
		ContentValues v = new ContentValues();
		v.put(SMSUtil.ADDRESS, getAddress());
		v.put(SMSUtil.BODY, getMessage());
		v.put(SMSUtil.DATE, getDateSent());
		v.put(SMSUtil.TYPE, getType());
		v.put(SMSUtil.MESSAGE_ID, getId());
		return db.insert(SMSUtil.TBL_SENT_SMS, v);		
	}
	
	public long insertSentThread(SqlDb db) {
		ContentValues v = new ContentValues();
		v.put(SMSUtil.ADDRESS, getAddress());
		v.put(SMSUtil.BODY, getMessage());
		v.put(SMSUtil.DATE, getDateReceived());
		return db.insert(SMSUtil.INBOX_TBL_SMS, v);		
	}

    public void insertWithId(SqlDb db) {
        ContentValues v = new ContentValues();
        v.put(SMSUtil.ADDRESS, getAddress());
        v.put(SMSUtil.BODY, getMessage());
        v.put(SMSUtil.DATE, getDateReceived());
        v.put("_id", getId());
        db.insert(SMSUtil.INBOX_TBL_SMS, v);
    }
	
	public void getSMSFromCursor(Cursor cursor) {
		String num = cursor.getString(cursor.getColumnIndex(SMSUtil.ADDRESS));
		String msg = cursor.getString(cursor.getColumnIndex(SMSUtil.BODY));
		setAddress(num);
		setMessage(msg);
		setDateReceived(cursor.getLong(cursor.getColumnIndex(SMSUtil.DATE)));
		setMmsCode(0);
		setId(cursor.getLong(cursor.getColumnIndex("_id")));		
	}

}