package com.ahn.vehiclerentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.ahn.vehiclerentapp.models.CityData;
import com.ahn.vehiclerentapp.models.CityDataList;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HostDashoardActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_dashoard);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

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

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        return true;
                    case R.id.profile:
                        Intent intentProfile = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intentProfile);
                        return true;
                    case R.id.about_us:
                        Intent intentAboutUS = new Intent(getApplicationContext(), AboutUsActivity.class);
                        startActivity(intentAboutUS);
                        return true;
                        case R.id.settings:
                            Intent intentSettings = new Intent(getApplicationContext(), SettingsActivity.class);
                            startActivity(intentSettings);
                        return true;
                }
                return false;
            }
        });
    }
}