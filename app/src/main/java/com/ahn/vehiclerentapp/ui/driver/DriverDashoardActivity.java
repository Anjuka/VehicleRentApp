package com.ahn.vehiclerentapp.ui.driver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahn.vehiclerentapp.BidsDetailsActivity;
import com.ahn.vehiclerentapp.R;
import com.ahn.vehiclerentapp.adaptes.AcceptedDriverPostAdapter;
import com.ahn.vehiclerentapp.adaptes.AcceptedPostAdapter;
import com.ahn.vehiclerentapp.adaptes.CompletedDriverPostAdapter;
import com.ahn.vehiclerentapp.adaptes.PostDriverViewAdapter;
import com.ahn.vehiclerentapp.constant.Constants;
import com.ahn.vehiclerentapp.models.driver.DriverDetails;
import com.ahn.vehiclerentapp.models.posts.DriverData;
import com.ahn.vehiclerentapp.models.posts.PostsDataList;
import com.ahn.vehiclerentapp.network.ApiClient;
import com.ahn.vehiclerentapp.network.ApiService;
import com.ahn.vehiclerentapp.ui.host.HostDashoardActivity;
import com.ahn.vehiclerentapp.ui.host.PostCreateActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverDashoardActivity extends AppCompatActivity implements PostDriverViewAdapter.itemClickListner, View.OnClickListener, AcceptedDriverPostAdapter.ItemClickListenerAccList, AcceptedDriverPostAdapter.ItemClickListenerCompleting {

    private BottomNavigationView bottomNavigationView;
    private ProgressDialog progressDialog;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    SharedPreferences sharedpreferences;

    private TextView tv_post;
    private View v_post;
    private TextView tv_accepted;
    private TextView tv_accepted_count;
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
    String mobi = "";
    private int REQUEST_PHONE_CALL = 10001;

    PostDriverViewAdapter postDriverViewAdapter;
    AcceptedDriverPostAdapter acceptedPostAdapter;
    CompletedDriverPostAdapter completedDriverPostAdapter;


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
        tv_accepted = findViewById(R.id.tv_accepted);
        v_accepted = findViewById(R.id.v_accepted);
        tv_complete = findViewById(R.id.tv_complete);
        v_complete = findViewById(R.id.v_complete);
        tv_post = findViewById(R.id.tv_post);
        v_post = findViewById(R.id.v_post);
        tv_accepted_count = findViewById(R.id.tv_accepted_count);

        tv_post.setOnClickListener(this);
        v_post.setOnClickListener(this);
        tv_accepted.setOnClickListener(this);
        v_accepted.setOnClickListener(this);
        tv_complete.setOnClickListener(this);
        v_complete.setOnClickListener(this);

        sharedpreferences = getSharedPreferences(Constants.SHARE_PREFERENCE_TAG, Context.MODE_PRIVATE);

        showProgress("Loading...");
        DocumentReference documentReferenceUser = firebaseFirestore.collection("drivers").document(userID);
        documentReferenceUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                hideProgress();
                driverDetails = documentSnapshot.toObject(DriverDetails.class);
                Log.d("TAG", "onSuccess: userDetails " + driverDetails);

                getPosts();

                if (driverDetails.getFcm_token().equals("") || driverDetails.getFcm_token() == null){
                    getToken();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgress();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.VIBRATE},
                    100001);
        }

/*
        firebaseFirestore.collection("posts")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    Log.w("TAG", "listen:error", error);
                                    return;
                                }

                                for (DocumentChange dc : value.getDocumentChanges()) {
                                    switch (dc.getType()) {
                                        case ADDED:
                                            Log.d("TAG Posts", "New Data: " + dc.getDocument().getData());

//                                            finish();
//                                            startActivity(getIntent());
                                            break;
                                        case MODIFIED:
                                            Log.d("TAG Posts", "Modified Data: " + dc.getDocument().getData());
                                            break;
                                        case REMOVED:
                                            Log.d("TAG Posts", "Removed Data: " + dc.getDocument().getData());
                                            break;
                                    }
                                }
                            }
                        });
*/

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

       /* firebaseFirestore.collection("posts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        finish();
                        startActivity(getIntent());
                    }
                });*/
    }

    private void getPosts() {

        showProgress("Loading...");
        firebaseFirestore.collection("posts")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                        List<PostsDataList> postsDataLists = querySnapshot.toObjects(PostsDataList.class);
                        postsDataListsMain.clear();
                        postsDataListsNew.clear();
                        postsDataListsAccepted.clear();
                        postsDataListsCompleted.clear();

                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            hideProgress();
                            return;
                        }
                        else {
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
                                                if (postsDataList.getApproved_driver().equals(userID)) {
                                                    postsDataListsAccepted.add(postsDataList);
                                                    if (postsDataListsAccepted.size() > 0) {
                                                        tv_accepted_count.setVisibility(View.VISIBLE);
                                                        tv_accepted_count.setText(String.valueOf(postsDataListsAccepted.size()));
                                                    }
                                                    else {
                                                        tv_accepted_count.setVisibility(View.INVISIBLE);
                                                    }
                                                }
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
                    }
                });

