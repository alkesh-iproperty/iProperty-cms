package com.oozeetech.iproperty_cms.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oozeetech.iproperty_cms.R;
import com.oozeetech.iproperty_cms.models.EventsResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import ca.barrenechea.widget.recyclerview.decoration.StickyHeaderAdapter;

/**
 * Created by divyeshshani on 23/06/16.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> implements
        StickyHeaderAdapter<EventsAdapter.HeaderHolder> {

    public EventListner eventListner;
    private ArrayList<EventsResponse.Datum> data;
    private LayoutInflater mInflater;
    private Context context;

    public EventsAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        data = new ArrayList<>();
    }

    public void addAll(ArrayList<EventsResponse.Datum> data) {

        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        this.data.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view = mInflater.inflate(R.layout.item_events, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        SimpleDateFormat myFormat = new SimpleDateFormat("MMM dd, yyyy");
        SimpleDateFormat myTimeFormate = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        viewHolder.tvEventListTitle.setText(data.get(i).eventName);
        viewHolder.tvEventListDesc.setText(data.get(i).eventDesc);
        try {
            viewHolder.tvEventListDate.setText("" + myFormat.format(originalFormat.parse(data.get(i).startTime)));
            viewHolder.tvEventListTime.setText("" + myTimeFormate.format(originalFormat.parse(data.get(i).startTime)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.llMoreEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eventListner != null) {
                    eventListner.onEventItemClick(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getHeaderId(int position) {
        return position;
    }

    public EventsResponse.Datum getEventData(int position) {
        return data.get(position);
    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        final View view = mInflater.inflate(R.layout.header_event, parent, false);
        return new HeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderHolder viewholder, int position) {
        viewholder.header.setText(data.get(position).startTime);
    }

    public void setEventListner(EventListner listner) {
        this.eventListner = listner;
    }

    public interface EventListner {

        void onEventItemClick(int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tvEventListTime)
        TextView tvEventListTime;
        @Bind(R.id.tvEventListDate)
        TextView tvEventListDate;
        @Bind(R.id.tvEventListTitle)
        TextView tvEventListTitle;
        @Bind(R.id.tvEventListDesc)
        TextView tvEventListDesc;
        @Bind(R.id.llMoreEvent)
        LinearLayout llMoreEvent;


        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    static class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView header;

        public HeaderHolder(View itemView) {
            super(itemView);

            header = (TextView) itemView;
        }
    }
}
