package com.immortal.vehicletracking.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.model.VehicleStatus;

import java.util.List;

public class VehicleStatusAdapter extends RecyclerView.Adapter<VehicleStatusAdapter.MyViewHolder> {
    private Context context;
    private List<VehicleStatus> vehicleStatusList;

    //declare interface
    private VehicleStatusAdapter.News_OnItemClicked onClick;

    //make interface like this
    public interface News_OnItemClicked {
        void news_onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView vehicle_number, vehicle_s_date, vehicle_s_time, vehicle_speed, vehicle_location;
        private CardView card_vehicle_status;
        private ImageView headerImage;
        public ImageView ac_alarm;
        private ImageView charging_alarm;
        private ImageView gps_alarm;
        private ImageView oil_alarm;
        public ImageView vehicle_icon_status;


        public MyViewHolder(View view) {
            super(view);
            card_vehicle_status = view.findViewById(R.id.card_vehicle_status);
//            headerImage = view.findViewById(R.id.headerImage);
            vehicle_number = (TextView) view.findViewById(R.id.vehicle_number);
            vehicle_s_date = view.findViewById(R.id.vehicle_s_date);
            vehicle_s_time = view.findViewById(R.id.vehicle_s_time);
            vehicle_speed = view.findViewById(R.id.vehicle_speed);
            vehicle_location = view.findViewById(R.id.vehicle_location);
            charging_alarm = (ImageView) view.findViewById(R.id.charging_alarm);
            oil_alarm = (ImageView) view.findViewById(R.id.oil_alarm);
            ac_alarm = (ImageView) view.findViewById(R.id.ac_alarm);
            gps_alarm = (ImageView) view.findViewById(R.id.gps_alarm);
            vehicle_icon_status = (ImageView) view.findViewById(R.id.vehicle_icon_status);
        }
    }

    public VehicleStatusAdapter(Context context, List<VehicleStatus> vehicleStatusList) {
        this.context = context;
        this.vehicleStatusList = vehicleStatusList;
    }

    @Override
    public VehicleStatusAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_vehicle_status, parent, false);

        return new VehicleStatusAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VehicleStatusAdapter.MyViewHolder holder, final int position) {
        VehicleStatus vehicleStatus = vehicleStatusList.get(position);
        holder.vehicle_number.setText(vehicleStatus.getItem_number());
        holder.vehicle_s_date.setText(vehicleStatus.getItem_date());
        holder.vehicle_s_time.setText(vehicleStatus.getItem_time());
        holder.vehicle_speed.setText(vehicleStatus.getItem_km() + " km/h");
        holder.vehicle_location.setText(vehicleStatus.getItem_location());
        Log.e("vehicle_speed", vehicleStatus.getItem_km() + " Km/h");

        TextView textView = holder.vehicle_speed;
        StringBuilder sb = new StringBuilder();
        sb.append(vehicleStatus.getItem_km());
        sb.append("");
        textView.setText(sb.toString());
        holder.vehicle_location.setText(vehicleStatus.getItem_location());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(vehicleStatus.getItem_km());
        sb2.append(" Km/h");
        Log.e("vehicle_speed", sb2.toString());
        holder.charging_alarm.setImageResource(vehicleStatus.getCharging_alarm_int());
        holder.oil_alarm.setImageResource(vehicleStatus.getOil_electricity_alarm_int());
        holder.ac_alarm.setImageResource(vehicleStatus.getAc_alarm_int());
        holder.gps_alarm.setImageResource(vehicleStatus.getGps_tracking_alarm_int());
        StringBuilder sb3 = new StringBuilder();
        sb3.append(vehicleStatus.getItem_img());
        sb3.append("-");
        Log.e("Image->", sb3.toString());
        Glide.with(this.context).load(vehicleStatus.getItem_img()).into(holder.vehicle_icon_status);
//

//        Glide.with(context).load(vehicleStatus.getItem_img()).into(holder.headerImage);
        holder.card_vehicle_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.news_onItemClick(position);
            }
        });
    }

    public void setOnClick(VehicleStatusAdapter.News_OnItemClicked onClick) {
        this.onClick = onClick;
    }

    @Override
    public int getItemCount() {
        return vehicleStatusList.size();

    }
}
