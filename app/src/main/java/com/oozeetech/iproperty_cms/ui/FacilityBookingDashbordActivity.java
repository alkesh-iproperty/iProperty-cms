package com.oozeetech.iproperty_cms.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.oozeetech.iproperty_cms.R;
import com.oozeetech.iproperty_cms.models.MasterFacilityResponse;
import com.oozeetech.iproperty_cms.utils.Constants;
import com.oozeetech.iproperty_cms.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by divyeshshani on 21/06/16.
 */
public class FacilityBookingDashbordActivity extends BaseActivity {

    @Bind(R.id.facilityGrid)
    GridView facilityGrid;

    MasterFacilityResponse response;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_booking_dash_bord_);

        ButterKnife.bind(this);
        initDrawer();

        response = new Gson().fromJson(Utils.getPref(getActivity(), Constants.MASTER_FACILITY, ""), MasterFacilityResponse.class);
        FacilityAdapter adapter = new FacilityAdapter();
        facilityGrid.setAdapter(adapter);
        adapter.addAll(response.data);


    }

    @Override
    protected void onResume() {
        super.onResume();

        navigationView.setCheckedItem(R.id.nav_action_facility_booking);
        setTitleText(getString(R.string.title_facility_booking));
        setNavUsername();
    }

    public class FacilityAdapter extends BaseAdapter {

        public ArrayList<MasterFacilityResponse.Datum> list;

        public FacilityAdapter() {
            list = new ArrayList<>();
        }

        public void addAll(List<MasterFacilityResponse.Datum> list) {
            this.list.addAll(list);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            ImageView ivIcon;
            TextView tvTitle = null;


            view = LayoutInflater.from(getActivity()).inflate(R.layout.item_facility_grid, null);
            view.setMinimumHeight(facilityGrid.getHeight() / 3);
            ivIcon = (ImageView) view.findViewById(R.id.ivFacilityGridIcon);
            tvTitle = (TextView) view.findViewById(R.id.tvFacilityGrid);

            if (list.get(i).fmId.equals("1")) {

                ivIcon.setImageResource(R.drawable.ic_playground);
            }
            if (list.get(i).fmId.equals("2")) {

                ivIcon.setImageResource(R.drawable.ic_bbq);
            }
            if (list.get(i).fmId.equals("4")) {

                ivIcon.setImageResource(R.drawable.ic_gym);
            }

            if (list.get(i).fmId.equals("5")) {

                ivIcon.setImageResource(R.drawable.ic_function_hall);
            }
            if (list.get(i).fmId.equals("7")) {

                ivIcon.setImageResource(R.drawable.ic_tennis_court);
            }
            if (list.get(i).fmId.equals("8")) {

                ivIcon.setImageResource(R.drawable.ic_basket_ball);
            }

            tvTitle.setText(list.get(i).fmName);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getActivity(), FacilityBookindActivity.class);
                    intent.putExtra(Constants.FACILITY_DETAILS, new Gson().toJson(list.get(i)));
                    startActivity(intent);
                }
            });

            return view;
        }
    }
}
