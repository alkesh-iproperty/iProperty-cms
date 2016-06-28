
package com.oozeetech.iproperty_cms.models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PushNotificationResponse {

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

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("pn_id")
    @Expose
    public String pnId;
    @SerializedName("condo_id")
    @Expose
    public String condoId;
    @SerializedName("res_id")
    @Expose
    public String resId;
    @SerializedName("datetime")
    @Expose
    public String datetime;
    @SerializedName("read")
    @Expose
    public String read;
    @SerializedName("message_title")
    @Expose
    public String messageTitle;
    @SerializedName("send_to")
    @Expose
    public String sendTo;
    @SerializedName("content_type")
    @Expose
    public String contentType;
    @SerializedName("content_type_data")
    @Expose
    public String contentTypeData;
    @SerializedName("send_on")
    @Expose
    public Object sendOn;
    @SerializedName("pn_sent")
    @Expose
    public String pnSent;

}

}
