package com.oozeetech.iproperty_cms.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oozeetech.iproperty_cms.R;
import com.oozeetech.iproperty_cms.models.GalleryDetailsListResponse;
import com.oozeetech.iproperty_cms.models.GalleryListResponse;
import com.oozeetech.iproperty_cms.models.ViewAllFaultsResponse;
import com.oozeetech.iproperty_cms.utils.URLs;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by divyeshshani on 21/06/16.
 */
public class GalleryDetailsListAdapter extends RecyclerView.Adapter<GalleryDetailsListAdapter.ImpNoHolder> {

    Context context;
    private List<GalleryDetailsListResponse.Datum> data = new ArrayList<>();

    private EventListener mEventListener;
    private View.OnClickListener mItemViewOnClickListener;


    public interface EventListener {
        void onItemViewClicked(View v);
        void onItemClick(int position);
    }

    public static class ImpNoHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.ivGalleryDetail)
        ImageView ivGalleryDetail;

        public ImpNoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public GalleryDetailsListAdapter(Context context) {

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


    public void addAll(List<GalleryDetailsListResponse.Datum> mData) {

        data.addAll(mData);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }


    public GalleryDetailsListResponse.Datum getItem(int position) {
        return data.get(position);
    }

    @Override
    public ImpNoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_gallery_detail, parent, false);
        return new ImpNoHolder(v);
    }

    @Override
    public void onBindViewHolder(final ImpNoHolder holder, final int position) {

        Glide.with(context)
                .load(new URLs().GALLERY_ITEMS_IMG_URL+data.get(position).photoName)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.drawable.ic_default_gallery)
                .crossFade()
                .into(holder.ivGalleryDetail);

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
