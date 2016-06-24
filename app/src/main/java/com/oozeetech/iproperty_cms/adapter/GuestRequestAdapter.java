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
import com.oozeetech.iproperty_cms.models.NoticesResponse;
import com.oozeetech.iproperty_cms.utils.URLs;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by divyeshshani on 21/06/16.
 */
public class GuestRequestAdapter extends RecyclerView.Adapter<GuestRequestAdapter.GuestRequestHolder> {

    Context context;
    private List<NoticesResponse.Datum> data = new ArrayList<>();

    private EventListener mEventListener;
    private View.OnClickListener mItemViewOnClickListener;


    public interface EventListener {
        void onItemViewClicked(View v);
        void onItemClick(int position);
    }

    public static class GuestRequestHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.tvNoticeTitle)
        TextView tvNoticeTitle;
        @Bind(R.id.ivNoticeItem)
        ImageView ivNoticeItem;
        @Bind(R.id.ivNoticeError)
        ImageView ivNoticeError;

        public GuestRequestHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public GuestRequestAdapter(Context context) {

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


    public void addAll(List<NoticesResponse.Datum> mData) {

        data.addAll(mData);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }


    public NoticesResponse.Datum getItem(int position) {
        return data.get(position);
    }

    @Override
    public GuestRequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_notices, parent, false);
        return new GuestRequestHolder(v);
    }

    @Override
    public void onBindViewHolder(final GuestRequestHolder holder, final int position) {

        holder.tvNoticeTitle.setText(data.get(position).name);

        Glide.with(context)
                .load(new URLs().NOTICE_IMG_URL+data.get(position).fileName)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.drawable.ic_default_notice)
                .crossFade()
                .into(holder.ivNoticeItem);

        if(data.get(position).read.equals("0")){

            holder.ivNoticeError.setVisibility(View.VISIBLE);

        }else {

            holder.ivNoticeError.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEventListener!=null){

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

}
