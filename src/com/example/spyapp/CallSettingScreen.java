package com.example.spyapp;
import com.example.spyapp.util.Const;
import com.example.spyapp.util.Prefs;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

public class CallSettingScreen extends Activity {

	boolean callBtnClick=true;
	boolean smsBtnClick=true;
	boolean webBtnClick=true;
	
	
	Prefs sharedpreferences;
	private int callFormat = 0;
	private int CallDirection = 0;
	
	final String direction[] = { "Both", "Uplink","DownLink"  };
	final String formats[] = { "MPEG 4", "3GPP","RAW_AMR"  };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_setting_dash_board);
		sharedpreferences=new Prefs(getApplicationContext());
		setLayoutHandlers();
		
	}

	
		
	private void setLayoutHandlers() {	
		
		((RelativeLayout) findViewById(R.id.call_format_layout)).setOnClickListener(btnClick);
		((RelativeLayout) findViewById(R.id.call_direction_layout)).setOnClickListener(btnClick);		
		
		((Switch) findViewById(R.id.call_switch_)).setOnCheckedChangeListener(new OnCheckedChangeListener() { 
		   @Override
		   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
		 
		    if(isChecked){
		    	enableSwitchButtons(true,R.id.call_switch_,Const.callSwitchPref);
		    }else{
		    	enableSwitchButtons(false,R.id.call_switch_,Const.callSwitchPref);
		    }
		 
		   }
		  });
		
		boolean call=sharedpreferences.GetBoolean(Const.callSwitchPref, false);
		callFormat=sharedpreferences.GetInt(Const.callRecordingFormatPref, 0);
		CallDirection=sharedpreferences.GetInt(Const.callRecordingDirectionPref, 0);
		
		enableSwitchButtons(call,R.id.call_switch_,Const.callSwitchPref);
		setText(formats[callFormat], R.id.call_recording_format_textview, Const.callRecordingFormatPref,callFormat);
		setText(direction[CallDirection], R.id.call_direction_textview, Const.callRecordingDirectionPref,CallDirection);
		
	}
	
	
	
	
	
	private View.OnClickListener btnClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.call_format_layout: {
//				boolean call=sharedpreferences.getBoolean(Const.callSwitchPref, false);
//				if(!call){
//					enableSwitchButtons(!call,R.id.call_switch_,Const.callSwitchPref);
//				}else{
//					enableSwitchButtons(call,R.id.call_switch_,Const.callSwitchPref);
//				}
				displayCallFormatFormatDialog();
				break;
			}
			case R.id.call_direction_layout: {				
				displayCallDirectionDialog();				
				break;
			}
			
			}
		}
	};
	
	private void displayCallFormatFormatDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		

		builder.setTitle(getString(R.string.choose_format_title))
				.setSingleChoiceItems(formats, callFormat,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								callFormat = which;
								setText(formats[which], R.id.call_recording_format_textview, Const.callRecordingFormatPref,which);
								dialog.dismiss();
							}
						}).show();
	}
	
	private void displayCallDirectionDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		

		builder.setTitle(getString(R.string.choose_format_title))
				.setSingleChoiceItems(formats, CallDirection,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								CallDirection = which;
								setText(formats[which], R.id.call_direction_textview, Const.callRecordingDirectionPref,CallDirection);
								dialog.dismiss();
							}
						}).show();
	}
	
	
		private void setText(String text,int id,String key,int value) {
			((TextView) findViewById(id)).setText(text);
			sharedpreferences.StoreInt(key, value);
		}
		
		private void enableSwitchButtons(boolean isRecording,int id,String key) {
			((Switch) findViewById(id)).setChecked(isRecording);
			sharedpreferences.StoreBoolean(key, isRecording);
		}
	
		@Override
		public void onBackPressed() {
		   Log.d("CDA", "onBackPressed Called");
		   Intent setIntent = new Intent(CallSettingScreen.this, MainActivity.class);
		   setIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		   startActivity(setIntent);
		   overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
		}
}
