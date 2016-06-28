package com.oozeetech.iproperty_cms.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by divyeshshani on 24/06/16.
 */
public class MasterFacilityResponse {

    @SerializedName("st")
    public long st;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public List<Datum> data = new ArrayList<Datum>();

    public class Datum {

        @SerializedName("fm_id")
        public String fmId;
        @SerializedName("fm_name")
        public String fmName;
        @SerializedName("total_no_of_facility")
        public String totalNoOfFacility;
        @SerializedName("book_limit_per_month")
        public String bookLimitPerMonth;
        @SerializedName("advance_book_period")
        public String advanceBookPeriod;
        @SerializedName("payment_type")
        public String paymentType;
        @SerializedName("images")
        public String images;
        @SerializedName("icon_image")
        public String iconImage;
        @SerializedName("details")
        public String details;
    }
}
