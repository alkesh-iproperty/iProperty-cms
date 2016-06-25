package com.oozeetech.iproperty_cms.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.oozeetech.iproperty_cms.R;
import com.oozeetech.iproperty_cms.adapter.DetailFacilityAdapter;
import com.oozeetech.iproperty_cms.models.ChildFacilityResponse;
import com.oozeetech.iproperty_cms.utils.AsyncHttpRequest;
import com.oozeetech.iproperty_cms.utils.AsyncResponseHandler;
import com.oozeetech.iproperty_cms.utils.Constants;
import com.oozeetech.iproperty_cms.utils.Debug;
import com.oozeetech.iproperty_cms.utils.RequestParamsUtils;
import com.oozeetech.iproperty_cms.utils.URLs;
import com.oozeetech.iproperty_cms.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by divyeshshani on 21/06/16.
 */
public class DetailFacilityBookingActivity extends BaseActivity {

    @Bind(R.id.rvDetailFacilityBooking)
    RecyclerView rvDetailFacilityBooking;
    @Bind(R.id.toolbar)
    Toolbar toolbar;


    @Bind(R.id.tvTitle)
    TextView tvTitle;
    private LinearLayoutManager mLayoutManager;
    private DetailFacilityAdapter adapter;
    public ChildFacilityResponse data;
    String title;
    String position;
    private String bookingDate;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_facility_booking);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        initIntentParam();
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                Debug.e("homebtnclicked","true");finish();

        }

        return super.onOptionsItemSelected(item);
    }

    public void initIntentParam(){

        if(getIntent()!=null && getIntent().hasExtra(Constants.DETAIL_FACILITY_DATA)) {
            Debug.e("dataParam",getIntent().getStringExtra(Constants.DETAIL_FACILITY_DATA));
            data = new Gson().fromJson(getIntent().getStringExtra(Constants.DETAIL_FACILITY_DATA),ChildFacilityResponse.class);
            position = getIntent().getStringExtra(Constants.POSITION);
            bookingDate = getIntent().getStringExtra(Constants.BOOKING_DATE);
            getSupportActionBar().setTitle("");
            tvTitle.setText(getIntent().getStringExtra(Constants.TITLE));
        }
    }

    public void init() {

        mLayoutManager = new LinearLayoutManager(getActivity());
        rvDetailFacilityBooking.setLayoutManager(mLayoutManager);
        rvDetailFacilityBooking.setLayoutAnimation(Utils.getRowFadeSpeedAnimation(getActivity()));

        adapter = new DetailFacilityAdapter(getActivity());
        adapter.addAll(data.data.get(1).timeslots.get(Integer.parseInt(position)).data);
        rvDetailFacilityBooking.setAdapter(adapter);

            adapter.setEventListener(new DetailFacilityAdapter.EventListener() {

            @Override
            public void onItemClick(int position) {

                if(Utils.isInternetConnected(getActivity())){

                    bookFacility(adapter.getItem(position).ftId);

                }else {
                    getActivity().showToast(R.string.internet_err,Toast.LENGTH_SHORT);
                }
            }
        });

    }

    public void bookFacility(String ftId){

        try {

            RequestParams params = new RequestParams();
            params.put(RequestParamsUtils.WS, Constants.CONDO);
            params.put(RequestParamsUtils.RES_ID, Utils.getPref(getActivity(),RequestParamsUtils.RES_ID,""));
            params.put(RequestParamsUtils.FT_ID,ftId);
            params.put(RequestParamsUtils.CHANGE_BK, "0");
            params.put(RequestParamsUtils.DATE, bookingDate);
            params.put(RequestParamsUtils.TOKEN,Utils.getPref(getActivity(),RequestParamsUtils.TOKEN,""));
            Debug.e("BookingFacilityParams", params.toString());

            AsyncHttpClient clientPhotos = AsyncHttpRequest.newRequest();
            clientPhotos.get(new URLs().ADD_BOOKING, params, new BookingHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class BookingHandler extends AsyncResponseHandler {

        public BookingHandler(Activity context) {
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
                Debug.e("", "BookingResponse# " + response);
                if (response != null && response.length() > 0) {


                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Throwable e, String content) {
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
