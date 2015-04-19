package com.example.spyapp.listenoutgoingcall;



/**
 * A response object is an object that holds a responding content. That's it :D
 * 
 * @author khoa.nguyen
 * 
 */
public class Response {

        private Content<?> content;

        /**
         * Create a response with the given content.
         * 
         * @param content
         *            The content to be carried by this object.
         */
        public Response(Content<?> content) {
                this.content = content;
        }

        /**
         * Get the embedded content.
         * 
         * @return The content inside this object.
         */
        public Content<?> getContent() {
                return content;
        }
}
