package com.oozeetech.iproperty_cms.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oozeetech.iproperty_cms.R;
import com.oozeetech.iproperty_cms.models.GuestRequestList;
import com.oozeetech.iproperty_cms.models.NoticesResponse;
import com.oozeetech.iproperty_cms.utils.URLs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by divyeshshani on 21/06/16.
 */
public class GuestRequestAdapter extends RecyclerView.Adapter<GuestRequestAdapter.GuestRequestHolder> {

    Context context;
    private List<GuestRequestList.Datum> data = new ArrayList<>();

    private EventListener mEventListener;
    private View.OnClickListener mItemViewOnClickListener;


    public interface EventListener {
        void onItemClick(View v,int position);
    }

    public static class GuestRequestHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.tvGuestListDate)
        TextView tvGuestListDate;
        @Bind(R.id.tvGuestListPersons)
        TextView tvGuestListPersons;
        @Bind(R.id.tvGuestListTime)
        TextView tvGuestListTime;
        @Bind(R.id.tvGuestName)
        TextView tvGuestName;
        @Bind(R.id.ivGuestOptions)
        ImageView ivGuestOptions;

        public GuestRequestHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public GuestRequestAdapter(Context context) {

        this.context = context;
    }

    public void updateList(int position,GuestRequestList.Datum datum){

        data.set(position,datum);
        notifyDataSetChanged();
    }

    public void addAll(List<GuestRequestList.Datum> mData) {

        data.addAll(mData);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void remove(int position){
        data.remove(position);
        notifyDataSetChanged();
    }


    public GuestRequestList.Datum getItem(int position) {
        return data.get(position);
    }

    @Override
    public GuestRequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_guest_recent_request, parent, false);
        return new GuestRequestHolder(v);
    }

    @Override
    public void onBindViewHolder(final GuestRequestHolder holder, final int position) {

        SimpleDateFormat myFormat = new SimpleDateFormat("MMM dd, yyyy");
        SimpleDateFormat myTimeFormate = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {

            holder.tvGuestListPersons.setText(data.get(position).noOfGuest);
            holder.tvGuestListDate.setText(myFormat.format(originalFormat.parse(data.get(position).visitingDate)));
            holder.tvGuestListTime.setText(myTimeFormate.format(originalFormat.parse(data.get(position).visitingDate)));
            holder.tvGuestName.setText(data.get(position).name);

        }catch (Exception e){

        }

        holder.ivGuestOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEventListener!=null){

                    mEventListener.onItemClick(holder.ivGuestOptions,position);
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

}
