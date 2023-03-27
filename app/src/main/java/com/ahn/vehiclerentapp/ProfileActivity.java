package com.ahn.vehiclerentapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahn.vehiclerentapp.models.UserDetails;
import com.ahn.vehiclerentapp.ui.LoginActivity;
import com.ahn.vehiclerentapp.ui.host.HostDashoardActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;

    private UserDetails userDetails;

    private ImageView iv_logout;
    private ImageView iv_back;
    private  TextView tv_name;
    private  EditText et_nic;
    private EditText et_host_name;
    private  EditText et_mobile_num;
    private  EditText et_br;
    private  EditText et_nearest_town;
    private  EditText et_address;
    private  EditText et_email;
    private  CircleImageView circleImageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userDetails = new UserDetails();
        userDetails = (UserDetails) getIntent().getSerializableExtra("user_data");

        firebaseAuth = FirebaseAuth.getInstance();

        iv_logout = findViewById(R.id.iv_logout);
        iv_back = findViewById(R.id.iv_back);
        tv_name = findViewById(R.id.textView16);
        et_nic = findViewById(R.id.et_nic);
        et_host_name = findViewById(R.id.et_host_name);
        et_br = findViewById(R.id.et_br);
        et_mobile_num = findViewById(R.id.et_mobile_num);
        et_nearest_town = findViewById(R.id.et_nearest_town);
        et_address = findViewById(R.id.et_address);
        et_email = findViewById(R.id.et_email);
        circleImageView2 = findViewById(R.id.circleImageView2);

        if (userDetails != null){

            if (userDetails.getImage().equals("")){

            }
            else {
                Glide.with(getApplicationContext())
                        .load(userDetails.getImage())
                        .placeholder(R.drawable.default_pfp)
                        .into(circleImageView2);
            }

            tv_name.setText(userDetails.getName());
            et_nic.setText(userDetails.getNic());
            et_host_name.setText(userDetails.getProperty_name());
            et_mobile_num.setText(userDetails.getPhone_number());
            et_br.setText(userDetails.getBr());
            et_nearest_town.setText(userDetails.getActive_town());
            et_address.setText(userDetails.getAddress());
            et_email.setText(userDetails.getEmail());
        }

        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ProfileActivity.this);
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

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),HostDashoardActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, 0);
            }
        });
    }
}