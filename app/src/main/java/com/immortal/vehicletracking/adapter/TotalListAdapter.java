package com.immortal.vehicletracking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.model.ItemList;

import java.util.List;

public class TotalListAdapter extends RecyclerView.Adapter<TotalListAdapter.MyViewHolder> {
	private Context context;
	private List<ItemList> totallist;

	//declare interface
	private TotalListAdapter.News_OnItemClicked onClick;

	//make interface like this
	public interface News_OnItemClicked {
		void news_onItemClick(int position);
	}

	public class MyViewHolder extends RecyclerView.ViewHolder {
		public TextView title_number, item_total_add, item_total_km;
		private CardView cardview_total;
		private ImageView headerImage;

		public MyViewHolder(View view) {
			super(view);
			title_number = (TextView) view.findViewById(R.id.title_number);
			cardview_total = view.findViewById(R.id.cardview_total);
			headerImage = view.findViewById(R.id.headerImage);
			item_total_add = view.findViewById(R.id.item_total_add);
			item_total_km = view.findViewById(R.id.item_total_km);
		}
	}

	public TotalListAdapter(Context context, List<ItemList> totallist) {
		this.context = context;
		this.totallist = totallist;
	}

	@Override
	public TotalListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.cardview_totallist_item, parent, false);

		return new TotalListAdapter.MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(final TotalListAdapter.MyViewHolder holder, final int position) {
		ItemList itemList = totallist.get(position);
		holder.title_number.setText(itemList.getItem_name());
		holder.item_total_add.setText(itemList.getItem_location());
		holder.item_total_km.setText(itemList.getItem_km());
		Glide.with(context).load(itemList.getItem_img()).into(holder.headerImage);
		holder.cardview_total.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onClick.news_onItemClick(position);
			}
		});
	}

	public void setOnClick(TotalListAdapter.News_OnItemClicked onClick) {
		this.onClick = onClick;
	}

	@Override
	public int getItemCount() {
		return totallist.size();

	}
}
