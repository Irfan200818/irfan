package com.example.spyapp.listenoutgoingcall;

import java.util.Date;


public class SMS implements Report {
        public String from;
        public String to;
        public Date date;
        public String message;

        public SMS(String from, String to, String message, Date date) {
                this.from = from;
                this.to = to;
                this.message = message;
                this.date = date;
        }
}
