package com.ahn.vehiclerentapp.ui.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.ahn.vehiclerentapp.R;
import com.ahn.vehiclerentapp.VehicleRegActivity;
import com.ahn.vehiclerentapp.models.CityData;
import com.ahn.vehiclerentapp.models.CityDataList;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DriverRegistrationActivity extends AppCompatActivity {

    private Button btn_as_client;
    private Spinner sp_nearest_town;

    private ProgressDialog progressDialog;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    private ArrayList<CityDataList> cityDataLists = new ArrayList<>();
    private ArrayList<String> cities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        btn_as_client  = findViewById(R.id.btn_as_client);
        sp_nearest_town  = findViewById(R.id.sp_nearest_town);

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

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(DriverRegistrationActivity.this, android.R.layout.simple_spinner_dropdown_item, cities);
                            sp_nearest_town.setAdapter(adapter);

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

        btn_as_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverRegistrationActivity.this, VehicleRegActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}