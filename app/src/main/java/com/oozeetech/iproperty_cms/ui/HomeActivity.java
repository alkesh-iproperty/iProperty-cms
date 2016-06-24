package com.oozeetech.iproperty_cms.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.oozeetech.iproperty_cms.R;

import butterknife.ButterKnife;

/**
 * Created by divyeshshani on 21/06/16.
 */
public class HomeActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_);

        ButterKnife.bind(this);

        initDrawer();



        setTitleText(getString(R.string.title_facility_booking));
    }

    @Override
    protected void onResume() {
        super.onResume();

        navigationView.setCheckedItem(R.id.nav_action_facility_booking);

        setNavUsername();
    }
}
