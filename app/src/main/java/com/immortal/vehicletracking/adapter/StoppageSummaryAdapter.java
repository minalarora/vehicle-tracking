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
import com.immortal.vehicletracking.model.StoppageSummary;
import com.immortal.vehicletracking.model.TravelSummary;

import java.util.List;

public class StoppageSummaryAdapter extends RecyclerView.Adapter<StoppageSummaryAdapter.MyViewHolder> {
    private Context context;
    private List<StoppageSummary> stoppageSummaryList;

    //declare interface
    private StoppageSummaryAdapter.News_OnItemClicked onClick;

    //make interface like this
    public interface News_OnItemClicked {
        void news_onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView stop_v_name, stop_running, stop_idle, stop_stop, stop_distance, stop_avg_speed, stop_max_speed, stop_total;
        private CardView card_stoppage_summary;

        public MyViewHolder(View view) {
            super(view);
            card_stoppage_summary = view.findViewById(R.id.card_stoppage_summary);
            stop_v_name = (TextView) view.findViewById(R.id.stop_v_name);
            stop_running = view.findViewById(R.id.stop_running);
            stop_idle = view.findViewById(R.id.stop_idle);
            stop_stop = view.findViewById(R.id.stop_stop);
            stop_distance = view.findViewById(R.id.stop_distance);
            stop_avg_speed = view.findViewById(R.id.stop_avg_speed);
            stop_max_speed = view.findViewById(R.id.stop_max_speed);
            stop_total = view.findViewById(R.id.stop_total);

        }
    }

    public StoppageSummaryAdapter(Context context, List<StoppageSummary> stoppageSummaryList) {
        this.context = context;
        this.stoppageSummaryList = stoppageSummaryList;
    }

    @Override
    public StoppageSummaryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_stoppage_summery, parent, false);

        return new StoppageSummaryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StoppageSummaryAdapter.MyViewHolder holder, final int position) {
        StoppageSummary stoppageSummary = stoppageSummaryList.get(position);

        holder.stop_v_name.setText(stoppageSummary.getItem_number());
        holder.stop_running.setText(stoppageSummary.getItem_running());
        holder.stop_idle.setText(stoppageSummary.getItem_idle());
        holder.stop_stop.setText(stoppageSummary.getItem_stop());
        holder.stop_distance.setText(stoppageSummary.getItem_distance());
        holder.stop_avg_speed.setText(stoppageSummary.getItem_avg_speed());
        holder.stop_max_speed.setText(stoppageSummary.getItem_max_speed());
        holder.stop_total.setText(stoppageSummary.getItem_total_stop());

        holder.card_stoppage_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.news_onItemClick(position);
            }
        });
    }

    public void setOnClick(StoppageSummaryAdapter.News_OnItemClicked onClick) {
        this.onClick = onClick;
    }

    @Override
    public int getItemCount() {
        return stoppageSummaryList.size();

    }
}
