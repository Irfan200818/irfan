package com.example.spyapp.listenoutgoingcall;

import android.util.Log;


public class Logger {
        private static Logger logger = new Logger();

        private boolean isDebug = true;
        
        public static Logger getDefault() {
                return logger;
        }

        public boolean isDebug() {
                return isDebug;
        }
        
        public void error(String message, Throwable e) {
                Log.e("MobileSpyService", message, e);
        }

        public void debug(String message) {
                if (isDebug) {
                        Log.d("MobileSpyService", message);
                }
        }

        public void info(String message) {
                Log.i("MobileSpyService", message);
        }

        public void trace(String message) {
                if (isDebug) {
                        Log.d("MobileSpyService", message);
                }
        }

}

