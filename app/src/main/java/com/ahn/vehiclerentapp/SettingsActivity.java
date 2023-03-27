package com.ahn.vehiclerentapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.ahn.vehiclerentapp.models.CityData;
import com.ahn.vehiclerentapp.models.CityDataList;
import com.ahn.vehiclerentapp.ui.driver.DriverDashoardActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private ArrayList<CityDataList> cityDataLists = new ArrayList<>();
    private ArrayList<String> cities = new ArrayList<>();

    private Spinner et_nearest_town;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        progressDialog = new ProgressDialog(this);

        et_nearest_town = findViewById(R.id.et_nearest_town);
        iv_back = findViewById(R.id.iv_back);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DriverDashoardActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
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

                            for (CityDataList cityDataList1 : cityDataLists){
                                cities.add(cityDataList1.getCity_name());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(SettingsActivity.this, android.R.layout.simple_spinner_dropdown_item, cities);
                            et_nearest_town.setAdapter(adapter);

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

        Log.d("TAG", "onCreate: cityDataLists " + cityDataLists);
    }
}