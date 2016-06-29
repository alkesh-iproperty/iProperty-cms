
package com.oozeetech.iproperty_cms.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NewBookingListResponse {

    @SerializedName("st")
    @Expose
    public long st;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("data")
    @Expose
    public List<Datum> data = new ArrayList<Datum>();

    public class Datum {

        @SerializedName("dateTime")
        @Expose
        public String dateTime;
        @SerializedName("date")
        @Expose
        public List<BookingData> bookingDetail = new ArrayList<BookingData>();
    }


}
