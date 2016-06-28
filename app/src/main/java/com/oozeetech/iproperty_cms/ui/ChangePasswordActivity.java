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
import com.oozeetech.iproperty_cms.models.Response;
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
public class ChangePasswordActivity extends BaseActivity {

    @Bind(R.id.etNewPassword)
    EditText etNewPassword;

    @Bind(R.id.etConfirmPass)
    EditText etConfirmPass;

    @Bind(R.id.btnUpdatePass)
    Button btnUpdatePass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

    }

    public boolean isValidate(){

        if(etNewPassword.getText().length()<=0){
            showToast(R.string.err_username, Toast.LENGTH_SHORT);
            return false;
        }else if(etConfirmPass.getText().length()<=0){
            showToast(R.string.err_password, Toast.LENGTH_SHORT);
            return false;
        }else if(!etNewPassword.getText().toString().equals(etConfirmPass.getText().toString())){
            return false;
        } else{
            return true;
        }
    }

    @OnClick(R.id.btnUpdatePass)
    public void onUpdatePassword(View v){

        if(isValidate()){

            callUpdatePass();
        }

    }

    public void callUpdatePass(){

        try {
            showProgress("");

            RequestParams params = new RequestParams();
            params.put(RequestParamsUtils.WS,Constants.CONDO);
            params.put(RequestParamsUtils.RES_ID,Utils.getPref(getActivity(),RequestParamsUtils.RES_ID,""));
            params.put(RequestParamsUtils.NEWPASS, "" + etNewPassword.getText().toString());
            params.put(RequestParamsUtils.TOKEN, Utils.getPref(getActivity(),RequestParamsUtils.TOKEN,""));
            Debug.e("changepassword param", params.toString());

            AsyncHttpClient clientPhotos = AsyncHttpRequest.newRequest();
            clientPhotos.get(new URLs().OTP, params, new ChangePasswordHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class ChangePasswordHandler extends AsyncResponseHandler {

        public ChangePasswordHandler(Activity context) {
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
                Debug.e("", "changePassword# " + response);
                if (response != null && response.length() > 0) {

                    Response res = new Gson().fromJson(
                            response, new TypeToken<Response>() {
                            }.getType());

                    if (res.st == 1) {

                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

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
