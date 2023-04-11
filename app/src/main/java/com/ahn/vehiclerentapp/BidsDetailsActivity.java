package com.ahn.vehiclerentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahn.vehiclerentapp.adaptes.BidsAdapter;
import com.ahn.vehiclerentapp.constant.Constants;
import com.ahn.vehiclerentapp.models.driver.DriverDetails;
import com.ahn.vehiclerentapp.models.posts.DriverData;
import com.ahn.vehiclerentapp.models.posts.PostsDataList;
import com.ahn.vehiclerentapp.models.user.ProfileActivity;
import com.ahn.vehiclerentapp.models.user.UserDetails;
import com.ahn.vehiclerentapp.network.ApiClient;
import com.ahn.vehiclerentapp.network.ApiService;
import com.ahn.vehiclerentapp.ui.driver.DriverDashoardActivity;
import com.ahn.vehiclerentapp.ui.driver.DriverProfileActivity;
import com.ahn.vehiclerentapp.ui.driver.DriverSettingsActivity;
import com.ahn.vehiclerentapp.ui.host.AboutUsActivity;
import com.ahn.vehiclerentapp.ui.host.HostDashoardActivity;
import com.ahn.vehiclerentapp.ui.host.SettingsActivity;
import com.ahn.vehiclerentapp.ui.login.LoginActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidsDetailsActivity extends AppCompatActivity implements BidsAdapter.ItemClickListener, BidsAdapter.ItemClickListenerButtons, BidsAdapter.DriveInfoClickListener {

    private GridView gv_bids;
    private TextView tv_no_pass;
    private TextView tv_vehi_type;
    private TextView tv_date;
    private TextView tv_time;
    private TextView tv_no_nights;
    private TextView tv_appl_bids;
    private TextView tv_trip_type;
    private TextView tv_start;
    private TextView tv_end;
    private BottomNavigationView bottomNavigationView;

    private EditText et_mobile_num;
    private EditText et_nearest_town;
    private EditText et_address;
    private EditText et_email;
    private TextView tv_name;
    private EditText et_vehicle_type;
    private EditText et_vehicle_number;
    private CircleImageView civ_driver;
    private ImageView iv_lic_img;
    private ImageView iv_v_img1;
    private ImageView iv_v_img2;
    private ImageView iv_v_img3;
    private ImageView iv_v_img4;
    private ConstraintLayout cl_drive_info;
    private ImageView iv_close_drive_info;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    private String userID = "";
    String remove_driver_fcm_token = "";

    PostsDataList postsDataList = new PostsDataList();
    BidsAdapter bidsAdapter;

    private DriverDetails driverDetails;
    private UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bids_details);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();

        progressDialog = new ProgressDialog(this);

        userDetails = new UserDetails();
        userDetails = (UserDetails) getIntent().getSerializableExtra("user_data");

        gv_bids = findViewById(R.id.gv_bids);
        tv_no_pass = findViewById(R.id.tv_no_pass);
        tv_vehi_type = findViewById(R.id.tv_vehi_type);
        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        tv_no_nights = findViewById(R.id.tv_no_nights);
        tv_appl_bids = findViewById(R.id.tv_appl_bids);
        tv_trip_type = findViewById(R.id.tv_trip_type);
        tv_start = findViewById(R.id.tv_start);
        tv_end = findViewById(R.id.tv_end);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        tv_name = findViewById(R.id.textView16);
        et_mobile_num = findViewById(R.id.et_mobile_num);
        et_nearest_town = findViewById(R.id.et_nearest_town);
        et_address = findViewById(R.id.et_address);
        et_email = findViewById(R.id.et_email);
        et_vehicle_type = findViewById(R.id.et_vehicle_type);
        et_vehicle_number = findViewById(R.id.et_vehicle_number);
        civ_driver = findViewById(R.id.circleImageView2);
        iv_lic_img = findViewById(R.id.iv_lic_img);
        iv_v_img1 = findViewById(R.id.iv_v_img1);
        iv_v_img2 = findViewById(R.id.iv_v_img2);
        iv_v_img3 = findViewById(R.id.iv_v_img3);
        iv_v_img4 = findViewById(R.id.iv_v_img4);
        cl_drive_info = findViewById(R.id.cl_drive_info);
        iv_close_drive_info = findViewById(R.id.iv_close_drive_info);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intentDashboard = new Intent(getApplicationContext(), HostDashoardActivity.class);
                        startActivity(intentDashboard);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        Intent intentProfile = new Intent(getApplicationContext(), ProfileActivity.class);
                        intentProfile.putExtra("user_data", userDetails);
                        startActivity(intentProfile);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.about_us:
                        Intent intentAboutUS = new Intent(getApplicationContext(), AboutUsActivity.class);
                        startActivity(intentAboutUS);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.settings:
                        Intent intentSettings = new Intent(getApplicationContext(), SettingsActivity.class);
                        intentSettings.putExtra("user_data", userDetails);
                        startActivity(intentSettings);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });


        postsDataList = (PostsDataList) getIntent().getSerializableExtra("post_data");
        Log.d("TAG", "onCreate: ");

        if (postsDataList != null) {
            tv_trip_type.setText(postsDataList.getTour_type());
            tv_no_pass.setText(getResources().getText(R.string.passengers) + " " + postsDataList.getNo_passengers());
            tv_date.setText(getResources().getText(R.string.date_post) + " " + postsDataList.getStart_date());
            tv_time.setText(getResources().getText(R.string.time_post) + " " + postsDataList.getStart_time());
            tv_appl_bids.setText(getResources().getText(R.string.applied_bids) + " " + String.valueOf(postsDataList.getDriverData().size()));
            tv_vehi_type.setText(String.valueOf(postsDataList.getVehicle_type()));
            tv_start.setText(postsDataList.getStart_location());
            tv_end.setText(postsDataList.getEnd_location());

            bidsAdapter = new BidsAdapter(BidsDetailsActivity.this, postsDataList.getDriverData(),
                    BidsDetailsActivity.this::onItemClick, BidsDetailsActivity.this::onItemClickButtons, postsDataList.getPosition(), BidsDetailsActivity.this::onItemClickInfo);
            gv_bids.setAdapter(bidsAdapter);
        }

        iv_close_drive_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cl_drive_info.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onItemClick(int position, PostsDataList postsDataLists) {

    }

    @Override
    public void onItemClickButtons(int position, DriverData driverData, int type, String post_position_id, String selected_bid) {
        //type = 1 -> accept || 0 -> cancel
        if (type == 1) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(BidsDetailsActivity.this);
            builder1.setMessage("Are you confirm this bid ?");
            builder1.setCancelable(true);
            builder1.setTitle("Confirmation");

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            // accept the bid
                            postsDataList.setApproved_bid(selected_bid);
                            postsDataList.setApproved_driver(driverData.getDriver_id());

                            Task<Void> documentReference = firebaseFirestore.
                                    collection("posts").
                                    document(post_position_id).
                                    update("approved_bid", selected_bid,
                                            "approved_driver", driverData.getDriver_id(),
                                            "status", "accepted")
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                           /* Toast.makeText(BidsDetailsActivity.this, "Bid approved...", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(BidsDetailsActivity.this, HostDashoardActivity.class);
                                            startActivity(intent);
                                            finish();*/

                                            ArrayList<String> allocated_time = driverData.getAllocated_time();
                                            allocated_time.add(postsDataList.getStart_timestamp());

                                            Task<Void> documentReference = firebaseFirestore.
                                                    collection("drivers").
                                                    document(driverData.getDriver_id()).
                                                    update("allocated_time", allocated_time).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {

                                                            JSONArray tokens = new JSONArray();
                                                            tokens.put(postsDataList.getDriverData().get(position).getDriver_fcm_token());

                                                            JSONObject data = new JSONObject();
                                                            try {
                                                                data.put("user_id", userID);
                                                                data.put("bid", postsDataList.getApproved_bid());
                                                                data.put("post_details", postsDataList);
                                                                data.put(Constants.FCM_TOKEN, postsDataList.getCreated_user_fcm_token());
                                                                data.put(Constants.KEY_MESSAGE, "bid_approve");

                                                                JSONObject body = new JSONObject();
                                                                body.put(Constants.REMOTE_MSG_DATA, data);
                                                                body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

                                                                sendNotification(body.toString());
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                            Toast.makeText(BidsDetailsActivity.this, "Bid approved...", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(BidsDetailsActivity.this, HostDashoardActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    });

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(BidsDetailsActivity.this, "Bid approve fail...", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else if (type == 0) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(BidsDetailsActivity.this);
            builder1.setMessage("Are you cancel this bid ?");
            builder1.setTitle("Cancel");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            // cancel the bid
                            for (int x = 0; x < postsDataList.getDriverData().size(); x++) {
                                if (postsDataList.getDriverData().get(x).getDriver_id().equals(driverData.getDriver_id())) {
                                    remove_driver_fcm_token = postsDataList.getDriverData().get(x).getDriver_fcm_token();
                                    postsDataList.getDriverData().remove(x);
                                }
                            }

                            Task<Void> documentReference = firebaseFirestore.
                                    collection("posts").
                                    document(post_position_id).
                                    update("driverData", postsDataList.getDriverData())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(BidsDetailsActivity.this, "Bid removed...", Toast.LENGTH_SHORT).show();

                                            JSONArray tokens = new JSONArray();
                                            tokens.put(remove_driver_fcm_token);

                                            JSONObject data = new JSONObject();
                                            try {
                                                data.put("user_id", userID);
                                                data.put(Constants.FCM_TOKEN, postsDataList.getCreated_user_fcm_token());
                                                data.put(Constants.KEY_MESSAGE, "bid_cancel");

                                                JSONObject body = new JSONObject();
                                                body.put(Constants.REMOTE_MSG_DATA, data);
                                                body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

                                                sendNotification(body.toString());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            Intent intent = new Intent(BidsDetailsActivity.this, HostDashoardActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(BidsDetailsActivity.this, "Fail...", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    });

            builder1.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }
    }

    private void sendNotification(String massageBody) {
        ApiClient.getClient().create(ApiService.class).sendMassage(
                Constants.getRemoteMsgHeaders(),
                massageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() != null) {
                            JSONObject responseJson = new JSONObject(response.body());
                            JSONArray result = responseJson.getJSONArray("results");
                            if (responseJson.getInt("failure") == 1) {
                                JSONObject error = (JSONObject) result.get(0);
                                showToast(error.getString("error"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //  showToast("Notification sent successfully");
                } else {
                    showToast("Error: " + response.code());
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast(t.getMessage());
            }
        });

    }

    private void showToast(String massage) {
        Toast.makeText(getApplicationContext(), massage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClickInfo(int position, String driver_id) {


        showProgress("Loading...");
        DocumentReference documentReferenceUser = firebaseFirestore.collection("drivers").document(driver_id);
        documentReferenceUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                hideProgress();
                driverDetails = documentSnapshot.toObject(DriverDetails.class);
                Log.d("TAG", "onSuccess: userDetails " + driverDetails);

                if (driverDetails != null) {

                    if (driverDetails.getDriver_image().equals("")) {

                    } else {
                        Glide.with(getApplicationContext())
                                .load(driverDetails.getDriver_image())
                                .placeholder(R.drawable.default_pfp)
                                .into(civ_driver);
                    }

                    if (!driverDetails.getLicence_image().isEmpty()) {
                        Glide.with(getApplicationContext())
                                .load(driverDetails.getLicence_image())
                                .placeholder(R.drawable.ic_outline_document_scanner_24)
                                .into(iv_lic_img);
                    }

                    if (!driverDetails.getImg_1().isEmpty()) {
                        Glide.with(getApplicationContext())
                                .load(driverDetails.getImg_1())
                                .placeholder(R.drawable.ic_outline_image_24)
                                .autoClone()
                                .into(iv_v_img1);
                    }

                    if (!driverDetails.getImg_2().isEmpty()) {
                        Glide.with(getApplicationContext())
                                .load(driverDetails.getImg_2())
                                .placeholder(R.drawable.ic_outline_image_24)
                                .into(iv_v_img2);
                    }

                    if (!driverDetails.getImg_3().isEmpty()) {
                        Glide.with(getApplicationContext())
                                .load(driverDetails.getImg_3())
                                .placeholder(R.drawable.ic_outline_image_24)
                                .into(iv_v_img3);
                    }

                    if (!driverDetails.getImg_4().isEmpty()) {
                        Glide.with(getApplicationContext())
                                .load(driverDetails.getImg_4())
                                .placeholder(R.drawable.ic_outline_image_24)
                                .into(iv_v_img4);
                    }

                    tv_name.setText(driverDetails.getName());
                    et_mobile_num.setText(driverDetails.getPhone_number());
                    et_nearest_town.setText(driverDetails.getActive_town());
                    et_address.setText(driverDetails.getAddress());
                    et_email.setText(driverDetails.getEmail());
                    et_vehicle_type.setText(driverDetails.getVehicle_type());
                    et_vehicle_number.setText(driverDetails.getVehicle_number());

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgress();
            }
        });

        cl_drive_info.setVisibility(View.VISIBLE);
    }

    private void showProgress(String msg) {
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgress() {
        progressDialog.cancel();
    }
}