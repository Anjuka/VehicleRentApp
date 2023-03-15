package com.ahn.vehiclerentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class DriverRegistrationActivity extends AppCompatActivity {

    private Button btn_as_client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_registration);

        btn_as_client  = findViewById(R.id.btn_as_client);

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