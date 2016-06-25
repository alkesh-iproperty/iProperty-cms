package com.oozeetech.iproperty_cms.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.oozeetech.iproperty_cms.R;
import com.oozeetech.iproperty_cms.models.MasterFacilityResponse;
import com.oozeetech.iproperty_cms.utils.AsyncHttpRequest;
import com.oozeetech.iproperty_cms.utils.AsyncResponseHandler;
import com.oozeetech.iproperty_cms.utils.Constants;
import com.oozeetech.iproperty_cms.utils.Debug;
import com.oozeetech.iproperty_cms.utils.RequestParamsUtils;
import com.oozeetech.iproperty_cms.utils.URLs;
import com.oozeetech.iproperty_cms.utils.Utils;

import butterknife.ButterKnife;


/**
 * Created by root on 17/6/16.
 */

public class SplashActivity extends BaseActivity {

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);



           proceed();




    }

    public void proceed(){

        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (Utils.getPref(getActivity(), Constants.LOGIN_INFO, "").equals("")) {

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                } else {

                    if(Utils.isInternetConnected(getActivity())){

                        getFacilityMaster();
                    }else {
                        showToast(R.string.internet_err,Toast.LENGTH_SHORT);
                    }


                }

            }
        }, 2000);
    }

    public void getFacilityMaster() {

        try {

            RequestParams params = new RequestParams();
            params.put(RequestParamsUtils.WS, Constants.CONDO);
            params.put(RequestParamsUtils.RES_ID, Utils.getPref(getActivity(), RequestParamsUtils.RES_ID, ""));
            params.put(RequestParamsUtils.CONDO_ID, Utils.getPref(getActivity(), RequestParamsUtils.CONDO_ID, ""));
            params.put(RequestParamsUtils.TOKEN, Utils.getPref(getActivity(), RequestParamsUtils.TOKEN, ""));
            Debug.e("MasterFacilityList", params.toString());

            AsyncHttpClient clientPhotos = AsyncHttpRequest.newRequest();
            clientPhotos.get(new URLs().MASTER_FACILITY, params, new MasterFacilityResponseHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class MasterFacilityResponseHandler extends AsyncResponseHandler {

        public MasterFacilityResponseHandler(Activity context) {
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSuccess(String response) {

            try {

                Debug.e("", "MasterFacilityListResponse# " + response);

                if (response != null && response.length() > 0) {

                    MasterFacilityResponse res = new Gson().fromJson(
                            response, new TypeToken<MasterFacilityResponse>() {
                            }.getType());

                    if (res.st == 1) {

                        Utils.setPref(getActivity(),Constants.MASTER_FACILITY,response);

                    } else if (res.st == 502) {

                        getActivity().showToast(R.string.msg_no_data_found, Toast.LENGTH_SHORT);

                    } else if (res.st == 505) {

                        getActivity().showToast(R.string.msg_exception, Toast.LENGTH_SHORT);

                    } else if (res.st == 506) {

                        getActivity().showToast(R.string.msg_unauthorised, Toast.LENGTH_SHORT);
                    }

                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(Throwable e, String content) {

            proceed();
        }
    }

}
