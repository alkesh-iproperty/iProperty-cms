
package com.oozeetech.iproperty_cms.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentDetailsResponse {

    @SerializedName("st")
    @Expose
    public long st;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("data")
    @Expose
    public List<Datum> data = new ArrayList<Datum>();

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

    public class Hisory {

        @SerializedName("amount")
        @Expose
        public String amount;
        @SerializedName("penalty_amount")
        @Expose
        public String penaltyAmount;
        @SerializedName("total_amount")
        @Expose
        public String totalAmount;
        @SerializedName("pay_date")
        @Expose
        public String payDate;
        @SerializedName("paid_by")
        @Expose
        public String paidBy;
        @SerializedName("transaction_id")
        @Expose
        public String transactionId;
        @SerializedName("transaction_status")
        @Expose
        public String transactionStatus;
        @SerializedName("bank_name")
        @Expose
        public String bankName;
        @SerializedName("cheque_no")
        @Expose
        public String chequeNo;
        @SerializedName("cheque_date")
        @Expose
        public String chequeDate;
        @SerializedName("datetime")
        @Expose
        public String datetime;

    }

    public class Datum {

        @SerializedName("can_pay")
        @Expose
        public String canPay;
        @SerializedName("pay_date")
        @Expose
        public String payDate;
        @SerializedName("amount")
        @Expose
        public String amount;
        @SerializedName("penalty_amount")
        @Expose
        public String penaltyAmount;
        @SerializedName("total_amount")
        @Expose
        public String totalAmount;
        @SerializedName("paypal")
        @Expose
        public Paypal paypal;
        @SerializedName("hisory")
        @Expose
        public List<Hisory> hisory = new ArrayList<Hisory>();

    }
}
