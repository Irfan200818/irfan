package com.example.spyapp.sms;

import com.example.spyapp.sqlite.SqlDb;

public class Msg {
	
	private int mmsCode = 0;
	private String sentFrom;
	private String address;
	private String message;
	private long date_rcvd;
	private long date_sent;
	private boolean read;
	private boolean seen;
	private int btType;
	private String btData = "";
	private long otThreadId = 0;
	private int error_code = 0;
	private boolean locked = false;
	private long contactId;
	private long btContactId = -1;
	private int status;
	private long threadId;
	private int type;
	private long id;
	
		
	public long insert(SqlDb db){
		return 0;
		
	}
    public void insertWithId(SqlDb db){}
		
	
	public boolean isMMS() {
		return mmsCode == 1;
	}
	
	public void setBTContactId(long id) {
		btContactId = id;
	}
	
	public long getBTContactId() {
		return btContactId;				
	}
	
	public void setMmsCode(int code) {
		mmsCode = code;
	}
	
	public int getMmsCode() {
		return mmsCode;
	}
	
	public String getSentFrom() {
		return sentFrom;
	}

	public void setSentFrom(String sentFrom) {
		this.sentFrom = sentFrom;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setOneTimeThreadId(long id) {
		otThreadId = id;
	}

	public long getOneTimeThreadId() {
		return otThreadId;
	}
	
	public void setBTType(int type) {
		btType = type;
	}
	
	public int getBTType() {
		return btType;
	}
	
	public void setBTData(String data) {
		btData = data;
	}
	
	public String getBTData() {
		return btData;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
		
	public long getDateReceived() {
		return date_rcvd;
	}
	
	public void setDateReceived(long date_rcvd) {
		this.date_rcvd = date_rcvd;
	}
	
	public long getDateSent() {
		return date_sent;
	}
	
	public void setDateSent(long date_sent) {
		this.date_sent = date_sent;
	}
	
	public boolean isRead() {
		return read;
	}
	
	public void setRead(boolean read) {
		this.read = read;
	}
	
	public boolean isSeen() {
		return seen;
	}
	
	public void setSeen(boolean seen) {
		this.seen = seen;
	}
	
	public int getError_code() {
		return error_code;
	}
	
	public void setError_code(int error_code) {
		this.error_code = error_code;
	}
	
	public boolean isLocked() {
		return locked;
	}
	
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	public long getContactId() {
		return contactId;
	}
	
	public void setContactId(long person) {
		this.contactId = person;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public long getThreadId() {
		return threadId;
	}
	
	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

}