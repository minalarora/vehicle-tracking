package com.immortal.vehicletracking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.model.Alert;
import com.immortal.vehicletracking.model.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    private Context context;
    private List<Notification> notificationList;

    //declare interface
    private NotificationAdapter.Alert_OnItemClicked onClick;

    //make interface like this
    public interface Alert_OnItemClicked {
        void alert_onItemClick(int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_alert_vehicle_name, txt_alert_name, txt_alert_address, txt_alert_date, txt_alert_time;
        private CardView card_alert;
//		private ImageView headerImage;

        public MyViewHolder(View view) {
            super(view);
            txt_alert_vehicle_name = (TextView) view.findViewById(R.id.txt_alert_vehicle_name);
            txt_alert_address = view.findViewById(R.id.txt_alert_address);
            txt_alert_name = view.findViewById(R.id.txt_alert_name);
            txt_alert_date = view.findViewById(R.id.txt_alert_date);
            txt_alert_time = view.findViewById(R.id.txt_alert_time);
            card_alert = view.findViewById(R.id.card_alert);
        }
    }

    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_notification, parent, false);

        return new NotificationAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NotificationAdapter.MyViewHolder holder, final int position) {
        Notification notification = notificationList.get(position);
        holder.txt_alert_vehicle_name.setText(notification.getNotification_number());
        holder.txt_alert_address.setText(notification.getNotification_address());
        holder.txt_alert_name.setText(notification.getNotification_name());
        holder.txt_alert_date.setText(notification.getNotification_date());
        holder.txt_alert_time.setText(notification.getNotification_time());
////		Glide.with(context).load(itemList.getItem_img()).into(holder.headerImage);
        holder.card_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.alert_onItemClick(position);
            }
        });
    }

    public void setOnClick(NotificationAdapter.Alert_OnItemClicked onClick) {
        this.onClick = onClick;
    }

    @Override
    public int getItemCount() {
        return notificationList.size();

    }
}
