package com.oozeetech.iproperty_cms.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oozeetech.iproperty_cms.R;
import com.oozeetech.iproperty_cms.models.ManagementResponse;
import com.oozeetech.iproperty_cms.utils.Constants;
import com.oozeetech.iproperty_cms.utils.URLs;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by divyeshshani on 21/06/16.
 */
public class ManagementAdapter extends RecyclerView.Adapter<ManagementAdapter.ImpNoHolder> {

    Context context;
    private List<ManagementResponse.Datum> data = new ArrayList<>();

    private EventListener mEventListener;
    private View.OnClickListener mItemViewOnClickListener;


    public ManagementAdapter(Context context) {

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

    public void addAll(List<ManagementResponse.Datum> mData) {

        data.addAll(mData);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public ManagementResponse.Datum getItem(int position) {
        return data.get(position);
    }

    @Override
    public ImpNoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_management, parent, false);
        return new ImpNoHolder(v);
    }

    @Override
    public void onBindViewHolder(final ImpNoHolder holder, final int position) {

        holder.tvManagementContact.setText(data.get(position).mobileNumber);
        holder.tvManagementPost.setText(data.get(position).designation);
        holder.tvManagementName.setText(data.get(position).name);
        Glide.with(context)
                .load(new URLs().MANAGEMENT_IMG_URL + data.get(position).photo)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.drawable.ic_default_profile)
                .crossFade()
                .into(holder.ivManagementThumb);

        holder.llCall.setOnClickListener(new View.OnClickListener() {
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
        void onItemViewClicked(View v);

        void onItemClick(int position);
    }

    public static class ImpNoHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.tvManagementContact)
        TextView tvManagementContact;
        @Bind(R.id.tvManagementName)
        TextView tvManagementName;
        @Bind(R.id.tvManagementPost)
        TextView tvManagementPost;
        @Bind(R.id.ivManagementThumb)
        ImageView ivManagementThumb;

        @Bind(R.id.llCall)
        LinearLayout llCall;

        public ImpNoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
