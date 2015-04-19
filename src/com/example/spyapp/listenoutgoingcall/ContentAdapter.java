package com.example.spyapp.listenoutgoingcall;

public class ContentAdapter<T> implements Content<T> {

    /**
     * The quintessence of all data we are trying to process.
     */
    private T content;

    public void setContent(T content) {
            this.content = content;
    }

    public T getContent() {
            return content;
    }
}