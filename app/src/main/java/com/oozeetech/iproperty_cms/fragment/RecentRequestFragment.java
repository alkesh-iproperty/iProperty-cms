package com.oozeetech.iproperty_cms.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.oozeetech.iproperty_cms.R;
import com.oozeetech.iproperty_cms.adapter.GuestRequestAdapter;
import com.oozeetech.iproperty_cms.models.GuestRequestList;
import com.oozeetech.iproperty_cms.models.Response;
import com.oozeetech.iproperty_cms.ui.BaseActivity;
import com.oozeetech.iproperty_cms.utils.AsyncHttpRequest;
import com.oozeetech.iproperty_cms.utils.AsyncResponseHandler;
import com.oozeetech.iproperty_cms.utils.Constants;
import com.oozeetech.iproperty_cms.utils.Debug;
import com.oozeetech.iproperty_cms.utils.EndlessList;
import com.oozeetech.iproperty_cms.utils.RequestParamsUtils;
import com.oozeetech.iproperty_cms.utils.URLs;
import com.oozeetech.iproperty_cms.utils.Utils;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.TimePickerDialog;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by divyeshshani on 23/06/16.
 */
public class RecentRequestFragment extends Fragment {

    @Bind(R.id.rvGuestList)
    RecyclerView rvGuestList;
    @Bind(R.id.llPlaceHolder)
    LinearLayout llPlaceHolder;
    @Bind(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    EndlessList endlessList;
    EditText etName;
    EditText etAddRequestExpected;
    EditText etAddReqTime;
    EditText etaddReqDate;
    private GuestRequestAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    private int page = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_recent_request, container, false);
        ButterKnife.bind(this, v);
        init();
        return v;
    }

    public void init() {

        ((BaseActivity) getActivity()).setTitleText(getString(R.string.title_guest_request));
        mLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new GuestRequestAdapter(getActivity());
        rvGuestList.setLayoutManager(mLayoutManager);
        rvGuestList.setLayoutAnimation(Utils.getRowFadeSpeedAnimation(getActivity()));
        rvGuestList.setAdapter(adapter);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.colorAccent, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isInternetConnected(getActivity())) {

                    resetPage();
                    getGuestLit();

                } else {
                    ((BaseActivity) getActivity()).showToast(R.string.internet_err, Toast.LENGTH_SHORT);
                }
            }
        });

        adapter.setEventListener(new GuestRequestAdapter.EventListener() {

            @Override
            public void onItemClick(View v, final int position) {

                PopupMenu popup = new PopupMenu(getActivity(), v);
                popup.getMenuInflater().inflate(R.menu.guest_list_popup, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.popup_delete:
                                deleteGuestRequest(adapter.getItem(position).glrId, position);
                                break;
                            case R.id.popup_edit:

                                editGuestRequest(position);

                                break;
                        }
                        return true;
                    }
                });

                popup.show();

            }

        });

        initEndlessList();
        resetPage();


        if (Utils.isInternetConnected(getActivity())) {

            getGuestLit();

        } else {
            ((BaseActivity) getActivity()).showToast(R.string.internet_err, Toast.LENGTH_SHORT);

        }

    }

    public void deleteGuestRequest(final String id, final int position) {

        AlertDialog builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.hint_delete)
                .setMessage(R.string.hint_confirm_delete)
                .setPositiveButton(R.string.hint_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (Utils.isInternetConnected(getActivity())) {

                            adapter.remove(position);
                            deleteGuest(id);

                        } else {
                            ((BaseActivity) getActivity()).showToast(R.string.internet_err, Toast.LENGTH_SHORT);
                        }
                    }
                })
                .setNegativeButton(R.string.hint_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        builder.show();

    }

    public void editGuestRequest(final int position) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_guest_request, null);
        etName = (EditText) v.findViewById(R.id.etAddReqName);
        etAddRequestExpected = (EditText) v.findViewById(R.id.etAddRequestExpected);
        etAddReqTime = (EditText) v.findViewById(R.id.etAddReqTime);
        etaddReqDate = (EditText) v.findViewById(R.id.etAddReqDate);

        etaddReqDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(etaddReqDate, getActivity());
            }
        });

        etAddReqTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(etAddReqTime, getActivity());
            }
        });

        etName.setText(adapter.getItem(position).name);
        etAddRequestExpected.setText(adapter.getItem(position).noOfGuest);
        etAddReqTime.setText(Utils.parseTime(adapter.getItem(position).visitingDate, "yyyy-MM-dd hh:mm:ss", "hh:mm:ss"));
        etaddReqDate.setTag((Utils.parseTime(adapter.getItem(position).visitingDate, "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd")));
        etaddReqDate.setText(Utils.parseTime(adapter.getItem(position).visitingDate, "yyyy-MM-dd hh:mm:ss", "MMM dd,yyyy"));

        AlertDialog builder = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(adapter.getItem(position).name)
                .setPositiveButton(R.string.hint_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (isValid()) {

                            adapter.getItem(position).noOfGuest = etAddRequestExpected.getText().toString();
                            adapter.getItem(position).name = etName.getText().toString();
                            adapter.getItem(position).visitingDate = etaddReqDate.getTag().toString() + " " + etAddReqTime.getText().toString();
                            adapter.notifyDataSetChanged();

                            callEditGuestRequest(position);
                        }
                    }
                })
                .setNegativeButton(R.string.hint_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        builder.show();

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

    private void showTimePicker(final EditText editBox, Context context) {


        android.support.v4.app.DialogFragment fragment;
        com.rey.material.app.TimePickerDialog.Builder builder = null;
        builder = new TimePickerDialog.Builder() {

            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                TimePickerDialog dialog = (TimePickerDialog) fragment.getDialog();
                editBox.setText(dialog.getHour() + ":" + dialog.getMinute() + ":00");
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

    public boolean isValid() {

        if (etAddReqTime.getText().length() <= 0) {
            ((BaseActivity) getActivity()).showToast(R.string.err_add_req_guest_name, Toast.LENGTH_SHORT);
            return false;
        } else if (etAddRequestExpected.getText().length() <= 0) {
            ((BaseActivity) getActivity()).showToast(R.string.err_add_req_expected_people, Toast.LENGTH_SHORT);
            return false;
        } else if (etName.getText().length() <= 0) {
            ((BaseActivity) getActivity()).showToast(R.string.err_add_req_time, Toast.LENGTH_SHORT);
            return false;
        } else {
            return true;
        }
    }


    public void refreshPlaceHolder() {

        if (adapter.getItemCount() > 0) {
            llPlaceHolder.setVisibility(View.GONE);
        } else {
            llPlaceHolder.setVisibility(View.VISIBLE);
        }
    }

    private void resetPage() {
        page = 0;
        adapter.clear();
    }


    private void initEndlessList() {
        endlessList = new EndlessList(rvGuestList, (LinearLayoutManager) mLayoutManager);
        endlessList.setOnLoadMoreListener(new EndlessList.OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                Debug.e("", "onLoadMore");
                if (Utils.isInternetConnected(getActivity())) {

                    getGuestLit();

                } else {
                    ((BaseActivity) getActivity()).showToast(R.string.internet_err, Toast.LENGTH_SHORT);
                }
            }

        });
    }


    public void getGuestLit() {

        try {

            page++;

            RequestParams params = new RequestParams();
            params.put(RequestParamsUtils.WS, Constants.CONDO);
            params.put(RequestParamsUtils.RES_ID, Utils.getPref(getActivity(), RequestParamsUtils.RES_ID, ""));
            params.put(RequestParamsUtils.CONDO_ID, Utils.getPref(getActivity(), RequestParamsUtils.CONDO_ID, ""));
            params.put(RequestParamsUtils.BLOCK_ID, Utils.getPref(getActivity(), RequestParamsUtils.BLOCK_ID, ""));
            params.put(RequestParamsUtils.UNIT_ID, Utils.getPref(getActivity(), RequestParamsUtils.UNIT_ID, ""));
            params.put(RequestParamsUtils.TOKEN, Utils.getPref(getActivity(), RequestParamsUtils.TOKEN, ""));
            Debug.e("GuestRequestListParams", params.toString());

            AsyncHttpClient clientPhotos = AsyncHttpRequest.newRequest();
            clientPhotos.get(new URLs().GUEST_REQUEST_LIST, params, new GuestListResponseHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteGuest(String id) {

        try {

            RequestParams params = new RequestParams();
            params.put(RequestParamsUtils.WS, Constants.CONDO);
            params.put(RequestParamsUtils.RES_ID, Utils.getPref(getActivity(), RequestParamsUtils.RES_ID, ""));
            params.put(RequestParamsUtils.GLR_ID, id);
            params.put(RequestParamsUtils.TOKEN, Utils.getPref(getActivity(), RequestParamsUtils.TOKEN, ""));
            Debug.e("DeleteGuestRequestParams", params.toString());

            AsyncHttpClient clientPhotos = AsyncHttpRequest.newRequest();
            clientPhotos.get(new URLs().DELETE_GUEST_REQUEST, params, new DeleteGuestListResponseHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void callEditGuestRequest(int position) {

        try {

            RequestParams params = new RequestParams();
            params.put(RequestParamsUtils.WS, Constants.CONDO);
            params.put(RequestParamsUtils.RES_ID, Utils.getPref(getActivity(), RequestParamsUtils.RES_ID, ""));
            params.put(RequestParamsUtils.NAME, etName.getText().toString());
            params.put(RequestParamsUtils.NO_OF_GUEST, etAddRequestExpected.getText().toString());
            params.put(RequestParamsUtils.VISITING_DATE, etaddReqDate.getTag().toString() + " " + etAddReqTime.getText().toString());
            params.put(RequestParamsUtils.GLR_ID, adapter.getItem(position).glrId);
            params.put(RequestParamsUtils.TOKEN, Utils.getPref(getActivity(), RequestParamsUtils.TOKEN, ""));
            Debug.e("EditGuestRequestListParams", params.toString());

            AsyncHttpClient clientPhotos = AsyncHttpRequest.newRequest();
            clientPhotos.get(new URLs().EDIT_GUEST_REQUEST, params, new EditGuestListResponseHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class GuestListResponseHandler extends AsyncResponseHandler {

        public GuestListResponseHandler(Activity context) {
            super(context);
        }

        @Override
        public void onStart() {
            super.onStart();
            mSwipeRefreshLayout.setRefreshing(true);
        }

        @Override
        public void onFinish() {
            super.onFinish();
            try {
                mSwipeRefreshLayout.setRefreshing(false);
                endlessList.releaseLock();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSuccess(String response) {

            try {

                Debug.e("", "GuestListResponse# " + response);
                if (response != null && response.length() > 0) {

                    GuestRequestList guestRequestList = new Gson().fromJson(
                            response, new TypeToken<GuestRequestList>() {
                            }.getType());

                    if (guestRequestList.st == 1) {

                        if (guestRequestList.data.size() > 0) {
                            adapter.addAll(guestRequestList.data);
                        }
                    } else {

                        Utils.showDialog(getActivity(), getString(R.string.title_important_no), guestRequestList.msg);
                    }

                }

                refreshPlaceHolder();
                endlessList.disableLoadMore();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(Throwable e, String content) {
            ((BaseActivity) getActivity()).dismissProgress();
            mSwipeRefreshLayout.setRefreshing(false);
            endlessList.releaseLock();
        }
    }

    private class DeleteGuestListResponseHandler extends AsyncResponseHandler {

        public DeleteGuestListResponseHandler(Activity context) {
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

                Debug.e("", "DeleteGuestListResponse# " + response);
                if (response != null && response.length() > 0) {

                    Response res = new Gson().fromJson(
                            response, new TypeToken<Response>() {
                            }.getType());

                    if (res.st == 402) {

                        ((BaseActivity) getActivity()).showToast(R.string.msg_not_updated, Toast.LENGTH_SHORT);

                    } else if (res.st == 505) {

                        ((BaseActivity) getActivity()).showToast(R.string.msg_exception, Toast.LENGTH_SHORT);

                    } else if (res.st == 506) {

                        ((BaseActivity) getActivity()).showToast(R.string.msg_unauthorised, Toast.LENGTH_SHORT);
                    }

                }

                refreshPlaceHolder();
                endlessList.disableLoadMore();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(Throwable e, String content) {
        }
    }

    private class EditGuestListResponseHandler extends AsyncResponseHandler {

        public EditGuestListResponseHandler(Activity context) {
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

                Debug.e("", "DeleteGuestListResponse# " + response);
                if (response != null && response.length() > 0) {

                    Response res = new Gson().fromJson(
                            response, new TypeToken<Response>() {
                            }.getType());

                    if (res.st == 402) {

                        ((BaseActivity) getActivity()).showToast(R.string.msg_not_updated, Toast.LENGTH_SHORT);

                    } else if (res.st == 502) {

                        ((BaseActivity) getActivity()).showToast(R.string.msg_no_data_found, Toast.LENGTH_SHORT);

                    } else if (res.st == 505) {

                        ((BaseActivity) getActivity()).showToast(R.string.msg_exception, Toast.LENGTH_SHORT);

                    } else if (res.st == 506) {

                        ((BaseActivity) getActivity()).showToast(R.string.msg_unauthorised, Toast.LENGTH_SHORT);
                    }

                }

                refreshPlaceHolder();
                endlessList.disableLoadMore();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(Throwable e, String content) {
        }
    }

}
