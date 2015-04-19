package com.example.spyapp.listenoutgoingcall;

public interface Observer {
    /**
     * Get notified by the monitor to which this observer
     * registered.
     * 
     * @param event A device context
     */
    void observed(Event event);

}