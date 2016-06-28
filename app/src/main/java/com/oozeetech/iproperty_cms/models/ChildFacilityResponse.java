package com.oozeetech.iproperty_cms.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by divyeshshani on 25/06/16.
 */
public class ChildFacilityResponse {

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

        @SerializedName("payment_type")
        @Expose
        public String paymentType;
        @SerializedName("date")
        @Expose
        public String date;
        @SerializedName("advance_book_period_start")
        @Expose
        public String advanceBookPeriodStart;
        @SerializedName("advance_book_period")
        @Expose
        public String advanceBookPeriod;
        @SerializedName("images")
        @Expose
        public List<String> images = new ArrayList<String>();
        @SerializedName("paypal")
        @Expose
        public Paypal paypal;
        @SerializedName("change_bk")
        @Expose
        public String changeBk;
        @SerializedName("timeslots")
        @Expose
        public List<Timeslot> timeslots = new ArrayList<Timeslot>();
    }

    public class Datum_ {

        @SerializedName("ft_id")
        @Expose
        public String ftId;
        @SerializedName("fm_id")
        @Expose
        public String fmId;
        @SerializedName("fc_id")
        @Expose
        public String fcId;
        @SerializedName("fc_name")
        @Expose
        public String fcName;
        @SerializedName("amount")
        @Expose
        public String amount;
        @SerializedName("deposit_amount")
        @Expose
        public String depositAmount;
        @SerializedName("booked")
        @Expose
        public String booked;

    }

    public class Paypal {

        @SerializedName("mode")
        @Expose
        public String mode;
        @SerializedName("live_url")
        @Expose
        public String liveUrl;
        @SerializedName("sandbox_url")
        @Expose
        public String sandboxUrl;

    }

    public class Timeslot {

        @SerializedName("slot")
        @Expose
        public String slot;
        @SerializedName("data")
        @Expose
        public List<Datum_> data = new ArrayList<Datum_>();

    }

}
