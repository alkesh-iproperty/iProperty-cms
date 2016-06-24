package com.oozeetech.iproperty_cms.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by divyeshshani on 22/06/16.
 */
public class Login {

    @SerializedName("st")
    public int st;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public List<Datum> data;

    public class Datum {

        @SerializedName("res_id")
        public String resId;
        @SerializedName("condo_name")
        public String condoName;
        @SerializedName("condo_id")
        public String condoId;
        @SerializedName("title")
        public String title;
        @SerializedName("first_name")
        public String firstName;
        @SerializedName("last_name")
        public String lastName;
        @SerializedName("resident_id")
        public String residentId;
        @SerializedName("otp")
        public String otp;
        @SerializedName("currency_code")
        public String currencyCode;
        @SerializedName("email")
        public String email;
        @SerializedName("contact_no")
        public String contactNo;
        @SerializedName("latitude")
        public String latitude;
        @SerializedName("longitude")
        public String longitude;
        @SerializedName("app_login")
        public String appLogin;
        @SerializedName("block_id")
        public String blockId;
        @SerializedName("block_name")
        public String blockName;
        @SerializedName("unit_id")
        public String unitId;
        @SerializedName("unit_name")
        public String unitName;
        @SerializedName("mobile_no")
        public String mobileNo;
        @SerializedName("maintance_amount")
        public String maintanceAmount;
        @SerializedName("slider_images")
        public List<String> sliderImages = new ArrayList<String>();
        @SerializedName("payment_gateway")
        public PaymentGateway paymentGateway;
        @SerializedName("token")
        public String token;

    }

    public class PaymentGateway {

        @SerializedName("account_type")
        public String accountType;
        @SerializedName("mode")
        public String mode;
        @SerializedName("live_url")
        public String liveUrl;
        @SerializedName("sandbox_url")
        public String sandboxUrl;
        @SerializedName("merchant_key")
        public String merchantKey;
        @SerializedName("merchant_code")
        public String merchantCode;

    }
}
