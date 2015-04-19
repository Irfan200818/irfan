package com.example.spyapp.listenoutgoingcall;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * It's quite intricate to serialize objects across services, activities &
 * receivers... by using built-in parceling machanism with {@link Parcelable} &
 * {@link Parcel}. Therefore, this implementation uses an object pool to host
 * all watchers and then whenever the target handlers want a watcher, it uses
 * {@link #hashCode()} value which is passed along with the {@link Intent}
 * extras object to look up in the object pool.<br>
 * <br>
 * Normally, subclass should extend {@link #service(AndroidEvent)} callback in
 * order to handle incoming events.
 * 
 * @author khoanguyen
 */
public abstract class AndroidWatcher extends WatcherAdapter implements Observer {
        /**
         * Put interested intent's names here.
         * 
         * @return The interested intents which will be used to register, look up &
         *         invoke call-backs.
         */
        public abstract String[] getIntents();

        /**
         * No specific implementation. Subclass may want to implement this method to
         * put actual event processing code here. Beware of the source of the event
         * might be from 2 different locations: either {@link BroadcastReceiver} or
         * {@link Service}.
         * 
         * @param dc
         */
        public void service(AndroidEvent dc) {
                // This method has empty body.
        }

        /**
         * {@inheritDoc}
         * <p>
         * Default implementation will spawn a service to handle the observation.
         * Subclass may need to override this method in order to modify this
         * behavior.
         * </p>
         */
        @Override
        public void observed(Event event) {
                AndroidEvent androidEvent = (AndroidEvent)event;
                AndroidWatchdogService.start(
                                androidEvent.getContext(),
                                androidEvent.getIntent(), this);
        }

        @Override
        public Observer getObserver() {
                return this;
        }
}