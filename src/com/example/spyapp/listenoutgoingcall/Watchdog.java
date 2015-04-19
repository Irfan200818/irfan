package com.example.spyapp.listenoutgoingcall;

public interface Watchdog {
    
    /**
     * Dispatch the observable data to the registered observers.
     * 
     * @param event
     */
    void watch(Event event);
    
    /**
     * Register an observer.
     * 
     * @param observer
     */
    void register(Observer observer);
    
    /**
     * Remove an observer from registration.
     * 
     * @param observer
     */
    void unregister(Observer observer);

    /**
     * Unregister all observers
     */
    void clear();
}
