package com.ahn.vehiclerentapp.ui.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahn.vehiclerentapp.R;
import com.ahn.vehiclerentapp.adaptes.PostDriverViewAdapter;
import com.ahn.vehiclerentapp.models.driver.DriverDetails;
import com.ahn.vehiclerentapp.models.posts.DriverData;
import com.ahn.vehiclerentapp.models.posts.PostsDataList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DriverDashoardActivity extends AppCompatActivity implements PostDriverViewAdapter.itemClickListner {

    private BottomNavigationView bottomNavigationView;
    private ProgressDialog progressDialog;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private TextView tv_post;
    private View v_post;
    private TextView tv_accepted;
    private View v_accepted;
    private TextView tv_complete;
    private View v_complete;
    private Button btn_create_job;
    private TextView tv_not_data;
    private GridView rv_new_post;
    private GridView rv_accepted_post;
    private GridView rv_complete_post;
    private ConstraintLayout cl_main;

    private String userID = "";
    private DriverDetails driverDetails;

    PostDriverViewAdapter postDriverViewAdapter;

    private ArrayList<PostsDataList> postsDataListsMain = new ArrayList<>();
    private ArrayList<PostsDataList> postsDataListsNew = new ArrayList<>();
    private ArrayList<PostsDataList> postsDataListsAccepted = new ArrayList<>();
    private ArrayList<PostsDataList> postsDataListsCompleted = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dashoard);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();

        driverDetails = new DriverDetails();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        tv_not_data = findViewById(R.id.tv_not_data);
        rv_new_post = findViewById(R.id.rv_new_post);
        rv_accepted_post = findViewById(R.id.rv_accepted_post);
        rv_complete_post = findViewById(R.id.rv_complete_post);
        cl_main = findViewById(R.id.cl_main);

        showProgress("Loading...");
        DocumentReference documentReferenceUser = firebaseFirestore.collection("drivers").document(userID);
        documentReferenceUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                hideProgress();
                driverDetails = documentSnapshot.toObject(DriverDetails.class);
                Log.d("TAG", "onSuccess: userDetails " + driverDetails);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgress();
            }
        });

        showProgress("Loading...");
        firebaseFirestore.collection("posts")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {

                        List<PostsDataList> postsDataLists = querySnapshot.toObjects(PostsDataList.class);
                                postsDataListsMain.clear();
                                if (postsDataLists.size() !=0) {

                                    postsDataListsMain.addAll(postsDataLists);

                                    for (PostsDataList postsDataList : postsDataListsMain) {
                                        if (postsDataList.getNearest_town().equals(driverDetails.getNearest_town())
                                                && postsDataList.getVehicle_type().equals(driverDetails.getVehicle_type())) {
                                            switch (postsDataList.isStatus()) {
                                                case "new":
                                                    postsDataListsNew.add(postsDataList);
                                                    break;
                                                case "accepted":
                                                    postsDataListsAccepted.add(postsDataList);
                                                    break;
                                                case "completed":
                                                    postsDataListsCompleted.add(postsDataList);
                                                    break;
                                            }

                                            //  }
                                        }
                                rv_new_post.setVisibility(View.VISIBLE);
                                tv_not_data.setVisibility(View.INVISIBLE);
                                showNewData();
                                Log.d("TAG", "onSuccess: ");
                                hideProgress();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "onFailure: " + e.getLocalizedMessage());
                        hideProgress();
                    }
                });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.profile:
                        Intent intentProfile = new Intent(getApplicationContext(), DriverProfileActivity.class);
                        intentProfile.putExtra("driver_data", driverDetails);
                        startActivity(intentProfile);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.about_us:
                        Intent intentAboutUS = new Intent(getApplicationContext(), DriverAboutUsActivity.class);
                        intentAboutUS.putExtra("driver_data", driverDetails);
                        startActivity(intentAboutUS);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.settings:
                        Intent intentSettings = new Intent(getApplicationContext(), DriverSettingsActivity.class);
                        intentSettings.putExtra("driver_data", driverDetails);
                        startActivity(intentSettings);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        cl_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(cl_main);
            }
        });
    }

    private void showNewData() {

        Log.d("TAG", "showNewData: postsDataListsNew " + postsDataListsNew);
        postDriverViewAdapter = new PostDriverViewAdapter(DriverDashoardActivity.this, postsDataListsNew, DriverDashoardActivity.this::onItemClick, userID);
        rv_new_post.setAdapter(postDriverViewAdapter);

    }

    private void showProgress(String msg) {
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgress() {
        progressDialog.cancel();
    }

    private void hideKeyboard(ConstraintLayout view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onItemClick(int position, String position_itm, ArrayList<DriverData> driverData, String bid) {
        Log.d("TAG", "onItemClick: " + position);

        driverData.add(new DriverData(driverDetails.getName(),
                driverDetails.getPhone_number(),
                driverDetails.getNearest_town(),
                driverDetails.getActive_town(),
                driverDetails.getAddress(),
                driverDetails.getDriver_image(),
                driverDetails.getLicence_image(),
                bid,
                userID));

        Task<Void> documentReference = firebaseFirestore.
                collection("posts").
                document(position_itm).
                update("driverData", driverData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DriverDashoardActivity.this, "Bid added...", Toast.LENGTH_SHORT).show();

                        finish();
                        startActivity(getIntent());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DriverDashoardActivity.this, "Bid added fail...", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}