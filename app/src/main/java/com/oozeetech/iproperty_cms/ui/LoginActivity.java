package com.oozeetech.iproperty_cms.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.oozeetech.iproperty_cms.R;
import com.oozeetech.iproperty_cms.models.Login;
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
public class LoginActivity extends BaseActivity {

    @Bind(R.id.etLoginUsername)
    EditText etLoginUsername;

    @Bind(R.id.etLoginPass)
    EditText etLoginPass;

    @Bind(R.id.btnLogin)
    Button btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

    }

    public boolean isValidate(){

        if(etLoginUsername.getText().length()<=0){
            showToast(R.string.err_username, Toast.LENGTH_SHORT);
            return false;
        }else if(etLoginPass.getText().length()<=0){
            showToast(R.string.err_password, Toast.LENGTH_SHORT);
            return false;
        }else {
            return true;
        }
    }

    @OnClick(R.id.btnLogin)
    public void onLogin(View v){

        if(isValidate()){

            callLogin();
        }

    }

    public void callLogin(){

        try {
            showProgress("");

            RequestParams params = new RequestParams();
            params.put(RequestParamsUtils.WS,Constants.CONDO);
            params.put(RequestParamsUtils.RESIDENT_ID,etLoginUsername.getText().toString());
            params.put(RequestParamsUtils.PASSWORD, "" + etLoginPass.getText().toString());
            params.put(RequestParamsUtils.TYPE,2);
            params.put(RequestParamsUtils.DEVICE_ID, Constants.DEVICE_ID);
//            params.put(RequestParamsUtils.DEVICE_ID, Utils.getPref(getActivity(),Constants.GCM_KEY,""));
            Debug.e("loginParams", params.toString());

            AsyncHttpClient clientPhotos = AsyncHttpRequest.newRequest();
            clientPhotos.get(new URLs().LOGIN_URL, params, new LoginHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class LoginHandler extends AsyncResponseHandler {

        public LoginHandler(Activity context) {
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
                Debug.e("", "login# " + response);
                if (response != null && response.length() > 0) {

                    Login loginRider = new Gson().fromJson(
                            response, new TypeToken<Login>() {
                            }.getType());

                    if (loginRider.st == 1) {

                        Utils.setPref(getActivity(), RequestParamsUtils.CONDO_ID, "" + loginRider.data.get(0).condoId);
                        Utils.setPref(getActivity(), RequestParamsUtils.RES_ID, "" + loginRider.data.get(0).resId);
                        Utils.setPref(getActivity(), RequestParamsUtils.TOKEN, "" + loginRider.data.get(0).token);
                        Utils.setPref(getActivity(), Constants.LOGIN_INFO, response);

                        Utils.setPref(getActivity(),Constants.USERNAME,loginRider.data.get(0).firstName+" "+loginRider.data.get(0).lastName);

                        if(loginRider.data.get(0).otp.equals("1")){

                            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }else {

                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }

                    } else if(loginRider.st==502) {

                        Utils.showDialog(getActivity(), "", "" + loginRider.msg);

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
