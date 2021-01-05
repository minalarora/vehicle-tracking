package com.immortal.vehicletracking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.model.TravelSummary;

import java.util.List;

public class TravelSummaryAdapter extends RecyclerView.Adapter<TravelSummaryAdapter.MyViewHolder> {
    private Context context;
    private List<TravelSummary> travelSummaryList;

    //declare interface
    private TravelSummaryAdapter.News_OnItemClicked onClick;

    //make interface like this
    public interface News_OnItemClicked {
        void news_onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView travel_v_name, travel_running, travel_idle, travel_stop, travel_distance,
                travel_avg_speed, travel_max_speed, travel_alert, travel_active,travel_v_date;
        private CardView cardview_travelsummary;

        public MyViewHolder(View view) {
            super(view);
            cardview_travelsummary = view.findViewById(R.id.cardview_travelsummary);
            travel_v_name = (TextView) view.findViewById(R.id.travel_v_name);
            travel_running = view.findViewById(R.id.travel_running);
            travel_idle = view.findViewById(R.id.travel_idle);
            travel_stop = view.findViewById(R.id.travel_stop);
            travel_distance = view.findViewById(R.id.travel_km);
            travel_avg_speed = view.findViewById(R.id.travel_avg_speed);
            travel_max_speed = view.findViewById(R.id.travel_max_speed);
            travel_active = view.findViewById(R.id.travel_active);
            travel_alert = view.findViewById(R.id.travel_alert);
            travel_v_date=view.findViewById(R.id.travel_v_date);
        }
    }

    public TravelSummaryAdapter(Context context, List<TravelSummary> travelSummaryList) {
        this.context = context;
        this.travelSummaryList = travelSummaryList;
    }

    @Override
    public TravelSummaryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_travel_summary, parent, false);

        return new TravelSummaryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TravelSummaryAdapter.MyViewHolder holder, final int position) {
        TravelSummary travelSummary = travelSummaryList.get(position);
        holder.travel_v_name.setText(travelSummary.getItem_number());
        holder.travel_running.setText(travelSummary.getItem_running());
        holder.travel_idle.setText(travelSummary.getItem_idle());
        holder.travel_stop.setText(travelSummary.getItem_stop());
        holder.travel_distance.setText(travelSummary.getItem_distance());
        holder.travel_avg_speed.setText(travelSummary.getItem_avg_speed());
        holder.travel_max_speed.setText(travelSummary.getItem_max_speed());
        holder.travel_active.setText(travelSummary.getItem_travel_active());
        holder.travel_alert.setText(travelSummary.getItem_travel_alert());
        holder.travel_v_date.setText(travelSummary.getItem_travel_date());
        holder.cardview_travelsummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.news_onItemClick(position);
            }
        });
    }

    public void setOnClick(TravelSummaryAdapter.News_OnItemClicked onClick) {
        this.onClick = onClick;
    }

    @Override
    public int getItemCount() {
        return travelSummaryList.size();

    }
}
