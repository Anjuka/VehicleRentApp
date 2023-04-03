package com.ahn.vehiclerentapp.ui.host;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ahn.vehiclerentapp.R;
import com.ahn.vehiclerentapp.models.city.CityData;
import com.ahn.vehiclerentapp.models.city.CityDataList;
import com.ahn.vehiclerentapp.models.posts.DriverData;
import com.ahn.vehiclerentapp.models.posts.PostData;
import com.ahn.vehiclerentapp.models.posts.PostsDataList;
import com.ahn.vehiclerentapp.models.user.UserDetails;
import com.ahn.vehiclerentapp.ui.driver.DriverProfileActivity;
import com.ahn.vehiclerentapp.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class PostCreateActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog progressDialog;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private Spinner sp_trip_type;
    private Spinner sp_tour_nights;
    private Spinner sp_tour_destination;
    private Spinner sp_no_passengers;
    private Spinner sp_vehicle_type;
    private Spinner sp_pick;
    private EditText et_date;
    private EditText et_time;
    private LinearLayout cl_drop;
    private LinearLayout cl_pick;
    private LinearLayout cl_tour;
    private TextView tv_tour_destination;
    private LinearLayout ll_night_list;
    private ImageView iv_close;
    private Button btn_submit;

    private String user_id = "";

    private Spinner sp_night_1;
    private Spinner sp_night_2;
    private Spinner sp_night_3;
    private Spinner sp_night_4;
    private Spinner sp_night_5;
    private Spinner sp_night_6;
    private Spinner sp_night_7;
    private Spinner sp_night_8;
    private Spinner sp_night_9;
    private Spinner sp_night_10;
    private TextView tv_night_1;
    private TextView tv_night_2;
    private TextView tv_night_3;
    private TextView tv_night_4;
    private TextView tv_night_5;
    private TextView tv_night_6;
    private TextView tv_night_7;
    private TextView tv_night_8;
    private TextView tv_night_9;
    private TextView tv_night_10;

    private RadioGroup rb_grp;
    private RadioButton rb_same_destination;
    private RadioButton rb_different_destination;

    private ArrayList<CityDataList> cityDataLists = new ArrayList<>();
    private ArrayList<String> cities = new ArrayList<>();
    private ArrayList<String> trip_types = new ArrayList<>();
    private ArrayList<String> no_of_days = new ArrayList<>();
    private ArrayList<String> no_of_passengers = new ArrayList<>();
    private ArrayList<String> vehicle_types = new ArrayList<>();
    private ArrayList<String> air_ports = new ArrayList<>();
    private ArrayList<PostsDataList> postsDataLists = new ArrayList();

    final Calendar myCalendar = Calendar.getInstance();

    private String tour_type = "";
    private String start_location = "";
    private String end_location = "";
    private String no_nights = "";
    private ArrayList<String> night_destination = new ArrayList<>();
    private String no_passengers = "";
    private String vehicle_type = "";
    private String start_date = "";
    private String start_time = "";
    private String nearest_town = "";

    private String n1 = "";
    private String n2 = "";
    private String n3 = "";
    private String n4 = "";
    private String n5 = "";
    private String n6 = "";
    private String n7 = "";
    private String n8 = "";
    private String n9 = "";
    private String n10 = "";

    UserDetails user_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_create);

        user_data = new UserDetails();
        user_data = (UserDetails) getIntent().getSerializableExtra("user_data");

        sp_trip_type = findViewById(R.id.sp_trip_type);
        sp_tour_nights = findViewById(R.id.sp_tour_nights);
        sp_tour_destination = findViewById(R.id.sp_tour_destination);
        tv_tour_destination = findViewById(R.id.tv_tour_destination);
        sp_no_passengers = findViewById(R.id.sp_no_passengers);
        sp_vehicle_type = findViewById(R.id.sp_vehicle_type);
        sp_pick = findViewById(R.id.sp_pick);
        et_date = findViewById(R.id.et_date);
        et_time = findViewById(R.id.et_time);
        cl_drop = findViewById(R.id.cl_drop);
        cl_pick = findViewById(R.id.cl_pick);
        cl_tour = findViewById(R.id.cl_tour);
        ll_night_list = findViewById(R.id.ll_night_list);
        iv_close = findViewById(R.id.iv_close);
        btn_submit = findViewById(R.id.btn_submit);

        sp_night_1 = findViewById(R.id.sp_night_1);
        sp_night_2 = findViewById(R.id.sp_night_2);
        sp_night_3 = findViewById(R.id.sp_night_3);
        sp_night_4 = findViewById(R.id.sp_night_4);
        sp_night_5 = findViewById(R.id.sp_night_5);
        sp_night_6 = findViewById(R.id.sp_night_6);
        sp_night_7 = findViewById(R.id.sp_night_7);
        sp_night_8 = findViewById(R.id.sp_night_8);
        sp_night_9 = findViewById(R.id.sp_night_9);
        sp_night_10 = findViewById(R.id.sp_night_10);
        tv_night_1 = findViewById(R.id.tv_night_1);
        tv_night_2 = findViewById(R.id.tv_night_2);
        tv_night_3 = findViewById(R.id.tv_night_3);
        tv_night_4 = findViewById(R.id.tv_night_4);
        tv_night_5 = findViewById(R.id.tv_night_5);
        tv_night_6 = findViewById(R.id.tv_night_6);
        tv_night_7 = findViewById(R.id.tv_night_7);
        tv_night_8 = findViewById(R.id.tv_night_8);
        tv_night_9 = findViewById(R.id.tv_night_9);
        tv_night_10 = findViewById(R.id.tv_night_10);

        rb_grp = findViewById(R.id.rb_grp);
        rb_same_destination = findViewById(R.id.rb_same_destination);
        rb_different_destination = findViewById(R.id.rb_different_destination);

        rb_same_destination.setChecked(true);
        rb_different_destination.setChecked(false);

        progressDialog = new ProgressDialog(this);

        btn_submit.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        user_id = firebaseAuth.getCurrentUser().getUid();

        trip_types.add("Airport Drop");
        trip_types.add("Airport Pick");
        trip_types.add("Tour");

        no_of_days.add("1");
        no_of_days.add("2");
        no_of_days.add("3");
        no_of_days.add("4");
        no_of_days.add("5");
        no_of_days.add("6");
        no_of_days.add("7");
        no_of_days.add("8");
        no_of_days.add("9");
        no_of_days.add("10");

        no_of_passengers.add("1");
        no_of_passengers.add("2");
        no_of_passengers.add("3");
        no_of_passengers.add("4");
        no_of_passengers.add("5");
        no_of_passengers.add("More than 5");

        vehicle_types.add(getString(R.string.car));
        vehicle_types.add(getString(R.string.van));
        vehicle_types.add(getString(R.string.bus));
        vehicle_types.add(getString(R.string.tuk));

        air_ports.add("Bandaranaike Airport (Katunayaka)");
        air_ports.add("Mattala Rajapaksha Airport (Maththala)");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(PostCreateActivity.this, android.R.layout.simple_spinner_dropdown_item, trip_types);
        sp_trip_type.setAdapter(adapter);

        ArrayAdapter<String> adapter_no_of_days = new ArrayAdapter<>(PostCreateActivity.this, android.R.layout.simple_spinner_dropdown_item, no_of_days);
        sp_tour_nights.setAdapter(adapter_no_of_days);

        ArrayAdapter<String> adapter_no_of_passg = new ArrayAdapter<>(PostCreateActivity.this, android.R.layout.simple_spinner_dropdown_item, no_of_passengers);
        sp_no_passengers.setAdapter(adapter_no_of_passg);

        ArrayAdapter<String> adapter_vehicle_type = new ArrayAdapter<>(PostCreateActivity.this, android.R.layout.simple_spinner_dropdown_item, vehicle_types);
        sp_vehicle_type.setAdapter(adapter_vehicle_type);

        ArrayAdapter<String> adapter_air_port = new ArrayAdapter<>(PostCreateActivity.this, android.R.layout.simple_spinner_dropdown_item, air_ports);
        sp_pick.setAdapter(adapter_air_port);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(PostCreateActivity.this);
                builder1.setMessage("Are you sure, you want to exit?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent intent = new Intent(getApplicationContext(), HostDashoardActivity.class);
                                startActivity(intent);
                                finish();
                                //overridePendingTransition(0, 0);
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
        });

        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseFirestore.collection("posts")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        List<PostData> postsDataLists_ = querySnapshot.toObjects(PostData.class);
                        postsDataLists.clear();
                        if (postsDataLists_.size() !=0) {
                            if (postsDataLists_.get(0).getPostsDataLists() != null) {
                                postsDataLists.addAll(postsDataLists_.get(0).getPostsDataLists());
                                Log.d("TAG", "onSuccess: ");
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "onFailure: " + e.getLocalizedMessage());
                        progressDialog.cancel();
                    }
                });

        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        firebaseFirestore.collection("cities")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {
                        List<CityData> cityDataList = querySnapshot.toObjects(CityData.class);
                        cityDataLists.clear();
                        if (cityDataList.get(0).getArrayList() != null) {
                            cityDataLists.addAll(cityDataList.get(0).getArrayList());
                            Log.d("TAG", "onSuccess: ");

                            for (CityDataList cityDataList1 : cityDataLists) {
                                cities.add(cityDataList1.getCity_name());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(PostCreateActivity.this, android.R.layout.simple_spinner_dropdown_item, cities);
                            sp_night_1.setAdapter(adapter);
                            sp_night_2.setAdapter(adapter);
                            sp_night_3.setAdapter(adapter);
                            sp_night_4.setAdapter(adapter);
                            sp_night_5.setAdapter(adapter);
                            sp_night_6.setAdapter(adapter);
                            sp_night_7.setAdapter(adapter);
                            sp_night_8.setAdapter(adapter);
                            sp_night_9.setAdapter(adapter);
                            sp_night_10.setAdapter(adapter);
                            sp_tour_destination.setAdapter(adapter);

                            progressDialog.cancel();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "onFailure: " + e.getLocalizedMessage());
                        progressDialog.cancel();
                    }
                });

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(PostCreateActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        sp_trip_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tour_type = trip_types.get(i);
                switch (i) {
                    case 0:
                        cl_drop.setVisibility(View.VISIBLE);
                        cl_pick.setVisibility(View.GONE);
                        cl_tour.setVisibility(View.GONE);
                        break;
                    case 1:
                        cl_drop.setVisibility(View.VISIBLE);
                        cl_pick.setVisibility(View.GONE);
                        cl_tour.setVisibility(View.GONE);
                        break;
                    case 2:
                        cl_tour.setVisibility(View.VISIBLE);
                        cl_pick.setVisibility(View.GONE);
                        cl_drop.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_pick.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (tour_type.equals("Airport Drop")){
                    end_location = air_ports.get(i);
                    start_location = user_data.getAddress();
                }
                else if (tour_type.equals("Airport Pick")){
                    start_location = air_ports.get(i);
                    end_location = user_data.getAddress();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_tour_nights.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                no_nights = no_of_days.get(position);

                switch (position + 1) {
                    case 1:
                        sp_night_1.setVisibility(View.VISIBLE);
                        sp_night_2.setVisibility(View.GONE);
                        sp_night_3.setVisibility(View.GONE);
                        sp_night_4.setVisibility(View.GONE);
                        sp_night_5.setVisibility(View.GONE);
                        sp_night_6.setVisibility(View.GONE);
                        sp_night_7.setVisibility(View.GONE);
                        sp_night_8.setVisibility(View.GONE);
                        sp_night_9.setVisibility(View.GONE);
                        sp_night_10.setVisibility(View.GONE);

                        tv_night_1.setVisibility(View.VISIBLE);
                        tv_night_2.setVisibility(View.GONE);
                        tv_night_3.setVisibility(View.GONE);
                        tv_night_4.setVisibility(View.GONE);
                        tv_night_5.setVisibility(View.GONE);
                        tv_night_6.setVisibility(View.GONE);
                        tv_night_7.setVisibility(View.GONE);
                        tv_night_8.setVisibility(View.GONE);
                        tv_night_9.setVisibility(View.GONE);
                        tv_night_10.setVisibility(View.GONE);

                        night_destination.clear();
                        night_destination.add(n1);

                        break;
                    case 2:
                        sp_night_1.setVisibility(View.VISIBLE);
                        sp_night_2.setVisibility(View.VISIBLE);
                        sp_night_3.setVisibility(View.GONE);
                        sp_night_4.setVisibility(View.GONE);
                        sp_night_5.setVisibility(View.GONE);
                        sp_night_6.setVisibility(View.GONE);
                        sp_night_7.setVisibility(View.GONE);
                        sp_night_8.setVisibility(View.GONE);
                        sp_night_9.setVisibility(View.GONE);
                        sp_night_10.setVisibility(View.GONE);

                        tv_night_1.setVisibility(View.VISIBLE);
                        tv_night_2.setVisibility(View.VISIBLE);
                        tv_night_3.setVisibility(View.GONE);
                        tv_night_4.setVisibility(View.GONE);
                        tv_night_5.setVisibility(View.GONE);
                        tv_night_6.setVisibility(View.GONE);
                        tv_night_7.setVisibility(View.GONE);
                        tv_night_8.setVisibility(View.GONE);
                        tv_night_9.setVisibility(View.GONE);
                        tv_night_10.setVisibility(View.GONE);

                        night_destination.clear();
                        night_destination.add(n1);
                        night_destination.add(n2);
                        break;
                    case 3:
                        sp_night_1.setVisibility(View.VISIBLE);
                        sp_night_2.setVisibility(View.VISIBLE);
                        sp_night_3.setVisibility(View.VISIBLE);
                        sp_night_4.setVisibility(View.GONE);
                        sp_night_5.setVisibility(View.GONE);
                        sp_night_6.setVisibility(View.GONE);
                        sp_night_7.setVisibility(View.GONE);
                        sp_night_8.setVisibility(View.GONE);
                        sp_night_9.setVisibility(View.GONE);
                        sp_night_10.setVisibility(View.GONE);

                        tv_night_1.setVisibility(View.VISIBLE);
                        tv_night_2.setVisibility(View.VISIBLE);
                        tv_night_3.setVisibility(View.VISIBLE);
                        tv_night_4.setVisibility(View.GONE);
                        tv_night_5.setVisibility(View.GONE);
                        tv_night_6.setVisibility(View.GONE);
                        tv_night_7.setVisibility(View.GONE);
                        tv_night_8.setVisibility(View.GONE);
                        tv_night_9.setVisibility(View.GONE);
                        tv_night_10.setVisibility(View.GONE);

                        night_destination.clear();
                        night_destination.add(n1);
                        night_destination.add(n2);
                        night_destination.add(n3);
                        break;
                    case 4:
                        sp_night_1.setVisibility(View.VISIBLE);
                        sp_night_2.setVisibility(View.VISIBLE);
                        sp_night_3.setVisibility(View.VISIBLE);
                        sp_night_4.setVisibility(View.VISIBLE);
                        sp_night_5.setVisibility(View.GONE);
                        sp_night_6.setVisibility(View.GONE);
                        sp_night_7.setVisibility(View.GONE);
                        sp_night_8.setVisibility(View.GONE);
                        sp_night_9.setVisibility(View.GONE);
                        sp_night_10.setVisibility(View.GONE);

                        tv_night_1.setVisibility(View.VISIBLE);
                        tv_night_2.setVisibility(View.VISIBLE);
                        tv_night_3.setVisibility(View.VISIBLE);
                        tv_night_4.setVisibility(View.VISIBLE);
                        tv_night_5.setVisibility(View.GONE);
                        tv_night_6.setVisibility(View.GONE);
                        tv_night_7.setVisibility(View.GONE);
                        tv_night_8.setVisibility(View.GONE);
                        tv_night_9.setVisibility(View.GONE);
                        tv_night_10.setVisibility(View.GONE);

                        night_destination.clear();
                        night_destination.add(n1);
                        night_destination.add(n2);
                        night_destination.add(n3);
                        night_destination.add(n4);
                        break;
                    case 5:
                        sp_night_1.setVisibility(View.VISIBLE);
                        sp_night_2.setVisibility(View.VISIBLE);
                        sp_night_3.setVisibility(View.VISIBLE);
                        sp_night_4.setVisibility(View.VISIBLE);
                        sp_night_5.setVisibility(View.VISIBLE);
                        sp_night_6.setVisibility(View.GONE);
                        sp_night_7.setVisibility(View.GONE);
                        sp_night_8.setVisibility(View.GONE);
                        sp_night_9.setVisibility(View.GONE);
                        sp_night_10.setVisibility(View.GONE);

                        tv_night_1.setVisibility(View.VISIBLE);
                        tv_night_2.setVisibility(View.VISIBLE);
                        tv_night_3.setVisibility(View.VISIBLE);
                        tv_night_4.setVisibility(View.VISIBLE);
                        tv_night_5.setVisibility(View.VISIBLE);
                        tv_night_6.setVisibility(View.GONE);
                        tv_night_7.setVisibility(View.GONE);
                        tv_night_8.setVisibility(View.GONE);
                        tv_night_9.setVisibility(View.GONE);
                        tv_night_10.setVisibility(View.GONE);

                        night_destination.clear();
                        night_destination.add(n1);
                        night_destination.add(n2);
                        night_destination.add(n3);
                        night_destination.add(n4);
                        night_destination.add(n5);
                        break;
                    case 6:
                        sp_night_1.setVisibility(View.VISIBLE);
                        sp_night_2.setVisibility(View.VISIBLE);
                        sp_night_3.setVisibility(View.VISIBLE);
                        sp_night_4.setVisibility(View.VISIBLE);
                        sp_night_5.setVisibility(View.VISIBLE);
                        sp_night_6.setVisibility(View.VISIBLE);
                        sp_night_7.setVisibility(View.GONE);
                        sp_night_8.setVisibility(View.GONE);
                        sp_night_9.setVisibility(View.GONE);
                        sp_night_10.setVisibility(View.GONE);

                        tv_night_1.setVisibility(View.VISIBLE);
                        tv_night_2.setVisibility(View.VISIBLE);
                        tv_night_3.setVisibility(View.VISIBLE);
                        tv_night_4.setVisibility(View.VISIBLE);
                        tv_night_5.setVisibility(View.VISIBLE);
                        tv_night_6.setVisibility(View.VISIBLE);
                        tv_night_7.setVisibility(View.GONE);
                        tv_night_8.setVisibility(View.GONE);
                        tv_night_9.setVisibility(View.GONE);
                        tv_night_10.setVisibility(View.GONE);

                        night_destination.clear();
                        night_destination.add(n1);
                        night_destination.add(n2);
                        night_destination.add(n3);
                        night_destination.add(n4);
                        night_destination.add(n5);
                        night_destination.add(n6);
                        break;
                    case 7:
                        sp_night_1.setVisibility(View.VISIBLE);
                        sp_night_2.setVisibility(View.VISIBLE);
                        sp_night_3.setVisibility(View.VISIBLE);
                        sp_night_4.setVisibility(View.VISIBLE);
                        sp_night_5.setVisibility(View.VISIBLE);
                        sp_night_6.setVisibility(View.VISIBLE);
                        sp_night_7.setVisibility(View.VISIBLE);
                        sp_night_8.setVisibility(View.GONE);
                        sp_night_9.setVisibility(View.GONE);
                        sp_night_10.setVisibility(View.GONE);

                        tv_night_1.setVisibility(View.VISIBLE);
                        tv_night_2.setVisibility(View.VISIBLE);
                        tv_night_3.setVisibility(View.VISIBLE);
                        tv_night_4.setVisibility(View.VISIBLE);
                        tv_night_5.setVisibility(View.VISIBLE);
                        tv_night_6.setVisibility(View.VISIBLE);
                        tv_night_7.setVisibility(View.VISIBLE);
                        tv_night_8.setVisibility(View.GONE);
                        tv_night_9.setVisibility(View.GONE);
                        tv_night_10.setVisibility(View.GONE);

                        night_destination.clear();
                        night_destination.add(n1);
                        night_destination.add(n2);
                        night_destination.add(n3);
                        night_destination.add(n4);
                        night_destination.add(n5);
                        night_destination.add(n6);
                        night_destination.add(n7);
                        break;
                    case 8:
                        sp_night_1.setVisibility(View.VISIBLE);
                        sp_night_2.setVisibility(View.VISIBLE);
                        sp_night_3.setVisibility(View.VISIBLE);
                        sp_night_4.setVisibility(View.VISIBLE);
                        sp_night_5.setVisibility(View.VISIBLE);
                        sp_night_6.setVisibility(View.VISIBLE);
                        sp_night_7.setVisibility(View.VISIBLE);
                        sp_night_8.setVisibility(View.VISIBLE);
                        sp_night_9.setVisibility(View.GONE);
                        sp_night_10.setVisibility(View.GONE);

                        tv_night_1.setVisibility(View.VISIBLE);
                        tv_night_2.setVisibility(View.VISIBLE);
                        tv_night_3.setVisibility(View.VISIBLE);
                        tv_night_4.setVisibility(View.VISIBLE);
                        tv_night_5.setVisibility(View.VISIBLE);
                        tv_night_6.setVisibility(View.VISIBLE);
                        tv_night_7.setVisibility(View.VISIBLE);
                        tv_night_8.setVisibility(View.VISIBLE);
                        tv_night_9.setVisibility(View.GONE);
                        tv_night_10.setVisibility(View.GONE);

                        night_destination.clear();
                        night_destination.add(n1);
                        night_destination.add(n2);
                        night_destination.add(n3);
                        night_destination.add(n4);
                        night_destination.add(n5);
                        night_destination.add(n6);
                        night_destination.add(n7);
                        night_destination.add(n8);
                        break;
                    case 9:
                        sp_night_1.setVisibility(View.VISIBLE);
                        sp_night_2.setVisibility(View.VISIBLE);
                        sp_night_3.setVisibility(View.VISIBLE);
                        sp_night_4.setVisibility(View.VISIBLE);
                        sp_night_5.setVisibility(View.VISIBLE);
                        sp_night_6.setVisibility(View.VISIBLE);
                        sp_night_7.setVisibility(View.VISIBLE);
                        sp_night_8.setVisibility(View.VISIBLE);
                        sp_night_9.setVisibility(View.VISIBLE);
                        sp_night_10.setVisibility(View.GONE);

                        tv_night_1.setVisibility(View.VISIBLE);
                        tv_night_2.setVisibility(View.VISIBLE);
                        tv_night_3.setVisibility(View.VISIBLE);
                        tv_night_4.setVisibility(View.VISIBLE);
                        tv_night_5.setVisibility(View.VISIBLE);
                        tv_night_6.setVisibility(View.VISIBLE);
                        tv_night_7.setVisibility(View.VISIBLE);
                        tv_night_8.setVisibility(View.VISIBLE);
                        tv_night_9.setVisibility(View.VISIBLE);
                        tv_night_10.setVisibility(View.GONE);

                        night_destination.clear();
                        night_destination.add(n1);
                        night_destination.add(n2);
                        night_destination.add(n3);
                        night_destination.add(n4);
                        night_destination.add(n5);
                        night_destination.add(n6);
                        night_destination.add(n7);
                        night_destination.add(n9);
                        break;
                    case 10:
                        sp_night_1.setVisibility(View.VISIBLE);
                        sp_night_2.setVisibility(View.VISIBLE);
                        sp_night_3.setVisibility(View.VISIBLE);
                        sp_night_4.setVisibility(View.VISIBLE);
                        sp_night_5.setVisibility(View.VISIBLE);
                        sp_night_6.setVisibility(View.VISIBLE);
                        sp_night_7.setVisibility(View.VISIBLE);
                        sp_night_8.setVisibility(View.VISIBLE);
                        sp_night_9.setVisibility(View.VISIBLE);
                        sp_night_10.setVisibility(View.VISIBLE);

                        tv_night_1.setVisibility(View.VISIBLE);
                        tv_night_2.setVisibility(View.VISIBLE);
                        tv_night_3.setVisibility(View.VISIBLE);
                        tv_night_4.setVisibility(View.VISIBLE);
                        tv_night_5.setVisibility(View.VISIBLE);
                        tv_night_6.setVisibility(View.VISIBLE);
                        tv_night_7.setVisibility(View.VISIBLE);
                        tv_night_8.setVisibility(View.VISIBLE);
                        tv_night_9.setVisibility(View.VISIBLE);
                        tv_night_10.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_night_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                n1 = cities.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        sp_night_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                n2 = cities.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_night_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                n3 = cities.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_night_4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                n4 = cities.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_night_5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                n5 = cities.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_night_6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                n6 = cities.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_night_7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                n7 = cities.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_night_8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                n8 = cities.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_night_9.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                n9 = cities.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_night_10.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                n10 = cities.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        rb_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.rb_same_destination:
                        tv_tour_destination.setVisibility(View.VISIBLE);
                        sp_tour_destination.setVisibility(View.VISIBLE);
                        ll_night_list.setVisibility(View.GONE);

                        break;
                    case R.id.rb_different_destination:
                        tv_tour_destination.setVisibility(View.GONE);
                        sp_tour_destination.setVisibility(View.GONE);
                        ll_night_list.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        sp_no_passengers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                no_passengers = no_of_passengers.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_vehicle_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vehicle_type = vehicle_types.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_submit:

                nearest_town = user_data.getActive_town();
               // start_location = user_data.getAddress();

                if (!no_nights.isEmpty()) {
                    setNightPlans(no_nights);
                }

                if (tour_type.equals("Airport Drop") || tour_type.equals("Airport Pick")){
                    night_destination = new ArrayList<>();
                }

                Log.d("TAG", "onClick: " +
                        "tour_typ = " + tour_type + "\n" +
                        " - no_nights = " + no_nights + "\n" +
                        " - vehicle_type = " + vehicle_type + "\n" +
                        " - no_passengers = " + no_passengers + "\n" +
                        " - start_date = " + start_date + "\n" +
                        " - start_time = " + start_time + "\n" +
                        " - start_location = " + start_location + "\n" +
                        " - end_location = " + end_location + "\n" +
                        " - nearest_town = " + nearest_town + "\n" +
                        " - nights = " + night_destination + "\n" +
                        " - nearest_town = " + night_destination);

                ArrayList<DriverData> driverData = new ArrayList<>();

                progressDialog.setMessage("Posting....");
                progressDialog.setCancelable(false);
                progressDialog.show();

                String uuid= UUID.randomUUID().toString() + String.valueOf(System.currentTimeMillis());

                PostsDataList postsDataList = new PostsDataList(
                        user_id,
                        "new",
                        nearest_town,
                        tour_type,
                        no_nights,
                        no_passengers,
                        start_date,
                        start_time,
                        start_location,
                        end_location,
                        night_destination,
                        "",
                        driverData,
                        vehicle_type,
                        uuid
                );

              ArrayList<PostsDataList> p = new ArrayList<>();
                p.add(postsDataList);
                postsDataLists.add(postsDataList);

                PostData postData = new PostData(p);

                DocumentReference documentReference = firebaseFirestore.
                        collection("posts").
                        document(uuid);
                documentReference.set(postsDataList).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.cancel();
                        Toast.makeText(PostCreateActivity.this, "Posting succeeded....", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), HostDashoardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.cancel();
                        Toast.makeText(PostCreateActivity.this, "Posting failed....", Toast.LENGTH_SHORT).show();
                    }
                });

                break;
        }
    }

    private void setNightPlans(String no_nights) {

        switch ((Integer.parseInt(no_nights))) {
            case 1:
                night_destination.clear();
                night_destination.add(n1);

                break;
            case 2:

                night_destination.clear();
                night_destination.add(n1);
                night_destination.add(n2);
                break;

                case 3:
                night_destination.clear();
                night_destination.add(n1);
                night_destination.add(n2);
                night_destination.add(n3);
                break;
             case 4:

                night_destination.clear();
                night_destination.add(n1);
                night_destination.add(n2);
                night_destination.add(n3);
                night_destination.add(n4);
                break;
            case 5:

                night_destination.clear();
                night_destination.add(n1);
                night_destination.add(n2);
                night_destination.add(n3);
                night_destination.add(n4);
                night_destination.add(n5);
                break;

            case 6:

                 night_destination.clear();
                night_destination.add(n1);
                night_destination.add(n2);
                night_destination.add(n3);
                night_destination.add(n4);
                night_destination.add(n5);
                night_destination.add(n6);
                break;

            case 7:

                night_destination.clear();
                night_destination.add(n1);
                night_destination.add(n2);
                night_destination.add(n3);
                night_destination.add(n4);
                night_destination.add(n5);
                night_destination.add(n6);
                night_destination.add(n7);
                break;

            case 8:

                night_destination.clear();
                night_destination.add(n1);
                night_destination.add(n2);
                night_destination.add(n3);
                night_destination.add(n4);
                night_destination.add(n5);
                night_destination.add(n6);
                night_destination.add(n7);
                night_destination.add(n8);
                break;

            case 9:

                night_destination.clear();
                night_destination.add(n1);
                night_destination.add(n2);
                night_destination.add(n3);
                night_destination.add(n4);
                night_destination.add(n5);
                night_destination.add(n6);
                night_destination.add(n7);
                night_destination.add(n9);
                break;

            case 10:

                night_destination.clear();
                night_destination.add(n1);
                night_destination.add(n2);
                night_destination.add(n3);
                night_destination.add(n4);
                night_destination.add(n5);
                night_destination.add(n6);
                night_destination.add(n7);
                night_destination.add(n9);
                night_destination.add(n10);
                break;
        }

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);

            String myFormat = "MM/dd/yy";
            SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
            et_date.setText(dateFormat.format(myCalendar.getTime()));
            start_date = dateFormat.format(myCalendar.getTime());
        }
    };

    private void showTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(PostCreateActivity.this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                et_time.setText(selectedHour + ":" + selectedMinute);
                start_time = selectedHour + ":" + selectedMinute;
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }
}