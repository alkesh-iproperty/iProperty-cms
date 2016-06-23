package com.oozeetech.iproperty_cms.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.oozeetech.iproperty_cms.R;
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
public class EventsActivity extends BaseActivity {

    @Bind(R.id.rvNotices)
    RecyclerView rvNotices;
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
        setContentView(R.layout.activity_events_);
        ButterKnife.bind(this);
        initDrawer();
        init();
    }

    public void init() {

        setTitleText(getString(R.string.title_event));
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvNotices.setLayoutManager(mLayoutManager);
        rvNotices.setLayoutAnimation(Utils.getRowFadeSpeedAnimation(getActivity()));

        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.colorAccent, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isInternetConnected(getActivity())) {



                } else {
                    showToast(R.string.internet_err, Toast.LENGTH_SHORT);
                }
            }
        });

//        adapter = new NewsAdapter(getActivity());
//        rvNotices.setAdapter(adapter);
        initEndlessList();
        resetPage();

        if (Utils.isInternetConnected(getActivity())) {

            getEventsList();

        } else {
            showToast(R.string.internet_err, Toast.LENGTH_SHORT);

        }

    }


    public void refreshPlaceHolder() {

//        if (adapter.getItemCount() > 0) {
//            llPlaceHolder.setVisibility(View.GONE);
//        } else {
//            llPlaceHolder.setVisibility(View.VISIBLE);
//        }
    }

    private void resetPage() {
        page = 0;
//        adapter.clear();
    }

    int TOP_RECORD = 25;

    private void initEndlessList() {
        endlessList = new EndlessList(rvNotices, (LinearLayoutManager) mLayoutManager);
        endlessList.setOnLoadMoreListener(new EndlessList.OnLoadMoreListener() {

            @Override
            public void onLoadMore() {
                Debug.e("", "onLoadMore");
                if (Utils.isInternetConnected(getActivity())) {

                    getEventsList();

                } else {
                    showToast(R.string.internet_err, Toast.LENGTH_SHORT);
                }
            }

        });
    }

    public void getEventsList(){

        try {

            showProgress("");

            RequestParams params = new RequestParams();
            params.put(RequestParamsUtils.WS, Constants.CONDO);
            params.put(RequestParamsUtils.RES_ID, Utils.getPref(getActivity(),RequestParamsUtils.RES_ID,""));
            params.put(RequestParamsUtils.PAGE, page);
            params.put(RequestParamsUtils.SEARCH, "");
            params.put(RequestParamsUtils.CONDO_ID, Utils.getPref(getActivity(),RequestParamsUtils.CONDO_ID,""));
            params.put(RequestParamsUtils.TOKEN,Utils.getPref(getActivity(),RequestParamsUtils.TOKEN,""));
            Debug.e("EventParams", params.toString());

            AsyncHttpClient clientPhotos = AsyncHttpRequest.newRequest();
            clientPhotos.get(new URLs().EVENTS_LIST, params, new EventsListHandler(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class EventsListHandler extends AsyncResponseHandler {

        public EventsListHandler(Activity context) {
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
                Debug.e("", "EventsResponse# " + response);
                if (response != null && response.length() > 0) {
//                    News news = new Gson().fromJson(
//                            response, new TypeToken<News>() {
//                            }.getType());
//
//                    if (news.st == 1)
//
//                        if (news.data.size() > 0) {
//                            adapter.addAll(news.data);
//                        } else {
//
//                            if (adapter.getItemCount() > 0) {
//                                llPlaceHolder.setVisibility(View.GONE);
//                            } else {
//                                llPlaceHolder.setVisibility(View.VISIBLE);
//                            }
//                            endlessList.disableLoadMore();
//                        }

                } else {
                    endlessList.disableLoadMore();
                }


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
