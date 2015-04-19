package com.example.spyapp.listenoutgoingcall;

public interface Content<T> {

    /**
     * Save the content for other purposes.
     * 
     * @param content
     *            The content to be saved.
     */
    public void setContent(T content);

    /**
     * Get the content :D (very silly comment =)))
     * 
     * @return The content inside this content :D
     */
    public T getContent();

}
