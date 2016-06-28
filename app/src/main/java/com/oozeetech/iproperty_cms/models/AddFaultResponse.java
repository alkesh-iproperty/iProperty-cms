package com.oozeetech.iproperty_cms.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by divyeshshani on 25/06/16.
 */
public class AddFaultResponse {

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

        @SerializedName("f_id")
        @Expose
        public String fId;

    }
}
