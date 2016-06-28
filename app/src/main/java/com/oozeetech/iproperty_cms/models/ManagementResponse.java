package com.oozeetech.iproperty_cms.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by divyeshshani on 22/06/16.
 */
public class ManagementResponse {


    @SerializedName("st")
    public long st;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public List<Datum> data = new ArrayList<Datum>();

    public class Datum {

        @SerializedName("mgmt_id")
        public String mgmtId;
        @SerializedName("name")
        public String name;
        @SerializedName("designation")
        public String designation;
        @SerializedName("photo")
        public String photo;
        @SerializedName("mobile_number")
        public String mobileNumber;
        @SerializedName("description")
        public String description;

    }
}
