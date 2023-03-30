package com.ahn.vehiclerentapp.ui.host;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.ahn.vehiclerentapp.R;
import com.ahn.vehiclerentapp.models.user.ProfileActivity;
import com.ahn.vehiclerentapp.models.user.UserDetails;
import com.ahn.vehiclerentapp.ui.host.HostDashoardActivity;
import com.ahn.vehiclerentapp.ui.host.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AboutUsActivity extends AppCompatActivity {

    private ImageView iv_back;
    private BottomNavigationView bottomNavigationView;
    private UserDetails userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        userDetails = new UserDetails();
        userDetails = (UserDetails) getIntent().getSerializableExtra("user_data");

        iv_back = findViewById(R.id.iv_back);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        iv_back.setVisibility(View.INVISIBLE);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HostDashoardActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });

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
                       /* Intent intentAboutUS = new Intent(getApplicationContext(), AboutUsActivity.class);
                        startActivity(intentAboutUS);
                        finish();
                        overridePendingTransition(0, 0);*/
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
}