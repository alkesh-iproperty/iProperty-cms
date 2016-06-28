
package com.oozeetech.iproperty_cms.models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class GalleryDetailsListResponse {

    @SerializedName("st")
    public long st;
    @SerializedName("msg")
    public String msg;
    @SerializedName("data")
    public List<Datum> data = new ArrayList<Datum>();

	public class Datum {

    @SerializedName("gi_id")
    public String giId;
    @SerializedName("g_id")
    public String gId;
    @SerializedName("condo_id")
    public String condoId;
    @SerializedName("name")
    public String name;
    @SerializedName("photo_name")
    public String photoName;
    @SerializedName("photo_desc")
    public String photoDesc;
    @SerializedName("datetime")
    public String datetime;

}
}
