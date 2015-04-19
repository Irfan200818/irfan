package com.example.spyapp.listenoutgoingcall;

public interface Watcher {
    /**
     * Start monitoring.
     *   
     * @param dc
     */
    void start(Event dc);
    
    /**
     * Stop monitoring.
     * 
     * @param dc
     */
    void stop(Event dc);
    
    /**
     * @return The observer.
     */
    Observer getObserver();
    
    /**
     * @return The reporter.
     */
    Reporter getReporter();
}
