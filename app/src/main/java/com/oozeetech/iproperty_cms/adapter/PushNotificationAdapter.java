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
public class PushNotificationAdapter extends RecyclerView.Adapter<PushNotificationAdapter.ImpNoHolder> {

    Context context;
    private List<PushNotificationResponse.Datum> data = new ArrayList<>();

    private EventListener mEventListener;
    private View.OnClickListener mItemViewOnClickListener;
    private int VIEW_UNREAD = 0;
    private int VIEW_READED = 1;


    public PushNotificationAdapter(Context context) {

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

    public void addAll(List<PushNotificationResponse.Datum> mData) {

        data.addAll(mData);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public PushNotificationResponse.Datum getItem(int position) {
        return data.get(position);
    }

    @Override
    public ImpNoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v;
        if(viewType == VIEW_UNREAD){
            v = inflater.inflate(R.layout.item_unread_push_notification, parent, false);
        }else {
            v = inflater.inflate(R.layout.item_push_notification, parent, false);
        }

        return new ImpNoHolder(v);
    }

    @Override
    public void onBindViewHolder(final ImpNoHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mEventListener!=null){
                    mEventListener.onItemClick(position);
                }
            }
        });

        holder.tvNotiTitle.setText(data.get(position).messageTitle);
        holder.tvNotiDateTime.setText(Utils.parseTime(data.get(position).datetime,"yyyy-MM-dd HH:mm:ss","MMM dd, yyyy HH:mm:ss"));

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
        if(data.get(position).read.equals("1")){
            return VIEW_UNREAD;
        }else {
            return VIEW_READED;
        }
    }

    public interface EventListener {
        void onItemViewClicked(View v);

        void onItemClick(int position);
    }

    public static class ImpNoHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.tvNotiTitle)
        TextView tvNotiTitle;

        @Bind(R.id.tvNotiDateTime)
        TextView tvNotiDateTime;


        public ImpNoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
