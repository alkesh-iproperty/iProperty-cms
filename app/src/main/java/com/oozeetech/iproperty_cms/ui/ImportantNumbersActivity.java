package com.oozeetech.iproperty_cms.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.oozeetech.iproperty_cms.R;
import com.oozeetech.iproperty_cms.adapter.ImpNoAdapter;
import com.oozeetech.iproperty_cms.models.ImpNos;
import com.oozeetech.iproperty_cms.models.ImportantNumberResponse;
import com.oozeetech.iproperty_cms.utils.AsyncHttpRequest;
import com.oozeetech.iproperty_cms.utils.AsyncResponseHandler;
import com.oozeetech.iproperty_cms.utils.Constants;
import com.oozeetech.iproperty_cms.utils.Debug;
import com.oozeetech.iproperty_cms.utils.EndlessList;
import com.oozeetech.iproperty_cms.utils.RequestParamsUtils;
import com.oozeetech.iproperty_cms.utils.URLs;
import com.oozeetech.iproperty_cms.utils.Utils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by divyeshshani on 21/06/16.
 */
public class ImportantNumbersActivity extends BaseActivity {

    public ImpNoAdapter adapter;
    @Bind(R.id.rvImportantNumbers)
    RecyclerView rvImportantNumbers;
    @Bind(R.id.llPlaceHolder)
    LinearLayout llPlaceHolder;
    @Bind(R.id.mSwipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    EndlessList endlessList;
    private LinearLayoutManager mLayoutManager;
    private int page;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important_numbers_);
        ButterKnife.bind(this);
        initDrawer();
        navigationView.setCheckedItem(R.id.nav_action_important_number);
        setNavUsername();
        init();
    }

    public void init() {

        setTitleText(getString(R.string.title_important_no));
        mLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new ImpNoAdapter(getActivity());
        rvImportantNumbers.setLayoutManager(mLayoutManager);
        rvImportantNumbers.setLayoutAnimation(Utils.getRowFadeSpeedAnimation(getActivity()));
        rvImportantNumbers.setAdapter(adapter);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.colorAccent, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isInternetConnected(getActivity())) {

                    resetPage();
                    getImportantNumbers();

                } else {
                    showToast(R.string.internet_err, Toast.LENGTH_SHORT);
                }
            }
        });

        adapter.setEventListener(new ImpNoAdapter.EventListener() {
            @Override
            public void onItemViewClicked(View v) {

            }

            @Override
            public void onItemClick(int position) {

                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + adapter.getItem(position).number));
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                } else {

                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                }
                startActivity(intent);
            }
        });

        initEndlessList();
        resetPage();


        if (Utils.isInternetConnected(getActivity())) {

            getImportantNumbers();

        } else {
            showToast(R.string.internet_err, Toast.LENGTH_SHORT);

        }

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {


    }

    public void getImportantNumbers() {

        try {

            page++;

            RequestParams params = new RequestParams();
            params.put(RequestParamsUtils.WS, Constants.CONDO);
            params.put(RequestParamsUtils.RES_ID, Utils.getPref(getActivity(), RequestParamsUtils.RES_ID, ""));
            params.put(RequestParamsUtils.CONDO_ID, Utils.getPref(getActivity(), RequestParamsUtils.CONDO_ID, ""));
            params.put(RequestParamsUtils.TOKEN, Utils.getPref(getActivity(), RequestParamsUtils.TOKEN, ""));
            Debug.e("ImportantNosParam", params.toString());

            AsyncHttpClient clientPhotos = AsyncHttpRequest.newRequest();
            clientPhotos.get(new URLs().IMPORTANT_NUMBERS, params, new ImportantNumbersHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
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
        endlessList = new EndlessList(rvImportantNumbers, (LinearLayoutManager) mLayoutManager);
        endlessList.setOnLoadMoreListener(new EndlessList.OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                Debug.e("", "onLoadMore");
                if (Utils.isInternetConnected(getActivity())) {

                    getImportantNumbers();

                } else {
                    showToast(R.string.internet_err, Toast.LENGTH_SHORT);
                }
            }

        });
    }

    private class ImportantNumbersHandler extends AsyncResponseHandler {

        public ImportantNumbersHandler(Activity context) {
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

                Debug.e("", "ImportantNumber# " + response);
                if (response != null && response.length() > 0) {

                    ImportantNumberResponse impNos = new Gson().fromJson(
                            response, new TypeToken<ImportantNumberResponse>() {
                            }.getType());

                    if (impNos.st == 1) {

                        if (impNos.data.size() > 0) {
                            adapter.addAll(impNos.data);
                        }
                    } else {

                        // Utils.showDialog(getActivity(),getString(R.string.title_important_no),impNos.msg);
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
            dismissProgress();
            mSwipeRefreshLayout.setRefreshing(false);
            endlessList.releaseLock();
        }
    }


}
