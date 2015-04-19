package com.example.spyapp.listenoutgoingcall;

import java.io.File;
import java.util.Map;


public class HttpMultipartRequest extends Request {

        private File file;
        
        /**
         * Create a multipart request
         * 
         * @param url
         * @param params String parameter/value pairs. Put a parameter with *null* value
         *               in to indicate file field name.
         * @param file file to upload
         */
        public HttpMultipartRequest(String url, Map<String, String> params, File file) {
                super(url, params);
                this.file = file;
        }
        
        public File getFile() {
                return file;
        }
}
