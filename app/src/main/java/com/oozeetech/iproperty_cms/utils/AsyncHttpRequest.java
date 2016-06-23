package com.oozeetech.iproperty_cms.utils;

import android.app.Activity;

import com.loopj.android.http.AsyncHttpClient;

public class AsyncHttpRequest extends AsyncHttpClient {

    Activity activity;

    public AsyncHttpRequest(Activity activity) {
        this.activity = activity;
    }

    public static AsyncHttpClient newRequest() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(Constants.TIMEOUT);
        return client;
    }
}
