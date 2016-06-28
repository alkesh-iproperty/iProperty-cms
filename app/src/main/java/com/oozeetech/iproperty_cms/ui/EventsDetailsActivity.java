package com.oozeetech.iproperty_cms.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.oozeetech.iproperty_cms.R;
import com.oozeetech.iproperty_cms.models.EventsResponse;
import com.oozeetech.iproperty_cms.models.Response;
import com.oozeetech.iproperty_cms.utils.AsyncHttpRequest;
import com.oozeetech.iproperty_cms.utils.AsyncResponseHandler;
import com.oozeetech.iproperty_cms.utils.Constants;
import com.oozeetech.iproperty_cms.utils.Debug;
import com.oozeetech.iproperty_cms.utils.EndlessList;
import com.oozeetech.iproperty_cms.utils.RequestParamsUtils;
import com.oozeetech.iproperty_cms.utils.URLs;
import com.oozeetech.iproperty_cms.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by divyeshshani on 21/06/16.
 */
public class EventsDetailsActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tvBannerText)
    TextView tvBannerText;
    @Bind(R.id.tvFromDate)
    TextView tvFromDate;
    @Bind(R.id.tvToDate)
    TextView tvToDate;
    @Bind(R.id.tvFromTime)
    TextView tvFromTime;
    @Bind(R.id.tvToTime)
    TextView tvToTime;
    @Bind(R.id.tvTitle)
    TextView tvTitle;
    @Bind(R.id.tvNoOfPeopleJoined)
    TextView tvNoOfPeopleJoined;
    @Bind(R.id.tvEventDetails)
    TextView tvEventDetails;
    @Bind(R.id.ivEventBanner)
    ImageView ivEventBanner;

    @Bind(R.id.llJoinEvent)
    LinearLayout llJoinEvent;
    @Bind(R.id.llLeaveEvent)
    LinearLayout llLeaveEvent;

    EventsResponse.Datum eventData;
    int totalPerson = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_desc);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");

        initIntentParam();

        setData();

        if (Utils.isInternetConnected(getActivity())) {

            updateReadStatus();

        } else {
            showToast(R.string.internet_err, Toast.LENGTH_SHORT);
        }
    }

    public void initIntentParam() {

        if (getIntent() != null && getIntent().hasExtra(Constants.PARAM_EVENT_DATA)) {

            eventData = new Gson().fromJson(getIntent().getStringExtra(Constants.PARAM_EVENT_DATA), EventsResponse.Datum.class);
        }

    }

    public void setData() {

        SimpleDateFormat myFormat = new SimpleDateFormat("MMM dd, yyyy");
        SimpleDateFormat myTimeFormate = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String fromDate = null;
        String toDate = null;
        String fromTime = null;
        String toTime = null;
        try {
            fromDate = myFormat.format(originalFormat.parse(eventData.startTime));
            toDate = myFormat.format(originalFormat.parse(eventData.endTime));
            fromTime = myTimeFormate.format(originalFormat.parse(eventData.startTime));
            toTime = myTimeFormate.format(originalFormat.parse(eventData.endTime));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Debug.e("fromDate", fromDate.toString());

        tvTitle.setText(eventData.eventName);
        tvBannerText.setText(eventData.eventName);
        tvFromDate.setText(fromDate);
        tvToDate.setText(toDate);
        tvFromTime.setText(fromTime);
        tvToTime.setText(toTime);
        tvNoOfPeopleJoined.setText(eventData.totalPerson);
        tvEventDetails.setText(eventData.eventDesc);
        Glide.with(getActivity())
                .load(new URLs().EVENT_IMG_URL + eventData.eventImg)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .listener(new RequestListener<String, GlideDrawable>() {

                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        return false;
                    }

                })
                .crossFade()
                .into(ivEventBanner);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @OnClick(R.id.llLeaveEvent)
    public void leaveEvent(View v) {

        eventAction(false);
    }

    @OnClick(R.id.llJoinEvent)
    public void joinEvent(View view) {


        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_join, null);
        final EditText etNoOfPerson = (EditText) v.findViewById(R.id.etNoOfPerson);
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(getString(R.string.hint_join), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (etNoOfPerson.getText().length() <= 0) {
                            showToast(R.string.err_no_of_person, Toast.LENGTH_SHORT);
                        } else {
                            totalPerson = Integer.parseInt(etNoOfPerson.getText().toString());
                            eventAction(true);
                            dialogInterface.dismiss();
                        }
                    }
                })
                .setNegativeButton(getString(R.string.hint_cencel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        dialog.show();
    }

    public void eventAction(boolean isJoined) {

        try {

            showProgress("");

            RequestParams params = new RequestParams();
            params.put(RequestParamsUtils.WS, Constants.CONDO);
            params.put(RequestParamsUtils.CONDO_ID, Utils.getPref(getActivity(), RequestParamsUtils.CONDO_ID, ""));
            params.put(RequestParamsUtils.EVENT_ID, eventData.id);
            if (isJoined) {
                params.put(RequestParamsUtils.ACTION, 1);
            } else {
                params.put(RequestParamsUtils.ACTION, 0);
            }
            params.put(RequestParamsUtils.NO_OF_PERSO, "" + totalPerson);
            params.put(RequestParamsUtils.RES_ID, Utils.getPref(getActivity(), RequestParamsUtils.RES_ID, ""));
            params.put(RequestParamsUtils.TOKEN, Utils.getPref(getActivity(), RequestParamsUtils.TOKEN, ""));
            Debug.e("eventJoinleftparams", params.toString());

            AsyncHttpClient clientPhotos = AsyncHttpRequest.newRequest();
            clientPhotos.get(new URLs().EVENT_JOIN_LEAVE, params, new EventJoinLeftResponseHandler(getActivity(), isJoined));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private class EventJoinLeftResponseHandler extends AsyncResponseHandler {

        boolean isJoined;

        public EventJoinLeftResponseHandler(Activity context, boolean isJoined) {
            super(context);
            this.isJoined = isJoined;
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
                dismissProgress();
                Debug.e("", "UpdateEventsResponse# " + response);
                if (response != null && response.length() > 0) {
                    Response res = new Gson().fromJson(
                            response, new TypeToken<Response>() {
                            }.getType());

                    if (res.st == 1) {
                        if (isJoined) {
                            showToast(R.string.msg_joined, Toast.LENGTH_SHORT);
                        } else {
                            showToast(R.string.msg_left, Toast.LENGTH_SHORT);
                        }
                    } else if (res.st == 2) {
                        showToast(R.string.msg_already_accepted_rejected, Toast.LENGTH_SHORT);
                    } else if (res.st == 402) {
                        showToast(R.string.msg_not_updated, Toast.LENGTH_SHORT);
                    } else if (res.st == 502) {
                        showToast(R.string.msg_no_data_found, Toast.LENGTH_SHORT);
                    } else if (res.st == 504) {
                        showToast(R.string.msg_invalid_event_type, Toast.LENGTH_SHORT);
                    } else if (res.st == 505) {
                        showToast(R.string.msg_exception, Toast.LENGTH_SHORT);
                    } else if (res.st == 506) {
                        showToast(R.string.msg_unauthorised, Toast.LENGTH_SHORT);
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


    public void updateReadStatus() {

        try {

            RequestParams params = new RequestParams();
            params.put(RequestParamsUtils.WS, Constants.CONDO);
            params.put(RequestParamsUtils.RES_ID, Utils.getPref(getActivity(), RequestParamsUtils.RES_ID, ""));
            params.put(RequestParamsUtils.ID, "" + eventData.id);
            params.put(RequestParamsUtils.TOKEN, Utils.getPref(getActivity(), RequestParamsUtils.TOKEN, ""));
            Debug.e("EventParams", params.toString());

            AsyncHttpClient clientPhotos = AsyncHttpRequest.newRequest();
            clientPhotos.get(new URLs().EVENT_READ_UPDATE, params, new UpdateReadStatusHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class UpdateReadStatusHandler extends AsyncResponseHandler {

        public UpdateReadStatusHandler(Activity context) {
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
                Debug.e("", "UpdateEventsResponse# " + response);
                if (response != null && response.length() > 0) {
                    Response res = new Gson().fromJson(
                            response, new TypeToken<Response>() {
                            }.getType());

                    if (res.st == 1) {


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

}
