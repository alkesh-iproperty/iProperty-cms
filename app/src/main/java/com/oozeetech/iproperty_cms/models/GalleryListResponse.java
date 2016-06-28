
package com.oozeetech.iproperty_cms.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class GalleryListResponse {

    @SerializedName("st")
    public long st;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public List<Datum> data = new ArrayList<Datum>();

    public class Datum {

        @SerializedName("id")
        public String id;
        @SerializedName("g_id")
        public String gId;
        @SerializedName("name")
        public String name;
        @SerializedName("photo")
        public String photo;
        @SerializedName("datetime")
        public String datetime;
        @SerializedName("read")
        public String read;
        @SerializedName("res_id")
        public String resId;

    }
}
