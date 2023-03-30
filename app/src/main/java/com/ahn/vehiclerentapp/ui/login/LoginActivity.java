package com.ahn.vehiclerentapp.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ahn.vehiclerentapp.R;
import com.ahn.vehiclerentapp.utils.RequestUserPermission;
import com.ahn.vehiclerentapp.otpVerifications.OTPVerificationLoginActivity;
import com.ahn.vehiclerentapp.ui.registrationSelection.RegistrationActivity;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 100;
   String[] permissions = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
    };

    private TextView tv_register;
    private Button btn_login;
    private EditText et_phone_number;

    private String phone_number = "";

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //loadLocal();
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        tv_register = findViewById(R.id.tv_register);
        btn_login = findViewById(R.id.btn_login);
        et_phone_number = findViewById(R.id.et_phone_number);

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });

        RequestUserPermission requestUserPermission = new RequestUserPermission(this);
        requestUserPermission.verifyStoragePermissions();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.
                PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest
                    .permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            },1000);
            return;
        }

        // do we have a camera?
        if (!getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "No camera on this device", Toast.LENGTH_LONG)
                    .show();
        }


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone_number = "+94" + et_phone_number.getText().toString().trim();
                loginWithPhone();
            }
        });
    }

    private void loginWithPhone() {
        showProgress("Loading...");
        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                hideProgress();
                Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d("SMS", "onVerificationFailed: " + e.getLocalizedMessage());
            }

            @Override
            public void onCodeSent(@NonNull String verification_code,
                                   @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                hideProgress();
                Intent intent = new Intent(LoginActivity.this, OTPVerificationLoginActivity.class);
                intent.putExtra("phone_num", phone_number);
                intent.putExtra("otp", verification_code);
                startActivity(intent);
            }
        };

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phone_number)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallBack)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

   /* public void loadLocal (){
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        String lang = sharedPreferences.getString("lan_settings", "en");
        setLocal(lang);
    }*/
   private void showProgress(String msg){
       progressDialog.setMessage(msg);
       progressDialog.show();
   }

    private void hideProgress(){
        progressDialog.cancel();
    }
}