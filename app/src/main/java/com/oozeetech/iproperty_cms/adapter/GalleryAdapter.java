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
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ImpNoHolder> {

    Context context;
    private List<GalleryListResponse.Datum> data = new ArrayList<>();

    private EventListener mEventListener;
    private View.OnClickListener mItemViewOnClickListener;

    public GalleryAdapter(Context context) {

        this.context = context;
    }

    public void addAll(List<GalleryListResponse.Datum> mData) {

        data.addAll(mData);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public GalleryListResponse.Datum getItem(int position) {
        return data.get(position);
    }

    @Override
    public ImpNoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_gallery, parent, false);
        return new ImpNoHolder(v);
    }

    @Override
    public void onBindViewHolder(final ImpNoHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEventListener != null) {
                    mEventListener.onItemClick(position);
                }
            }
        });
        holder.tvGalleryTitle.setText(data.get(position).name);
        Glide.with(context)
                .load(new URLs().GELLERY_IMG_URL + data.get(position).photo)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .crossFade()
                .into(holder.ivGallery);
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

    public static class ImpNoHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.ivGallery)
        ImageView ivGallery;
        @Bind(R.id.tvGalleryTitle)
        TextView tvGalleryTitle;

        public ImpNoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
