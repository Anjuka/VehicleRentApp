package com.ahn.vehiclerentapp.ui.host;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahn.vehiclerentapp.BidsDetailsActivity;
import com.ahn.vehiclerentapp.adaptes.AcceptedPostAdapter;
import com.ahn.vehiclerentapp.adaptes.PostAdapter;
import com.ahn.vehiclerentapp.constant.Constants;
import com.ahn.vehiclerentapp.models.city.CityData;
import com.ahn.vehiclerentapp.models.city.CityDataList;
import com.ahn.vehiclerentapp.models.posts.PostData;
import com.ahn.vehiclerentapp.models.posts.PostsDataList;
import com.ahn.vehiclerentapp.models.user.ProfileActivity;
import com.ahn.vehiclerentapp.R;
import com.ahn.vehiclerentapp.models.user.UserDetails;
import com.ahn.vehiclerentapp.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class HostDashoardActivity extends AppCompatActivity implements View.OnClickListener, PostAdapter.ItemClickListener, AcceptedPostAdapter.ItemClickListenerAccList{

    private int REQUEST_PHONE_CALL = 10001;
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
    private TextView tv_not_data;
    private Button btn_create_job;
    private GridView rv_new_post;
    private GridView rv_accepted_post;
    private GridView rv_complete_post;

    private String userID = "";
    private UserDetails userDetails;
    String mobi = "";

    private ArrayList<PostsDataList> postsDataListsMain = new ArrayList<>();
    private ArrayList<PostsDataList> postsDataListsNew = new ArrayList<>();
    private ArrayList<PostsDataList> postsDataListsAccepted = new ArrayList<>();
    private ArrayList<PostsDataList> postsDataListsCompleted = new ArrayList<>();

    private PostAdapter postAdapter;
    private AcceptedPostAdapter acceptedPostAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_dashoard);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();

        userDetails = new UserDetails();

        sharedpreferences = getSharedPreferences(Constants.SHARE_PREFERENCE_TAG, Context.MODE_PRIVATE);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        tv_post = findViewById(R.id.tv_post);
        v_post = findViewById(R.id.v_post);
        tv_accepted = findViewById(R.id.tv_accepted);
        v_accepted = findViewById(R.id.v_accepted);
        tv_complete = findViewById(R.id.tv_complete);
        v_complete = findViewById(R.id.v_complete);
        btn_create_job = findViewById(R.id.btn_create_job);
        tv_not_data = findViewById(R.id.tv_not_data);
        rv_new_post = findViewById(R.id.rv_new_post);
        rv_accepted_post = findViewById(R.id.rv_accepted_post);
        rv_complete_post = findViewById(R.id.rv_complete_post);
        tv_accepted_count = findViewById(R.id.tv_accepted_count);

        tv_post.setOnClickListener(this);
        v_post.setOnClickListener(this);
        tv_accepted.setOnClickListener(this);
        v_accepted.setOnClickListener(this);
        tv_complete.setOnClickListener(this);
        v_complete.setOnClickListener(this);
        btn_create_job.setOnClickListener(this);

        showProgress("Loading...");
        DocumentReference documentReferenceUser = firebaseFirestore.collection("users").document(userID);
        documentReferenceUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                hideProgress();
                userDetails = documentSnapshot.toObject(UserDetails.class);
                Log.d("TAG", "onSuccess: userDetails " + userDetails);

                if (userDetails.getFcm_token().equals("")){
                    getToken();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgress();
            }
        });


        /*ArrayList<CityDataList> cityDataLists = new ArrayList<>();
        cityDataLists.add(new CityDataList( "Colombo"));
        cityDataLists.add(new CityDataList( "Moratuwa"));
        cityDataLists.add(new CityDataList( "Kandy"));
        cityDataLists.add(new CityDataList( "Negombo"));
        cityDataLists.add(new CityDataList( "Galle"));
        cityDataLists.add(new CityDataList( "Hikkaduwa"));
        cityDataLists.add(new CityDataList( "Kotte"));
        cityDataLists.add(new CityDataList( "Kilinochchi"));
        cityDataLists.add(new CityDataList( "Trincomalee"));
        cityDataLists.add(new CityDataList( "Jaffna"));
        cityDataLists.add(new CityDataList( "Matara"));
        cityDataLists.add(new CityDataList( "Anuradhapura"));
        cityDataLists.add(new CityDataList( "Ratnapura"));
        cityDataLists.add(new CityDataList( "Puttalam"));
        cityDataLists.add(new CityDataList( "Badulla"));
        cityDataLists.add(new CityDataList( "Mullaittivu"));
        cityDataLists.add(new CityDataList( "Matale"));
        cityDataLists.add(new CityDataList( "Mannar"));
        cityDataLists.add(new CityDataList( "Kurunegala"));
        cityDataLists.add(new CityDataList( "Batticaloa"));

        CityData cityData = new CityData(cityDataLists);

        DocumentReference documentReference = firebaseFirestore.collection("cities").document("citiesList");
        documentReference.set(cityData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });*/

        showProgress("Loading...");


       firebaseFirestore.collection("posts")
                       .addSnapshotListener(new EventListener<QuerySnapshot>() {
                           @Override
                           public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                               if (e != null) {
                                   Log.w("TAG", "Listen failed.", e);
                                   hideProgress();
                                   return;
                               }
                               else {
                                   List<PostsDataList> postsDataLists = querySnapshot.toObjects(PostsDataList.class);
                                   postsDataListsMain.clear();
                                   postsDataListsNew.clear();
                                   postsDataListsAccepted.clear();
                                   postsDataListsCompleted.clear();

                                   if (postsDataLists.size() !=0) {
                           /* if (postsDataLists.get(0).getPostsDataLists() != null) {
                                postsDataListsMain.addAll(postsDataLists.get(0).getPostsDataLists());
                                Log.d("TAG", "onSuccess: ");*/

                                       postsDataListsMain.addAll(postsDataLists);

                                       for (PostsDataList postsDataList : postsDataListsMain) {
                                           if (postsDataList.getCreated_user_id().equals(userID)) {
                                               switch (postsDataList.isStatus()) {
                                                   case "new":
                                                       postsDataListsNew.add(postsDataList);
                                                       if (postsDataListsNew.size() < 1){
                                                           tv_not_data.setVisibility(View.VISIBLE);
                                                       }
                                                       else {
                                                           tv_not_data.setVisibility(View.INVISIBLE);
                                                       }
                                                       break;
                                                   case "accepted":
                                                       postsDataListsAccepted.add(postsDataList);
                                                       if (postsDataListsAccepted.size() > 0) {
                                                           tv_accepted_count.setVisibility(View.VISIBLE);
                                                           tv_accepted_count.setText(String.valueOf(postsDataListsAccepted.size()));
                                                       }

                                                       if (postsDataListsAccepted.size() < 1){
                                                           tv_not_data.setVisibility(View.VISIBLE);
                                                       }
                                                       else {
                                                           tv_not_data.setVisibility(View.INVISIBLE);
                                                       }
                                                       break;
                                                   case "completed":
                                                       postsDataListsCompleted.add(postsDataList);
                                                       if (postsDataListsCompleted.size() < 1){
                                                           tv_not_data.setVisibility(View.VISIBLE);
                                                       }
                                                       else {
                                                           tv_not_data.setVisibility(View.INVISIBLE);
                                                       }
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
                           */
/* if (postsDataLists.get(0).getPostsDataLists() != null) {
                                postsDataListsMain.addAll(postsDataLists.get(0).getPostsDataLists());
                                Log.d("TAG", "onSuccess: ");*//*


                            postsDataListsMain.addAll(postsDataLists);

                                for (PostsDataList postsDataList : postsDataListsMain) {
                                    if (postsDataList.getCreated_user_id().equals(userID)) {
                                        switch (postsDataList.isStatus()) {
                                            case "new":
                                                postsDataListsNew.add(postsDataList);
                                                if (postsDataListsNew.size() < 1){
                                                    tv_not_data.setVisibility(View.VISIBLE);
                                                }
                                                else {
                                                    tv_not_data.setVisibility(View.INVISIBLE);
                                                }
                                                break;
                                            case "accepted":
                                                postsDataListsAccepted.add(postsDataList);
                                                if (postsDataListsAccepted.size() > 0) {
                                                    tv_accepted_count.setVisibility(View.VISIBLE);
                                                    tv_accepted_count.setText(String.valueOf(postsDataListsAccepted.size()));
                                                }

                                                if (postsDataListsAccepted.size() < 1){
                                                    tv_not_data.setVisibility(View.VISIBLE);
                                                }
                                                else {
                                                    tv_not_data.setVisibility(View.INVISIBLE);
                                                }
                                                break;
                                            case "completed":
                                                postsDataListsCompleted.add(postsDataList);
                                                if (postsDataListsCompleted.size() < 1){
                                                    tv_not_data.setVisibility(View.VISIBLE);
                                                }
                                                else {
                                                    tv_not_data.setVisibility(View.INVISIBLE);
                                                }
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



        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
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
                        intentAboutUS.putExtra("user_data", userDetails);
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
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection("users").document(userID);
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
                break;
            case R.id.btn_create_job:
                Intent intent = new Intent(getApplicationContext(), PostCreateActivity.class);
                intent.putExtra("user_data", userDetails);
                startActivity(intent);
                break;
        }
    }

    private void showAcceptedData() {
        acceptedPostAdapter = new AcceptedPostAdapter(HostDashoardActivity.this, postsDataListsAccepted, HostDashoardActivity.this::onItemClickAccept);
        rv_accepted_post.setAdapter(acceptedPostAdapter);
    }

    private void showNewData() {

        Log.d("TAG", "showNewData: postsDataListsNew " + postsDataListsNew);
        postAdapter = new PostAdapter(HostDashoardActivity.this, postsDataListsNew, HostDashoardActivity.this::onItemClick);
        rv_new_post.setAdapter(postAdapter);
        
    }

    private void showProgress(String msg) {
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgress() {
        progressDialog.cancel();
    }

    @Override
    public void onItemClick(int position, PostsDataList postsDataLists) {
        Log.d("TAG", "onItemClick: ");
        Intent intent = new Intent(HostDashoardActivity.this, BidsDetailsActivity.class);
        intent.putExtra("post_data", postsDataLists);
        startActivity(intent);
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
    }

    @Override
    public void onItemClickAccept(int position, PostsDataList postsDataLists, String phone) {

        this.mobi = phone;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            // Permission is already granted, make a phone call

            AlertDialog.Builder builder1 = new AlertDialog.Builder(HostDashoardActivity.this);
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

        if(ActivityCompat.shouldShowRequestPermissionRationale(HostDashoardActivity.this,Manifest.permission.CALL_PHONE)){
        }
        else {
            ActivityCompat.requestPermissions(HostDashoardActivity.this, new String[]{Manifest.permission.CALL_PHONE},100);
        }
    }

}