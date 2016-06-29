package com.oozeetech.iproperty_cms.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.oozeetech.iproperty_cms.R;
import com.oozeetech.iproperty_cms.adapter.GalleryDetailsListAdapter;
import com.oozeetech.iproperty_cms.adapter.MaintananceListAdapter;
import com.oozeetech.iproperty_cms.adapter.PaymentHistoryListAdapter;
import com.oozeetech.iproperty_cms.models.MaintanancePaymentResponse;
import com.oozeetech.iproperty_cms.models.PaymentDetailsResponse;
import com.oozeetech.iproperty_cms.utils.AsyncHttpRequest;
import com.oozeetech.iproperty_cms.utils.AsyncResponseHandler;
import com.oozeetech.iproperty_cms.utils.Constants;
import com.oozeetech.iproperty_cms.utils.Debug;
import com.oozeetech.iproperty_cms.utils.RequestParamsUtils;
import com.oozeetech.iproperty_cms.utils.URLs;
import com.oozeetech.iproperty_cms.utils.Utils;
import com.paypal.android.sdk.payments.PayPalConfiguration;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by root on 28/6/16.
 */
public class PaymentDetailsActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.btnPayNow)
    Button btnPayNow;

    @Bind(R.id.tvPaymentDetailAmount)
    TextView tvPaymentDetailAmount;
    @Bind(R.id.tvPaymentDetailTotalAmount)
    TextView tvPayment_DetailTotalAmount;
    @Bind(R.id.tvPaymentDetailPeneltyAmount)
    TextView tvPaymentDetailPeneltyAmount;
    @Bind(R.id.tvPaymentDetailPayDate)
    TextView tvPaymentDetailPayDate;
    @Bind(R.id.tvPaymentDetailPayStatus)
    TextView tvPaymentDetailPayStatus;
    @Bind(R.id.tvTitle)
    TextView tvTitle;

    @Bind(R.id.rvPaymentHistoryList)
    RecyclerView rvPaymentHistoryList;
    @Bind(R.id.llPlaceHolder)
    LinearLayout llPlaceHolder;

    MaintananceListAdapter maintananceListAdapter;
    PaymentHistoryListAdapter adapter;

    int paymentType = 3;
    String url = null;

    MaintanancePaymentResponse mRes;
    PaymentDetailsResponse res;
    private LinearLayoutManager mLayoutManager;

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "credentials from developer.paypal.com";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Example Merchant")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        initIntentParam();

        init();
    }

    public void initIntentParam() {

        if (getIntent() != null && getIntent().hasExtra(Constants.FROM_PAYMENT_TYPE)) {
            paymentType = getIntent().getIntExtra(Constants.FROM_PAYMENT_TYPE, 0);
        }

        if (paymentType == Constants.TYPE_MAINTANANCE) {

            tvTitle.setText(getString(R.string.title_maintanance_payment));
            url = new URLs().CHECK_MAINTANANCE;

        } else if (paymentType == Constants.TYPE_FIRE_INSURENCE) {

            tvTitle.setText(getString(R.string.title_fire_insurence_payment));
            url = new URLs().CHECK_INSURANCE;

        } else if (paymentType == Constants.TYPE_QUITE_RENT) {

            tvTitle.setText(getString(R.string.title_quite_rent_payment));
            url = new URLs().CHECK_QUIT_RENT;

        } else if (paymentType == Constants.TYPE_SINKING_FUND) {

            tvTitle.setText(getString(R.string.title_sinking_fund_payment));
            url = new URLs().CHECK_SINKING;

        }

    }

    public void init() {

        mLayoutManager = new LinearLayoutManager(getActivity());
        rvPaymentHistoryList.setLayoutManager(mLayoutManager);
        rvPaymentHistoryList.setLayoutAnimation(Utils.getRowFadeSpeedAnimation(getActivity()));

        adapter = new PaymentHistoryListAdapter(getActivity());
        maintananceListAdapter = new MaintananceListAdapter(getActivity());

        if(paymentType == Constants.TYPE_MAINTANANCE){

            rvPaymentHistoryList.setAdapter(maintananceListAdapter);

        }else {

            rvPaymentHistoryList.setAdapter(adapter);
        }


        if (Utils.isInternetConnected(getActivity())) {

            getPaymentDetails();

        } else {
            showToast(R.string.internet_err, Toast.LENGTH_SHORT);

        }

    }


    @OnClick(R.id.btnPayNow)
    public void pay(View v) {

    }

    public void getPaymentDetails() {

        try {

            RequestParams params = new RequestParams();
            params.put(RequestParamsUtils.WS, Constants.CONDO);
            params.put(RequestParamsUtils.RES_ID, Utils.getPref(getActivity(), RequestParamsUtils.RES_ID, ""));
            params.put(RequestParamsUtils.CONDO_ID, Utils.getPref(getActivity(), RequestParamsUtils.CONDO_ID, ""));
            params.put(RequestParamsUtils.TOKEN, Utils.getPref(getActivity(), RequestParamsUtils.TOKEN, ""));
            Debug.e("PaymentParams", params.toString());

            AsyncHttpClient clientPhotos = AsyncHttpRequest.newRequest();
            clientPhotos.get(url, params, new PaymentDetailsHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class PaymentDetailsHandler extends AsyncResponseHandler {

        public PaymentDetailsHandler(Activity context) {
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

                Debug.e("", "PaymentDetailsResponse# " + response);
                if (response != null && response.length() > 0) {



                    if(paymentType==Constants.TYPE_MAINTANANCE){

                        mRes = new Gson().fromJson(
                                response, new TypeToken<MaintanancePaymentResponse>() {
                                }.getType());

                        if (mRes.st == 1) {

                            if (mRes.data.size() > 0) {

                                tvPayment_DetailTotalAmount.setText(""+mRes.data.get(0).totalAmount);
                                tvPaymentDetailAmount.setText(""+mRes.data.get(0).amount);
                                tvPaymentDetailPeneltyAmount.setText(""+mRes.data.get(0).penaltyAmount);
                                tvPaymentDetailPayStatus.setText(""+mRes.data.get(0).canPay);
                                tvPaymentDetailPayDate.setText(""+Utils.parseTime(mRes.data.get(0).payDate,"yyyy-MM-dd","MMM dd, yyyy"));

                                maintananceListAdapter.addAll(mRes.data.get(0).hisory);
                            }

                        } else if (mRes.st == 502) {

                            getActivity().showToast(R.string.msg_no_data_found, Toast.LENGTH_SHORT);

                        } else if (mRes.st == 505) {

                            getActivity().showToast(R.string.msg_exception, Toast.LENGTH_SHORT);

                        } else if (mRes.st == 506) {

                            getActivity().showToast(R.string.msg_unauthorised, Toast.LENGTH_SHORT);
                        }

                    }else {

                        res = new Gson().fromJson(
                                response, new TypeToken<PaymentDetailsResponse>() {
                                }.getType());

                        if (res.st == 1) {

                            if (res.data.size() > 0) {

                                tvPayment_DetailTotalAmount.setText(""+res.data.get(0).totalAmount);
                                tvPaymentDetailAmount.setText(""+res.data.get(0).amount);
                                tvPaymentDetailPeneltyAmount.setText(""+res.data.get(0).penaltyAmount);
                                tvPaymentDetailPayStatus.setText(""+res.data.get(0).canPay);
                                tvPaymentDetailPayDate.setText(""+Utils.parseTime(res.data.get(0).payDate,"yyyy-MM-dd","MMM dd, yyyy"));

                                adapter.addAll(res.data.get(0).hisory);
                            }

                        } else if (res.st == 502) {

                            getActivity().showToast(R.string.msg_no_data_found, Toast.LENGTH_SHORT);

                        } else if (res.st == 505) {

                            getActivity().showToast(R.string.msg_exception, Toast.LENGTH_SHORT);

                        } else if (res.st == 506) {

                            getActivity().showToast(R.string.msg_unauthorised, Toast.LENGTH_SHORT);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable e, String content) {
        }
    }

//    private void configurePayPal() {
//
//        PayPalConfiguration config = new PayPalConfiguration()
//                .merchantName("Navsix Residents")
//                .merchantPrivacyPolicyUri(
//                        Uri.parse("https://www.paypal.com/webapps/mpp/ua/privacy-full"))
//                .merchantUserAgreementUri(
//                        Uri.parse("https://www.paypal.com/webapps/mpp/ua/useragreement-full"));
//
//        SharedPreferences appPrefs = PreferenceManager
//                .getDefaultSharedPreferences(MainActivity.getInstance());
//
//        boolean isToAllowPay = true;
//        if (appPrefs.getString(SharedPreferenceConstants.MODE, "")
//                .equalsIgnoreCase("1")) {
//            // set live environment
//            config.clientId(appPrefs.getString(
//                    SharedPreferenceConstants.LIVE_URL, ""));
//            config.environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION);
//
//            if (appPrefs.getString(SharedPreferenceConstants.LIVE_URL, "")
//                    .length() == 0)
//                isToAllowPay = false;
//            else
//                isToAllowPay = true;
//        } else {
//            // set sandbox environment
//            config.clientId(appPrefs.getString(
//                    SharedPreferenceConstants.SANDBOX_URL, ""));
//            config.environment(PayPalConfiguration.ENVIRONMENT_SANDBOX);
//
//            if (appPrefs.getString(SharedPreferenceConstants.SANDBOX_URL, "")
//                    .length() == 0)
//                isToAllowPay = false;
//            else
//                isToAllowPay = true;
//        }
//
//        if (isToAllowPay) {
//            PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
//
//            Intent intent = new Intent(MainActivity.getInstance(),
//                    PaymentActivity.class);
//            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
//            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
//
//            MainActivity.getInstance().startActivityForResult(intent,
//                    REQUEST_CODE_PAYMENT);
//        } else {
//            Utility.showAlertWithOKOnly(MainActivity.getInstance(), "",
//                    getString(R.string.contact_management_office));
//        }
//    }


}
