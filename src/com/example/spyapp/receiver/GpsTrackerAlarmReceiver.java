package com.example.spyapp.receiver;

import com.example.spyapp.util.MyLocationListener;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.content.WakefulBroadcastReceiver;

// make sure we use a WakefulBroadcastReceiver so that we acquire a partial wakelock
public class GpsTrackerAlarmReceiver extends WakefulBroadcastReceiver {
    private static final String TAG = "GpsTrackerAlarmReceiver";
    public LocationManager locationManager;
	boolean isWebHistoryTrackingEnable=false;
	public Location location;
    @Override
    public void onReceive(Context context, Intent intent) {
    	String location_context = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) context.getSystemService(location_context);
		MyLocationListener myLocationListener = new MyLocationListener(context);
		locationManager.requestLocationUpdates(
		LocationManager.GPS_PROVIDER, 1000, 1, myLocationListener);
    }
}
