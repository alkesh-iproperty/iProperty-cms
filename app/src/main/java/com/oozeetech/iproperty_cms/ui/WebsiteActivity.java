package com.oozeetech.iproperty_cms.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.oozeetech.iproperty_cms.R;
import com.oozeetech.iproperty_cms.models.Response;
import com.oozeetech.iproperty_cms.models.WebsiteResponse;
import com.oozeetech.iproperty_cms.utils.AsyncHttpRequest;
import com.oozeetech.iproperty_cms.utils.AsyncResponseHandler;
import com.oozeetech.iproperty_cms.utils.Constants;
import com.oozeetech.iproperty_cms.utils.Debug;
import com.oozeetech.iproperty_cms.utils.RequestParamsUtils;
import com.oozeetech.iproperty_cms.utils.URLs;
import com.oozeetech.iproperty_cms.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by divyeshshani on 22/06/16.
 */
public class WebsiteActivity extends BaseActivity {

    @Bind(R.id.wvWebsite)
    WebView wvWebSite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website_);

        ButterKnife.bind(this);

        initDrawer();

        setNavUsername();

        getWebsite();
    }


    public void getWebsite() {

        try {
            showProgress("");

            RequestParams params = new RequestParams();
            params.put(RequestParamsUtils.WS, Constants.CONDO);
            params.put(RequestParamsUtils.RES_ID, Utils.getPref(getActivity(), RequestParamsUtils.RES_ID, ""));
            params.put(RequestParamsUtils.CONDO_ID, Utils.getPref(getActivity(), RequestParamsUtils.CONDO_ID, ""));
            params.put(RequestParamsUtils.TOKEN, Utils.getPref(getActivity(), RequestParamsUtils.TOKEN, ""));
            Debug.e("getwebsite param", params.toString());

            AsyncHttpClient clientPhotos = AsyncHttpRequest.newRequest();
            clientPhotos.get(new URLs().WEB_SITE, params, new GetWebsiteHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class GetWebsiteHandler extends AsyncResponseHandler {

        public GetWebsiteHandler(Activity context) {
            super(context);
        }

        @Override
        public void onStart() {
            super.onStart();
        }

        @Override
        public void onFinish() {
            super.onFinish();
            try {
                dismissProgress();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSuccess(String response) {

            try {
                Debug.e("", "getWebSiteResponse# " + response);
                if (response != null && response.length() > 0) {

                    WebsiteResponse res = new Gson().fromJson(
                            response, new TypeToken<WebsiteResponse>() {
                            }.getType());

                    if (res.st == 1) {

                        WebSettings settings = wvWebSite.getSettings();
                        settings.setJavaScriptEnabled(true);
                        settings.setAllowContentAccess(true);
                        settings.setSupportZoom(true);

                        wvWebSite.loadUrl(res.data.get(0).website);

                    } else {

                        Utils.showDialog(getActivity(), "", "" + res.msg);

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable e, String content) {
            dismissProgress();
        }
    }

}
