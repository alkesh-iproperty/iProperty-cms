package com.oozeetech.iproperty_cms.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.oozeetech.iproperty_cms.R;
import com.oozeetech.iproperty_cms.models.Response;
import com.oozeetech.iproperty_cms.ui.BaseActivity;
import com.oozeetech.iproperty_cms.utils.AsyncHttpRequest;
import com.oozeetech.iproperty_cms.utils.AsyncResponseHandler;
import com.oozeetech.iproperty_cms.utils.Constants;
import com.oozeetech.iproperty_cms.utils.Debug;
import com.oozeetech.iproperty_cms.utils.RequestParamsUtils;
import com.oozeetech.iproperty_cms.utils.URLs;
import com.oozeetech.iproperty_cms.utils.Utils;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.TimePickerDialog;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by divyeshshani on 23/06/16.
 */
public class AddRequestFragment extends Fragment {

    @Bind(R.id.etAddReqName)
    EditText etAddReqName;
    @Bind(R.id.etAddReqDate)
    EditText etAddReqDate;
    @Bind(R.id.etAddReqTime)
    EditText etAddReqTime;
    @Bind(R.id.etAddRequestExpected)
    EditText etAddRequestExpected;
    @Bind(R.id.btnAddReq)
    Button btnAddReq;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_request, container, false);

        ButterKnife.bind(this, v);

        etAddReqDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(etAddReqDate, getActivity());
            }
        });

        etAddReqTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(etAddReqTime, getActivity());
            }
        });

        return v;

    }

    @OnClick(R.id.btnAddReq)
    public void onSubmit(View v) {

        if (Utils.isInternetConnected(getActivity())) {

            if (isValid()) {

                addGuestRequest();
            }

        } else {

            ((BaseActivity) getActivity()).showToast(R.string.internet_err, Toast.LENGTH_SHORT);
        }
    }

    public boolean isValid() {

        if (etAddReqTime.getText().length() <= 0) {
            ((BaseActivity) getActivity()).showToast(R.string.err_add_req_guest_name, Toast.LENGTH_SHORT);
            return false;
        } else if (etAddRequestExpected.getText().length() <= 0) {
            ((BaseActivity) getActivity()).showToast(R.string.err_add_req_expected_people, Toast.LENGTH_SHORT);
            return false;
        } else if (etAddReqName.getText().length() <= 0) {
            ((BaseActivity) getActivity()).showToast(R.string.err_add_req_time, Toast.LENGTH_SHORT);
            return false;
        } else if (etAddReqDate.getText().length() <= 0) {
            ((BaseActivity) getActivity()).showToast(R.string.err_add_req_date, Toast.LENGTH_SHORT);
            return false;
        } else {
            return true;
        }
    }

    private void showTimePicker(final EditText editBox, Context context) {


        android.support.v4.app.DialogFragment fragment;
        com.rey.material.app.TimePickerDialog.Builder builder = null;
        builder = new TimePickerDialog.Builder() {

            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                TimePickerDialog dialog = (TimePickerDialog) fragment.getDialog();
                editBox.setText(Utils.parseTime(dialog.getHour() + ":" + dialog.getMinute() + ":00","HH:mm:ss","HH:mm:ss"));
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };

        builder.positiveAction("OK")
                .negativeAction("CANCEL");

        fragment = DialogFragment.newInstance(builder);
        fragment.show(getActivity().getSupportFragmentManager(), null);
    }

    private void showDatePicker(final EditText editBox, Context context) {
        android.support.v4.app.DialogFragment fragment;
        com.rey.material.app.DatePickerDialog.Builder builder = null;
        builder = new DatePickerDialog.Builder(R.style.Material_App_Dialog_DatePicker_Light) {

            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                DatePickerDialog dialog = (DatePickerDialog) fragment.getDialog();

                editBox.setText(Utils.parseTime(dialog.getDate(), "dd/MM/yyyy"));
                editBox.setTag(Utils.parseTime(dialog.getDate(), "yyyy-MM-dd"));
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
        builder.date(Utils.parseTime(editBox.getText().toString(), "dd/MM/yyyy").getTime());
//        }
        fragment = DialogFragment.newInstance(builder);
        fragment.show(getActivity().getSupportFragmentManager(), null);
    }

    public void addGuestRequest() {

        try {

            RequestParams params = new RequestParams();
            params.put(RequestParamsUtils.WS, Constants.CONDO);
            params.put(RequestParamsUtils.RES_ID, Utils.getPref(getActivity(), RequestParamsUtils.RES_ID, ""));
            params.put(RequestParamsUtils.CONDO_ID, Utils.getPref(getActivity(), RequestParamsUtils.CONDO_ID, ""));
            params.put(RequestParamsUtils.BLOCK_ID, Utils.getPref(getActivity(), RequestParamsUtils.BLOCK_ID, ""));
            params.put(RequestParamsUtils.UNIT_ID, Utils.getPref(getActivity(), RequestParamsUtils.UNIT_ID, ""));
            params.put(RequestParamsUtils.NAME, etAddReqName.getText().toString());
            params.put(RequestParamsUtils.NO_OF_GUEST, etAddRequestExpected.getText().toString());
            params.put(RequestParamsUtils.VISITING_DATE, etAddReqDate.getTag().toString() + " " + etAddReqTime.getText().toString());
            params.put(RequestParamsUtils.TOKEN, Utils.getPref(getActivity(), RequestParamsUtils.TOKEN, ""));
            Debug.e("EditGuestRequestListParams", params.toString());

            AsyncHttpClient clientPhotos = AsyncHttpRequest.newRequest();
            clientPhotos.get(new URLs().ADD_GUEST_REQUEST, params, new AddGuestListResponseHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class AddGuestListResponseHandler extends AsyncResponseHandler {

        public AddGuestListResponseHandler(Activity context) {
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

                Debug.e("", "AddGuestListResponse# " + response);
                if (response != null && response.length() > 0) {

                    Response res = new Gson().fromJson(
                            response, new TypeToken<Response>() {
                            }.getType());

                    if (res.st == 1) {

                        clearFields();
                        ((BaseActivity) getActivity()).showToast(R.string.msg_success_add_guest, Toast.LENGTH_SHORT);

                    } else if (res.st == 402) {

                        ((BaseActivity) getActivity()).showToast(R.string.msg_not_updated, Toast.LENGTH_SHORT);

                    } else if (res.st == 502) {

                        ((BaseActivity) getActivity()).showToast(R.string.msg_no_data_found, Toast.LENGTH_SHORT);

                    } else if (res.st == 505) {

                        ((BaseActivity) getActivity()).showToast(R.string.msg_exception, Toast.LENGTH_SHORT);

                    } else if (res.st == 506) {

                        ((BaseActivity) getActivity()).showToast(R.string.msg_unauthorised, Toast.LENGTH_SHORT);
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

    public void clearFields() {

        etAddReqDate.setText("");
        etAddReqDate.setTag("");
        etAddReqTime.setText("");
        etAddReqName.setText("");
        etAddRequestExpected.setText("");
    }
}
