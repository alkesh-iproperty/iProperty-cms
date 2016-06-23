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
import com.oozeetech.iproperty_cms.R;
import com.oozeetech.iproperty_cms.models.ImpNos;
import com.oozeetech.iproperty_cms.models.ImportantNumberResponse;
import com.oozeetech.iproperty_cms.utils.Debug;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by divyeshshani on 21/06/16.
 */
public class ImpNoAdapter extends RecyclerView.Adapter<ImpNoAdapter.ImpNoHolder> {

    Context context;
    private List<ImportantNumberResponse.Datum> data = new ArrayList<>();

    private EventListener mEventListener;
    private View.OnClickListener mItemViewOnClickListener;


    public interface EventListener {
        void onItemViewClicked(View v);
        void onItemClick(int position);
    }

    public static class ImpNoHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.tvImpNoName)
        TextView tvImpNoName;
        @Bind(R.id.tvImpNo)
        TextView tvImpNo;
        @Bind(R.id.llCall)
        LinearLayout llCall;

        public ImpNoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public ImpNoAdapter(Context context) {

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


    public void addAll(List<ImportantNumberResponse.Datum> mData) {

        data.addAll(mData);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }


    public ImportantNumberResponse.Datum getItem(int position) {
        return data.get(position);
    }

    @Override
    public ImpNoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_important_number, parent, false);
        return new ImpNoHolder(v);
    }

    @Override
    public void onBindViewHolder(final ImpNoHolder holder, final int position) {

        holder.tvImpNo.setText(data.get(position).number);
        holder.tvImpNoName.setText(data.get(position).name);
        holder.llCall.setOnClickListener(new View.OnClickListener() {
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
