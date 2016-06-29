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
import com.oozeetech.iproperty_cms.models.BookingData;
import com.oozeetech.iproperty_cms.models.BookingListResponse;
import com.oozeetech.iproperty_cms.models.FacilityBookingResponse;
import com.oozeetech.iproperty_cms.models.GalleryDetailsListResponse;
import com.oozeetech.iproperty_cms.models.PushNotificationResponse;
import com.oozeetech.iproperty_cms.utils.URLs;
import com.oozeetech.iproperty_cms.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by divyeshshani on 21/06/16.
 */
public class FacilityBookingListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    private List<BookingData> data = new ArrayList<>();

    private EventListener mEventListener;
    private View.OnClickListener mItemViewOnClickListener;
    private static final int VIEW_HEADER = 0;
    private static final int VIEW_ITEM = 1;
    int counter = 0;


    public FacilityBookingListAdapter(Context context) {

        this.context = context;
        mItemViewOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemViewClick(v);
            }
        };
    }

    private void onItemViewClick(View v) {
        if (mEventListener != null) {
            mEventListener.onItemViewClicked(v); // true --- pinned
        }
    }

    public void addAll(List<BookingData> mData) {

        data.addAll(mData);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public BookingData getItem(int position) {
        return data.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v;
        RecyclerView.ViewHolder holder;

        if(viewType==VIEW_ITEM){
            v = inflater.inflate(R.layout.item_booking_list, parent, false);
            holder = new FacilityItem(v);
        }else {
            v = inflater.inflate(R.layout.item_date_header, parent, false);
            holder = new HeaderItem(v);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){

            case VIEW_HEADER:

                HeaderItem  headerHolder = (HeaderItem) holder;
                headerHolder.tvHeaderDate.setText(data.get(position).timeSlotStart);
                break;

            case VIEW_ITEM:
                FacilityItem  itemHolder = (FacilityItem) holder;
                itemHolder.tvBookingListName.setText(data.get(position).fmName);
                itemHolder.tvBookingListSubName.setText(data.get(position).fcName);
                break;
        }

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

    @Override
    public int getItemViewType(int position) {

        if(data.get(position).fbId==""){
            return VIEW_HEADER;
        }else {
            return VIEW_ITEM;
        }

    }

    public interface EventListener {
        void onItemViewClicked(View v);

        void onItemClick(int position);
    }

    public static class FacilityItem extends RecyclerView.ViewHolder {


        @Bind(R.id.tvBookingListName)
        TextView tvBookingListName;

        @Bind(R.id.tvBookingListSubName)
        TextView tvBookingListSubName;

        public FacilityItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class HeaderItem extends RecyclerView.ViewHolder {


        @Bind(R.id.tvHeaderDate)
        TextView tvHeaderDate;

        public HeaderItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
