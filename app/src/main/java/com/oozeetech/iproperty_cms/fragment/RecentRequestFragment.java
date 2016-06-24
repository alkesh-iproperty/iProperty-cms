package com.oozeetech.iproperty_cms.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oozeetech.iproperty_cms.R;

/**
 * Created by divyeshshani on 23/06/16.
 */
public class RecentRequestFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_recent_request,container,false);

        return v;
    }
}
