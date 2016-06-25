package com.oozeetech.iproperty_cms.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oozeetech.iproperty_cms.R;
import com.oozeetech.iproperty_cms.utils.AsyncProgressDialog;
import com.oozeetech.iproperty_cms.utils.Constants;
import com.oozeetech.iproperty_cms.utils.Utils;

/**
 * Created by divyeshshani on 21/06/16.
 */
public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    AsyncProgressDialog ad;
    TextView tvTitle;
    private Toast toast;
    DrawerLayout drawer;
    public NavigationView navigationView;
    public TextView tvNavUsername;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toast = Toast.makeText(getActivity(), "", Toast.LENGTH_LONG);
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

    public void setNavUsername(){

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

    public void setTitleText(String titleText){
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

        if (id == R.id.nav_action_facility_booking) {

            if(!navigationView.getMenu().getItem(0).isChecked()) {

                Intent intent = new Intent(getActivity(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        }

        if (id == R.id.nav_action_fault_repairing) {

            Intent intent = new Intent(getActivity(), FaultReportingActivity.class);
            startActivity(intent);

        }

        if (id == R.id.nav_action_important_number) {

            if(!navigationView.getMenu().getItem(2).isChecked()){

                Intent intent = new Intent(getActivity(), ImportantNumbersActivity.class);
                startActivity(intent);
            }


        } if (id == R.id.nav_action_notices) {

            if(!navigationView.getMenu().getItem(3).isChecked()) {
                Intent intent = new Intent(getActivity(), NoticesActivity.class);
                startActivity(intent);
            }

        } if (id == R.id.nav_action_management) {

            if(!navigationView.getMenu().getItem(4).isChecked()) {
                Intent intent = new Intent(getActivity(), ManagementActivity.class);
                startActivity(intent);
            }

        }
        if (id == R.id.nav_action_events) {


            if(!navigationView.getMenu().getItem(5).isChecked()) {
                Intent intent = new Intent(getActivity(), EventsActivity.class);
                startActivity(intent);
            }

        }

        if (id == R.id.nav_action_website) {


                Intent intent = new Intent(getActivity(), WebsiteActivity.class);
                startActivity(intent);

        }

        if (id == R.id.nav_action_logout) {

           confirmLogout();

        }

        if (id == R.id.nav_action_guest_list_request) {

            Intent intent = new Intent(getActivity(), GuestRequestActivity.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
