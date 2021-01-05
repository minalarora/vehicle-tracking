package com.immortal.vehicletracking.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.model.DocumentModel;
import com.immortal.vehicletracking.ui.activity.DocumentUploadActivity;

import java.util.List;

public class DocumentAdapter extends Adapter<DocumentAdapter.MyViewHolder> {
    private Context context;
    private List<DocumentModel> documentList;
    /* access modifiers changed from: private */
    public Alert_OnItemClicked onClick;
    private Intent intent;

    public interface Alert_OnItemClicked {
        void alert_onItemClick(int i);
    }

    public class MyViewHolder extends ViewHolder {
        /* access modifiers changed from: private */
        public ImageView cardview_document;
        private ImageView document_Image, upload_btn;
        public TextView document_expiry_date;
        public TextView document_issue_date;
        public TextView document_name;
        public TextView document_total_days;

        public MyViewHolder(View view) {
            super(view);
            this.document_name = (TextView) view.findViewById(R.id.document_name);
            this.document_issue_date = (TextView) view.findViewById(R.id.document_issue_date);
            this.document_expiry_date = (TextView) view.findViewById(R.id.document_expiry_date);
            this.document_total_days = (TextView) view.findViewById(R.id.document_total_days);
            this.cardview_document = view.findViewById(R.id.edit_btn);
            this.upload_btn = view.findViewById(R.id.upload_btn);
            this.document_Image = (ImageView) view.findViewById(R.id.document_Image);
        }
    }

    public DocumentAdapter(Context context2, List<DocumentModel> documentList2) {
        this.context = context2;
        this.documentList = documentList2;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_documentlist_item, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, final int position) {
        DocumentModel documentModel = (DocumentModel) this.documentList.get(position);
        holder.document_name.setText(documentModel.getItem_name());
        holder.document_issue_date.setText(documentModel.getItem_issue_date());
        holder.document_expiry_date.setText(documentModel.getItem_expire_date());
        holder.document_total_days.setText(documentModel.getItem_total_days());

        holder.cardview_document.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DocumentAdapter.this.onClick.alert_onItemClick(position);
            }
        });
        holder.upload_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                intent = new Intent(context, DocumentUploadActivity.class);
                intent.putExtra("img", documentModel.getItem_img());
                intent.putExtra("id", documentModel.getVehicle_id());
                intent.putExtra("type", documentModel.getItem_type());
                context.startActivity(intent);
            }
        });
    }

    public void setOnClick(Alert_OnItemClicked onClick2) {
        this.onClick = onClick2;
    }

    public int getItemCount() {
        return this.documentList.size();
    }
}
