package com.example.spyapp.util;

import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;


public class Prefs {
	

	private final Context context;
	private String prefsFileName = "AB_Prefs";
	private SharedPreferences prefs;
	

	public Prefs(Context context) {
		this.context = context;
		loadPrefsInstance();
	}

	/**
	 * Sets the filename of this prefs instance.
	 * 
	 * @param filename
	 */
	public void setFileName(String filename) {
		prefsFileName = filename;
		loadPrefsInstance();
	}

	/**
	 * 
	 * @return the filename this prefs instance is storing to
	 */
	public String getFileName() {
		return prefsFileName;
	}

	/**
	 * Store a boolean to shared prefs
	 * 
	 * @param tag The name to store this boolean under
	 * @param valueToStore
	 */
	public void StoreBoolean(String tag, boolean valueToStore) {
		prefs.edit().putBoolean(tag, valueToStore).commit();
	}

	/**
	 * Store an integer to prefs
	 * 
	 * @param tag The name to store this integer under
	 * @param valueToStore
	 */
	public void StoreInt(String tag, int valueToStore) {
		prefs.edit().putInt(tag, valueToStore).commit();
	}

	/**
	 * Store a Long to prefs
	 * 
	 * @param tag  The name to store this long under
	 * @param valueToStore
	 */
	public void StoreLong(String tag, long valueToStore) {
		prefs.edit().putLong(tag, valueToStore).commit();
	}

	/**
	 * Store a String to prefs
	 * 
	 * @param tag  The name to store this String under
	 * @param valueToStore
	 */
	public void StoreString(String tag, String valueToStore) {
		prefs.edit().putString(tag, valueToStore).commit();
	}

	/**
	 * 
	 * @param tag The name of the boolean to retrieve from prefs
	 * @return false if none found
	 */
	public boolean GetBoolean(String tag) {
		return prefs.getBoolean(tag, false);		
	}
	
	/**
	 * This returns null, if nothing was found for that tag.
	 * 
	 * @param tag
	 * @return
	 */
	public Set<String> getStringSet(String tag) {
		return prefs.getStringSet(tag, null);
	}
	
	public Set<String> getStringSet(String tag, Set<String> defVal) {
		return prefs.getStringSet(tag, defVal);
	}
	
	public void storeStringSet(String tag, Set<String> set) {
		prefs.edit().putStringSet(tag, set).commit();
	}

	/**
	 * 
	 * @param tag The name of the integer to retrieve from prefs
	 * @return 0 if none found
	 */
	public int GetInt(String tag) {
		return prefs.getInt(tag, 0);
	}

	/**
	 * 
	 * @param tag The name of the long to retrieve from prefs
	 * @return 0 if none found
	 */
	public long GetLong(String tag) {
		return prefs.getLong(tag, 0);
	}

	/**
	 * 
	 * @param tag The name of the String to retrieve from prefs
	 * @return "" if none found
	 */
	public String GetString(String tag) {
		return prefs.getString(tag, "");
	}

	/**
	 * 
	 * @param tag The name of the boolean to retrieve from prefs
	 * @param defValue A default value to return if none found
	 * @return
	 */
	public boolean GetBoolean(String tag, boolean defValue) {
		return prefs.getBoolean(tag, defValue);
	}

	/**
	 * 
	 * @param tag The name of the integer to retrieve from prefs
	 * @param defValue A default value to return if none found
	 * @return
	 */
	public int GetInt(String tag, int defValue) {
		return prefs.getInt(tag, defValue);
	}

	/**
	 * 
	 * @param tag The name of the long to retrieve from prefs
	 * @param defValue A default value to return if none found
	 * @return
	 */
	public long GetLong(String tag, long defValue) {
		return prefs.getLong(tag, defValue);
	}

	/**
	 * 
	 * @param tag The name of the String to retrieve from prefs
	 * @param defValue A default value to return if none found
	 * @return
	 */
	public String GetString(String tag, String defValue) {
		return prefs.getString(tag, defValue);
	}
	
	private void loadPrefsInstance() {
		prefs = context.getSharedPreferences(Const.PreferencesName, Context.MODE_PRIVATE);
	}

}