package com.oozeetech.iproperty_cms.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by divyeshshani on 22/06/16.
 */
public class NoticesResponse {

    @SerializedName("st")
    public long st;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public List<Datum> data = new ArrayList<Datum>();

    public class Datum {

        @SerializedName("id")
        public String id;
        @SerializedName("notice_id")
        public String noticeId;
        @SerializedName("name")
        public String name;
        @SerializedName("file_name")
        public String fileName;
        @SerializedName("read")
        public String read;
        @SerializedName("res_id")
        public String resId;

    }
}
