package com.example.spyapp.sqlite;

public class DBConst {
	
	public static final String F_MAIN_DB = "MainSpy.db";
	public static final String F_SMS_DB = "SmsMmsData.db";
	
	public static final String PH_NUM = "Phone_Number";
	public static final String F_NAME = "First_Name";
	public static final String L_NAME = "Last_Name";
	
	public static final String[] CONTACTS_COLUMNS = { PH_NUM, F_NAME, L_NAME};
	public static final String[] CONTACTS_DATATYPES = { "TEXT UNIQUE", "TEXT", "TEXT"};
}