/*
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
                                            if (postsDataList.getApproved_driver().equals(userID)) {
                                                postsDataListsAccepted.add(postsDataList);
                                                if (postsDataListsAccepted.size() > 0) {
                                                    tv_accepted_count.setVisibility(View.VISIBLE);
                                                    tv_accepted_count.setText(String.valueOf(postsDataListsAccepted.size()));
                                                }
                                                else {
                                                    tv_accepted_count.setVisibility(View.INVISIBLE);
                                                }
                                            }
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
*/
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection("drivers").document(userID);
        documentReference.update(Constants.FCM_TOKEN, token)
                .addOnSuccessListener(unused -> showToast("Token updated successfully"))
                .addOnFailureListener(e -> showToast("Unable to update toke"));

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Constants.FCM_TOKEN, token);
        editor.commit();
        editor.apply();

    }

    private void showToast(String massage){
        Toast.makeText(getApplicationContext(), massage, Toast.LENGTH_SHORT).show();
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
    public void onItemClick(int position, String position_itm, ArrayList<DriverData> driverData, String bid, String start_timestamp, String created_user_id, String created_user_fcm_token) {
        Log.d("TAG", "onItemClick: " + position);

        ArrayList<String> allocated_times = driverDetails.getAllocated_time();

        driverData.add(new DriverData(driverDetails.getName(),
                driverDetails.getPhone_number(),
                driverDetails.getNearest_town(),
                driverDetails.getActive_town(),
                driverDetails.getAddress(),
                driverDetails.getDriver_image(),
                driverDetails.getLicence_image(),
                bid,
                userID,
                allocated_times,
                driverDetails.getFcm_token())
        );

        Task<Void> documentReference = firebaseFirestore.
                collection("posts").
                document(position_itm).
                update("driverData", driverData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DriverDashoardActivity.this, "Bid added...", Toast.LENGTH_SHORT).show();


                        JSONArray tokens = new JSONArray();
                        tokens.put(created_user_fcm_token);

                        JSONObject data = new JSONObject();
                        try {
                            data.put("user_id", userID);
                            data.put("name", driverDetails.getName());
                            data.put("driver_details", driverDetails);
                            data.put(Constants.FCM_TOKEN, driverDetails.getFcm_token());
                            data.put(Constants.KEY_MESSAGE, "bid");

                            JSONObject body = new JSONObject();
                            body.put(Constants.REMOTE_MSG_DATA, data);
                            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

                            sendNotification(body.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


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

    private void sendNotification(String massageBody) {
        ApiClient.getClient().create(ApiService.class).sendMassage(
                Constants.getRemoteMsgHeaders(),
                massageBody
        ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    try {
                        if (response.body() != null){
                            JSONObject responseJson = new JSONObject(response.body());
                            JSONArray result = responseJson.getJSONArray("results");
                            if (responseJson.getInt("failure") == 1){
                                JSONObject error = (JSONObject) result.get(0);
                                showToast(error.getString("error"));
                            }
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    //  showToast("Notification sent successfully");
                }
                else {
                    showToast("Error: " + response.code());
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showToast(t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.tv_post:
                tv_post.setTextColor(getResources().getColor(R.color.gray));
                v_post.setVisibility(View.VISIBLE);
                tv_accepted.setTextColor(getResources().getColor(R.color.gray_light));
                v_accepted.setVisibility(View.INVISIBLE);
                tv_complete.setTextColor(getResources().getColor(R.color.gray_light));
                v_complete.setVisibility(View.INVISIBLE);
                tv_post.setBackgroundColor(getResources().getColor(R.color.background));
                tv_accepted.setBackgroundColor(getResources().getColor(R.color.white));
                tv_complete.setBackgroundColor(getResources().getColor(R.color.white));

                rv_new_post.setVisibility(View.VISIBLE);
                rv_accepted_post.setVisibility(View.INVISIBLE);
                rv_complete_post.setVisibility(View.INVISIBLE);

                showNewData();

                break;
            case R.id.v_post:
                tv_post.setTextColor(getResources().getColor(R.color.gray));
                v_post.setVisibility(View.VISIBLE);
                tv_accepted.setTextColor(getResources().getColor(R.color.gray_light));
                v_accepted.setVisibility(View.INVISIBLE);
                tv_complete.setTextColor(getResources().getColor(R.color.gray_light));
                v_complete.setVisibility(View.INVISIBLE);
                tv_post.setBackgroundColor(getResources().getColor(R.color.background));
                tv_accepted.setBackgroundColor(getResources().getColor(R.color.white));
                tv_complete.setBackgroundColor(getResources().getColor(R.color.white));

                rv_new_post.setVisibility(View.VISIBLE);
                rv_accepted_post.setVisibility(View.INVISIBLE);
                rv_complete_post.setVisibility(View.INVISIBLE);

                showNewData();
                break;
            case R.id.tv_accepted:
                tv_accepted.setTextColor(getResources().getColor(R.color.gray));
                v_accepted.setVisibility(View.VISIBLE);
                tv_post.setTextColor(getResources().getColor(R.color.gray_light));
                v_post.setVisibility(View.INVISIBLE);
                tv_complete.setTextColor(getResources().getColor(R.color.gray_light));
                v_complete.setVisibility(View.INVISIBLE);
                tv_accepted.setBackgroundColor(getResources().getColor(R.color.background));
                tv_post.setBackgroundColor(getResources().getColor(R.color.white));
                tv_complete.setBackgroundColor(getResources().getColor(R.color.white));

                rv_accepted_post.setVisibility(View.VISIBLE);
                rv_new_post.setVisibility(View.INVISIBLE);
                rv_complete_post.setVisibility(View.INVISIBLE);

                showAcceptedData();
                break;
            case R.id.v_accepted:
                tv_accepted.setTextColor(getResources().getColor(R.color.gray));
                v_accepted.setVisibility(View.VISIBLE);
                tv_post.setTextColor(getResources().getColor(R.color.gray_light));
                v_post.setVisibility(View.INVISIBLE);
                tv_complete.setTextColor(getResources().getColor(R.color.gray_light));
                v_complete.setVisibility(View.INVISIBLE);
                tv_accepted.setBackgroundColor(getResources().getColor(R.color.background));
                tv_post.setBackgroundColor(getResources().getColor(R.color.white));
                tv_complete.setBackgroundColor(getResources().getColor(R.color.white));

                rv_accepted_post.setVisibility(View.VISIBLE);
                rv_new_post.setVisibility(View.INVISIBLE);
                rv_complete_post.setVisibility(View.INVISIBLE);

                showAcceptedData();
                break;
            case R.id.tv_complete:
                tv_complete.setTextColor(getResources().getColor(R.color.gray));
                v_complete.setVisibility(View.VISIBLE);
                tv_accepted.setTextColor(getResources().getColor(R.color.gray_light));
                v_accepted.setVisibility(View.INVISIBLE);
                tv_post.setTextColor(getResources().getColor(R.color.gray_light));
                v_post.setVisibility(View.INVISIBLE);
                tv_complete.setBackgroundColor(getResources().getColor(R.color.background));
                tv_accepted.setBackgroundColor(getResources().getColor(R.color.white));
                tv_post.setBackgroundColor(getResources().getColor(R.color.white));

                rv_complete_post.setVisibility(View.VISIBLE);
                rv_accepted_post.setVisibility(View.INVISIBLE);
                rv_new_post.setVisibility(View.INVISIBLE);

                showCompletedData();
                break;
            case R.id.v_complete:
                tv_complete.setTextColor(getResources().getColor(R.color.gray));
                v_complete.setVisibility(View.VISIBLE);
                tv_accepted.setTextColor(getResources().getColor(R.color.gray_light));
                v_accepted.setVisibility(View.INVISIBLE);
                tv_post.setTextColor(getResources().getColor(R.color.gray_light));
                v_post.setVisibility(View.INVISIBLE);
                tv_complete.setBackgroundColor(getResources().getColor(R.color.background));
                tv_accepted.setBackgroundColor(getResources().getColor(R.color.white));
                tv_post.setBackgroundColor(getResources().getColor(R.color.white));

                rv_complete_post.setVisibility(View.VISIBLE);
                rv_accepted_post.setVisibility(View.INVISIBLE);
                rv_new_post.setVisibility(View.INVISIBLE);

                showCompletedData();
                break;
        }

    }

    private void showCompletedData() {
        completedDriverPostAdapter = new CompletedDriverPostAdapter(DriverDashoardActivity.this, postsDataListsCompleted);
        rv_complete_post.setAdapter(completedDriverPostAdapter);
    }

    private void showAcceptedData() {
        acceptedPostAdapter = new AcceptedDriverPostAdapter(DriverDashoardActivity.this, postsDataListsAccepted, DriverDashoardActivity.this::onItemClickAcceptDriver, DriverDashoardActivity.this::onItemClickComplete);
        rv_accepted_post.setAdapter(acceptedPostAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PHONE_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, make a phone call
                /*phone = phone.substring(0, 2);
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);*/
            } else {
                // Permission is denied, show a message to the user
                Toast.makeText(this, "Phone call permission is required to make a phone call.", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == 100001) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied
            }
        }
    }

    @Override
    public void onItemClickAcceptDriver(int position, PostsDataList postsDataLists, String phone) {

        this.mobi = phone;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted, make a phone call

            AlertDialog.Builder builder1 = new AlertDialog.Builder(DriverDashoardActivity.this);
            builder1.setMessage(R.string.chaeges_msg);
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Confirm",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            mobi = mobi.replace("+94", "");
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + mobi));
                            startActivity(intent);
                        }
                    }).setNegativeButton(
                    "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        } else {
            // Permission is not granted, request for the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
        }

        if(ActivityCompat.shouldShowRequestPermissionRationale(DriverDashoardActivity.this,Manifest.permission.CALL_PHONE)){
        }
        else {
            ActivityCompat.requestPermissions(DriverDashoardActivity.this, new String[]{Manifest.permission.CALL_PHONE},100);
        }
    }

    @Override
    public void onItemClickComplete(int position, PostsDataList postsDataLists, String end_time) {
        long current_time = System.currentTimeMillis();
        long end_time_l = Long.parseLong(end_time);
        if (current_time >= end_time_l){
            //ok
            AlertDialog.Builder builder1 = new AlertDialog.Builder(DriverDashoardActivity.this);
            builder1.setMessage("Are you confirm this action ?");
            builder1.setCancelable(true);
            builder1.setTitle("Confirmation");

            builder1.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            // accept the bid
                            Task<Void> documentReference = firebaseFirestore.
                                    collection("posts").
                                    document(postsDataLists.getPosition()).
                                    update( "status", "completed")
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            JSONArray tokens = new JSONArray();
                                            tokens.put(postsDataLists.getCreated_user_fcm_token());

                                            JSONObject data = new JSONObject();
                                            try {
                                                data.put("user_id", userID);
                                                data.put("post_details", postsDataLists);
                                                data.put(Constants.FCM_TOKEN, driverDetails.getFcm_token());
                                                data.put(Constants.KEY_MESSAGE, "job_completed");

                                                JSONObject body = new JSONObject();
                                                body.put(Constants.REMOTE_MSG_DATA, data);
                                                body.put(Constants.REMOTE_MSG_REGISTRATION_IDS, tokens);

                                                sendNotification(body.toString());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            Toast.makeText(DriverDashoardActivity.this, "Job Completed...", Toast.LENGTH_SHORT).show();
                                            finish();
                                            startActivity(getIntent());
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(DriverDashoardActivity.this, "Bid approve fail...", Toast.LENGTH_SHORT).show();
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
        else {
            String msg = getString(R.string.cant_complete_job_this_time);
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }
}