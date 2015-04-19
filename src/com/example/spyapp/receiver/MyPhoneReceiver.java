/*
 *  Copyright 2012 Kobi Krasnoff
 * 
 * This file is part of Call recorder For Android.

    Call recorder For Android is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Call recorder For Android is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Call recorder For Android.  If not, see <http://www.gnu.org/licenses/>
 */
package com.example.spyapp.receiver;

import java.io.File;

import com.example.spyapp.MainActivity;
import com.example.spyapp.util.Const;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Environment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MyPhoneReceiver extends BroadcastReceiver {

	private String phoneNumber;
	MediaRecorder mediaRecorder = new MediaRecorder();
	boolean isRecording=true;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		 if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
	            String numberToCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
	            Log.d("CallRecorder", "CallBroadcastReceiver intent has EXTRA_PHONE_NUMBER: " + numberToCall);
	        }
//		PhoneLogListener phoneListener = new PhoneLogListener(context);
//        TelephonyManager telephony = (TelephonyManager)
//        context.getSystemService(Context.TELEPHONY_SERVICE);
//        telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        
	}

}
