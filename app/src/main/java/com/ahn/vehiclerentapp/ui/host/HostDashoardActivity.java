package com.ahn.vehiclerentapp.ui.host;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.ahn.vehiclerentapp.adaptes.PostAdapter;
import com.ahn.vehiclerentapp.models.city.CityData;
import com.ahn.vehiclerentapp.models.city.CityDataList;
import com.ahn.vehiclerentapp.models.posts.PostData;
import com.ahn.vehiclerentapp.models.posts.PostsDataList;
import com.ahn.vehiclerentapp.models.user.ProfileActivity;
import com.ahn.vehiclerentapp.R;
import com.ahn.vehiclerentapp.models.user.UserDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HostDashoardActivity extends AppCompatActivity implements View.OnClickListener {

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
    private TextView tv_not_data;
    private Button btn_create_job;
    private GridView rv_new_post;
    private GridView rv_accepted_post;
    private GridView rv_complete_post;

    private String userID = "";
    private UserDetails userDetails;

    private ArrayList<PostsDataList> postsDataListsMain = new ArrayList<>();
    private ArrayList<PostsDataList> postsDataListsNew = new ArrayList<>();
    private ArrayList<PostsDataList> postsDataListsAccepted = new ArrayList<>();
    private ArrayList<PostsDataList> postsDataListsCompleted = new ArrayList<>();

    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_dashoard);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();

        userDetails = new UserDetails();

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
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        List<PostData> postsDataLists = querySnapshot.toObjects(PostData.class);
                        postsDataListsMain.clear();
                        if (postsDataLists.size() !=0) {
                            if (postsDataLists.get(0).getPostsDataLists() != null) {
                                postsDataListsMain.addAll(postsDataLists.get(0).getPostsDataLists());
                                Log.d("TAG", "onSuccess: ");

                                for (PostsDataList postsDataList : postsDataListsMain) {
                                    if (postsDataList.getCreated_user_id().equals(userID)) {
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

                                    }
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
                break;
            case R.id.btn_create_job:
                Intent intent = new Intent(getApplicationContext(), PostCreateActivity.class);
                intent.putExtra("user_data", userDetails);
                startActivity(intent);
                break;
        }
    }

    private void showNewData() {

        Log.d("TAG", "showNewData: postsDataListsNew " + postsDataListsNew);
        postAdapter = new PostAdapter(HostDashoardActivity.this, postsDataListsNew);
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
}