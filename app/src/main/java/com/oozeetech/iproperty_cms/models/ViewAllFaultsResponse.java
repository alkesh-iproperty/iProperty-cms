package com.oozeetech.iproperty_cms.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by divyeshshani on 25/06/16.
 */
public class ViewAllFaultsResponse {

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

        @SerializedName("fault_id")
        @Expose
        public String faultId;
        @SerializedName("fault_photo")
        @Expose
        public String faultPhoto;

    }
}
