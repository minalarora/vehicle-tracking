package com.immortal.vehicletracking.ui.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.immortal.vehicletracking.R;
import com.immortal.vehicletracking.network.MyApplication;
import com.immortal.vehicletracking.utils.Constant;
import com.immortal.vehicletracking.utils.MyProgressDialog;
import com.immortal.vehicletracking.utils.UserPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.Manifest.permission.CAMERA;
import static com.immortal.vehicletracking.helperClass.ApiConstants.DOCUMENT_UPDATE;
import static com.immortal.vehicletracking.utils.Constant.TIMEOUT_VOLLY;

public class DocumentUploadActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_GALLERY = 33;
    private static final int PERMISSION_REQUEST_CODE = 200;
    int PICK_CAMERA = 100;
    private String key;
    private Bitmap f_bitmap, b_bitmap;
    private ImageView panImage;
    private TextView panProofSubmit;
    private TextView upload_pan_proof;
    private String typeKey = "";
    UserPreferences userPreferences = UserPreferences.getUserPreferences();
    private Context context;
    private Toolbar toolbar;
    private String num, img;
    private String profileImgUrl = "https://d1la9pe4pbdpwl.cloudfront.net/uploads/profile/docs/";
    private String doc_type, vehicle_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_upload);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Upload Documents");
        initView();
        getdatafromIntent();
        setdata();
    }

    private void initView() {
        panImage = findViewById(R.id.pan_photo);
        panProofSubmit = findViewById(R.id.submit_panproof);
        upload_pan_proof = findViewById(R.id.upload_pan_proof);
        toolbar = findViewById(R.id.toolbar);
        upload_pan_proof.setOnClickListener(this);
        panProofSubmit.setOnClickListener(this);
    }

    private void setdata() {

        if (!img.equals("")) {
            Log.e("image", img);
            Glide.with(DocumentUploadActivity.this).load(img).into(panImage);
        }
    }

    private void getdatafromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            img = intent.getStringExtra("img");
            vehicle_id = intent.getStringExtra("id");
            doc_type = intent.getStringExtra("type");

        }
    }

    private void selectAddressImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                try {
                    PackageManager pm = getPackageManager();
                    int hasPerm = pm.checkPermission(CAMERA, getPackageName());
                    if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                        final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Select Option");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                if (options[item].equals("Take Photo")) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, PICK_CAMERA);
                                } else if (options[item].equals("Choose From Gallery")) {
                                    dialog.dismiss();
                                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                                } else if (options[item].equals("Cancel")) {
                                    dialog.dismiss();
                                }
                            }
                        });
                        builder.show();
                    } else
                        Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestCameraPermission();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{CAMERA}, 1);
        else
            selectAddressImage();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == PICK_CAMERA && resultCode == RESULT_OK && data != null) {
                if (data.getExtras().get("data") != null) {
                    f_bitmap = (Bitmap) data.getExtras().get("data");
                    panImage.setImageBitmap(f_bitmap);

                } else {
                    Toast.makeText(DocumentUploadActivity.this, "Image not captured", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK) {
                if (data.getData() != null) {
                    Uri contentURI = data.getData();
                    f_bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    panImage.setImageBitmap(f_bitmap);
                } else {
                    Toast.makeText(DocumentUploadActivity.this, "image not selected", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_panproof:
                callPanDocApi();
                break;
            case R.id.upload_pan_proof:
                selectAddressImage();
                break;
        }
    }

    private void callPanDocApi() {


        if (f_bitmap == null) {
            Toast.makeText(DocumentUploadActivity.this, "Please select document", Toast.LENGTH_SHORT).show();
            return;
        } else {
            callSubmitPanDoc(doc_type, vehicle_id);
        }

    }

    private void callSubmitPanDoc(String doc_type, String vehicle_id) {
        String bitmapString = "";
        if (f_bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            f_bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            bitmapString = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        }
        MyProgressDialog.showPDialog(DocumentUploadActivity.this);
        //call submit api......................................
        JSONObject postparams = new JSONObject();
        try {
            postparams.put("vehicle", vehicle_id + "");
            postparams.put("type", doc_type);
            postparams.put("image", bitmapString);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("postparams", postparams + "");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, DOCUMENT_UPDATE, postparams,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Doc->Response", response + "");
                        try {
                            if (response.getString("status").equals("true")) {
                                MyProgressDialog.hidePDialog();
//                                Log.e("status", String.valueOf(response));
                                Toast.makeText(DocumentUploadActivity.this, "Document uploaded Successfully!", Toast.LENGTH_SHORT).show();

                                finish();
                                overridePendingTransition(0, 0);
                            } else {
                                MyProgressDialog.hidePDialog();
                                Toast.makeText(DocumentUploadActivity.this, "Document not sent!", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            MyProgressDialog.hidePDialog();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Login Activity" + "dayInStat", "Error: " + error.getMessage());
                        MyProgressDialog.hidePDialog();
                    }
                }
        );
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_VOLLY,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}