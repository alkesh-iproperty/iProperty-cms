package com.oozeetech.iproperty_cms.ui;

import android.app.Activity;
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
import com.oozeetech.iproperty_cms.adapter.ViewAllFaultsAdapter;
import com.oozeetech.iproperty_cms.models.ViewAllFaultsResponse;
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
public class ViewAllFaultsActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rvFaults)
    RecyclerView rvFaults;
    @Bind(R.id.tvTitle)
    TextView tvTitle;
    @Bind(R.id.llPlaceHolder)
    LinearLayout llPlaceHolder;
    @Bind(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    EndlessList endlessList;
    private LinearLayoutManager mLayoutManager;
    private int page;
    public ViewAllFaultsAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_faults);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");
        init();
    }

    public void init() {

        tvTitle.setText(getString(R.string.title_all_faults));
        mLayoutManager = new GridLayoutManager(getActivity(),2);
        rvFaults.setLayoutManager(mLayoutManager);
        rvFaults.setLayoutAnimation(Utils.getRowFadeSpeedAnimation(getActivity()));

        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.colorAccent, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isInternetConnected(getActivity())) {

                    getFaultList();

                } else {
                    showToast(R.string.internet_err, Toast.LENGTH_SHORT);
                }
            }
        });

        adapter = new ViewAllFaultsAdapter(getActivity());
        rvFaults.setAdapter(adapter);
        initEndlessList();
        resetPage();

        if (Utils.isInternetConnected(getActivity())) {

            getFaultList();


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
        page = 1;
        adapter.clear();
    }


    private void initEndlessList() {
        endlessList = new EndlessList(rvFaults, (LinearLayoutManager) mLayoutManager);
        endlessList.setOnLoadMoreListener(new EndlessList.OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                Debug.e("", "onLoadMore");
                if (Utils.isInternetConnected(getActivity())) {

                    getFaultList();

                } else {
                    showToast(R.string.internet_err, Toast.LENGTH_SHORT);
                }
            }

        });
    }

    public void getFaultList(){

        try {

            RequestParams params = new RequestParams();
            params.put(RequestParamsUtils.WS, Constants.CONDO);
            params.put(RequestParamsUtils.RES_ID, Utils.getPref(getActivity(),RequestParamsUtils.RES_ID,""));
            params.put(RequestParamsUtils.CONDO_ID, Utils.getPref(getActivity(),RequestParamsUtils.CONDO_ID,""));
            params.put(RequestParamsUtils.TOKEN,Utils.getPref(getActivity(),RequestParamsUtils.TOKEN,""));
            Debug.e("ViewAllFaultsParam", params.toString());

            AsyncHttpClient clientPhotos = AsyncHttpRequest.newRequest();
            clientPhotos.get(new URLs().VIEW_ALL_FAULTS, params, new AllFaultsHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class AllFaultsHandler extends AsyncResponseHandler {

        public AllFaultsHandler(Activity context) {
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
                Debug.e("", "ManagementResponse# " + response);
                if (response != null && response.length() > 0) {

                    ViewAllFaultsResponse res = new Gson().fromJson(
                            response, new TypeToken<ViewAllFaultsResponse>() {
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
                endlessList.disableLoadMore();


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


}
