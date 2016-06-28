
package com.oozeetech.iproperty_cms.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookingListResponse {

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

        @SerializedName("fb_id")
        @Expose
        public String fbId;
        @SerializedName("fm_id")
        @Expose
        public String fmId;
        @SerializedName("fm_name")
        @Expose
        public String fmName;
        @SerializedName("res_id")
        @Expose
        public String resId;
        @SerializedName("fc_id")
        @Expose
        public String fcId;
        @SerializedName("fc_name")
        @Expose
        public String fcName;
        @SerializedName("time_slot_start")
        @Expose
        public String timeSlotStart;
        @SerializedName("time_slot_end")
        @Expose
        public String timeSlotEnd;
        @SerializedName("payment_type")
        @Expose
        public String paymentType;
        @SerializedName("insufficient_deposit")
        @Expose
        public String insufficientDeposit;
        @SerializedName("amount")
        @Expose
        public String amount;
        @SerializedName("deposit_amount")
        @Expose
        public String depositAmount;
        @SerializedName("transaction_status")
        @Expose
        public String transactionStatus;
        @SerializedName("transaction_id")
        @Expose
        public String transactionId;
        @SerializedName("datetime")
        @Expose
        public String datetime;
        @SerializedName("approve")
        @Expose
        public String approve;
        @SerializedName("booking_changed")
        @Expose
        public String bookingChanged;
        @SerializedName("booking_cancelled")
        @Expose
        public String bookingCancelled;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("is_cancel_or_change")
        @Expose
        public String isCancelOrChange;

    }
}
