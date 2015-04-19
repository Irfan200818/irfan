package com.example.spyapp.listenoutgoingcall;

import java.io.InputStream;

/**
 * Represent a basic webpage content which has an {@link InputStream} and
 * a charset value.
 * 
 * @author khoa.nguyen
 */
public class WebpageContent extends InputStreamContent {

        private String charset;

        /**
         * Create a web content.
         * 
         * @param content
         *              The raw content in stream format.
         * @param charset
         *              The charset.
         */
        public WebpageContent(InputStream content, String charset) {
                super(content);
                this.charset = charset;
        }

        /**
         * The charset which is originally collected from the server.
         * 
         * @return
         *              The charset.
         */
        public String getCharset() {
                return charset;
        }
}