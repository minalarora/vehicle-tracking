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
import com.immortal.vehicletracking.model.DistanceSummary;

import java.util.List;

public class DistanceSummaryAdapter extends RecyclerView.Adapter<DistanceSummaryAdapter.MyViewHolder> {
    private Context context;
    private List<DistanceSummary> totallist;

    //declare interface
    private DistanceSummaryAdapter.News_OnItemClicked onClick;

    //make interface like this
    public interface News_OnItemClicked {
        void news_onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_vehicle_name, txt_vehicle_distance, txt_vehicle_duration;
        private CardView card_distance_summary;
//		private ImageView headerImage;

        public MyViewHolder(View view) {
            super(view);
            txt_vehicle_name = (TextView) view.findViewById(R.id.txt_vehicle_name);
            card_distance_summary = view.findViewById(R.id.card_distance_summary);
            txt_vehicle_distance = view.findViewById(R.id.txt_vehicle_distance);
            txt_vehicle_duration = view.findViewById(R.id.txt_vehicle_duration);
//			item_total_km = view.findViewById(R.id.item_total_km);
        }
    }

    public DistanceSummaryAdapter(Context context, List<DistanceSummary> totallist) {
        this.context = context;
        this.totallist = totallist;
    }

    @Override
    public DistanceSummaryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_distance_summary, parent, false);

        return new DistanceSummaryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DistanceSummaryAdapter.MyViewHolder holder, final int position) {
        DistanceSummary distanceSummary = totallist.get(position);
        holder.txt_vehicle_name.setText(distanceSummary.getItem_number());
        holder.txt_vehicle_distance.setText(distanceSummary.getItem_km());
        holder.txt_vehicle_duration.setText(distanceSummary.getItem_duration());
//		Glide.with(context).load(itemList.getItem_img()).into(holder.headerImage);
        holder.card_distance_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.news_onItemClick(position);
            }
        });
    }

    public void setOnClick(DistanceSummaryAdapter.News_OnItemClicked onClick) {
        this.onClick = onClick;
    }

    @Override
    public int getItemCount() {
        return totallist.size();

    }
}
