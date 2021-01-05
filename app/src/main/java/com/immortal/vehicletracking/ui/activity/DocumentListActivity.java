package com.immortal.vehicletracking.ui.activity;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty;
import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.adapter.DocumentAdapter;
import com.immortal.vehicletracking.adapter.DocumentAdapter.Alert_OnItemClicked;
import com.immortal.vehicletracking.helperClass.ApiConstants;
import com.immortal.vehicletracking.model.DocumentModel;
import com.immortal.vehicletracking.network.MyApplication;
import com.immortal.vehicletracking.utils.MyProgressDialog;
import com.immortal.vehicletracking.utils.UserPreferences;
import com.immortal.vehicletracking.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/* renamed from: com.immortal.vehicletracking.ui.activity.DocumentListActivity */
public class DocumentListActivity extends AppCompatActivity implements Alert_OnItemClicked {
    private DocumentAdapter adapter;
    /* access modifiers changed from: private */
    public Context context;
    /* access modifiers changed from: private */
    public Dialog dialog;
    private List<DocumentModel> documentList;
    private DocumentModel documentModel;
    private Intent intent;
    private GridLayoutManager mLayoutManager;
    private RecyclerView recyclerView;
//    private Toolbar toolbar;
    UserPreferences userPreferences = UserPreferences.getUserPreferences();
    /* access modifiers changed from: private */
    public String vehicle_id;
    private String vehicle_name;
    String[] doc_type = {"rc", "insurance", "permit", "pollution", "roadTax", "goodstax"};

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_list);
        this.context = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initId();
        getdatafromIntent();
        setupToolBar();
        setRecyclerViewCode();
    }

    private void getdatafromIntent() {
        if (getIntent() != null) {
            this.vehicle_id = getIntent().getStringExtra("id");
            this.vehicle_name = getIntent().getStringExtra(ConditionalUserProperty.NAME);
        }
    }

    private void initId() {
        this.recyclerView = (RecyclerView) findViewById(R.id.document_RecyclerView);
//        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void setupToolBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(vehicle_name);
    }

    private void setRecyclerViewCode() {
        this.documentList = new ArrayList();
        this.adapter = new DocumentAdapter(this.context, this.documentList);
        this.mLayoutManager = new GridLayoutManager(this.context, 1);
        this.recyclerView.setLayoutManager(this.mLayoutManager);
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setAdapter(this.adapter);
        this.adapter.setOnClick(this);
    }

    public void openEditDocument(final String key, String date) {
        this.dialog = new Dialog(this);
        this.dialog.setContentView(R.layout.dailog_edit_document);
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_gray)));
        TextView cancel = (TextView) this.dialog.findViewById(R.id.cancel_port);
        TextView save = (TextView) this.dialog.findViewById(R.id.save_port);
        final TextView endDate = (TextView) this.dialog.findViewById(R.id.endDate);
        endDate.setText(date);
        cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DocumentListActivity.this.dialog.cancel();
            }
        });
        endDate.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(DocumentListActivity.this.context, new OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);
                        endDate.setText(Utils.getFormattedDate(selectedDate));
                    }
                }, mYear, mMonth, mDay);
                dialog.show();
            }
        });
        save.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DocumentListActivity documentListActivity = DocumentListActivity.this;
                documentListActivity.updatedocumentDateCall(documentListActivity.vehicle_id, key, endDate.getText().toString());
            }
        });
        this.dialog.show();
    }

    /* access modifiers changed from: private */
    public void updatedocumentDateCall(String vehicle_id2, String key, String date) {
        String str = "";
        MyProgressDialog.showPDialog(this.context);
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("vehicle", vehicle_id2);
            StringBuilder sb = new StringBuilder();
            sb.append(key);
            sb.append(str);
            postparams.put("key", sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append(date);
            sb2.append(str);
            postparams.put("date", sb2.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append(postparams);
        sb3.append(str);
        Log.e("postparams", sb3.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(1, ApiConstants.DOCUMENT_UPDATE_LIST, postparams, new Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                MyProgressDialog.hidePDialog();
                StringBuilder sb = new StringBuilder();
                sb.append(response);
                sb.append("");
                Log.e("DocumentList", sb.toString());
                try {
                    if (response.getString(NotificationCompat.CATEGORY_STATUS).equals("true")) {
                        DocumentListActivity.this.dialog.cancel();
                        return;
                    }
                    Toast.makeText(DocumentListActivity.this.context, response.getString("message"), Toast.LENGTH_SHORT).show();
                    DocumentListActivity.this.dialog.cancel();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                StringBuilder sb = new StringBuilder();
                sb.append("Error: ");
                sb.append(error.getMessage());
                Log.e("TotalListScreen", sb.toString());
                MyProgressDialog.hidePDialog();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 1, 1.0f));
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void documentListApiCall(String device_id) {
        String str = "";
        MyProgressDialog.showPDialog(this.context);
        JSONObject postparams = new JSONObject();
        String str2 = "vehicle";
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(device_id);
            sb.append(str);
            postparams.put(str2, sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(postparams);
        sb2.append(str);
        Log.e("postparams", sb2.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(1, ApiConstants.DOCUMENT_LIST, postparams, new Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                MyProgressDialog.hidePDialog();
                StringBuilder sb = new StringBuilder();
                sb.append(response);
                sb.append("");
                Log.e("DocumentList", sb.toString());
                try {
                    if (response.getString(NotificationCompat.CATEGORY_STATUS).equals("true")) {
                        DocumentListActivity.this.setdatatoTotalList(response.getJSONObject("data"));
                        return;
                    }
                    Toast.makeText(DocumentListActivity.this.context, response.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                StringBuilder sb = new StringBuilder();
                sb.append("Error: ");
                sb.append(error.getMessage());
                Log.e("TotalListScreen", sb.toString());
                MyProgressDialog.hidePDialog();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 1, 1.0f));
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    /* access modifiers changed from: private */
    public void setdatatoTotalList(JSONObject list) {
        JSONObject jSONObject = list;
        String str = "vehicle_goods_tax_expiry";
        String str2 = "vehicle_road_tax_expiry";
        String str3 = "vehicle_pollution_expiry_date";
        String str4 = "vehicle_pollution_date";
        String str5 = "vehicle_insurance_expiry_date";
        String str6 = "vehicle_insurance_date";
        String str7 = "vehicle_permit_expiry";
        String str8 = "vehicle_rc_expiration";
        String str9 = "vehicle_rc";
        String str10 = "vehicle_no";
        this.documentList.clear();
        try {
            String str11 = "";
            String vehicle_no = jSONObject.isNull(str10) ? str11 : jSONObject.getString(str10);
            if (jSONObject.isNull(str9)) {
                String str12 = str11;
            } else {
                String string = jSONObject.getString(str9);
            }
            String str13 = "NA";
            String vehicle_rc_expiration = jSONObject.isNull(str8) ? str13 : jSONObject.getString(str8);
            String vehicle_permit_expiry = jSONObject.isNull(str7) ? str13 : jSONObject.getString(str7);
            String vehicle_insurance_date = jSONObject.isNull(str6) ? str13 : jSONObject.getString(str6);
            String vehicle_insurance_expiry_date = jSONObject.isNull(str5) ? str13 : jSONObject.getString(str5);
            String vehicle_pollution_date = jSONObject.isNull(str4) ? str13 : jSONObject.getString(str4);
            String vehicle_pollution_expiry_date = jSONObject.isNull(str3) ? str13 : jSONObject.getString(str3);
            String vehicle_road_tax_expiry = jSONObject.isNull(str2) ? str13 : jSONObject.getString(str2);

            String vehicle_rc_doc = jSONObject.isNull("vehicle_rc_doc") ? "" : jSONObject.getString("vehicle_rc_doc");
            String vehicle_insurance_doc = jSONObject.isNull("vehicle_insurance_doc") ? "" : jSONObject.getString("vehicle_insurance_doc");
            String vehicle_permit_doc = jSONObject.isNull("vehicle_permit_doc") ? "" : jSONObject.getString("vehicle_permit_doc");
            String vehicle_pollution_doc = jSONObject.isNull("vehicle_pollution_doc") ? "" : jSONObject.getString("vehicle_pollution_doc");
            String vehicle_road_tax_doc = jSONObject.isNull("vehicle_road_tax_doc") ? "" : jSONObject.getString("vehicle_road_tax_doc");
            String vehicle_goods_tax_doc = jSONObject.isNull("vehicle_goods_tax_doc") ? "" : jSONObject.getString("vehicle_goods_tax_doc");

            if (!jSONObject.isNull(str)) {
                str13 = jSONObject.getString(str);
            }
            String vehicle_goods_tax_expiry = str13;
            StringBuilder sb = new StringBuilder();
            sb.append(vehicle_no);
            sb.append(str11);
            Log.e(str10, sb.toString());
            DocumentModel documentModel2 = new DocumentModel(vehicle_rc_expiration, "", vehicle_rc_doc, vehicle_rc_expiration, "10 days", "RC", doc_type[0], vehicle_id);
            this.documentModel = documentModel2;
            this.documentList.add(this.documentModel);
            DocumentModel documentModel3 = new DocumentModel(vehicle_insurance_expiry_date, vehicle_insurance_date, vehicle_insurance_doc, vehicle_insurance_expiry_date, "10 days", "INSURANCE", doc_type[1], vehicle_id);
            this.documentModel = documentModel3;
            this.documentList.add(this.documentModel);
            DocumentModel documentModel4 = new DocumentModel(vehicle_permit_expiry, "", vehicle_permit_doc, vehicle_permit_expiry, "10 days", "PERMIT", doc_type[2], vehicle_id);
            this.documentModel = documentModel4;
            this.documentList.add(this.documentModel);
            DocumentModel documentModel5 = new DocumentModel(vehicle_pollution_expiry_date, vehicle_pollution_date, vehicle_pollution_doc, vehicle_pollution_expiry_date, "10 days", "POLLUTION", doc_type[3], vehicle_id);
            this.documentModel = documentModel5;
            this.documentList.add(this.documentModel);
            DocumentModel documentModel6 = new DocumentModel(vehicle_road_tax_expiry, "", vehicle_road_tax_doc, vehicle_road_tax_expiry, "10 days", "ROAD TAX", doc_type[4], vehicle_id);
            this.documentModel = documentModel6;
            this.documentList.add(this.documentModel);
            DocumentModel documentModel7 = new DocumentModel(vehicle_goods_tax_expiry, "", vehicle_goods_tax_doc, vehicle_goods_tax_expiry, "10 days", "GOODS TAX", doc_type[5], vehicle_id);
            this.documentModel = documentModel7;
            this.documentList.add(this.documentModel);
            this.adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        documentListApiCall(vehicle_id);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    public void alert_onItemClick(int position) {
        DocumentModel documentModel2 = (DocumentModel) this.documentList.get(position);
        openEditDocument(documentModel2.getItem_id(), documentModel2.getItem_expire_date());
    }
}
