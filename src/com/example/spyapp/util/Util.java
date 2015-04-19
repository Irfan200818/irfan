package com.example.spyapp.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Util {

	public static boolean isMainServiceRunning(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.example.spyapp.Services".equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isNetworkAvaiable(Context context){
		 ConnectivityManager connectivityManager 
        = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
  NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
  return activeNetworkInfo.isConnected();
	}
}
