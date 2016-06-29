package com.oozeetech.iproperty_cms.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.oozeetech.iproperty_cms.R;
import com.oozeetech.iproperty_cms.models.TabConfigResponse;
import com.oozeetech.iproperty_cms.utils.AsyncHttpRequest;
import com.oozeetech.iproperty_cms.utils.AsyncProgressDialog;
import com.oozeetech.iproperty_cms.utils.AsyncResponseHandler;
import com.oozeetech.iproperty_cms.utils.Constants;
import com.oozeetech.iproperty_cms.utils.Debug;
import com.oozeetech.iproperty_cms.utils.RequestParamsUtils;
import com.oozeetech.iproperty_cms.utils.URLs;
import com.oozeetech.iproperty_cms.utils.Utils;

/**
 * Created by divyeshshani on 21/06/16.
 */
public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public NavigationView navigationView;
    public TextView tvNavUsername;
    AsyncProgressDialog ad;
    TextView tvTitle;
    DrawerLayout drawer;
    TextView navMenuNoticeBadge, navMenuEventsBadge, navMenuGalleryBadge, navMenuPushNotificationBadge;
    MenuItem navMenuFacility,
            navMenuNotice,
            navMenuFault,
            navMenuGallery,
            navMenuManagement,
            navMenuWebsite,
            navMenuImpNos,
            navMenuEvents,
            navGuestList,
            navMenuMaintanance,
            navMenuQuitRent,
            navMenuFireInsurance,
            navMenuSinkingFund,
            navMenuPushNotification;
    private Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toast = Toast.makeText(getActivity(), "", Toast.LENGTH_LONG);


        if (Utils.isInternetConnected(getActivity())) {

            getTabSettings();

        } else {

            showToast(R.string.internet_err, Toast.LENGTH_SHORT);
        }
    }

    public void showToast(final int text, final int duration) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                toast.setText(getString(text).toString());
                toast.setDuration(duration);
                toast.show();
            }
        });
    }

    public void showProgress(String msg) {

        try {
            if (ad != null && ad.isShowing()) {
                return;
            }

            ad = AsyncProgressDialog.getInstant(getActivity());
            ad.show(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setNavUsername() {

        tvNavUsername.setText(Utils.getPref(getActivity(), Constants.USERNAME, ""));
    }

    public void initDrawer() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) toolbar.findViewById(R.id.tvTitleText);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setSelected(true);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        tvNavUsername = (TextView) headerView.findViewById(R.id.tvNavUsername);

        ImageView imageView = (ImageView) findViewById(R.id.imgMenu);
        if (imageView != null) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else {
                        drawer.openDrawer(GravityCompat.START);
                    }
                }
            });
        }


        navMenuNotice = navigationView.getMenu().findItem(R.id.nav_action_notices);
        navMenuPushNotification = navigationView.getMenu().findItem(R.id.nav_action_push_notification);
        navMenuEvents = navigationView.getMenu().findItem(R.id.nav_action_events);
        navMenuGallery = navigationView.getMenu().findItem(R.id.nav_action_gallery);
        navGuestList = navigationView.getMenu().findItem(R.id.nav_action_guest_list_request);
        navMenuFacility = navigationView.getMenu().findItem(R.id.nav_action_facility_booking);
        navMenuFault = navigationView.getMenu().findItem(R.id.nav_action_fault_repairing);
        navMenuFireInsurance = navigationView.getMenu().findItem(R.id.nav_action_fire_insurance);
        navMenuMaintanance = navigationView.getMenu().findItem(R.id.nav_action_maintenance);
        navMenuManagement = navigationView.getMenu().findItem(R.id.nav_action_management);
        navMenuQuitRent = navigationView.getMenu().findItem(R.id.nav_action_quit_rent);
        navMenuSinkingFund = navigationView.getMenu().findItem(R.id.nav_action_sinking_fund);
        navMenuImpNos = navigationView.getMenu().findItem(R.id.nav_action_important_number);
        navMenuWebsite = navigationView.getMenu().findItem(R.id.nav_action_website);

        navMenuGalleryBadge = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_action_gallery));
        navMenuEventsBadge = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_action_events));
        navMenuNoticeBadge = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_action_notices));
        navMenuPushNotificationBadge = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_action_push_notification));

        initializeCountDrawer();

    }


    public BaseActivity getActivity() {
        return this;
    }

    public void dismissProgress() {
        try {
            if (ad != null) {
                ad.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTitleText(String titleText) {
        tvTitle.setText(titleText);
    }

    public void confirmLogout() {

        AlertDialog builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.logout_title)
                .setMessage(R.string.logout_msg)
                .setPositiveButton(R.string.hint_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Utils.clearLoginCredetials(getActivity());

                        Intent intent = new Intent(getActivity(),
                                LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                                | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        finish();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_action_home) {

            if (!navigationView.getMenu().getItem(0).isChecked()) {

                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        }

        if (id == R.id.nav_action_facility_booking) {

                Intent intent = new Intent(getActivity(), FacilityBookingDashbordActivity.class);
                startActivity(intent);

        }

        if (id == R.id.nav_action_fault_repairing) {

            Intent intent = new Intent(getActivity(), FaultReportingActivity.class);
            startActivity(intent);

        }

        if (id == R.id.nav_action_important_number) {

            if (!navigationView.getMenu().getItem(2).isChecked()) {

                Intent intent = new Intent(getActivity(), ImportantNumbersActivity.class);
                startActivity(intent);
            }


        }
        if (id == R.id.nav_action_notices) {

            if (!navigationView.getMenu().getItem(3).isChecked()) {
                Intent intent = new Intent(getActivity(), NoticesActivity.class);
                startActivity(intent);
            }

        }
        if (id == R.id.nav_action_gallery) {

            if (!navigationView.getMenu().getItem(3).isChecked()) {
                Intent intent = new Intent(getActivity(), GelleryActivity.class);
                startActivity(intent);
            }

        }
        if (id == R.id.nav_action_management) {

            if (!navigationView.getMenu().getItem(4).isChecked()) {
                Intent intent = new Intent(getActivity(), ManagementActivity.class);
                startActivity(intent);
            }

        }
        if (id == R.id.nav_action_events) {


            if (!navigationView.getMenu().getItem(5).isChecked()) {
                Intent intent = new Intent(getActivity(), EventsActivity.class);
                startActivity(intent);
            }

        }

        if (id == R.id.nav_action_website) {


            Intent intent = new Intent(getActivity(), WebsiteActivity.class);
            startActivity(intent);

        }

        if (id == R.id.nav_action_maintenance) {

            Intent intent = new Intent(getActivity(), PaymentDetailsActivity.class);
            intent.putExtra(Constants.FROM_PAYMENT_TYPE,Constants.TYPE_MAINTANANCE);
            startActivity(intent);

        }

        if (id == R.id.nav_action_sinking_fund) {

            Intent intent = new Intent(getActivity(), PaymentDetailsActivity.class);
            intent.putExtra(Constants.FROM_PAYMENT_TYPE,Constants.TYPE_SINKING_FUND);
            startActivity(intent);

        }

        if (id == R.id.nav_action_quit_rent) {

            Intent intent = new Intent(getActivity(), PaymentDetailsActivity.class);
            intent.putExtra(Constants.FROM_PAYMENT_TYPE,Constants.TYPE_QUITE_RENT);
            startActivity(intent);

        }

        if (id == R.id.nav_action_fire_insurance) {

            Intent intent = new Intent(getActivity(), PaymentDetailsActivity.class);
            intent.putExtra(Constants.FROM_PAYMENT_TYPE,Constants.TYPE_FIRE_INSURENCE);
            startActivity(intent);

        }

        if (id == R.id.nav_action_push_notification) {

            Intent intent = new Intent(getActivity(), PushNotificationActivity.class);
            startActivity(intent);

        }

        if (id == R.id.nav_action_guest_list_request) {

            Intent intent = new Intent(getActivity(), GuestRequestActivity.class);
            startActivity(intent);

        }

        if (id == R.id.nav_action_logout) {

            confirmLogout();

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void getTabSettings() {

        try {

            showProgress("");
            RequestParams params = new RequestParams();
            params.put(RequestParamsUtils.WS, Constants.CONDO);
            params.put(RequestParamsUtils.RES_ID, Utils.getPref(getActivity(), RequestParamsUtils.RES_ID, ""));
            params.put(RequestParamsUtils.CONDO_ID, Utils.getPref(getActivity(), RequestParamsUtils.CONDO_ID, ""));
            params.put(RequestParamsUtils.TOKEN, Utils.getPref(getActivity(), RequestParamsUtils.TOKEN, ""));
            Debug.e("TabsParams", params.toString());

            AsyncHttpClient clientPhotos = AsyncHttpRequest.newRequest();
            clientPhotos.post(new URLs().TAB_CONFIG, params, new GetTabsConfigParams(getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initializeCountDrawer() {


        navMenuGalleryBadge.setGravity(Gravity.CENTER);
        navMenuGalleryBadge.setTextColor(getResources().getColor(R.color.white));
        navMenuGalleryBadge.setText("7");
        navMenuGalleryBadge.setTextSize(12);
        navMenuGalleryBadge.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_circle_black));

    }

    private class GetTabsConfigParams extends AsyncResponseHandler {

        public GetTabsConfigParams(Activity context) {
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
                Debug.e("", "TabsConfigResponse# " + response);
                if (response != null && response.length() > 0) {

                    dismissProgress();

                    TabConfigResponse res = new Gson().fromJson(
                            response, new TypeToken<TabConfigResponse>() {
                            }.getType());

                    if (res.st == 1) {

                        String[] notiVals = res.data.get(0).notice.split("||");
                        String[] gallery = res.data.get(0).gallery.split("||");
                        String[] events = res.data.get(0).event.split("||");

                        boolean isMngmntVisible = res.data.get(0).management.equals("1");
                        boolean isMaintananceVisible = res.data.get(0).maintenance.equals("1");
                        boolean isQuiteRentVisible = res.data.get(0).quitRent.equals("1");
                        boolean isImpNosVisible = res.data.get(0).emergencyNo.equals("1");
                        boolean isGuestListVisible = res.data.get(0).guestListRequest.equals("1");
                        boolean isWebVisible = res.data.get(0).website.equals("1");
                        boolean isFireInsurenceVisible = res.data.get(0).fireInsurance.equals("1");
                        boolean isSinkingVisible = res.data.get(0).sinkingFund.equals("1");
                        boolean isFacilityVisible = res.data.get(0).facility.equals("1");
                        boolean isFaultVisible = res.data.get(0).faultReporting.equals("1");
                        boolean isEventVisible = events[1].equals("1");
                        boolean isGalleryVisible = gallery[1].equals("1");
                        boolean isNotiVisible = notiVals[1].equals("1");


                        int notiCount = Integer.parseInt(res.data.get(0).notice.substring(res.data.get(0).notice.lastIndexOf("|") + 1, res.data.get(0).notice.length()));
                        int eventCount = Integer.parseInt(res.data.get(0).event.substring(res.data.get(0).event.lastIndexOf("|") + 1, res.data.get(0).event.length()));
                        int galleryCount = Integer.parseInt(res.data.get(0).gallery.substring(res.data.get(0).gallery.lastIndexOf("|") + 1, res.data.get(0).gallery.length()));
                        int pushNoti = Integer.parseInt(res.data.get(0).pushNotification.substring(res.data.get(0).pushNotification.lastIndexOf("|") + 1, res.data.get(0).pushNotification.length()));

                        if (notiCount <= 0) {

                            navMenuNoticeBadge.setVisibility(View.GONE);

                        } else {

                            navMenuNoticeBadge.setVisibility(View.VISIBLE);
                            navMenuNoticeBadge.setGravity(Gravity.CENTER);
                            navMenuNoticeBadge.setTextColor(getResources().getColor(R.color.white));
                            navMenuNoticeBadge.setText("" + notiCount);
                            navMenuNoticeBadge.setTextSize(12);
                            navMenuNoticeBadge.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_circle_black));
                        }

                        if (galleryCount <= 0) {

                            navMenuGalleryBadge.setVisibility(View.GONE);

                        } else {

                            navMenuGalleryBadge.setVisibility(View.VISIBLE);
                            navMenuGalleryBadge.setGravity(Gravity.CENTER);
                            navMenuGalleryBadge.setTextColor(getResources().getColor(R.color.white));
                            navMenuGalleryBadge.setText("" + galleryCount);
                            navMenuGalleryBadge.setTextSize(12);
                            navMenuGalleryBadge.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_circle_black));
                        }

                        if (eventCount <= 0) {

                            navMenuEventsBadge.setVisibility(View.GONE);

                        } else {

                            navMenuEventsBadge.setVisibility(View.VISIBLE);
                            navMenuEventsBadge.setGravity(Gravity.CENTER);
                            navMenuEventsBadge.setTextColor(getResources().getColor(R.color.white));
                            navMenuEventsBadge.setText("" + eventCount);
                            navMenuEventsBadge.setTextSize(12);
                            navMenuEventsBadge.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_circle_black));
                        }

                        if (pushNoti <= 0) {

                            navMenuPushNotificationBadge.setVisibility(View.GONE);

                        } else {

                            navMenuPushNotificationBadge.setVisibility(View.VISIBLE);
                            navMenuPushNotificationBadge.setGravity(Gravity.CENTER);
                            navMenuPushNotificationBadge.setTextColor(getResources().getColor(R.color.white));
                            navMenuPushNotificationBadge.setText("" + pushNoti);
                            navMenuPushNotificationBadge.setTextSize(12);
                            navMenuPushNotificationBadge.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_circle_black));
                        }


                        navMenuPushNotification.setVisible(true);

                        if (!isMngmntVisible) {
                            navMenuManagement.setVisible(false);
                        } else {
                            navMenuManagement.setVisible(true);
                        }

                        if (!isQuiteRentVisible) {
                            navMenuQuitRent.setVisible(false);
                        } else {
                            navMenuQuitRent.setVisible(true);
                        }

                        if (!isWebVisible) {
                            navMenuWebsite.setVisible(false);
                        } else {
                            navMenuWebsite.setVisible(true);
                        }

                        if (!isGuestListVisible) {
                            navGuestList.setVisible(false);
                        } else {
                            navGuestList.setVisible(true);
                        }

                        if (!isSinkingVisible) {
                            navMenuSinkingFund.setVisible(false);
                        } else {
                            navMenuSinkingFund.setVisible(true);
                        }

                        if (!isGalleryVisible) {
                            navMenuGallery.setVisible(false);
                        } else {
                            navMenuGallery.setVisible(true);
                        }

                        if (!isMaintananceVisible) {
                            navMenuMaintanance.setVisible(false);
                        } else {
                            navMenuMaintanance.setVisible(true);
                        }

                        if (!isImpNosVisible) {
                            navMenuImpNos.setVisible(false);
                        } else {
                            navMenuImpNos.setVisible(true);
                        }

                        if (!isFireInsurenceVisible) {
                            navMenuFireInsurance.setVisible(false);
                        } else {
                            navMenuFireInsurance.setVisible(true);
                        }

                        if (!isFacilityVisible) {
                            navMenuFacility.setVisible(false);
                        } else {
                            navMenuFacility.setVisible(true);
                        }

                        if (!isFaultVisible) {
                            navMenuFault.setVisible(false);
                        } else {
                            navMenuFault.setVisible(true);
                        }

                        if (!isEventVisible) {
                            navMenuEvents.setVisible(false);
                        } else {
                            navMenuEvents.setVisible(true);
                        }

                        if (!isNotiVisible) {
                            navMenuNotice.setVisible(false);
                        } else {
                            navMenuNotice.setVisible(true);
                        }


                    } else if (res.st == 505) {

                        getActivity().showToast(R.string.msg_exception, Toast.LENGTH_SHORT);

                    } else if (res.st == 506) {

                        getActivity().showToast(R.string.msg_unauthorised, Toast.LENGTH_SHORT);
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
