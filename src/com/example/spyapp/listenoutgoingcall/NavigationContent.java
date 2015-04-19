package com.example.spyapp.listenoutgoingcall;

public class NavigationContent extends DomTreeContent {

    /**
     * List of all urls in the content.
     */
    private String[] urls;

    /**
     * Construct a navigation object with the given URLs
     * 
     * @param urls
     *            The urls that can be used to travel next...
     */
    public NavigationContent(DomTreeContent content, String[] urls) {
            super(content.getContent(), content.getDocument());
            this.urls = urls;
    }

    /**
     * Just return what it's holding...
     * 
     * @return The list of urls.
     */
    public String[] getNextURLs() {
            return urls;
    }
}
