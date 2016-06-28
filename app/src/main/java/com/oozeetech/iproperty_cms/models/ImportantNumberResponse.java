package com.oozeetech.iproperty_cms.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by divyeshshani on 22/06/16.
 */
public class ImportantNumberResponse {

    public class Datum {

        @SerializedName("emn_id")
        public String emnId;
        @SerializedName("condo_id")
        public String condoId;
        @SerializedName("name")
        public String name;
        @SerializedName("number")
        public String number;

    }

    @SerializedName("st")
    public long st;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public List<Datum> data = new ArrayList<Datum>();


}
