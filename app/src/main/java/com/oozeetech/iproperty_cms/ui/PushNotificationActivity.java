package com.oozeetech.iproperty_cms.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.oozeetech.iproperty_cms.R;
import com.oozeetech.iproperty_cms.adapter.GalleryDetailsListAdapter;
import com.oozeetech.iproperty_cms.adapter.PushNotificationAdapter;
import com.oozeetech.iproperty_cms.models.GalleryDetailsListResponse;
import com.oozeetech.iproperty_cms.models.GalleryListResponse;
import com.oozeetech.iproperty_cms.models.PushNotificationResponse;
import com.oozeetech.iproperty_cms.utils.AsyncHttpRequest;
import com.oozeetech.iproperty_cms.utils.AsyncResponseHandler;
import com.oozeetech.iproperty_cms.utils.Constants;
import com.oozeetech.iproperty_cms.utils.Debug;
import com.oozeetech.iproperty_cms.utils.EndlessList;
import com.oozeetech.iproperty_cms.utils.RequestParamsUtils;
import com.oozeetech.iproperty_cms.utils.URLs;
import com.oozeetech.iproperty_cms.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by divyeshshani on 21/06/16.
 */
public class PushNotificationActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvTitle)
    TextView tvTitle;
    @Bind(R.id.rvPushNotification)
    RecyclerView rvPushNotification;
    @Bind(R.id.llPlaceHolder)
    LinearLayout llPlaceHolder;
    @Bind(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    EndlessList endlessList;
    private LinearLayoutManager mLayoutManager;
    private int page;
    private PushNotificationAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notification);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        init();
    }

    public void init() {

        tvTitle.setText(getString(R.string.title_push_noti));

        mLayoutManager = new LinearLayoutManager(getActivity());
        rvPushNotification.setLayoutManager(mLayoutManager);
        rvPushNotification.setLayoutAnimation(Utils.getRowFadeSpeedAnimation(getActivity()));

        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.colorAccent, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isInternetConnected(getActivity())) {

                    resetPage();
                    getPushNotifications();

                } else {
                    showToast(R.string.internet_err, Toast.LENGTH_SHORT);
                }
            }
        });

        adapter = new PushNotificationAdapter(getActivity());
        rvPushNotification.setAdapter(adapter);

        adapter.setEventListener(new PushNotificationAdapter.EventListener() {
            @Override
            public void onItemViewClicked(View v) {

            }

            @Override
            public void onItemClick(int position) {

                if(Utils.isInternetConnected(getActivity())) {

                    if(adapter.getItem(position).read.equals("1")){
                        adapter.getItem(position).read = "0";
                        updateUnread(adapter.getItem(position).id);
                        adapter.notifyDataSetChanged();
                    }

                }else {
                    showToast(R.string.internet_err,Toast.LENGTH_SHORT);
                }

                if(adapter.getItem(position).contentType.equals("2")){

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(adapter.getItem(position).contentTypeData));
                    startActivity(i);
                }

                if(adapter.getItem(position).contentType.equals("3")){

                    if(adapter.getItem(position).contentTypeData.equals("1")){

                        Intent intent = new Intent(getActivity(),FacilityBookingDashbordActivity.class);
                        startActivity(intent);
                    }
                    if(adapter.getItem(position).contentTypeData.equals("2")){

                        Intent intent = new Intent(getActivity(),FaultReportingActivity.class);
                        startActivity(intent);
                    }
                    if(adapter.getItem(position).contentTypeData.equals("3")){

                        Intent intent = new Intent(getActivity(),ImportantNumbersActivity.class);
                        startActivity(intent);
                    }
                    if(adapter.getItem(position).contentTypeData.equals("4")){

                        Intent intent = new Intent(getActivity(),NoticesActivity.class);
                        startActivity(intent);
                    }
                    if(adapter.getItem(position).contentTypeData.equals("5")){

                        Intent intent = new Intent(getActivity(),ManagementActivity.class);
                        startActivity(intent);
                    }
                    if(adapter.getItem(position).contentTypeData.equals("6")){

                        Intent intent = new Intent(getActivity(),EventsActivity.class);
                        startActivity(intent);
                    }
                    if(adapter.getItem(position).contentTypeData.equals("7")){

                        Intent intent = new Intent(getActivity(),GelleryActivity.class);
                        startActivity(intent);
                    }
                    if(adapter.getItem(position).contentTypeData.equals("8")){

                        Intent intent = new Intent(getActivity(),WebsiteActivity.class);
                        startActivity(intent);
                    }
                    if(adapter.getItem(position).contentTypeData.equals("9")){

                        Intent intent = new Intent(getActivity(),GuestRequestActivity.class);
                        startActivity(intent);
                    }
                    if(adapter.getItem(position).contentTypeData.equals("9")){

                        Intent intent = new Intent(getActivity(),PaymentDetailsActivity.class);
                        intent.putExtra(Constants.FROM_PAYMENT_TYPE,Constants.TYPE_MAINTANANCE);
                        startActivity(intent);
                    }
                    if(adapter.getItem(position).contentTypeData.equals("10")){

                        Intent intent = new Intent(getActivity(),PaymentDetailsActivity.class);
                        intent.putExtra(Constants.FROM_PAYMENT_TYPE,Constants.TYPE_MAINTANANCE);
                        startActivity(intent);
                    }

                }
            }
        });

        initEndlessList();
        resetPage();

        if (Utils.isInternetConnected(getActivity())) {

            getPushNotifications();


        } else {
            showToast(R.string.internet_err, Toast.LENGTH_SHORT);

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

        endlessList = new EndlessList(rvPushNotification, (LinearLayoutManager) mLayoutManager);
        endlessList.setOnLoadMoreListener(new EndlessList.OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                Debug.e("", "onLoadMore");
                if (Utils.isInternetConnected(getActivity())) {

                    getPushNotifications();

                } else {
                    showToast(R.string.internet_err, Toast.LENGTH_SHORT);
                }
            }

        });
    }

    public void getPushNotifications() {

        try {

            endlessList.lockMoreLoading();
            page++;

            RequestParams params = new RequestParams();
            params.put(RequestParamsUtils.WS, Constants.CONDO);
            params.put(RequestParamsUtils.RES_ID, Utils.getPref(getActivity(), RequestParamsUtils.RES_ID, ""));
            params.put(RequestParamsUtils.PAGE, page);
            params.put(RequestParamsUtils.TOKEN, Utils.getPref(getActivity(), RequestParamsUtils.TOKEN, ""));
            Debug.e("PushNotificationParams", params.toString());

            AsyncHttpClient clientPhotos = AsyncHttpRequest.newRequest();
            clientPhotos.get(new URLs().PUSH_NOTIFICATION_LIST, params, new PushuNotificationHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class PushuNotificationHandler extends AsyncResponseHandler {

        public PushuNotificationHandler(Activity context) {
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

                Debug.e("", "PushNotiResposne# " + response);
                if (response != null && response.length() > 0) {

                    PushNotificationResponse res = new Gson().fromJson(
                            response, new TypeToken<PushNotificationResponse>() {
                            }.getType());

                    if (res.st == 1) {

                        if (res.data.size() > 0) {
                            adapter.addAll(res.data);
                        } else {
                            endlessList.disableLoadMore();
                        }


                    } else if (res.st == 502) {

                        getActivity().showToast(R.string.msg_no_data_found, Toast.LENGTH_SHORT);
                        endlessList.disableLoadMore();

                    } else if (res.st == 505) {

                        getActivity().showToast(R.string.msg_exception, Toast.LENGTH_SHORT);
                        endlessList.disableLoadMore();

                    } else if (res.st == 506) {

                        getActivity().showToast(R.string.msg_unauthorised, Toast.LENGTH_SHORT);
                        endlessList.disableLoadMore();
                    }

                }

                refreshPlaceHolder();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(Throwable e, String content) {
            mSwipeRefreshLayout.setRefreshing(false);
            endlessList.releaseLock();
        }
    }

    public void updateUnread(String id) {

        try {

            RequestParams params = new RequestParams();
            params.put(RequestParamsUtils.WS, Constants.CONDO);
            params.put(RequestParamsUtils.ID, id);
            params.put(RequestParamsUtils.RES_ID, Utils.getPref(getActivity(), RequestParamsUtils.RES_ID, ""));
            params.put(RequestParamsUtils.TOKEN, Utils.getPref(getActivity(), RequestParamsUtils.TOKEN, ""));
            Debug.e("UnreadPushNotiParam", params.toString());

            AsyncHttpClient clientPhotos = AsyncHttpRequest.newRequest();
            clientPhotos.get(new URLs().UPDATE_PUSH_NOTIFICATION_READ, params, new UnreadPushuNotificationHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class UnreadPushuNotificationHandler extends AsyncResponseHandler {

        public UnreadPushuNotificationHandler(Activity context) {
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

                Debug.e("", "PushNotiResposne# " + response);
                if (response != null && response.length() > 0) {

                    PushNotificationResponse res = new Gson().fromJson(
                            response, new TypeToken<PushNotificationResponse>() {
                            }.getType());

                    if (res.st == 1) {

                        if (res.data.size() > 0) {
                            adapter.addAll(res.data);
                        } else {
                            refreshPlaceHolder();
                            endlessList.disableLoadMore();
                        }


                    } else if (res.st == 502) {

                        getActivity().showToast(R.string.msg_no_data_found, Toast.LENGTH_SHORT);

                    } else if (res.st == 505) {

                        getActivity().showToast(R.string.msg_exception, Toast.LENGTH_SHORT);

                    } else if (res.st == 506) {

                        getActivity().showToast(R.string.msg_unauthorised, Toast.LENGTH_SHORT);
                    }

                }

                refreshPlaceHolder();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFailure(Throwable e, String content) {
        }
    }

}
