package com.ahn.vehiclerentapp.ui.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahn.vehiclerentapp.models.user.ProfileActivity;
import com.ahn.vehiclerentapp.ui.host.AboutUsActivity;
import com.ahn.vehiclerentapp.R;
import com.ahn.vehiclerentapp.ui.host.SettingsActivity;
import com.ahn.vehiclerentapp.models.driver.DriverDetails;
import com.ahn.vehiclerentapp.ui.login.LoginActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class DriverProfileActivity extends AppCompatActivity {

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
    private ImageView iv_logout;
    private BottomNavigationView bottomNavigationView;

    private FirebaseAuth firebaseAuth;

    private DriverDetails driverDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);

        driverDetails = new DriverDetails();
        driverDetails = (DriverDetails) getIntent().getSerializableExtra("driver_data");

        firebaseAuth = FirebaseAuth.getInstance();

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
        iv_logout = findViewById(R.id.iv_logout);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);


        if (driverDetails != null) {

            if (driverDetails.getDriver_image().equals("")) {

            } else {
                Glide.with(getApplicationContext())
                        .load(driverDetails.getDriver_image())
                        .placeholder(R.drawable.default_pfp)
                        .into(civ_driver);
            }

            if (!driverDetails.getLicence_image().isEmpty()){
                Glide.with(getApplicationContext())
                        .load(driverDetails.getLicence_image())
                        .placeholder(R.drawable.ic_outline_document_scanner_24)
                        .into(iv_lic_img);
            }

            if (!driverDetails.getImg_1().isEmpty()){
                Glide.with(getApplicationContext())
                        .load(driverDetails.getImg_1())
                        .placeholder(R.drawable.ic_outline_image_24)
                        .autoClone()
                        .into(iv_v_img1);
            }

            if (!driverDetails.getImg_2().isEmpty()){
                Glide.with(getApplicationContext())
                        .load(driverDetails.getImg_2())
                        .placeholder(R.drawable.ic_outline_image_24)
                        .into(iv_v_img2);
            }

            if (!driverDetails.getImg_3().isEmpty()){
                Glide.with(getApplicationContext())
                        .load(driverDetails.getImg_3())
                        .placeholder(R.drawable.ic_outline_image_24)
                        .into(iv_v_img3);
            }

            if (!driverDetails.getImg_4().isEmpty()){
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

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intentDash = new Intent(getApplicationContext(), DriverDashoardActivity.class);
                        startActivity(intentDash);
                        finish();
                        overridePendingTransition(0, 0);
                        break;
                    //return true;
                    case R.id.profile:
                        Intent intentProfile = new Intent(getApplicationContext(), DriverProfileActivity.class);
                        intentProfile.putExtra("driver_data", driverDetails);
                        startActivity(intentProfile);
                        finish();
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.about_us:
                        Intent intentAboutUS = new Intent(getApplicationContext(), AboutUsActivity.class);
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

        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(DriverProfileActivity.this);
                builder1.setMessage("Are you sure you want to sign out?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                firebaseAuth.signOut();
                                Intent intentLogout = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intentLogout);
                                finish();
                                overridePendingTransition(0, 0);
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

    }
}