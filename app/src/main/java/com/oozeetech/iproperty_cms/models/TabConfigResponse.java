
package com.oozeetech.iproperty_cms.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class TabConfigResponse {

    @SerializedName("st")
    public long st;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public List<Datum> data = new ArrayList<Datum>();

    public class Datum {

        @SerializedName("facility")
        public String facility;
        @SerializedName("fault_reporting")
        public String faultReporting;
        @SerializedName("emergency_no")
        public String emergencyNo;
        @SerializedName("notice")
        public String notice;
        @SerializedName("management")
        public String management;
        @SerializedName("event")
        public String event;
        @SerializedName("gallery")
        public String gallery;
        @SerializedName("website")
        public String website;
        @SerializedName("guest_list_request")
        public String guestListRequest;
        @SerializedName("maintenance")
        public String maintenance;
        @SerializedName("quit_rent")
        public String quitRent;
        @SerializedName("fire_insurance")
        public String fireInsurance;
        @SerializedName("sinking_fund")
        public String sinkingFund;
        @SerializedName("push_notification")
        public String pushNotification;

    }
}
