package com.ahn.vehiclerentapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ahn.vehiclerentapp.DriverRegistrationActivity;
import com.ahn.vehiclerentapp.R;

public class RegistrationActivity extends AppCompatActivity {

    private ImageView iv_back;

    private Button btn_as_client;
    private Button btn_as_driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        iv_back = findViewById(R.id.iv_back);
        btn_as_client = findViewById(R.id.btn_as_client);
        btn_as_driver = findViewById(R.id.btn_as_driver);

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_as_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ClientRegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_as_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), DriverRegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}