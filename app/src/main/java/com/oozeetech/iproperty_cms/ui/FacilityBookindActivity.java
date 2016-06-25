package com.oozeetech.iproperty_cms.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.oozeetech.iproperty_cms.R;
import com.oozeetech.iproperty_cms.models.ChildFacilityResponse;
import com.oozeetech.iproperty_cms.models.MasterFacilityResponse;
import com.oozeetech.iproperty_cms.utils.AsyncHttpRequest;
import com.oozeetech.iproperty_cms.utils.AsyncResponseHandler;
import com.oozeetech.iproperty_cms.utils.Constants;
import com.oozeetech.iproperty_cms.utils.Debug;
import com.oozeetech.iproperty_cms.utils.RequestParamsUtils;
import com.oozeetech.iproperty_cms.utils.URLs;
import com.oozeetech.iproperty_cms.utils.Utils;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by divyeshshani on 21/06/16.
 */
public class FacilityBookindActivity extends BaseActivity implements ViewPagerEx.OnPageChangeListener, BaseSliderView.OnSliderClickListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.slider)
    SliderLayout slider;
    @Bind(R.id.llFacilityTimeContainer)
    LinearLayout llFacilityTimeContainer;
    @Bind(R.id.tvTitle)
    TextView tvTitle;
    @Bind(R.id.tvDate)
    TextView tvDate;
    @Bind(R.id.llDate)
    LinearLayout llDate;

    @Bind(R.id.llDataTimeSlot)
    LinearLayout llDataTimeSlot;
    @Bind(R.id.llFreeFacility)
    LinearLayout llFreeFacility;

    MasterFacilityResponse.Datum facilityDetails;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_booking);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");

        initIntentParam();

        if(Utils.isInternetConnected(getActivity())){

            setData();
            getChildFacility();
            initSlider();

        } else {

            getActivity().showToast(R.string.internet_err,Toast.LENGTH_SHORT);
        }
    }

    public void initSlider(){

        HashMap<String,String> url_maps = new HashMap<String, String>();

        String[] imgs = facilityDetails.images.split(",");

        for (int i=0;i<imgs.length;i++) {
            url_maps.put(""+i,imgs[i]);
        }

        for (int i = 0; i < imgs.length; i++) {

            TextSliderView textSliderView = new TextSliderView(getActivity());
            textSliderView
                    .description("")
                    .image(new URLs().SLIDING_IMAGES + imgs[i])
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .setOnSliderClickListener(this);

            slider.addSlider(textSliderView);
        }

        slider.destroyDrawingCache();
        slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(4000);
        slider.startAutoCycle();
        slider.addOnPageChangeListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        slider.stopAutoCycle();
    }

    public void initIntentParam() {

        if (getIntent() != null && getIntent().hasExtra(Constants.FACILITY_DETAILS)) {

            facilityDetails = new Gson().fromJson(getIntent().getStringExtra(Constants.FACILITY_DETAILS), MasterFacilityResponse.Datum.class);
        }
    }

    public void getChildFacility(){

        try {

            showProgress("");

            RequestParams params = new RequestParams();
            params.put(RequestParamsUtils.WS, Constants.CONDO);
            params.put(RequestParamsUtils.RES_ID, Utils.getPref(getActivity(),RequestParamsUtils.RES_ID,""));
            params.put(RequestParamsUtils.CONDO_ID, Utils.getPref(getActivity(),RequestParamsUtils.CONDO_ID,""));
            params.put(RequestParamsUtils.FM_ID, facilityDetails.fmId);
            params.put(RequestParamsUtils.CHANGE_BK, "0");
            params.put(RequestParamsUtils.DATE, tvDate.getTag().toString());
            params.put(RequestParamsUtils.TOKEN,Utils.getPref(getActivity(),RequestParamsUtils.TOKEN,""));
            Debug.e("getChildFacility", params.toString());

            AsyncHttpClient clientPhotos = AsyncHttpRequest.newRequest();
            clientPhotos.get(new URLs().CHILD_FACILITY, params, new ChildFacilityHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    private class ChildFacilityHandler extends AsyncResponseHandler {

        public ChildFacilityHandler(Activity context) {
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

                Debug.e("", "ChildFacilityBooking# " + response);

                dismissProgress();

                final ChildFacilityResponse childFaility = new Gson().fromJson(
                        response, new TypeToken<ChildFacilityResponse>() {
                        }.getType());

                if (childFaility.st == 5) {

                    if (childFaility.data.get(1).timeslots.size() > 0) {

                        llDataTimeSlot.setVisibility(View.VISIBLE);
                        llFreeFacility.setVisibility(View.GONE);

                        if(llFacilityTimeContainer.getChildCount()>0){

                            llFacilityTimeContainer.removeAllViews();
                        }

                        for (int i=0;i<childFaility.data.get(1).timeslots.size();i++){

                            View timeView = LayoutInflater.from(getActivity()).inflate(R.layout.item_facility_time,null);
                            final int finalI = i;
                            timeView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(getActivity(),DetailFacilityBookingActivity.class);
                                    intent.putExtra(Constants.TITLE,childFaility.data.get(1).timeslots.get(finalI).slot);
                                    intent.putExtra(Constants.DETAIL_FACILITY_DATA,""+new Gson().toJson(childFaility));
                                    intent.putExtra(Constants.BOOKING_DATE, tvDate.getTag().toString());
                                    intent.putExtra(Constants.POSITION,""+finalI);
                                    startActivity(intent);
                                }
                            });
                            TextView tvTimeSlot = (TextView) timeView.findViewById(R.id.tvTimeSlot);
                            tvTimeSlot.setText(childFaility.data.get(1).timeslots.get(i).slot);

                            llFacilityTimeContainer.addView(timeView);

                        }

                    } else {

                        llDataTimeSlot.setVisibility(View.GONE);
                        llFreeFacility.setVisibility(View.VISIBLE);

                    }

                }else if(childFaility.st == 0){


                    getActivity().showToast(R.string.msg_facility_not_found, Toast.LENGTH_SHORT);
                }else if(childFaility.st == 1){

                    getActivity().showToast(R.string.msg_child_facility_not_found, Toast.LENGTH_SHORT);
                }else if(childFaility.st == 2){

                    getActivity().showToast(R.string.msg_to_erlier, Toast.LENGTH_SHORT);
                }else if(childFaility.st == 3){

                    getActivity().showToast(R.string.msg_blocked_by_admin, Toast.LENGTH_LONG);
                }else if(childFaility.st == 4){

                    getActivity().showToast(R.string.msg_exeed_limit, Toast.LENGTH_LONG);
                }else if(childFaility.st == 505){

                    getActivity().showToast(R.string.msg_exception, Toast.LENGTH_SHORT);
                }else if(childFaility.st == 506){

                    getActivity().showToast(R.string.msg_unauthorised, Toast.LENGTH_SHORT);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(Throwable e, String content) {
        }
    }

    public void setData() {

        tvTitle.setText(facilityDetails.fmName);
        tvDate.setText(Utils.parseTime(new Date().getTime(),"MMM dd, yyyy"));
        tvDate.setTag(Utils.parseTime(new Date().getTime(),"yyyy-MM-dd"));

    }

    @OnClick(R.id.llDate)
    public void onSelectDate(){

        showDatePicker(tvDate,getActivity());
    }

    private void showDatePicker(final TextView editBox, Context context) {
        android.support.v4.app.DialogFragment fragment;
        com.rey.material.app.DatePickerDialog.Builder builder = null;
        builder = new DatePickerDialog.Builder(R.style.Material_App_Dialog_DatePicker_Light) {

            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                DatePickerDialog dialog = (DatePickerDialog) fragment.getDialog();

                editBox.setText(Utils.parseTime(dialog.getDate(), "MMM dd, yyyy"));
                editBox.setTag(Utils.parseTime(dialog.getDate(), "yyyy-MM-dd"));
                if(Utils.isInternetConnected(getActivity())) {
                    getChildFacility();
                }else {
                    getActivity().showToast(R.string.internet_err,Toast.LENGTH_SHORT);
                }

                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };

        builder.positiveAction("OK")
                .negativeAction("CANCEL");

        Calendar cal = Calendar.getInstance();
        int mDay = cal.get(Calendar.DAY_OF_MONTH);
        int mMonth = cal.get(Calendar.MONTH);
        int mYear = cal.get(Calendar.YEAR);
        int mMinDay = mDay;
        int mMinMonth = mMonth;
        int mMinYear = mYear;
        int mMaxDay = mDay;
        int mMaxMonth = mMonth + 6;
        int mMaxYear = mYear;

        builder.dateRange(mMinDay, mMinMonth, mMinYear, mMaxDay, mMaxMonth, mMaxYear);
//        if (editBox.getText().length() > 0) {
        builder.date(Utils.parseTime(editBox.getText().toString(), "MMM dd, yyyy").getTime());
//        }
        fragment = DialogFragment.newInstance(builder);
        fragment.show(getActivity().getSupportFragmentManager(), null);
    }

}
