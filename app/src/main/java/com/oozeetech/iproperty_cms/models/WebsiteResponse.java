package com.oozeetech.iproperty_cms.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by divyeshshani on 23/06/16.
 */
public class WebsiteResponse {

    @SerializedName("st")
    public long st;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    
    public List<Datum> data = new ArrayList<Datum>();

    public class Datum {

        @SerializedName("web_id")
        public String webId;
        @SerializedName("condo_id")
        public String condoId;
        @SerializedName("website")
        public String website;

    }
    
}
