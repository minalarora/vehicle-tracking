package com.immortal.vehicletracking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.model.StoppageDetail;

import java.util.List;

public class StoppageDetailsAdapter extends RecyclerView.Adapter<StoppageDetailsAdapter.MyViewHolder> {
    private Context context;
    private List<StoppageDetail> stoppageSummaryList;

    //declare interface
    private StoppageDetailsAdapter.News_OnItemClicked onClick;

    //make interface like this
    public interface News_OnItemClicked {
        void news_onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_stop_a_date, tv_stop_a_time, tv_stop_d_date, tv_stop_d_time, tv_stop_alert, tv_stop_duration,
                tv_stop_distance, tv_stop_parking_time, tv_stop_address;
        private CardView card_stoppage_details;
        private ImageView headerImage;

        public MyViewHolder(View view) {
            super(view);
            tv_stop_address = view.findViewById(R.id.tv_stop_address);
            card_stoppage_details = view.findViewById(R.id.card_stoppage_details);
            headerImage = view.findViewById(R.id.headerImage);
            tv_stop_a_date = view.findViewById(R.id.tv_stop_a_date);
            tv_stop_a_time = view.findViewById(R.id.tv_stop_a_time);
            headerImage = view.findViewById(R.id.headerImage);
            tv_stop_d_date = view.findViewById(R.id.tv_stop_d_date);
            tv_stop_d_time = view.findViewById(R.id.tv_stop_d_time);
            tv_stop_alert = view.findViewById(R.id.tv_stop_alert);
            tv_stop_duration = view.findViewById(R.id.tv_stop_duration);
            tv_stop_distance = view.findViewById(R.id.tv_stop_distance);
            tv_stop_parking_time = view.findViewById(R.id.tv_stop_parking_time);
            tv_stop_address = view.findViewById(R.id.tv_stop_address);

        }
    }

    public StoppageDetailsAdapter(Context context, List<StoppageDetail> stoppageSummaryList) {
        this.context = context;
        this.stoppageSummaryList = stoppageSummaryList;
    }

    @Override
    public StoppageDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_stoppage_summery_details, parent, false);

        return new StoppageDetailsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StoppageDetailsAdapter.MyViewHolder holder, final int position) {
        StoppageDetail stoppageDetails = stoppageSummaryList.get(position);
        holder.tv_stop_address.setText(stoppageDetails.getItem_address());
        holder.tv_stop_parking_time.setText(stoppageDetails.getItem_parking());
        holder.tv_stop_distance.setText(stoppageDetails.getItem_distance());
        holder.tv_stop_duration.setText(stoppageDetails.getItem_duration());
        holder.tv_stop_alert.setText(stoppageDetails.getItem_alert());
        holder.tv_stop_d_time.setText(stoppageDetails.getItem_departure_time());
        holder.tv_stop_d_date.setText(stoppageDetails.getItem_departure_date());
        holder.tv_stop_a_time.setText(stoppageDetails.getItem_arrival_time());
        holder.tv_stop_a_date.setText(stoppageDetails.getItem_arrival_date());

//        Glide.with(context).load(travelSummary.getItem_img()).into(holder.headerImage);
        holder.card_stoppage_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.news_onItemClick(position);
            }
        });
    }

    public void setOnClick(StoppageDetailsAdapter.News_OnItemClicked onClick) {
        this.onClick = onClick;
    }

    @Override
    public int getItemCount() {
        return stoppageSummaryList.size();

    }
}
