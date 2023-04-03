package com.ahn.vehiclerentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahn.vehiclerentapp.adaptes.BidsAdapter;
import com.ahn.vehiclerentapp.models.posts.DriverData;
import com.ahn.vehiclerentapp.models.posts.PostsDataList;
import com.ahn.vehiclerentapp.ui.driver.DriverDashoardActivity;
import com.ahn.vehiclerentapp.ui.driver.DriverProfileActivity;
import com.ahn.vehiclerentapp.ui.host.HostDashoardActivity;
import com.ahn.vehiclerentapp.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class BidsDetailsActivity extends AppCompatActivity implements BidsAdapter.ItemClickListener, BidsAdapter.ItemClickListenerButtons {

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

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private String userID = "";

    PostsDataList postsDataList = new PostsDataList();
    BidsAdapter bidsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bids_details);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();

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

        postsDataList = (PostsDataList) getIntent().getSerializableExtra("post_data");
        Log.d("TAG", "onCreate: ");

        if (postsDataList !=null){
            tv_trip_type.setText(postsDataList.getTour_type());
            tv_no_pass.setText(getResources().getText(R.string.passengers) + " " + postsDataList.getNo_passengers());
            tv_date.setText(getResources().getText(R.string.date_post) + " " + postsDataList.getStart_date());
            tv_time.setText(getResources().getText(R.string.time_post) + " " + postsDataList.getStart_time());
            tv_appl_bids.setText(getResources().getText(R.string.applied_bids) + " " + String.valueOf(postsDataList.getDriverData().size()));
            tv_vehi_type.setText(String.valueOf(postsDataList.getVehicle_type()));
            tv_start.setText(postsDataList.getStart_location());
            tv_end.setText(postsDataList.getEnd_location());

            bidsAdapter = new BidsAdapter(BidsDetailsActivity.this, postsDataList.getDriverData(),
                    BidsDetailsActivity.this::onItemClick, BidsDetailsActivity.this::onItemClickButtons, postsDataList.getPosition());
            gv_bids.setAdapter(bidsAdapter);
        }

    }

    @Override
    public void onItemClick(int position, PostsDataList postsDataLists) {

    }

    @Override
    public void onItemClickButtons(int position, DriverData driverData, int type, String post_position_id, String selected_bid) {
        //type = 1 -> accept || 0 -> cancel
        if (type == 1){
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
                                            Toast.makeText(BidsDetailsActivity.this, "Bid approved...", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(BidsDetailsActivity.this, HostDashoardActivity.class);
                                            startActivity(intent);
                                            finish();

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
        }
        else if (type == 0){
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
                            for (int x=0; x < postsDataList.getDriverData().size(); x++){
                                if (postsDataList.getDriverData().get(x).getDriver_id().equals(driverData.getDriver_id())){
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
}