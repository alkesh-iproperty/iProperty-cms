package com.oozeetech.iproperty_cms.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by divyeshshani on 24/06/16.
 */
public class GuestRequestList {

    @SerializedName("st")
    public long st;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public List<Datum> data = new ArrayList<Datum>();

    public class Datum {

        @SerializedName("glr_id")
        public String glrId;
        @SerializedName("condo_id")
        public String condoId;
        @SerializedName("block_id")
        public String blockId;
        @SerializedName("unit_id")
        public String unitId;
        @SerializedName("name")
        public String name;
        @SerializedName("no_of_guest")
        public String noOfGuest;
        @SerializedName("visiting_date")
        public String visitingDate;
        @SerializedName("datetime")
        public String datetime;

    }
}
