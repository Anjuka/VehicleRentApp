package com.ahn.vehiclerentapp.ui.splash;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.ahn.vehiclerentapp.ChooseLanguageActivity;
import com.ahn.vehiclerentapp.R;
import com.ahn.vehiclerentapp.ui.LoginActivity;
import com.ahn.vehiclerentapp.ui.driver.DriverDashoardActivity;
import com.ahn.vehiclerentapp.ui.host.HostDashoardActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class SplashActivity extends AppCompatActivity {

    //Firebase
    private FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    private FirebaseFirestore firebaseFirestore;
    private String users_type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (currentUser != null) {
            DocumentReference documentReference = firebaseFirestore.collection("users_type").document(firebaseAuth.getUid());
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                    users_type = documentSnapshot.getString("userType");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (users_type != null) {
                                if (users_type.equals("host")) {
                                    Intent hostDashboard = new Intent(getApplicationContext(), HostDashoardActivity.class);
                                    startActivity(hostDashboard);
                                    finish();
                                    overridePendingTransition(0, 0);
                                } else {
                                    Intent driverDashboard = new Intent(getApplicationContext(), DriverDashoardActivity.class);
                                    startActivity(driverDashboard);
                                    finish();
                                    overridePendingTransition(0, 0);
                                }
                            } else {
                                users_type = "";
                                if (users_type.equals("")) {
                                    Intent i = new Intent(getApplicationContext(), ChooseLanguageActivity.class);
                                    startActivity(i);
                                    finish();
                                    overridePendingTransition(0, 0);
                                } else {
                                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(i);
                                    finish();
                                    overridePendingTransition(0, 0);
                                }
                            }

                        }
                    }, 5000);
                }
            });
        }
        else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (users_type.equals("")) {
                        Intent i = new Intent(getApplicationContext(), ChooseLanguageActivity.class);
                        startActivity(i);
                        finish();
                        overridePendingTransition(0, 0);
                    } else {
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                        finish();
                        overridePendingTransition(0, 0);
                    }
                }
            }, 3000);
        }
    }
}