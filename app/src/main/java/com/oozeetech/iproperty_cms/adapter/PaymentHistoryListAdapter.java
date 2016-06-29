package com.oozeetech.iproperty_cms.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oozeetech.iproperty_cms.R;
import com.oozeetech.iproperty_cms.models.MaintanancePaymentResponse;
import com.oozeetech.iproperty_cms.models.PaymentDetailsResponse;
import com.oozeetech.iproperty_cms.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by divyeshshani on 21/06/16.
 */
public class PaymentHistoryListAdapter extends RecyclerView.Adapter<PaymentHistoryListAdapter.ImpNoHolder> {

    Context context;
    private List<PaymentDetailsResponse.Hisory> data = new ArrayList<>();

    private EventListener mEventListener;
    private View.OnClickListener mItemViewOnClickListener;


    public PaymentHistoryListAdapter(Context context) {

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

    public void addAll(List<PaymentDetailsResponse.Hisory> mData) {

        data.addAll(mData);
        notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public PaymentDetailsResponse.Hisory getItem(int position) {
        return data.get(position);
    }

    @Override
    public ImpNoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_payment_history, parent, false);
        return new ImpNoHolder(v);
    }

    @Override
    public void onBindViewHolder(final ImpNoHolder holder, final int position) {

        holder.tvPaymentHistoryAmount.setText(data.get(position).amount);
        holder.tvPaymentHistoryBankName.setText(data.get(position).bankName);
        holder.tvPaymentHistoryChequeNo.setText(data.get(position).chequeNo);
        holder.tvPaymentHistoryPayDate.setText(Utils.parseTime(data.get(position).payDate,"yyyy-MM-dd","MMM dd, yyyy"));
        holder.tvPaymentHistoryDate.setText(Utils.parseTime(data.get(position).datetime,"yyyy-MM-dd HH:mm:ss","MMM dd, yyyy"));
        holder.tvPaymentHistoryTime.setText(Utils.parseTime(data.get(position).datetime,"yyyy-MM-dd HH:mm:ss","HH:mm:ss"));
        holder.tvPaymentHistoryPeneltyAmount.setText(data.get(position).penaltyAmount);
        holder.tvPaymentHistoryTransNo.setText(data.get(position).transactionId);
        holder.tvPaymentHistoryTotalAmount.setText(data.get(position).totalAmount);
        holder.tvPaymentHistoryStatus.setText("("+data.get(position).transactionStatus+")");
        holder.tvPaymentHistoryPaymentType.setText(data.get(position).paidBy);

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


        @Bind(R.id.tvPaymentHistoryBankName)
        TextView tvPaymentHistoryBankName;
        @Bind(R.id.tvPaymentHistoryPayDate)
        TextView tvPaymentHistoryPayDate;
        @Bind(R.id.tvPaymentHistoryChequeNo)
        TextView tvPaymentHistoryChequeNo;
        @Bind(R.id.tvPaymentHistoryDate)
        TextView tvPaymentHistoryDate;
        @Bind(R.id.tvPaymentHistoryPaymentType)
        TextView tvPaymentHistoryPaymentType;
        @Bind(R.id.tvPaymentHistoryTime)
        TextView tvPaymentHistoryTime;
        @Bind(R.id.tvPaymentHistoryTransNo)
        TextView tvPaymentHistoryTransNo;
        @Bind(R.id.tvPaymentHistoryAmount)
        TextView tvPaymentHistoryAmount;
        @Bind(R.id.tvPaymentHistoryTotalAmount)
        TextView tvPaymentHistoryTotalAmount;
        @Bind(R.id.tvPaymentHistoryPeneltyAmount)
        TextView tvPaymentHistoryPeneltyAmount;
        @Bind(R.id.tvPaymentHistoryStatus)
        TextView tvPaymentHistoryStatus;

        public ImpNoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
