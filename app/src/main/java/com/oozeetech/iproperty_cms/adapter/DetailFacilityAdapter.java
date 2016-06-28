package com.oozeetech.iproperty_cms.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.oozeetech.iproperty_cms.R;
import com.oozeetech.iproperty_cms.models.ChildFacilityResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by divyeshshani on 25/06/16.
 */
public class DetailFacilityAdapter extends RecyclerView.Adapter<DetailFacilityAdapter.DetailsFacilityHolder> {

    Context context;
    private List<ChildFacilityResponse.Datum_> data = new ArrayList<>();

    private EventListener mEventListener;
    private View.OnClickListener mItemViewOnClickListener;


    public DetailFacilityAdapter(Context context) {

        this.context = context;

    }

    public void addAll(List<ChildFacilityResponse.Datum_> mData) {

        data.addAll(mData);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public ChildFacilityResponse.Datum_ getItem(int position) {
        return data.get(position);
    }

    @Override
    public DetailsFacilityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_detail_facility, parent, false);
        return new DetailsFacilityHolder(v);
    }

    @Override
    public void onBindViewHolder(final DetailsFacilityHolder holder, final int position) {

        holder.tvDetailFacilityFees.setText(data.get(position).depositAmount);
        holder.tvDetailFacilityName.setText(data.get(position).fcName);
        holder.btnBookFacility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEventListener != null) {

                    mEventListener.onItemClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public EventListener getEventListener() {
        return mEventListener;
    }

    public void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }


    public interface EventListener {
        void onItemClick(int position);
    }

    public static class DetailsFacilityHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.tvDetailFacilityName)
        TextView tvDetailFacilityName;
        @Bind(R.id.tvDetailFacilityFees)
        TextView tvDetailFacilityFees;
        @Bind(R.id.btnBookFacility)
        Button btnBookFacility;


        public DetailsFacilityHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
