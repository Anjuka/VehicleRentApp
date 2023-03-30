package com.ahn.vehiclerentapp.ui.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.ahn.vehiclerentapp.R;
import com.ahn.vehiclerentapp.models.driver.DriverDetails;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DriverAboutUsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private DriverDetails driverDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_about_us);

        driverDetails = new DriverDetails();
        driverDetails = (DriverDetails) getIntent().getSerializableExtra("driver_data");

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intentDashboard = new Intent(getApplicationContext(), DriverDashoardActivity.class);
                        startActivity(intentDashboard);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        Intent intentProfile = new Intent(getApplicationContext(), DriverProfileActivity.class);
                        intentProfile.putExtra("driver_data", driverDetails);
                        startActivity(intentProfile);
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.about_us:
                        /*Intent intentAboutUS = new Intent(getApplicationContext(), DriverAboutUsActivity.class);
                        intentAboutUS.putExtra("driver_data", driverDetails);
                        startActivity(intentAboutUS);
                        finish();
                        overridePendingTransition(0, 0);*/
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
    }
}