package com.oozeetech.iproperty_cms.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by divyeshshani on 23/06/16.
 */
public class EventsResponse {

    @SerializedName("st")
    public long st;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public List<Datum> data = new ArrayList<Datum>();

    public class Datum {

        @SerializedName("id")
        public String id;
        @SerializedName("event_id")
        public String eventId;
        @SerializedName("condo_id")
        public String condoId;
        @SerializedName("event_type")
        public String eventType;
        @SerializedName("event_name")
        public String eventName;
        @SerializedName("event_desc")
        public String eventDesc;
        @SerializedName("start_time")
        public String startTime;
        @SerializedName("end_time")
        public String endTime;
        @SerializedName("read")
        public String read;
        @SerializedName("res_id")
        public String resId;
        @SerializedName("total_person")
        public String totalPerson;
        @SerializedName("event_img")
        public String eventImg;

    }
}
