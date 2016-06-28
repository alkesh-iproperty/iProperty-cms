
package com.oozeetech.iproperty_cms.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class FacilityBookingResponse {

    @SerializedName("st")
    public long st;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public List<Datum> data = new ArrayList<Datum>();

    public class Datum {

        @SerializedName("fb_id")
        public String fbId;
    }
}
