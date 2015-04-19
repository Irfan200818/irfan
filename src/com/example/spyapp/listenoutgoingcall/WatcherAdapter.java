package com.example.spyapp.listenoutgoingcall;

public abstract class WatcherAdapter implements Watcher {
    private Reporter reporter;
    
    public void setReporter(Reporter reporter) {
            this.reporter = reporter;
    }
    
    public Reporter getReporter() {
            return reporter;
    }
    
    @Override
    public void start(Event dc) {
    }
    
    @Override
    public void stop(Event dc) {
    }
}