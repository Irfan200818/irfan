package com.example.spyapp.listenoutgoingcall;

public interface ConnectionListener {

    /**
     * Notify about the status of a request is being made.
     * 
     * @param event
     *            The connection event
     */
    public void notifyEvent(ConnectionEvent event);
}
