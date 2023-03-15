package com.ahn.vehiclerentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;

import com.ahn.vehiclerentapp.adaptes.Sp_adapter;

import java.util.ArrayList;

public class VehicleRegActivity extends AppCompatActivity {

    private Spinner sp_type;

    private ArrayList<String> vehicle_type = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_reg);

        sp_type = findViewById(R.id.sp_type);

        vehicle_type.add(getString(R.string.car));
        vehicle_type.add(getString(R.string.van));
        vehicle_type.add(getString(R.string.bus));
        vehicle_type.add(getString(R.string.tuk));

        Sp_adapter sp_adapter = new Sp_adapter(VehicleRegActivity.this, vehicle_type);
        sp_type.setAdapter(sp_adapter);
;    }
}