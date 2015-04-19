package com.example.spyapp.listenoutgoingcall;

import java.io.InputStream;


/**
 * 
 * @author khoa.nguyen
 */
public class InputStreamContent extends ContentAdapter<InputStream> {

        public InputStreamContent(InputStream content) {
                setContent(content);
        }
}
